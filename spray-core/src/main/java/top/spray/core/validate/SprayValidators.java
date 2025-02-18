package top.spray.core.validate;

import top.spray.common.bean.SprayServiceUtil;
import top.spray.core.global.exception.SprayCoreException;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class SprayValidators {
    public static final SprayValidators INSTANCE = new SprayValidators();


    public static final String FAILED_MSG_SEPARATOR = "@validator:";

    private static final Map<String, List<SprayValidator>> VALIDATORS = new LinkedHashMap<>();
    public static final String DEFAULT_NAMESPACE = "default";
    static {
        SprayServiceUtil.loadServiceClassNameMap(SprayValidator.class).values().forEach(SprayValidators::register);
    }

    public static void register(SprayValidator validator) {
        register(DEFAULT_NAMESPACE, validator);
    }

    public static void register(String namespace, SprayValidator validator) {
        VALIDATORS.computeIfAbsent(namespace, k -> new LinkedList<>()).add(validator);
    }


    public static List<SprayValidator> list(String namespace) {
        return VALIDATORS.get(namespace);
    }

    /**
     * validate
     * @param bean bean to be validated with
     * @param fieldValidatorMap field's validator map
     * @param failed failed handler  key: {fieldName}@validator:{validatorName} value: {fieldValue}
     */
    public static void validate(Map<String, Object> bean, Map<String, List<SprayValidator>> fieldValidatorMap, BiConsumer<String, Object> failed) {
        for (Map.Entry<String, Object> entry : bean.entrySet()) {
            SprayValidator curValidator = null;
            List<SprayValidator> validators = fieldValidatorMap.get(entry.getKey());
            if (validators == null || validators.isEmpty()) {
                continue;
            }
            try {
                for (SprayValidator validator : validators) {
                    curValidator = validator;
                    if (! validator.validate(entry.getKey(), entry.getValue())) {
                        failed.accept(entry.getKey() + "@validator:" + validator.validatorName(), entry.getValue());
                    }
                }
            } catch (Exception error) {
                if (curValidator == null) {
                    throw new SprayCoreException(error);
                }
                throw new SprayValidateException(curValidator.validatorName(), entry.getKey(), String.valueOf(entry.getValue()), error);
            }
        }
    }


}
