package pers.zhangch.systemmonitor.monitor.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pers.zhangch.systemmonitor.monitor.vo.MonitorJobVO;

import java.util.List;

/**
 * @author Zhang ChangHao
 * @time 2021/10/11 9:39
 */
@Configuration
@ConfigurationProperties(prefix = "monitor.job")
public class MonitorJobProperties {
    
    protected static List<MonitorJobVO> jobs;
    
    public static List<MonitorJobVO> getJobs() {
        return jobs;
    }
    
    public void setJobs(List<MonitorJobVO> jobs) {
        MonitorJobProperties.jobs = jobs;
    }
}
