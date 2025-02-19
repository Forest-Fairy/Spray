package top.spray.engine.process.processor.data.rule;


import top.spray.core.dynamic.compile.SprayLanguage;

public interface SprayRule {
    String id();
    String name();
    SprayLanguage type();
    <T> T getAttr(String key);
}
