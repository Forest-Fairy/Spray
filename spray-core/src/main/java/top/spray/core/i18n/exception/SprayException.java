package top.spray.core.i18n.exception;

import top.spray.core.i18n.message.SprayMessageObject;

public class SprayException extends RuntimeException implements SprayMessageObject {
    @Override
    public String getBundleName() {
    }


}
