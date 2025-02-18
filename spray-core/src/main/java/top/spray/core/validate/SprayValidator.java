package top.spray.core.validate;

@FunctionalInterface
public interface SprayValidator {
    String validate(String field, Object value);
}
