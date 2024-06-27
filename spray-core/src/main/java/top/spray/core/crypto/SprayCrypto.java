package top.spray.core.crypto;


public interface SprayCrypto {
    static void register(SprayCrypto crypto) {
        SprayCryptoFactory.register(crypto);
    }
    boolean match(String name);
    String encrypt(String text, String key);
    String decrypt(String text, String key);
}
