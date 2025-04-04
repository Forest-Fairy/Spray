package top.spray.common.tools.tuple;

import java.util.Arrays;
import java.util.List;

public abstract class SprayTuples {
    private static abstract class Tuple {
        private final List<Object> members;
        protected Tuple(Object... objects) {
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
    }

    public static class _2<T1, T2> extends Tuple {
        protected _2(Object... objects) {
            super(objects);
        }
        public _2(T1 t1, T2 t2) {
            super(t1, t2);
        }
        public T1 t1() { return get(0);}
        public T2 t2() { return get(1);}
    }

    public static class _3<T1, T2, T3> extends _2<T1, T2> {
        protected _3(Object... objects) {
            super(objects);
        }
        public _3(T1 t1, T2 t2, T3 t3) {
            super(t1, t2, t3);
        }
        public T3 t3() { return get(2); }
    }

    public static class _4<T1, T2, T3, T4> extends _3<T1, T2, T3> {
        protected _4(Object... objects) {
            super(objects);
        }
        public _4(T1 t1, T2 t2, T3 t3, T4 t4) {
            super(t1, t2, t3, t4);
        }
        public T4 t4() { return get(3); }
    }

    public static class _5<T1, T2, T3, T4, T5> extends _4<T1, T2, T3, T4> {
        protected _5(Object... objects) {
            super(objects);
        }
        public _5(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
            super(t1, t2, t3, t4, t5);
        }
        public T5 t5() { return get(4); }
    }

    public static class _6<T1, T2, T3, T4, T5, T6> extends _5<T1, T2, T3, T4, T5> {
        protected _6(Object... objects) {
            super(objects);
        }
        public _6(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
            super(t1, t2, t3, t4, t5, t6);
        }
        public T6 t6() { return get(5); }
    }

    public static class _7<T1, T2, T3, T4, T5, T6, T7> extends _6<T1, T2, T3, T4, T5, T6> {
        protected _7(Object... objects) {
            super(objects);
        }
        public _7(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
            super(t1, t2, t3, t4, t5, t6, t7);
        }
        public T7 t7() { return get(6); }
    }

    public static class _8<T1, T2, T3, T4, T5, T6, T7, T8> extends _7<T1, T2, T3, T4, T5, T6, T7> {
        protected _8(Object... objects) {
            super(objects);
        }
        public _8(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
            super(t1, t2, t3, t4, t5, t6, t7, t8);
        }
        public T8 t8() { return get(7); }
    }

    public static class _9<T1, T2, T3, T4, T5, T6, T7, T8, T9> extends _8<T1, T2, T3, T4, T5, T6, T7, T8> {
        protected _9(Object... objects) {
            super(objects);
        }
        public _9(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
            super(t1, t2, t3, t4, t5, t6, t7, t8, t9);
        }
        public T9 t9() { return get(8); }
    }

    public static class _10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends _9<T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        protected _10(Object... objects) {
            super(objects);
        }
        public _10(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
            super(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
        }
        public T10 t10() { return get(9); }
    }
}