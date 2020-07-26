package com.dd.poker.model;

import java.util.Collections;
import java.util.List;

public class Card implements Comparable<Card> {
    private Color color;
    private List<Integer> values;

    public Card(Color color, int value) {
        this.color = color;
        this.values = List.of(value);
    }

    public Card(Color color, List<Integer> values) {
        this.color = color;
        this.values = values;
    }

    public Color getColor() {
        return color;
    }

    public List<Integer> getValues() {
        return values;
    }

    public int getHighestValue() {
        return Collections.max(values);
    }

    @Override
    public int compareTo(Card card) {
        return card.getValues().get(0) - values.get(0);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
//        return obj instanceof Card && ((Card) obj).getValues() == this.getValues() && ((Card) obj).getColor().equals(this.getColor());
    }
}
