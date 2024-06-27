package top.spray.core.crypto;

public class SprayCryptos {
    private SprayCryptos() {}

    public static String encrypt(String text, String key, String name) {
        return SprayCryptoFactory.getCrypto(name).encrypt(text, key);
    }
    public static String decrypt(String text, String key, String name) {
        return SprayCryptoFactory.getCrypto(name).decrypt(text, key);
    }

}
