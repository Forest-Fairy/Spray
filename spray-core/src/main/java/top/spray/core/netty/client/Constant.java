package top.spray.core.netty.client;

public class Constant {
    // 常量，Client需要的一些参数信息，需要根据实际情况进行自定义修改
    static final String HOST = "127.0.0.1";   // 要连接的服务器地址
    static final int PORT = 8888;   // 要连接的服务器端口号
    static final String CLIENT_PREFIX = "Client received:";   // Client显示接收到Server发送的信息的前缀
}