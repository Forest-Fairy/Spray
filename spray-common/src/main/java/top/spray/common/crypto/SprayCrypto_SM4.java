package top.spray.common.crypto;

import cn.hutool.crypto.symmetric.SM4;

public class SprayCrypto_SM4 extends SprayBaseCrypto {
    public SprayCrypto_SM4() {
        super("SM4");
    }

    @Override
    protected byte[] encrypt0(byte[] content, String key) {
        SM4 sm4 = new SM4(decode(key));
        return sm4.encrypt(content);
    }

    @Override
    protected byte[] decrypt0(byte[] content, String key) {
        SM4 sm4 = new SM4(decode(key));
        return sm4.decrypt(content);
    }
}
