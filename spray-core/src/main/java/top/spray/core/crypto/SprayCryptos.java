package top.spray.core.crypto;

public class SprayCryptos {
    private SprayCryptos() {}

    public static String encrypt(String name, String text, String key) {
        return SprayCryptoFactory.getCrypto(name).encrypt(text, key);
    }
    public static String decrypt(String name, String text, String key) {
        return SprayCryptoFactory.getCrypto(name).decrypt(text, key);
    }

}
