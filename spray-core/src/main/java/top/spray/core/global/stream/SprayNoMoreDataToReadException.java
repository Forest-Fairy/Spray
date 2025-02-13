package top.spray.core.global.stream;

import top.spray.core.global.exception.SprayException;

public class SprayNoMoreDataToReadException extends SprayException {
    public SprayNoMoreDataToReadException() {
        super("data.done");
    }
    @Override
    protected String getBundleNameSuffix() {
        return null;
    }

}
