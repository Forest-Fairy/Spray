package top.spray.core.crypto;

import top.spray.core.util.SprayServiceUtil;

import java.util.*;

public class SprayCryptoFactory {
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
