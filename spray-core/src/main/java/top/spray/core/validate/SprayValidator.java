package top.spray.core.validate;

public interface SprayValidator {
    String validatorName();
    boolean validate(String field, Object value);
}
