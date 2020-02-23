package com.dd.poker.model;

public class Card implements Comparable<Card> {
    private Color color;
    private int value;

    public Card(Color color, int value) {
        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Card card) {
        return card.getValue() - value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
//        return obj instanceof Card && ((Card) obj).getValue() == this.getValue() && ((Card) obj).getColor().equals(this.getColor());
    }
}
