package com.dd.poker.service;

import com.dd.poker.model.Hand;

import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.stream.IntStream;

public class HandComparator implements Comparator<Hand> {

    @Override
    public int compare(Hand h1, Hand h2) {
        int handTypeComparisonResult = h1.getType().getPriority() - h2.getType().getPriority();
        return handTypeComparisonResult != 0 ? handTypeComparisonResult : compareValues(h1, h2);
    }

    private int compareValues(Hand h1, Hand h2) {
        if (h1.getValues().size() != h2.getValues().size()) {
            throw new InvalidParameterException("Incompatible hand value sizes");
        }
        return IntStream.range(0, h1.getValues().size() - 1)
                .mapToObj(i -> h1.getValues().get(i) - h2.getValues().get(i))
                .filter(comparisonResult -> comparisonResult != 0)
                .findFirst()
                .orElse(0);
    }
}
