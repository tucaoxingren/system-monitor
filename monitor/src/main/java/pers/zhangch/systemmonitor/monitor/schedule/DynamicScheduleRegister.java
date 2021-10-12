package pers.zhangch.systemmonitor.monitor.schedule;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.cron.CronUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pers.zhangch.systemmonitor.monitor.autoconfigure.MonitorJobProperties;
import pers.zhangch.systemmonitor.monitor.vo.MonitorJobVO;

import java.util.List;

/**
 * 系统启动时读取配置文件中的定时任务配置并注册
 * @author Zhang ChangHao
 * @time 2021/10/11 11:26
 */
@Slf4j
@Component
@Order(2)
public class DynamicScheduleRegister implements ApplicationRunner {
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MonitorJobVO> jobs = MonitorJobProperties.getJobs();
        try {
            for (MonitorJobVO jobVO : jobs) {
                if (!jobVO.check()) {
                    continue;
                }
                log.debug("定时任务[{}({})]开始注册", jobVO.getName(), jobVO.getDescription());
                Class<DefaultParentTask> task = ClassUtil.loadClass(jobVO.getExecuteTaskClass());
                DefaultParentTask defaultParentTask = task.newInstance();
                defaultParentTask.setMonitorJobVO(jobVO);
                CronUtil.schedule(jobVO.getCron(), defaultParentTask);
                // 支持秒级别定时任务
                CronUtil.setMatchSecond(true);
                CronUtil.start();
                log.debug("定时任务[{}({})]注册成功", jobVO.getName(), jobVO.getDescription());
            }
        } catch (Exception e) {
            log.error("定时任务注册失败", e);
        }
    }
}
