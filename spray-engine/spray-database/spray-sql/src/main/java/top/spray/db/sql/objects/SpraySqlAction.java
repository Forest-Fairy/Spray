package top.spray.db.sql.objects;

import java.util.HashMap;
import java.util.Map;

public interface SpraySqlAction {
    
    default boolean isAction(SpraySqlAction action) {
        return this.equals(action);
    }

    enum TableAction implements SpraySqlAction { 
        CREATE_TABLE,
        DROP_TABLE,
        ALTER_TABLE,
        RENAME_TABLE,
        ;
    }

    enum ColumnAction implements SpraySqlAction {
        ADD_COLUMN,
        DROP_COLUMN,
        ALTER_COLUMN_STRUCT,
        ALTER_COLUMN_CONSTRAINT,
    }

    enum ConstraintAction implements SpraySqlAction {
        ADD_CONSTRAINT,
        DROP_CONSTRAINT,
        ALTER_CONSTRAINT,
        ;
    }

    enum ViewAction implements SpraySqlAction {
        CREATE_VIEW,
        DROP_VIEW,
        ALTER_VIEW
    }

    enum ProcedureAction implements SpraySqlAction {
        CREATE_PROCEDURE,
        DROP_PROCEDURE,
        ALTER_PROCEDURE
    }

    class ActionWrapper implements SpraySqlAction {
        private final Map<Class<? extends SpraySqlAction>, SpraySqlAction> actionMap;

        public ActionWrapper(SpraySqlAction... actions) {
            if (actions == null || actions.length == 0) {
                throw new IllegalArgumentException("Actions cannot be null or empty");
            }
            this.actionMap = new HashMap<>();
            for (SpraySqlAction action : actions) {
                if (action instanceof TableAction) {
                    this.actionMap.put(TableAction.class, action);
                } else if (action instanceof ColumnAction) {
                    this.actionMap.put(ColumnAction.class, action);
                } else if (action instanceof ConstraintAction) {
                    this.actionMap.put(ConstraintAction.class, action);
                } else if (action instanceof ViewAction) {
                    this.actionMap.put(ViewAction.class, action);
                } else if (action instanceof ProcedureAction) {
                    this.actionMap.put(ProcedureAction.class, action);
                } else if (action instanceof ActionWrapper wrapper) {
                    this.actionMap.putAll(wrapper.actionMap);
                }
                else {
                    throw new IllegalArgumentException("Invalid action: " + action);
                }
            }
        }

        @Override
        public boolean isAction(SpraySqlAction action) {
            return actionMap.values().stream().anyMatch(action::isAction);
        }
    }
}