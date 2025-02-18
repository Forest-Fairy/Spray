package top.spray.core.logger;

import org.slf4j.Logger;
import org.slf4j.Marker;
import top.spray.core.i18n.SprayResourceBundleDef;

/**
 * enhanced logger with spray i18n support
 */
public interface SprayLogger extends Logger {
    static SprayLogger create(Class<? extends SprayResourceBundleDef> c) {
        return new SprayLoggerImpl(c);
    }

    void trace(boolean i8n, String s) ;


    void trace(boolean i8n, String s, Object o) ;


    void trace(boolean i8n, String s, Object o, Object o1) ;


    void trace(boolean i8n, String s, Object... objects) ;


    void trace(boolean i8n, String s, Throwable throwable) ;


    void trace(boolean i8n, Marker marker, String s) ;


    void trace(boolean i8n, Marker marker, String s, Object o) ;


    void trace(boolean i8n, Marker marker, String s, Object o, Object o1) ;


    void trace(boolean i8n, Marker marker, String s, Object... objects) ;


    void trace(boolean i8n, Marker marker, String s, Throwable throwable) ;


    void debug(boolean i8n, String s) ;


    void debug(boolean i8n, String s, Object o) ;


    void debug(boolean i8n, String s, Object o, Object o1) ;


    void debug(boolean i8n, String s, Object... objects) ;


    void debug(boolean i8n, String s, Throwable throwable) ;


    void debug(boolean i8n, Marker marker, String s) ;


    void debug(boolean i8n, Marker marker, String s, Object o) ;


    void debug(boolean i8n, Marker marker, String s, Object o, Object o1) ;


    void debug(boolean i8n, Marker marker, String s, Object... objects) ;


    void debug(boolean i8n, Marker marker, String s, Throwable throwable) ;


    void info(boolean i8n, String s) ;


    void info(boolean i8n, String s, Object o) ;


    void info(boolean i8n, String s, Object o, Object o1) ;


    void info(boolean i8n, String s, Object... objects) ;


    void info(boolean i8n, String s, Throwable throwable) ;


    void info(boolean i8n, Marker marker, String s) ;

    void info(boolean i8n, Marker marker, String s, Object o) ;


    void info(boolean i8n, Marker marker, String s, Object o, Object o1) ;


    void info(boolean i8n, Marker marker, String s, Object... objects) ;


    void info(boolean i8n, Marker marker, String s, Throwable throwable) ;


    void warn(boolean i8n, String s) ;


    void warn(boolean i8n, String s, Object o) ;


    void warn(boolean i8n, String s, Object... objects) ;


    void warn(boolean i8n, String s, Object o, Object o1) ;


    void warn(boolean i8n, String s, Throwable throwable) ;


    void warn(boolean i8n, Marker marker, String s) ;


    void warn(boolean i8n, Marker marker, String s, Object o) ;


    void warn(boolean i8n, Marker marker, String s, Object o, Object o1) ;


    void warn(boolean i8n, Marker marker, String s, Object... objects) ;


    void warn(boolean i8n, Marker marker, String s, Throwable throwable) ;


    void error(boolean i8n, String s) ;


    void error(boolean i8n, String s, Object o) ;


    void error(boolean i8n, String s, Object o, Object o1) ;


    void error(boolean i8n, String s, Object... objects) ;


    void error(boolean i8n, String s, Throwable throwable) ;


    void error(boolean i8n, Marker marker, String s) ;


    void error(boolean i8n, Marker marker, String s, Object o) ;


    void error(boolean i8n, Marker marker, String s, Object o, Object o1) ;


    void error(boolean i8n, Marker marker, String s, Object... objects) ;


    void error(boolean i8n, Marker marker, String s, Throwable throwable) ;


    @Override
    default void trace(String s) {
        this.trace(true, s);
    }

    @Override
    default void trace(String s, Object o) {
        this.trace(true, s, o);
    }

    @Override
    default void trace(String s, Object o, Object o1) {
        this.trace(true, s, o, o1);
    }

    @Override
    default void trace(String s, Object... objects) {
        this.trace(true, s, objects);
    }

    @Override
    default void trace(String s, Throwable throwable) {
        this.trace(true, s, throwable);
    }

    @Override
    default void trace(Marker marker, String s) {
        this.trace(true, marker, s);
    }

    @Override
    default void trace(Marker marker, String s, Object o) {
        this.trace(true, marker, s, o);
    }

    @Override
    default void trace(Marker marker, String s, Object o, Object o1) {
        this.trace(true, marker, s, o, o1);
    }

    @Override
    default void trace(Marker marker, String s, Object... objects) {
        this.trace(true, marker, s, objects);
    }

    @Override
    default void trace(Marker marker, String s, Throwable throwable) {
        this.trace(true, marker, s, throwable);
    }

    @Override
    default void debug(String s) {
        this.debug(true, s);
    }

    @Override
    default void debug(String s, Object o) {
        this.debug(true, s, o);
    }

    @Override
    default void debug(String s, Object o, Object o1) {
        this.debug(true, s, o, o1);
    }

    @Override
    default void debug(String s, Object... objects) {
        this.debug(true, s, objects);
    }

    @Override
    default void debug(String s, Throwable throwable) {
        this.debug(true, s, throwable);
    }

    @Override
    default void debug(Marker marker, String s) {
        this.debug(true, marker, s);
    }

    @Override
    default void debug(Marker marker, String s, Object o) {
        this.debug(true, marker, s, o);
    }

    @Override
    default void debug(Marker marker, String s, Object o, Object o1) {
        this.debug(true, marker, s, o, o1);
    }

    @Override
    default void debug(Marker marker, String s, Object... objects) {
        this.debug(true, marker, s, objects);
    }

    @Override
    default void debug(Marker marker, String s, Throwable throwable) {
        this.debug(true, marker, s, throwable);
    }

    @Override
    default void info(String s) {
        this.info(true, s);
    }

    @Override
    default void info(String s, Object o) {
        this.info(true, s, o);
    }

    @Override
    default void info(String s, Object o, Object o1) {
        this.info(true, s, o, o1);
    }

    @Override
    default void info(String s, Object... objects) {
        this.info(true, s, objects);
    }

    @Override
    default void info(String s, Throwable throwable) {
        this.info(true, s, throwable);
    }

    @Override
    default void info(Marker marker, String s) {
        this.info(true, marker, s);
    }

    @Override
    default void info(Marker marker, String s, Object o) {
        this.info(true, marker, s, o);
    }

    @Override
    default void info(Marker marker, String s, Object o, Object o1) {
        this.info(true, marker, s, o, o1);
    }

    @Override
    default void info(Marker marker, String s, Object... objects) {
        this.info(true, marker, s, objects);
    }

    @Override
    default void info(Marker marker, String s, Throwable throwable) {
        this.info(true, marker, s, throwable);
    }

    @Override
    default void warn(String s) {
        this.warn(true, s);
    }

    @Override
    default void warn(String s, Object o) {
        this.warn(true, s, o);
    }

    @Override
    default void warn(String s, Object... objects) {
        this.warn(true, s, objects);
    }

    @Override
    default void warn(String s, Object o, Object o1) {
        this.warn(true, s, o, o1);
    }

    @Override
    default void warn(String s, Throwable throwable) {
        this.warn(true, s, throwable);
    }

    @Override
    default void warn(Marker marker, String s) {
        this.warn(true, marker, s);
    }

    @Override
    default void warn(Marker marker, String s, Object o) {
        this.warn(true, marker, s, o);
    }

    @Override
    default void warn(Marker marker, String s, Object o, Object o1) {
        this.warn(true, marker, s, o, o1);
    }

    @Override
    default void warn(Marker marker, String s, Object... objects) {
        this.warn(true, marker, s, objects);
    }

    @Override
    default void warn(Marker marker, String s, Throwable throwable) {
        this.warn(true, marker, s, throwable);
    }

    @Override
    default void error(String s) {
        this.error(true, s);
    }

    @Override
    default void error(String s, Object o) {
        this.error(true, s, o);
    }

    @Override
    default void error(String s, Object o, Object o1) {
        this.error(true, s, o, o1);
    }

    @Override
    default void error(String s, Object... objects) {
        this.error(true, s, objects);
    }

    @Override
    default void error(String s, Throwable throwable) {
        this.error(true, s, throwable);
    }

    @Override
    default void error(Marker marker, String s) {
        this.error(true, marker, s);
    }

    @Override
    default void error(Marker marker, String s, Object o) {
        this.error(true, marker, s, o);
    }

    @Override
    default void error(Marker marker, String s, Object o, Object o1) {
        this.error(true, marker, s, o, o1);
    }

    @Override
    default void error(Marker marker, String s, Object... objects) {
        this.error(true, marker, s, objects);
    }

    @Override
    default void error(Marker marker, String s, Throwable throwable) {
        this.error(true, marker, s, throwable);
    }
}
