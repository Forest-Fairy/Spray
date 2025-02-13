package top.spray.processor.process.data.rule;

import top.spray.core.global.prop.SprayLanguage;

public interface SprayRule {
    String id();
    String name();
    SprayLanguage type();
    <T> T getAttr(String key);
}
