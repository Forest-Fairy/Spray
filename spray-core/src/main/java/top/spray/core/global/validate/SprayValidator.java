package top.spray.core.global.validate;

public interface SprayValidator {
    String validatorName();
    boolean validate(String field, Object value);
}
