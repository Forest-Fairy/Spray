package top.spray.core.netty.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spray.netty.server")
public class SprayServerProperties {

    // 常量，Server需要的一些参数信息，需要根据实际情况进行自定义修改
//    static final int PORT = 8888;   // 可以自定义端口号
    private int port;

    static final String CLIENT_PREFIX = "Server received:";   // Server显示接收到client发送的信息的前缀
    static final String SERVER_PREFIX = "Server send:";   // Server将接收到client发送的信息返回给client的前缀
}