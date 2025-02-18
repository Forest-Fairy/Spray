package top.spray.core.stream;

import top.spray.core.exception.SprayException;

public class SprayNoMoreDataToReadException extends SprayException {
    public SprayNoMoreDataToReadException() {
        super("data.done");
    }
    @Override
    protected String getBundleNameSuffix() {
        return null;
    }

}
