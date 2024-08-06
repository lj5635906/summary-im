package com.summary.im.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息体类型
 *
 * @author jie.luo
 * @since 2024/8/6
 */
@Getter
@AllArgsConstructor
public enum BodyType {

    // 文本
    text(0),
    // 图片
    image(1),
    // 语音
    voice(2),
    // 视频
    video(3),
    // 文件
    file(4),
    // 位置
    location(5),
    // 链接
    link(6),
    // 提示
    tips(7),
    // 通知
    notification(8);

    private final int code;

    public static BodyType getByCode(int code) {
        for (BodyType value : BodyType.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

}
