package top.spray.core.system.type;

import top.spray.core.i18n.Spray_i18nBundleDef;
import top.spray.core.global.prop.SprayUnsupportedOperation;

public interface SprayTypeI18nType extends SprayType {

    @Override
    default String typeName() {
        return Spray_i18nBundleDef.get(this.i18nClass(TargetType.NAME), this.i18nKey());
    }

    @Override
    default String getDescription() {
        return Spray_i18nBundleDef.get(this.i18nClass(TargetType.DESCRIPTION), this.i18nKey());
    }

    default Class<? extends Spray_i18nBundleDef> i18nClass(TargetType target) {
        return switch (target) {
            case NAME -> TargetType.DefaultNameBundle.class;
            case DESCRIPTION -> TargetType.DefaultDescriptionBundle.class;
            default -> unsupported(target);
        };
    }
    default <T> T unsupported(TargetType type) {
        return SprayUnsupportedOperation.unsupported("unsupported target type " + type.name());
    }

    String i18nKey();

    enum TargetType {
        NAME,
        DESCRIPTION,
//        CODE
        ;

        public static class DefaultNameBundle extends SprayType_i18nBundleDef {
            DefaultNameBundle() {}
            @Override
            protected String getTypeNameSuffix() {
                return "name";
            }
        }
        public static class DefaultDescriptionBundle extends SprayType_i18nBundleDef {
            DefaultDescriptionBundle() {}
            @Override
            protected String getTypeNameSuffix() {
                return "description";
            }
        }

    }


}
