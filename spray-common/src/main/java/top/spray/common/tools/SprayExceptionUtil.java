package top.spray.common.tools;

import cn.hutool.core.exceptions.ExceptionUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

public class SprayExceptionUtil extends ExceptionUtil {
    public static String getDetailTraceMessage(Throwable e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        return out.toString();
    }


}
