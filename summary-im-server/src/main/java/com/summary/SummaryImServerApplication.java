package com.summary;

import com.summary.im.server.netty.ImServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 启动主程序
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SummaryImServerApplication implements ApplicationRunner, DisposableBean {
    private static ConfigurableApplicationContext ctx;

    @Autowired
    private ImServer imServer;

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
        imServer.start();
        log.info("App started run method");
    }
}
