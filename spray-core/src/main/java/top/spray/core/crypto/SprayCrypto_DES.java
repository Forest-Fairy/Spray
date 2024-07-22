package top.spray.core.crypto;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.symmetric.DES;
import top.spray.core.i18n.SprayUtf8s;

public class SprayCrypto_DES implements SprayCrypto {

    @Override
    public boolean match(String name) {
        return "DES".equalsIgnoreCase(name);
    }

    @Override
    public byte[] encrypt(byte[] content, String key) {
        Assert.notBlank(key, "DES key could not be blank!");
        DES des = new DES(key.getBytes(SprayUtf8s.Charset));
        return des.encrypt(content);
    }

    @Override
    public byte[] decrypt(byte[] content, String key) {
        Assert.notBlank(key, "DES key could not be blank!");
        DES des = new DES(key.getBytes(SprayUtf8s.Charset));
        return des.decrypt(content);
    }
}