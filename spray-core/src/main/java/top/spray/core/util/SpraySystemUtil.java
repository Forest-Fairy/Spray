package top.spray.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class SpraySystemUtil {
    public static class Const {
        public static final String MAC_ADDRESS = SpraySystemUtil.MAC_ADDRESS;



    }










    private static final String MAC_ADDRESS;
    static {
        String macAddress;
        try {
            macAddress = Arrays.toString(NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress());
        } catch (SocketException | UnknownHostException ignored) {
            macAddress = "UNKNOWN_MAC_ADDRESS";
        }
        MAC_ADDRESS = macAddress;
    }
}
