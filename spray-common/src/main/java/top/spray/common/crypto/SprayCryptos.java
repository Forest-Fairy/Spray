package top.spray.common.crypto;

import top.spray.common.bean.SprayServiceUtil;

import java.util.LinkedHashSet;
import java.util.Set;

public class SprayCryptos {
    private SprayCryptos() {}
    public static SprayCrypto DES() {
        return SprayCryptoFactory.getCrypto("DES");
    }
    public static SprayCrypto SM4() {
        return SprayCryptoFactory.getCrypto("SM4");
    }
    public static SprayCrypto RSA() {
        return SprayCryptoFactory.getCrypto("RSA");
    }
    public static SprayCrypto getCrypto(String name) {
        return SprayCryptoFactory.getCrypto(name);
    }
    public static void register(SprayCrypto crypto) {
        SprayCryptoFactory.register(crypto);
    }



    private static class SprayCryptoFactory {
        private static final Set<SprayCrypto> CRYPTO_LIST = new LinkedHashSet<>();

        public static void register(SprayCrypto crypto) {
            CRYPTO_LIST.add(crypto);
        }

        public static SprayCrypto getCrypto(String name) {
            for (SprayCrypto crypto : CRYPTO_LIST) {
                if (crypto.match(name)) {
                    return crypto;
                }
            }
            for (SprayCrypto crypto : SprayServiceUtil.loadServiceClassNameMapCache(SprayCrypto.class).values()) {
                if (crypto.match(name)) {
                    return crypto;
                }
            }
            throw new IllegalArgumentException("crypto: " + name + " not found");
        }
    }
}
