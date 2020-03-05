package com.dd.poker.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Hand {

    private HandType type;
    private List<Integer> values;

    public Hand(HandType type, Collection<Integer> values) {
        this.type = type;
        this.values = new ArrayList<>(values);
    }

    public HandType getType() {
        return type;
    }

    public List<Integer> getValues() {
        return values;
    }
}
