package top.spray.common.test;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SM4;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;
import top.spray.common.crypto.SprayCryptos;
import top.spray.common.data.SprayBrotliCompressUtil;
import top.spray.common.analyse.SprayClassInfoUtil;
import top.spray.common.tools.Sprays;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class CommonTester {
    @Test
    public void testBrotli() throws IOException {
        byte[] compressed = SprayBrotliCompressUtil.compress("test brotli".getBytes(Sprays.UTF_8));
        assert "test brotli".equals(new String(SprayBrotliCompressUtil.depress(compressed), Sprays.UTF_8)) :
                new Exception("not right");
    }

    @Test
    public void tmp() {
        System.out.println(URLEncoder.encode("$", StandardCharsets.UTF_8));
    }

    @Test
    public void testRSACrypto() {
        KeyPair pair = SecureUtil.generateKeyPair("RSA");
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        String priKey = Sprays.toString(Base64.getEncoder().encode(
                privateKey.getEncoded()));
        String pubKey = Sprays.toString(Base64.getEncoder().encode(
                publicKey.getEncoded()));

        byte[] enc = SprayCryptos.RSA().encrypt(
                Sprays.getBytes("Hi there"),
                priKey);
        String s = Sprays.toString(
                SprayCryptos.RSA().decrypt(enc, pubKey));
        System.out.println(s);
        assert "Hi there".equals(s);
    }
    @Test
    public void testDESCrypto() {
        SecretKey des = SecureUtil.generateKey("DES");
        String key = Sprays.toString(Base64.getEncoder().encode(
                des.getEncoded()));
        byte[] enc = SprayCryptos.DES().encrypt(
                Sprays.getBytes("Hi there"), key);
        String s = Sprays.toString(
                SprayCryptos.DES().decrypt(enc, key));
        System.out.println(s);
        assert "Hi there".equals(s);
    }
    @Test
    public void testSM4Crypto() throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());

        byte[] encoded = new SM4().getSecretKey().getEncoded();
        String key = Sprays.toString(Base64.getEncoder().encode(encoded));
        byte[] enc = SprayCryptos.SM4().encrypt(
                Sprays.getBytes("Hi there"), key);
        String s = Sprays.toString(
                SprayCryptos.SM4().decrypt(enc, key));
        System.out.println(s);
        assert "Hi there".equals(s);
    }

    @Test
    public void testSprayClassInfoUtil() throws ClassNotFoundException {
        String[] classInfo = SprayClassInfoUtil.getClassInfo("package top.spray.common.test;\n" +
                "\n" +
                "\n" +
                "import cn.hutool.crypto.SecureUtil;\n" +
                "import cn.hutool.crypto.symmetric.SM4;\n" +
                "import org.bouncycastle.jce.provider.BouncyCastleProvider;\n" +
                "import org.junit.Test;\n" +
                "import top.spray.common.crypto.SprayCryptos;\n" +
                "import top.spray.common.util.data.SprayBrotliCompressUtil;\n" +
                "import top.spray.common.util.bean.SprayClassInfoUtil;\n" +
                "import top.spray.common.tools.SprayUtf8s;\n" +
                "\n" +
                "import javax.crypto.KeyGenerator;\n" +
                "import javax.crypto.SecretKey;\n" +
                "import javax.crypto.spec.SecretKeySpec;\n" +
                "import java.io.IOException;\n" +
                "import java.security.*;\n" +
                "import java.util.Base64;\n" +
                "\n" +
                "public class CommonTester {}\n");
        System.out.println("CLASS_NAME: " + classInfo[SprayClassInfoUtil.CLASS_NAME]);
        System.out.println("CLASS_PACKAGE: " + classInfo[SprayClassInfoUtil.CLASS_PACKAGE]);
        System.out.println("CLASS_CONTENT: " + classInfo[SprayClassInfoUtil.CLASS_CONTENT]);
    }

}
