package top.spray.core.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.*;
import top.spray.core.engine.props.SprayData;

public class SprayDataUtil {
    public static String toJson(Object bean) {
        return toJson(bean, false);
    }
    public static String toJson(Object bean, boolean pretty) {
        return pretty ? JSON.toJSONString(bean, JSONWriter.Feature.PrettyFormat) : JSON.toJSONString(bean);
    }
    private static <T> boolean isSprayDataClass(Class<T> tClass) {
        return SprayData.class.isAssignableFrom(tClass);
    }

    public static <T> T parseJson(String jsonText, Class<T> tClass) {
        return isSprayDataClass(tClass) ? (T) parseToSprayData(jsonText) :
                JSON.parseObject(jsonText, tClass);
    }


    public static SprayData parseToSprayData(String jsonText) {
        SprayData sprayData = JSON.parseObject(jsonText, SprayData.class);
        return convertMapToSprayData(sprayData);
    }
    private static SprayData convertMapToSprayData(Map<?, ?> map) {
        if (map == null) {
            return null;
        }
        SprayData sprayData = new SprayData();
        map.forEach((k, v) -> {
            String key = k == null ? null : String.valueOf(k);
            if (v instanceof Map<?, ?> m) {
                sprayData.put(key, convertMapToSprayData(m));
            } else if (v instanceof List<?> l) {
                if (l.isEmpty()) {
                    sprayData.put(key, new JSONArray());
                } else {
                    List<Object> copyList = new JSONArray();
                    for (Object o : l) {
                        if (o instanceof Map<?,?> m) {
                            copyList.add(convertMapToSprayData(m));
                        } else {
                            copyList.add(o);
                        }
                    }
                    sprayData.put(key, copyList);
                }
            } else {
                sprayData.put(key, v);
            }
        });
        return sprayData;
    }

    public static <T> List<T> parseJsonList(String jsonText, Class<T> tClass) {
        return isSprayDataClass(tClass) ? (List<T>) parseToSprayDataList(jsonText) : JSON.parseArray(jsonText, tClass);
    }

    public static List<SprayData> parseToSprayDataList(String jsonText) {
        List<SprayData> sprayData = JSON.parseArray(jsonText, SprayData.class);
        return sprayData.stream().map(SprayDataUtil::convertMapToSprayData).collect(Collectors.toList());
    }

    public static JSONPath createJsonPath(String jsonPath, JSONPath.Feature... features) {
        return (features == null || features.length == 0) ?
                JSONPath.of(jsonPath) :
                JSONPath.of(jsonPath, features);
    }

    public static Object getByJsonPath(JSONPath jsonPath, Object targetObj) {
        return jsonPath.eval(targetObj);
    }



    public static <T> T convertValue(Object val, Class<? super T> tClass) {
        if (val == null) {
            return null;
        } else if (tClass.isAssignableFrom(val.getClass())) {
            return (T) val;
        }
        Object result = val;
        if (String.class.isAssignableFrom(tClass)) {
            result = val.toString();
        } else {
            String trimString;
            try {
                trimString = val.toString().trim();
            } catch (Exception e) {
                trimString = null;
            }
            if (Integer.class.isAssignableFrom(tClass)) {
                result = Integer.valueOf(trimString);
            } else if (Long.class.isAssignableFrom(tClass)) {
                result = Long.valueOf(trimString);
            } else if (Double.class.isAssignableFrom(tClass)) {
                result = Double.valueOf(trimString);
            } else if (Boolean.class.isAssignableFrom(tClass)) {
                if ("f".equalsIgnoreCase(trimString)) {
                    result = Boolean.FALSE;
                } else if ("t".equalsIgnoreCase(trimString)) {
                    result = Boolean.TRUE;
                } else {
                    result = Boolean.valueOf(trimString);
                }
            } else {
                try {
                    // TODO cast with value castor util
                    result = tClass.getMethod("valueOf", String.class)
                            .invoke(null, trimString);
                } catch (Exception ignored) {
                }
            }
        }
        return (T) tClass.cast(result);
    }

}

