package top.spray.core.crypto;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.symmetric.SM4;

import java.nio.charset.StandardCharsets;

public class SprayCrypto_SM4 implements SprayCrypto {
    @Override
    public boolean match(String name) {
        return "SM4".equalsIgnoreCase(name);
    }

    @Override
    public String encrypt(String text, String key) {
        Assert.notBlank(key, "SM4 key could not be blank!");
        SM4 sm4 = new SM4(key.getBytes(StandardCharsets.UTF_8));
        return new String(sm4.encrypt(text));
    }

    @Override
    public String decrypt(String text, String key) {
        Assert.notBlank(key, "SM4 key could not be blank!");
        SM4 sm4 = new SM4(key.getBytes(StandardCharsets.UTF_8));
        return sm4.decryptStr(text);
    }
}
