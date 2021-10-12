package pers.zhangch.systemmonitor.monitor.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pers.zhangch.systemmonitor.monitor.autoconfigure.MonitorJobProperties;
import pers.zhangch.systemmonitor.monitor.vo.MonitorJobVO;

import java.util.List;

/**
 * 应用启动时初始化检查定时任务配置
 * @author Zhang ChangHao
 * @time 2021/10/11 14:08
 */
@Slf4j
@Component
@Order(1)
public class MonitorJobCheck implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MonitorJobVO> jobs = MonitorJobProperties.getJobs();
        for (MonitorJobVO jobVO : jobs) {
            jobVO.checks();
        }
    }
}
