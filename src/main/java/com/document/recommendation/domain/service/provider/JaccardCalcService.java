package com.document.recommendation.domain.service.provider;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JaccardCalcService {

    private static double calculate(final Set<String> s1, final Set<String> s2) {
        return (double) Sets.intersection(s1, s2).size() / (double) Sets.union(s1, s2).size();
    }

    public static double similarity(final List<String> l1, final List<String> l2) {
        return calculate(new HashSet<>(l1), new HashSet<>(l2));
    }

}