package top.spray.common.crypto;

public interface SprayCrypto {
    static void register(SprayCrypto crypto) {
        SprayCryptos.SprayCryptoFactory.register(crypto);
    }
    boolean match(String name);
    byte[] encrypt(byte[] content, String key);
    byte[] decrypt(byte[] content, String key);
}
