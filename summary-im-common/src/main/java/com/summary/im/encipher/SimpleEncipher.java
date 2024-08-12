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
        // TODO 数据加密算法
        return data;
    }

    @Override
    public byte[] decipher(byte[] data) {
        // TODO 数据解密算法
        return data;
    }
}
