package com.summary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 启动主程序
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Slf4j
@SpringBootApplication
public class SummaryImServerApplication implements ApplicationRunner, DisposableBean {
    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(SummaryImServerApplication.class, args);
        for (String str : ctx.getEnvironment().getActiveProfiles()) {
            log.info(str);
        }
        log.info("App started!");
    }

    @Override
    public void destroy() throws Exception {
        if (null != ctx && ctx.isRunning()) {
            log.info("App closed!");
            ctx.close();
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("App started run method");
    }
}
