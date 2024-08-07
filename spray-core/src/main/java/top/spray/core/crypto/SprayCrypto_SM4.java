package top.spray.core.crypto;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.symmetric.SM4;
import top.spray.core.util.SprayUtf8s;

public class SprayCrypto_SM4 implements SprayCrypto {
    @Override
    public boolean match(String name) {
        return "SM4".equalsIgnoreCase(name);
    }

    @Override
    public byte[] encrypt(byte[] content, String key) {
        Assert.notBlank(key, "SM4 key could not be blank!");
        SM4 sm4 = new SM4(key.getBytes(SprayUtf8s.Charset));
        return sm4.encrypt(content);
    }

    @Override
    public byte[] decrypt(byte[] content, String key) {
        Assert.notBlank(key, "SM4 key could not be blank!");
        SM4 sm4 = new SM4(key.getBytes(SprayUtf8s.Charset));
        return sm4.decrypt(content);
    }
}
