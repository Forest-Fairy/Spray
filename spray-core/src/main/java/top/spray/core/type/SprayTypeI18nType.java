package top.spray.core.type;

import top.spray.core.i18n.SprayResourceBundle;
import top.spray.core.i18n.SprayResourceBundleDef;

@SprayResourceBundle("type")
public interface SprayTypeI18nType extends SprayType {

    @Override
    default String typeName() {
        return SprayResourceBundleDef.get("name", this.getClass(), this.i18nKey());
    }

    @Override
    default String getDescription() {
        return SprayResourceBundleDef.get("description", this.getClass(), this.i18nKey());
    }

    String i18nKey();

}
