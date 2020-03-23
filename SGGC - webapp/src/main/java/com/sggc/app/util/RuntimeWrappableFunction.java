package com.sggc.app.util;

@FunctionalInterface
public interface RuntimeWrappableFunction<T> {
    boolean test(T t) throws Exception;
}
