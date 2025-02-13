package top.spray.common.tools;

import java.util.Arrays;
import java.util.List;

public abstract class SprayTuple {
    private final List<Object> members;
    protected SprayTuple(Object... objects) {
        if (objects == null || objects.length == 0) {
            throw new IllegalArgumentException("empty tuple");
        }
        this.members = List.of(objects);
    }
    protected <T> T get(int index) {
        // noinspection unchecked
        return (T) members.get(index);
    }

    @Override
    public int hashCode() {
        return 0x11111 * Arrays.hashCode(members.toArray());
    }

    public static class _2<T0, T1> extends SprayTuple {
        public _2(T0 t0, T1 t1) {
            super(t0, t1);
        }
        public T0 t0() { return get(0);}
        public T1 t1() { return get(1);}
    }
    public static class _3<T0, T1, T2> extends SprayTuple {
        public _3(T0 t0, T1 t1, T2 t2) {
            super(t0, t1, t2);
        }
        public T0 t0() { return get(0); }
        public T1 t1() { return get(1); }
        public T2 t2() { return get(2); }
    }

    public static class _4<T0, T1, T2, T3> extends SprayTuple {
        public _4(T0 t0, T1 t1, T2 t2, T3 t3) {
            super(t0, t1, t2, t3);
        }
        public T0 t0() { return get(0); }
        public T1 t1() { return get(1); }
        public T2 t2() { return get(2); }
        public T3 t3() { return get(3); }
    }

    public static class _5<T0, T1, T2, T3, T4> extends SprayTuple {
        public _5(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4) {
            super(t0, t1, t2, t3, t4);
        }
        public T0 t0() { return get(0); }
        public T1 t1() { return get(1); }
        public T2 t2() { return get(2); }
        public T3 t3() { return get(3); }
        public T4 t4() { return get(4); }
    }

    public static class _6<T0, T1, T2, T3, T4, T5> extends SprayTuple {
        public _6(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
            super(t0, t1, t2, t3, t4, t5);
        }
        public T0 t0() { return get(0); }
        public T1 t1() { return get(1); }
        public T2 t2() { return get(2); }
        public T3 t3() { return get(3); }
        public T4 t4() { return get(4); }
        public T5 t5() { return get(5); }
    }

    public static class _7<T0, T1, T2, T3, T4, T5, T6> extends SprayTuple {
        public _7(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
            super(t0, t1, t2, t3, t4, t5, t6);
        }
        public T0 t0() { return get(0); }
        public T1 t1() { return get(1); }
        public T2 t2() { return get(2); }
        public T3 t3() { return get(3); }
        public T4 t4() { return get(4); }
        public T5 t5() { return get(5); }
        public T6 t6() { return get(6); }
    }

    public static class _8<T0, T1, T2, T3, T4, T5, T6, T7> extends SprayTuple {
        public _8(T0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
            super(t0, t1, t2, t3, t4, t5, t6, t7);
        }
        public T0 t0() { return get(0); }
        public T1 t1() { return get(1); }
        public T2 t2() { return get(2); }
        public T3 t3() { return get(3); }
        public T4 t4() { return get(4); }
        public T5 t5() { return get(5); }
        public T6 t6() { return get(6); }
        public T7 t7() { return get(7); }
    }

}
