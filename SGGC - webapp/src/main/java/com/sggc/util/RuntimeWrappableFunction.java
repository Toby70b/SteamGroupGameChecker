package com.sggc.util;

@FunctionalInterface
public interface RuntimeWrappableFunction<T> {
    boolean test(T t) throws Exception;
}
