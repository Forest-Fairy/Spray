package top.spray.common.crypto;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

public class SprayCrypto_RSA extends SprayBaseCrypto {
    public SprayCrypto_RSA() {
        super("RSA", "RSA private key could not be blank!",
                "RSA public key could not be blank!");
    }

    @Override
    protected byte[] encrypt0(byte[] content, String key) {
        RSA rsa = new RSA(key, null);
        // encrypt with private key
        return rsa.encrypt(content, KeyType.PrivateKey);
    }

    @Override
    protected byte[] decrypt0(byte[] content, String key) {
        RSA rsa = new RSA(null, key);
        return rsa.decrypt(content, KeyType.PublicKey);
    }
}
