package top.spray.core.base.validate;

import org.apache.commons.lang3.StringUtils;
import top.spray.common.bean.SprayFieldUtil;
import top.spray.common.bean.SprayMethodUtil;

import java.util.function.BiFunction;

public enum SpraySimpleValidator implements SprayValidator {
    NOT_NULL((n, o) -> {
        boolean illegal = o == null;
        return illegal;
    }),
    NOT_EMPTY((n, o) -> {
        boolean illegal = o == null;
        if (! illegal) {
            if (o instanceof String string) {
                illegal = string.isEmpty();
            } else if (o.getClass().isArray()) {
                Integer length = SprayFieldUtil.getLength(o);
                illegal = length == null || length == 0;
            } else {
                Boolean empty = SprayMethodUtil.isEmpty(o);
                if (empty == null) {
                    Integer size = SprayMethodUtil.size(o);
                    if (size != null) {
                        illegal = size == 0;
                    }
                } else {
                    illegal = empty;
                }
            }
        }
        return illegal;
    }),
    NOT_BLANK((n, o) -> {
        boolean illegal = o == null;
        if (! illegal) {
            if (o instanceof CharSequence sequence) {
                illegal = sequence.toString().isBlank();
            }
        }
        return illegal;
    }),
    NOT_ZERO((n, o) -> {
        boolean illegal = o == null;
        if (! illegal) {
            String s = String.valueOf(o).trim();
            if (s.startsWith("+") || s.startsWith("-")) {
                s = s.substring(1);
            }
            illegal = !"0".equals(s);
        }
        return illegal;
    }),
    LTE_ZERO((n, o) -> {
        boolean illegal = o == null;
        if (! illegal) {
            String s = String.valueOf(o).trim();
            String[] numbers = s.split("\\.");
            if (numbers.length > 2) {
                throw e;
            }
            boolean negative;
            if ((negative = s.startsWith("-")) || s.startsWith("+")) {
                numbers[0] = numbers[0].substring(1);
            }
            if (! StringUtils.isNumeric(numbers[0])) {
                throw e;
            } else {
                if (numbers.length == 2) {
                    if (! StringUtils.isNumeric(numbers[1])) {
                        throw e;
                    }
                }
                if (! negative) {
                    throw e;
                }
            }
        }
        return illegal;
    }),
    GTE_ZERO((n, o) -> {
        if (o == null) {
            throw e;
        } else {
            String s = String.valueOf(o).trim();
            String[] numbers = s.split("\\.");
            if (numbers.length > 2) {
                throw e;
            }
            boolean negative;
            if ((negative = s.startsWith("-")) || s.startsWith("+")) {
                numbers[0] = numbers[0].substring(1);
            }
            if (! StringUtils.isNumeric(numbers[0])) {
                throw e;
            } else {
                if (numbers.length == 2) {
                    if (! StringUtils.isNumeric(numbers[1])) {
                        throw e;
                    }
                }
                if (negative) {
                    throw e;
                }
            }
        }
    }),
    ;
    private final BiFunction<String, Object, Boolean> validator;

    SpraySimpleValidator(BiFunction<String, Object, Boolean> validator) {
        this.validator = validator;
    }

    @Override
    public String validatorName() {
        return SpraySimpleValidator.class.getName();
    }

    @Override
    public boolean validate(String paramName, Object paramValue) {
        return ! validator.apply(paramName, paramValue);
    }



}
