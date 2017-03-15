package com.document.recommendation.domain.util.func;

import java.util.function.Function;

@FunctionalInterface
public interface ElseFunction<T, R> extends Function<T, R> {

    default R elseApply(final T t) {
        return this.apply(t);
    }

}
