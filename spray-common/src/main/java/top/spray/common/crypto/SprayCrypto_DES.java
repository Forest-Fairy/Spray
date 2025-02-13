package top.spray.common.crypto;

import cn.hutool.crypto.symmetric.DES;

public class SprayCrypto_DES extends SprayBaseCrypto {

    public SprayCrypto_DES() {
        super("DES");
    }

    @Override
    protected byte[] encrypt0(byte[] content, String key) {
        DES des = new DES(decode(key));
        return des.encrypt(content);
    }

    @Override
    protected byte[] decrypt0(byte[] content, String key) {
        DES des = new DES(decode(key));
        return des.decrypt(content);
    }
}