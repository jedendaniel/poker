package com.dd.poker.model;

public enum HandType {
    HIGHCARD(1),
    PAIR(2),
    TWO_PAIRS(3),
    THREE(4),
    STRAIGHT(5),
    FLUSH(6),
    FULL_HOUSE(7),
    FOUR(8),
    STRAIGHT_FLUSH(9),
    ROYAL_FLUSH(10);

    private int priority;

    HandType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
