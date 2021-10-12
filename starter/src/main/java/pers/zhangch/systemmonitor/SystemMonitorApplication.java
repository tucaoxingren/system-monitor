package pers.zhangch.systemmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"pers.zhangch"})
public class SystemMonitorApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SystemMonitorApplication.class);
        application.run(args);
    }
}
