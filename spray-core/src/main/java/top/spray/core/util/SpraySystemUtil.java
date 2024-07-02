package top.spray.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class SpraySystemUtil {
    private static final String SPRAY_VERSION = SpraySystemUtil.getConfig("spray.version");
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

    public static String getConfig(String key) {
        return getConfig(key, "");
    }
    public static String getConfig(String key, String defVal) {
        return defVal;
    }
    public static String getMacAddress() {
        return MAC_ADDRESS;
    }
    public static String getSystemVersion() {
        return SPRAY_VERSION;
    }
}
