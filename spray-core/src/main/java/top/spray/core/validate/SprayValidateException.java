package top.spray.core.validate;

import top.spray.core.exception.SprayException;

public class SprayValidateException extends SprayException {

    public SprayValidateException(String validateOptionName, String paramName, Object paramValue) {
        super(validateOptionName, paramName, paramValue);
    }

    public SprayValidateException(String validateOptionName, String paramName, Object paramValue, Throwable throwable) {
        super(validateOptionName, paramName, paramValue);
        this.initCause(throwable);
    }

    @Override
    protected String getBundleNameSuffix() {
        return "validate";
    }
}
