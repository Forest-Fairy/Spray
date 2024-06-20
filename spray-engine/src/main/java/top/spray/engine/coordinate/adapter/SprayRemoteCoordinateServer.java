package top.spray.engine.coordinate.adapter;


public interface SprayRemoteCoordinateServer extends Runnable, AutoCloseable{
    String getServerId();
    String getServerName();

    String getServerHost();
    int getServerPort();

    /**
     * start listen
     */
    @Override
    void run();

    @Override
    void close();


}
