package top.spray.core.base.validate;

public interface SprayValidator {
    String validatorName();
    boolean validate(String field, Object value);
}
