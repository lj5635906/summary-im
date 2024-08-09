package com.summary.im.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author jie.luo
 * @since 2024/8/9
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "im.server")
public class ImProperties {
    /**
     * 启动端口
     */
    private int port;
    /**
     * 启动IP
     */
    private String host;
}
