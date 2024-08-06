package com.summary.im.encipher;

/**
 * 默认消息 加密、解密
 *
 * @author jie.luo
 * @since 2024/8/6
 */
public class SimpleEncipher implements Encipher {
    @Override
    public byte[] encipher(byte[] data) {
        return data;
    }

    @Override
    public byte[] decipher(byte[] data) {
        return data;
    }
}
