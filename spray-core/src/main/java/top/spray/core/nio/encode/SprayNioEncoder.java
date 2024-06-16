package top.spray.core.nio.encode;

import top.spray.core.engine.props.SprayData;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public interface SprayNioEncoder {
    ByteBuffer encode(SprayData sprayData);

}
