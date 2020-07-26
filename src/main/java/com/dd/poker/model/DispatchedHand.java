package com.dd.poker.model;

import java.util.*;

public class DispatchedHand {

    private SortedSet<Integer> sortedCardsValues = new TreeSet<>(Collections.reverseOrder());
    private SortedSet<Integer> pairs = new TreeSet<>(Collections.reverseOrder());
    private SortedSet<Integer> threes = new TreeSet<>(Collections.reverseOrder());
    private List<SortedSet<Card>> straightCards;
    private Map<Color, SortedSet<Card>> suitedCards;
    private SortedSet<Integer> fours = new TreeSet<>(Collections.reverseOrder());

    public SortedSet<Integer> getSortedCardsValues() {
        return sortedCardsValues;
    }

    public void addCardsValues(Collection<Integer> sortedCards) {
        this.sortedCardsValues.addAll(sortedCards);
    }

    public SortedSet<Integer> getPairs() {
        return pairs;
    }

    public void addPairs(Collection<Integer> pairs) {
        this.pairs.addAll(pairs);
    }

    public SortedSet<Integer> getThrees() {
        return threes;
    }

    public void addThrees(Collection<Integer> threes) {
        this.threes.addAll(threes);
    }

    public List<SortedSet<Card>> getStraightCards() {
        return straightCards;
    }

    public void setStraightCards(List<SortedSet<Card>> straightCards) {
        this.straightCards = straightCards;
    }

    public Map<Color, SortedSet<Card>> getSuitedCards() {
        return suitedCards;
    }

    public void setSuitedCards(Map<Color, SortedSet<Card>> suitedCards) {
        this.suitedCards = suitedCards;
    }

    public SortedSet<Integer> getFours() {
        return fours;
    }

    public void addFours(Collection<Integer> fours) {
        this.fours.addAll(fours);
    }
}
