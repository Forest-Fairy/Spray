package top.spray.core.nio.decode;

import top.spray.core.engine.props.SprayData;

import java.nio.ByteBuffer;

public interface SprayNioDecoder {
    SprayData decode(ByteBuffer byteBuffer);
}
