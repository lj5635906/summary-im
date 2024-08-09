package com.summary.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户端类型
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Getter
@AllArgsConstructor
public enum ClientType {
    // 服务端
    SERVER(-1),
    // 安卓 app
    ANDROID(0),
    // IOS app
    IOS(1),
    // window
    window(2),
    // PC 浏览器
    PC(3),
    // 小程序
    APPLET(4),
    // H5
    H5(5),
    // 通过接口发送
    REST(6);

    private final int code;

    public static ClientType getClientType(int code) {
        for (ClientType clientType : ClientType.values()) {
            if (clientType.code == code) {
                return clientType;
            }
        }
        return null;
    }
}
