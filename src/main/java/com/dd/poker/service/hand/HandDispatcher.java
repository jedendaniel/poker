package com.dd.poker.service.hand;

import com.dd.poker.model.Card;
import com.dd.poker.model.Color;
import com.dd.poker.model.DispatchedHand;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class HandDispatcher {

    DispatchedHand getHandPossibilities(DispatchedHand dispatchedHand, List<Card> cards) {
        List<Integer> cardsValues = cards.stream().flatMap(c -> c.getValues().stream()).collect(Collectors.toList());
        dispatchedHand.addCardsValues(cardsValues);
        dispatchedHand.addPairs(getNCardOfKind(cardsValues, 2));
        dispatchedHand.addThrees(getNCardOfKind(cardsValues, 3));
        dispatchedHand.setStraightCards(getStraightVariations(cards));
        dispatchedHand.setSuitedCards(groupCardsPerColor(cards));
        dispatchedHand.addFours(new TreeSet<>(getNCardOfKind(cardsValues, 4)));
        return dispatchedHand;
    }

    List<Integer> getNCardOfKind(List<Integer> cards, int count) {
        return cards.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
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
                if (!other.equals(card) && isInLowerStraightRange(card.getValues(), other.getValues()) &&
                        !variation.stream().map(Card::getValues).collect(Collectors.toList()).contains(other.getValues())) {
                    TreeSet<Card> newSet = new TreeSet<>(variation);
                    newSet.add(other);
                    newSets.add(newSet);
                }
            });
            result.addAll(newSets);
        });
        return result;
    }

    private boolean isInLowerStraightRange(List<Integer> highValues, List<Integer> testedValues) {
        return highValues.stream().anyMatch(highValue -> testedValues.stream()
                .anyMatch(testedValue ->highValue >= 5 && highValue > testedValue && highValue - testedValue < 5));
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
