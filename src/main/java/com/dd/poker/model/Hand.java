package com.dd.poker.model;

public class Hand {

    private HandType handType;
    private String handValue;

    public Hand() {
    }

    public Hand(HandType handType, String handValue) {
        this.handType = handType;
        this.handValue = handValue;
    }

    public HandType getHandType() {
        return handType;
    }

    public void setHandType(HandType handType) {
        this.handType = handType;
    }

    public String getValue() {
        return handValue;
    }

    public void setHandValue(String handValue) {
        this.handValue = handValue;
    }
}
