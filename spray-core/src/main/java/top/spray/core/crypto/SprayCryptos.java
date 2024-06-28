package top.spray.core.crypto;

public class SprayCryptos {
    private SprayCryptos() {}
    public static SprayCrypto getCrypto(String name) {
        return SprayCryptoFactory.getCrypto(name);
    }
    public static SprayCrypto DES() {
        return SprayCryptoFactory.getCrypto("DES");
    }
    public static SprayCrypto SM4() {
        return SprayCryptoFactory.getCrypto("SM4");
    }
    public static SprayCrypto RSA() {
        return SprayCryptoFactory.getCrypto("RSA");
    }

}
