package com.dd.poker.service;

import com.dd.poker.model.Card;
import com.dd.poker.model.Color;
import com.dd.poker.model.HandDispatched;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HandDispatcher {

    private HandDispatched handDispatched = new HandDispatched();

    public void dispatch(List<Card> cards) {
        handDispatched.addCardsValues(cards.stream().map(Card::getValue).collect(Collectors.toList()));
        handDispatched.addPairs(getNCardOfKind(cards, 2));
        handDispatched.addThrees(getNCardOfKind(cards, 3));
        handDispatched.setStraightCards(getStraightVariations(cards));
        handDispatched.setSuitedCards(groupCardsPerColor(cards));
        handDispatched.addFours(new TreeSet<>(getNCardOfKind(cards, 4)));
    }

    public HandDispatched getHandDispatched() {
        return handDispatched;
    }

    List<Integer> getNCardOfKind(List<Card> cards, int count) {
        return cards.stream()
                .map(Card::getValue).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().filter(e -> e.getValue() == count)
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    List<SortedSet<Card>> getStraightVariations(List<Card> sortedCards) {
        List<SortedSet<Card>> allVariations = new ArrayList<>();
        sortedCards.forEach(card -> allVariations.addAll(getCardStraightVariations(card, sortedCards)));
        return filterNotNeededStraightVariations(allVariations);
    }

    Map<Color, SortedSet<Card>> groupCardsPerColor(List<Card> cards) {
        return cards.stream().collect(Collectors.groupingBy(Card::getColor)).entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new TreeSet<>(e.getValue())));
    }

    private List<SortedSet<Card>> getCardStraightVariations(Card card, List<Card> otherCards) {
        List<SortedSet<Card>> result = new ArrayList<>();
        result.add(new TreeSet<>(List.of(card)));
        otherCards.forEach(other -> {
            List<TreeSet<Card>> newSets = new ArrayList<>();
            result.forEach(variation -> {
                if (!other.equals(card) && isInLowerStraightRange(card.getValue(), other.getValue()) &&
                        !variation.stream().map(Card::getValue).collect(Collectors.toList()).contains(other.getValue())) {
                    TreeSet<Card> newSet = new TreeSet<>(variation);
                    newSet.add(other);
                    newSets.add(newSet);
                }
            });
            result.addAll(newSets);
        });
        return result;
    }

    private boolean isInLowerStraightRange(int highValue, int testedValue) {
        return highValue >= 5 && highValue > testedValue && highValue - testedValue < 5;
    }

    private List<SortedSet<Card>> filterNotNeededStraightVariations(List<SortedSet<Card>> variations) {
        List<SortedSet<Card>> variationsAfterFiltration = new ArrayList<>(variations);
        variationsAfterFiltration.removeIf(tested ->  !isStraightFlushPossible(tested) &&
                variations.stream().anyMatch(other -> !other.equals(tested) && other.containsAll(tested)));

        List<SortedSet<Card>> flushSetsToRemove = variations.stream()
                .collect(Collectors.groupingBy(this::isStraightFlushPossible)).get(true);
        flushSetsToRemove.removeIf(tested -> flushSetsToRemove.stream()
                .noneMatch(other -> !other.equals(tested) && other.containsAll(tested)));

        variationsAfterFiltration.removeAll(flushSetsToRemove);

        return variationsAfterFiltration;
    }

    private boolean isStraightFlushPossible(SortedSet<Card> variation) {
        return variation.stream().collect(Collectors.groupingBy(Card::getColor)).size() == 1;
    }
}
