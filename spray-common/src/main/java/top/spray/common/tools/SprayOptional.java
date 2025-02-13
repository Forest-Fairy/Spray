package top.spray.common.tools;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SprayOptional<T> {
    private static final SprayOptional<?> EMPTY = new SprayOptional<>(null);
    private static final Supplier<?> NullSupplier = () -> null;
    public static<T> SprayOptional<T> empty() {
        // noinspection unchecked
        return (SprayOptional<T>) EMPTY;
    }
    public static <T> SprayOptional<T> of(T value) {
        return new SprayOptional<>(value);
    }


    private final Object value;

    public SprayOptional(T value) {
        this.value = value;
    }

    private Supplier<T> nullSupplier() {
        // noinspection unchecked
        return (Supplier<T>) NullSupplier;
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isNotPresent() {
        return !this.isPresent();
    }

    public void ifPresent(Consumer<? super T> action) {
        if (value != null) {
            action.accept(this.orElseGet(nullSupplier()));
        }
    }

    public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (value != null) {
            action.accept(this.orElseGet(nullSupplier()));
        } else {
            emptyAction.run();
        }
    }

    public SprayOptional<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (this.isPresent()) {
            if (! predicate.test(this.orElseGet(nullSupplier()))) {
                return empty();
            }
        }
        return this;
    }

    public <U> SprayOptional<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (this.isPresent()) {
            U u = mapper.apply(this.orElseGet(nullSupplier()));
            if (u != null) {
                return SprayOptional.of(u);
            }
        }
        return empty();
    }

    public <U> SprayOptional<U> flatMap(Function<? super T, ? extends SprayOptional<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        if (isPresent()) {
            @SuppressWarnings("unchecked")
            SprayOptional<U> r = (SprayOptional<U>) mapper.apply(this.orElseGet(nullSupplier()));
            return Objects.requireNonNull(r);
        }
        return empty();
    }

    public SprayOptional<T> or(Supplier<? extends SprayOptional<? extends T>> supplier) {
        Objects.requireNonNull(supplier);
        if (isPresent()) {
            return this;
        } else {
            @SuppressWarnings("unchecked")
            SprayOptional<T> r = (SprayOptional<T>) supplier.get();
            return Objects.requireNonNull(r);
        }
    }

    public Stream<T> stream() {
        if (!isPresent()) {
            return Stream.empty();
        } else {
            return Stream.of(this.orElseGet(nullSupplier()));
        }
    }

    public T orElse(T other) {
        return this.isPresent() ? this.orElseGet(nullSupplier()) : other;
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        return this.isPresent() ? this.orElseGet(nullSupplier()) : supplier.get();
    }

    public T orElseThrow() {
        if (this.isNotPresent()) {
            throw new NoSuchElementException("No value present");
        }
        return this.orElseGet(nullSupplier());
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.isPresent()) {
            return this.orElseGet(nullSupplier());
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof SprayOptional<?> other
                && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return this.isPresent()
                ? ("SprayOptional[" + value + "]")
                : "SprayOptional[]";
    }
}
