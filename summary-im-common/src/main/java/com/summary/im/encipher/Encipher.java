package com.summary.im.encipher;

/**
 * 消息 加密、解密
 *
 * @author jie.luo
 * @since 2024/8/6
 */
public interface Encipher {

    /**
     * 加密
     *
     * @param data 待加密数据
     * @return 加密后数据
     */
    byte[] encipher(byte[] data);

    /**
     * 解密
     *
     * @param data 待解密数据
     * @return 解密后数据
     */
    byte[] decipher(byte[] data);
}
