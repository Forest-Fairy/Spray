package top.spray.core.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import top.spray.core.i18n.SprayResourceBundleDef;

/**
 * spray logger needs to implement Spray_i18n interface
 * TODO: need performance with judging loglevel
 */
public class SprayLoggerImpl implements SprayLogger {
    protected Logger loggerImpl;
    protected Class<?> logClass;
    
    /**
     * same with calling method #getLogger(String)
     * @param logClass loggerName, usually with class name
     */
    public SprayLoggerImpl(Class<?> logClass) {
        this.logClass = logClass;
        this.loggerImpl = LoggerFactory.getLogger(logClass);
    }

    @Override
    public void trace(boolean i8n, String s) {
        this.loggerImpl.trace(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s) : s
        );
    }


    @Override
    public void trace(boolean i8n, String s, Object o) {
        this.loggerImpl.trace(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o) : s,
                o
        );
    }


    @Override
    public void trace(boolean i8n, String s, Object o, Object o1) {
        this.loggerImpl.trace(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o, o1) : s,
                o, o1
        );
    }


    @Override
    public void trace(boolean i8n, String s, Object... objects) {
        this.loggerImpl.trace(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, objects) : s,
                objects
        );
    }


    @Override
    public void trace(boolean i8n, String s, Throwable throwable) {
        this.loggerImpl.trace(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, throwable) : s,
                throwable
        );
    }


    @Override
    public void trace(boolean i8n, Marker marker, String s) {
        this.loggerImpl.trace(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s) : s
        );
    }


    @Override
    public void trace(boolean i8n, Marker marker, String s, Object o) {
        this.loggerImpl.trace(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o) : s,
                o
        );
    }


    @Override
    public void trace(boolean i8n, Marker marker, String s, Object o, Object o1) {
        this.loggerImpl.trace(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o, o1) : s,
                o, o1
        );
    }


    @Override
    public void trace(boolean i8n, Marker marker, String s, Object... objects) {
        this.loggerImpl.trace(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, objects) : s,
                objects
        );
    }


    @Override
    public void trace(boolean i8n, Marker marker, String s, Throwable throwable) {
        this.loggerImpl.trace(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, throwable) : s,
                throwable
        );
    }


    @Override
    public void debug(boolean i8n, String s) {
        this.loggerImpl.debug(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s) : s
        );
    }


    @Override
    public void debug(boolean i8n, String s, Object o) {
        this.loggerImpl.debug(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o) : s,
                o
        );
    }


    @Override
    public void debug(boolean i8n, String s, Object o, Object o1) {
        this.loggerImpl.debug(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o, o1) : s,
                o, o1
        );
    }


    @Override
    public void debug(boolean i8n, String s, Object... objects) {
        this.loggerImpl.debug(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, objects) : s,
                objects
        );
    }


    @Override
    public void debug(boolean i8n, String s, Throwable throwable) {
        this.loggerImpl.debug(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, throwable) : s,
                throwable
        );
    }


    @Override
    public void debug(boolean i8n, Marker marker, String s) {
        this.loggerImpl.debug(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s) : s
        );
    }


    @Override
    public void debug(boolean i8n, Marker marker, String s, Object o) {
        this.loggerImpl.debug(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o) : s,
                o
        );
    }


    @Override
    public void debug(boolean i8n, Marker marker, String s, Object o, Object o1) {
        this.loggerImpl.debug(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o, o1) : s,
                o, o1
        );
    }


    @Override
    public void debug(boolean i8n, Marker marker, String s, Object... objects) {
        this.loggerImpl.debug(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, objects) : s,
                objects
        );
    }


    @Override
    public void debug(boolean i8n, Marker marker, String s, Throwable throwable) {
        this.loggerImpl.debug(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, throwable) : s,
                throwable
        );
    }


    @Override
    public void info(boolean i8n, String s) {
        this.loggerImpl.info(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s) : s
        );
    }


    @Override
    public void info(boolean i8n, String s, Object o) {
        this.loggerImpl.info(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o) : s,
                o
        );
    }


    @Override
    public void info(boolean i8n, String s, Object o, Object o1) {
        this.loggerImpl.info(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o, o1) : s,
                o, o1
        );
    }


    @Override
    public void info(boolean i8n, String s, Object... objects) {
        this.loggerImpl.info(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, objects) : s,
                objects
        );
    }


    @Override
    public void info(boolean i8n, String s, Throwable throwable) {
        this.loggerImpl.info(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, throwable) : s,
                throwable
        );
    }


    @Override
    public void info(boolean i8n, Marker marker, String s) {
        this.loggerImpl.info(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s) : s
        );
    }


    @Override
    public void info(boolean i8n, Marker marker, String s, Object o) {
        this.loggerImpl.info(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o) : s,
                o
        );
    }


    @Override
    public void info(boolean i8n, Marker marker, String s, Object o, Object o1) {
        this.loggerImpl.info(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o, o1) : s,
                o, o1
        );
    }


    @Override
    public void info(boolean i8n, Marker marker, String s, Object... objects) {
        this.loggerImpl.info(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, objects) : s,
                objects
        );
    }


    @Override
    public void info(boolean i8n, Marker marker, String s, Throwable throwable) {
        this.loggerImpl.info(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, throwable) : s,
                throwable
        );
    }


    @Override
    public void warn(boolean i8n, String s) {
        this.loggerImpl.warn(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s) : s
        );
    }


    @Override
    public void warn(boolean i8n, String s, Object o) {
        this.loggerImpl.warn(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o) : s,
                o
        );
    }


    @Override
    public void warn(boolean i8n, String s, Object... objects) {
        this.loggerImpl.warn(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, objects) : s,
                objects
        );
    }


    @Override
    public void warn(boolean i8n, String s, Object o, Object o1) {
        this.loggerImpl.warn(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o, o1) : s,
                o, o1
        );
    }


    @Override
    public void warn(boolean i8n, String s, Throwable throwable) {
        this.loggerImpl.warn(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, throwable) : s,
                throwable
        );
    }


    @Override
    public void warn(boolean i8n, Marker marker, String s) {
        this.loggerImpl.warn(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s) : s
        );
    }


    @Override
    public void warn(boolean i8n, Marker marker, String s, Object o) {
        this.loggerImpl.warn(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o) : s,
                o
        );
    }


    @Override
    public void warn(boolean i8n, Marker marker, String s, Object o, Object o1) {
        this.loggerImpl.warn(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o, o1) : s,
                o, o1
        );
    }


    @Override
    public void warn(boolean i8n, Marker marker, String s, Object... objects) {
        this.loggerImpl.warn(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, objects) : s,
                objects
        );
    }


    @Override
    public void warn(boolean i8n, Marker marker, String s, Throwable throwable) {
        this.loggerImpl.warn(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, throwable) : s,
                throwable
        );
    }


    @Override
    public void error(boolean i8n, String s) {
        this.loggerImpl.error(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s) : s
        );
    }


    @Override
    public void error(boolean i8n, String s, Object o) {
        this.loggerImpl.error(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o) : s,
                o
        );
    }


    @Override
    public void error(boolean i8n, String s, Object o, Object o1) {
        this.loggerImpl.error(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o, o1) : s,
                o, o1
        );
    }


    @Override
    public void error(boolean i8n, String s, Object... objects) {
        this.loggerImpl.error(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, objects) : s,
                objects
        );
    }


    @Override
    public void error(boolean i8n, String s, Throwable throwable) {
        this.loggerImpl.error(
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, throwable) : s,
                throwable
        );
    }


    @Override
    public void error(boolean i8n, Marker marker, String s) {
        this.loggerImpl.error(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s) : s
        );
    }


    @Override
    public void error(boolean i8n, Marker marker, String s, Object o) {
        this.loggerImpl.error(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o) : s,
                o
        );
    }


    @Override
    public void error(boolean i8n, Marker marker, String s, Object o, Object o1) {
        this.loggerImpl.error(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, o, o1) : s,
                o, o1
        );
    }


    @Override
    public void error(boolean i8n, Marker marker, String s, Object... objects) {
        this.loggerImpl.error(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, objects) : s,
                objects
        );
    }
    
    @Override
    public void error(boolean i8n, Marker marker, String s, Throwable throwable) {
        this.loggerImpl.error(
                marker,
                i8n ? SprayResourceBundleDef.get(null, this.logClass, s, throwable) : s,
                throwable
        );
    }

    @Override
    public String getName() {
        return this.loggerImpl.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return this.loggerImpl.isTraceEnabled();
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.loggerImpl.isTraceEnabled(marker);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.loggerImpl.isDebugEnabled();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.loggerImpl.isDebugEnabled(marker);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.loggerImpl.isInfoEnabled();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.loggerImpl.isInfoEnabled(marker);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.loggerImpl.isWarnEnabled();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.loggerImpl.isWarnEnabled(marker);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.loggerImpl.isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.loggerImpl.isErrorEnabled(marker);
    }
}
