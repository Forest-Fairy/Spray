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
    public String encrypt(String text, String key) {
        Assert.notBlank(key, "RSA private key could not be blank!");
        RSA rsa = new RSA(key, null);
        // encrypt with private key
        return rsa.encryptBase64(text, KeyType.PrivateKey);
    }

    @Override
    public String decrypt(String text, String key) {
        Assert.notBlank(key, "RSA public key could not be blank!");
        RSA rsa = new RSA(null, key);
        return rsa.decryptStr(text, KeyType.PublicKey);
    }
}
