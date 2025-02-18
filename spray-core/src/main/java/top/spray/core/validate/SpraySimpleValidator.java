package top.spray.core.validate;

import org.apache.commons.lang3.StringUtils;
import top.spray.common.bean.SprayFieldUtil;
import top.spray.common.bean.SprayMethodUtil;

import java.util.function.BiFunction;

public enum SpraySimpleValidator implements SprayValidator {
    NOT_NULL((n, o) -> {
        boolean valid = o != null;
        return valid ? null : String.format("field [%s] is null", n) ;
    }),
    NOT_EMPTY((n, o) -> {
        if (o == null) {
            return String.format("field [%s] is null", n);
        }
        if (o instanceof String string) {
            if (string.isEmpty()) {
                return String.format("field [%s] is empty", n);
            }
        } else if (o.getClass().isArray()) {
            Integer length = SprayFieldUtil.getLength(o);
            if (length == null || length == 0) {
                return String.format("field [%s] is empty", n);
            }
        } else {
            Boolean empty = SprayMethodUtil.isEmpty(o);
            if (empty == null) {
                Integer size = SprayMethodUtil.size(o);
                if (size != null && size == 0) {
                    return String.format("field [%s] is empty", n);
                }
            } else if (empty) {
                return String.format("field [%s] is empty", n);
            }
        }
        return null;
    }),
    NOT_BLANK((n, o) -> {
        if (o == null) {
            return String.format("field [%s] is null", n);
        }
        if (o instanceof CharSequence sequence) {
            if (sequence.toString().isBlank()) {
                return String.format("field [%s] is blank", n);
            }
        }
        return null;
    }),
    NOT_ZERO((n, o) -> {
        if (o == null) {
            return String.format("field [%s] is null", n);
        }
        String s = String.valueOf(o).trim();
        if (s.startsWith("+") || s.startsWith("-")) {
            s = s.substring(1).trim();
        }
        if (!"0".equals(s)) {
            return String.format("field [%s] is not zero", n);
        }
        return null;
    }),
    LTE_ZERO((n, o) -> {
        if (o == null) {
            return String.format("field [%s] is null", n);
        }
        String s = String.valueOf(o).trim();
        String[] numbers = s.split("\\.");
        if (numbers.length > 2) {
            return String.format("field [%s] is not a valid number", n);
        }
        boolean negative;
        if ((negative = s.startsWith("-")) || s.startsWith("+")) {
            numbers[0] = numbers[0].substring(1).trim();
        }
        if (!StringUtils.isNumeric(numbers[0])) {
            return String.format("field [%s] is not a valid number", n);
        }
        if (numbers.length == 2) {
            if (!StringUtils.isNumeric(numbers[1])) {
                return String.format("field [%s] is not a valid number", n);
            }
        }
        if (!negative) {
            // 需要判断是否是 0 或者 ...0000.000..
            if (StringUtils.countMatches(numbers[0], "0") != numbers[0].length()) {
                return String.format("field [%s] is greater than zero", n);
            }
            if (numbers.length == 2 && StringUtils.countMatches(numbers[1], "0") != numbers[1].trim().length()) {
                return String.format("field [%s] is greater than zero", n);
            }
        }
        return null;
    }),
    GTE_ZERO((n, o) -> {
        if (o == null) {
            return String.format("field [%s] is null", n);
        }
        String s = String.valueOf(o).trim();
        String[] numbers = s.split("\\.");
        if (numbers.length > 2) {
            return String.format("field [%s] is not a valid number", n);
        }
        boolean negative;
        if ((negative = s.startsWith("-")) || s.startsWith("+")) {
            numbers[0] = numbers[0].substring(1).trim();
        }
        if (!StringUtils.isNumeric(numbers[0])) {
            return String.format("field [%s] is not a valid number", n);
        }
        if (numbers.length == 2) {
            if (!StringUtils.isNumeric(numbers[1])) {
                return String.format("field [%s] is not a valid number", n);
            }
        }
        if (negative) {
            // 需要判断是否是 0 或者 ...0000.000..
            if (StringUtils.countMatches(numbers[0], "0") != numbers[0].length()) {
                return String.format("field [%s] is less than zero", n);
            }
            if (numbers.length == 2 && StringUtils.countMatches(numbers[1], "0") != numbers[1].trim().length()) {
                return String.format("field [%s] is less than zero", n);
            }
        }
        return null;
    }),
    ;
    private final BiFunction<String, Object, String> validator;

    SpraySimpleValidator(BiFunction<String, Object, String> validator) {
        this.validator = validator;
    }


    @Override
    public String validate(String paramName, Object paramValue) {
        return validator.apply(paramName, paramValue);
    }



}
