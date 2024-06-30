package top.spray.core.crypto;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

public class SprayCrypto_RSA implements SprayCrypto {
    @Override
    public boolean match(String name) {
        return "RSA".equalsIgnoreCase(name);
    }

    @Override
    public byte[] encrypt(byte[] content, String key) {
        Assert.notBlank(key, "RSA private key could not be blank!");
        RSA rsa = new RSA(key, null);
        // encrypt with private key
        return rsa.encrypt(content, KeyType.PrivateKey);
    }

    @Override
    public byte[] decrypt(byte[] content, String key) {
        Assert.notBlank(key, "RSA public key could not be blank!");
        RSA rsa = new RSA(null, key);
        return rsa.decrypt(content, KeyType.PublicKey);
    }
}
