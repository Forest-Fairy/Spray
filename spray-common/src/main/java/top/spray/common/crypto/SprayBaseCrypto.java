package top.spray.common.crypto;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;

abstract class SprayBaseCrypto implements SprayCrypto {
    protected final String name;
    protected final String encryptKeyAssertion;
    protected final String decryptKeyAssertion;

    protected SprayBaseCrypto(String name) {
        this(name, name + " key could not be blank!", name + " key could not be blank!");
    }
    protected SprayBaseCrypto(String name, String encryptKeyAssertion, String decryptKeyAssertion) {
        this.name = name;
        this.encryptKeyAssertion = encryptKeyAssertion;
        this.decryptKeyAssertion = decryptKeyAssertion;
    }

    @Override
    public boolean match(String name) {
        return this.name.equals(name);
    }

    @Override
    public final byte[] encrypt(byte[] content, String key) {
        try {
            assertBlank(key, this.encryptKeyAssertion);
            return encrypt0(content, key);
        } catch (Throwable throwable) {
            throw new SprayCryptoException(
                    StrUtil.format("failed to encrypt content with {} crypto", name),
                    throwable);
        }
    }

    @Override
    public final byte[] decrypt(byte[] content, String key) {
        try {
            assertBlank(key, this.decryptKeyAssertion);
            return decrypt0(content, key);
        } catch (Throwable throwable) {
            throw new SprayCryptoException(
                    StrUtil.format("failed to decrypt content with {} crypto", name),
                    throwable);
        }
    }

    protected abstract byte[] encrypt0(byte[] content, String key);

    protected abstract byte[] decrypt0(byte[] content, String key);

    private void assertBlank(String key, String msg) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException(msg);
        }
    }
    protected byte[] decode(String key) {
        return Base64.getDecoder().decode(key);
    }
}
