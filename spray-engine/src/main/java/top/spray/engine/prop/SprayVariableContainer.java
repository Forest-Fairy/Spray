package top.spray.engine.prop;

import top.spray.core.engine.props.SprayData;
import top.spray.core.util.SprayDataUtil;
import top.spray.engine.coordinate.coordinator.SprayProcessCoordinator;
import top.spray.engine.step.executor.SprayProcessStepExecutor;

import java.util.*;

public class SprayVariableContainer {
    private final Map<String, SprayVariableContainer> ancestors;
    private final Set<String> bannedKeys;
    private final String creator;
    private final long createTime;
    private final SprayData data;
    private final String key;

    private SprayVariableContainer(String creator, long createTime, SprayData data, String key) {
        this.creator = creator;
        this.createTime = createTime;
        this.data = data;
        this.key = key;
        this.ancestors = new HashMap<>(1);
        this.bannedKeys = new HashSet<>(0);
    }

    public String creator() {
        return creator;
    }

    public long createTime() {
        return createTime;
    }


    public Object get(SprayProcessStepExecutor lastExecutor, SprayProcessStepExecutor executor, String key) {
        return this.get(lastExecutor, executor, key, Object.class);
    }
    public String getJsonString(SprayProcessStepExecutor lastExecutor, SprayProcessStepExecutor executor, String key) {
        Object o = this.get(lastExecutor, executor, key);
        if (o == null) {
            return null;
        }
        return SprayDataUtil.toJson(o);
    }

    public <T> T get(SprayProcessStepExecutor lastExecutor, SprayProcessStepExecutor executor, String key, Class<T> clazz) {
        // TODO compute value with ancestors

    }

    public <T> T getOrElse(SprayProcessStepExecutor lastExecutor, SprayProcessStepExecutor executor, String key, T def) {
        Object o = this.get(lastExecutor, executor, key);
        if (o == null) {
            return def;
        }

        return o;
    }

    public <T> T computeIfAbsent(SprayProcessStepExecutor lastExecutor, SprayProcessStepExecutor executor, String key, T value) {
        if (value == null) {
            throw new IllegalArgumentException("can not compute with null");
        }
        T o = (T) data.get(key, value.getClass());
        if (o == null) {
            this.set(executor, key, value);
            o = value;
        }
        return o;
    }

    public void remove(SprayProcessStepExecutor executor, String key) {
        data.remove(key);
    }

    public SprayVariableContainer set(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public String identityDataKey() {
        return key;
    }

    public String nextKey(SprayProcessStepExecutor lastExecutor, SprayProcessStepExecutor executor) {
        return generateKey(lastExecutor, this, executor);
    }

//    private void registerListener(Listener listener) {
//        this.listeners.add(listener);
//    }
//    private void onSet(SprayProcessStepExecutor executor, String key, Object value) {
//        listeners.forEach(listener -> listener.onSet(executor, key, value));
//    }
//    private void onRemove(SprayProcessStepExecutor executor, String key) {
//        listeners.forEach(listener -> listener.onRemove(executor, key));
//    }
//
//    private void receiveSet(Listener listener, SprayProcessStepExecutor executor, String key, Object value) {
//        set(executor, key, value);
//    }
//    private void receiveRemove(Listener listener, SprayProcessStepExecutor executor, String key) {
//        remove(executor, key);
//    }



    public static SprayVariableContainer create(SprayProcessCoordinator coordinator) {
        String creator = coordinator.getMeta().getName() + "[" + coordinator.getMeta().transactionId() + "]";
        long createTime = System.currentTimeMillis();
        SprayData data = SprayData.deepCopy(coordinator.getMeta().getDefaultProcessData());
        String key = createTime + "#" + creator;
        return new SprayVariableContainer(creator, createTime, data, key);
    }
    public static SprayVariableContainer easyCopy(SprayProcessStepExecutor lastExecutor, SprayVariableContainer last, SprayProcessStepExecutor executor) {
        return new SprayVariableContainer(
                executor.getExecutorNameKey(),
                System.currentTimeMillis(),
                new SprayData(last.data),
                generateKey(lastExecutor, last, executor));
    }
    public static SprayVariableContainer deepCopy(SprayProcessStepExecutor lastExecutor, SprayVariableContainer last, SprayProcessStepExecutor executor) {
        return new SprayVariableContainer(
                executor.getExecutorNameKey(),
                System.currentTimeMillis(),
                SprayData.deepCopy(last.data),
                generateKey(lastExecutor, last, executor));
    }

    private static final String SEPARATOR = "_";
    private static String generateKey(SprayProcessStepExecutor fromExecutor, SprayVariableContainer last, SprayProcessStepExecutor executor) {
        SprayProcessCoordinator coordinator = executor.getCoordinator();
        if (fromExecutor == null) {
            // the first execute of the process, last is the process default variables
            return String.format("%s"+SEPARATOR+"%s(%s)", last.identityDataKey(),
                    executor.getMeta().getName(), executor.getExecutorNameKey());
        }
        return String.format("%s"+SEPARATOR+"%s(%s->%s)", last.identityDataKey(),
                executor.getMeta().getName(),
                fromExecutor.getExecutorNameKey(), executor.getExecutorNameKey());
    }

    private static class Listener {
        private final SprayVariableContainer last;
        private final SprayVariableContainer cur;
        private Listener(SprayVariableContainer last, SprayVariableContainer cur) {
            this.last = last;
            this.cur = cur;
            this.last.registerListener(this);
        }
        public void onSet(SprayProcessStepExecutor executor, String key, Object value) {
            if (valid(executor)) {
                this.cur.receiveSet(this, executor, key, value);
            }
        }

        public void onRemove(SprayProcessStepExecutor executor, String key) {
            if (valid(executor)) {
                this.cur.receiveRemove(this, executor, key);
            }
        }

        private boolean valid(SprayProcessStepExecutor modifyExecutor) {
            String[] tokens = cur.identityDataKey().split(SEPARATOR);
            String lastKey = tokens[tokens.length-1];
            String prefix = lastKey.substring(lastKey.lastIndexOf("(")+1);
            return prefix.startsWith(modifyExecutor.getExecutorNameKey() + "->");
        }
    }
}
