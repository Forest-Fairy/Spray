package top.spray.core.crypto;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.symmetric.DES;

import java.nio.charset.StandardCharsets;

public class SprayCrypto_DES implements SprayCrypto {

    @Override
    public boolean match(String name) {
        return "DES".equalsIgnoreCase(name);
    }

    @Override
    public byte[] encrypt(byte[] content, String key) {
        Assert.notBlank(key, "DES key could not be blank!");
        DES des = new DES(key.getBytes(StandardCharsets.UTF_8));
        return des.encrypt(content);
    }

    @Override
    public byte[] decrypt(byte[] content, String key) {
        Assert.notBlank(key, "DES key could not be blank!");
        DES des = new DES(key.getBytes(StandardCharsets.UTF_8));
        return des.decrypt(content);
    }
}