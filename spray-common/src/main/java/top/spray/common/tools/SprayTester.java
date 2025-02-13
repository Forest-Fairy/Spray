package top.spray.common.tools;

import top.spray.common.bean.SprayFieldUtil;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * a functional class for simplify try catch code block
 * @see SprayFieldUtil#getFieldValue(Object, Field)
 */
public interface SprayTester<T> {
    T test() throws Throwable;

    static <T> T supply(SprayTester<T> tester, Function<Throwable, T> whenError) {
        try {
            return tester.test();
        } catch (Throwable t) {
            return whenError == null ? null : whenError.apply(t);
        }
    }
}