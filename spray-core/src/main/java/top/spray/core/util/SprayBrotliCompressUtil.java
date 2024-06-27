package top.spray.core.util;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.decoder.Decoders;
import com.aayushatharva.brotli4j.encoder.Encoders;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SprayBrotliCompressUtil {
    static {
        Brotli4jLoader.ensureAvailability();
        if (!Brotli4jLoader.isAvailable()) {
            throw new RuntimeException("无法加载Brotli压缩库", Brotli4jLoader.getUnavailabilityCause());
        }
    }

    public static String compress(String text) throws IOException {
        if (text == null || text.isEmpty()) {
            return "";
        }
        ByteBuf source = PooledByteBufAllocator.DEFAULT.directBuffer()
                .writeBytes(text.getBytes(StandardCharsets.UTF_8));
        ByteBuf compressed = PooledByteBufAllocator.DEFAULT.directBuffer().asByteBuf();
        Encoders.compress(source, compressed);
        return new String(Base64.getEncoder().encode(ByteBufUtil.getBytes(compressed)));
    }

    public static String depress(String text) throws IOException {
        if (text == null || text.isEmpty()) {
            return text;
        }
        ByteBuf compressed = PooledByteBufAllocator.DEFAULT.directBuffer()
                .writeBytes(Base64.getDecoder().decode(text));
        ByteBuf depressed = PooledByteBufAllocator.DEFAULT.directBuffer().asByteBuf();
        Decoders.decompress(compressed, depressed);
        return StandardCharsets.UTF_8.decode(depressed.nioBuffer()).toString();
    }

}
