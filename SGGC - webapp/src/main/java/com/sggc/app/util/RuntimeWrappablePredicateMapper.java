package com.sggc.app.util;

import java.util.function.Predicate;

public class RuntimeWrappablePredicateMapper {
    public static <T> Predicate<T> wrap(
            RuntimeWrappableFunction<T> wrappable) {
        return t -> {
            try {
                return wrappable.test(t);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        };
    }
}