package com.dd.poker.service;

import com.dd.poker.model.Card;
import com.dd.poker.model.Hand;
import com.dd.poker.model.HandDispatched;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dd.poker.model.HandType.*;

@Service
class HandFactory {

    private static final int HIGHCARD_VALUES_SIZE = 5;
    private static final int FOUR_VALUES_SIZE = 2;
    private static final int FULL_HOUSE_VALUES_SIZE = 2;
    private static final int THREE_VALUES_SIZE = 3;
    private static final int TWO_PAIRS_VALUES_SIZE = 3;
    private static final int PAIR_VALUES_SIZE = 4;

    private List<Function<HandDispatched, Optional<Hand>>> handTypesChain = List.of(
            this::getRoyalFlush,
            this::getStraightFlush,
            this::getFourOfKind,
            this::getFullHouse,
            this::getFlush,
            this::getStraight,
            this::getThreeOfKind,
            this::getTwoPairs,
            this::getTwoOfKind
    );

    Hand getHand(HandDispatched handDispatched) {
        return handTypesChain.stream().map(f -> f.apply(handDispatched)).filter(Optional::isPresent)
                .findFirst().map(Optional::get)
                .orElse(new Hand(HIGHCARD, getValues(handDispatched.getSortedCardsValues(), List.of(), HIGHCARD_VALUES_SIZE)));
    }

    private Collection<Integer> getValues(Collection<Integer> allCards, Collection<Integer> cardsInSet, int handValuesSize) {
        ArrayList<Integer> values = new ArrayList<>(cardsInSet);
        values.addAll(allCards.stream()
                .filter(card -> cardsInSet.stream().noneMatch(c -> c.equals(card)))
                .collect(Collectors.toList()));
        return values.size() <= handValuesSize ? values : values.subList(0, handValuesSize);
    }

    private Optional<Hand> getRoyalFlush(HandDispatched handDispatched) {
        return handDispatched.getStraightCards().stream()
                .filter(cards -> cards.first().getValue() == 15 && isStraight(cards) && isFlush(cards))
                .findAny()
                .map(cards -> new Hand(ROYAL_FLUSH, Collections.emptyList()));
    }

    private Optional<Hand> getStraightFlush(HandDispatched handDispatched) {
        return handDispatched.getStraightCards().stream()
                .filter(cards -> isStraight(cards) && isFlush(cards))
                .findAny()
                .map(cards -> new Hand(STRAIGHT_FLUSH, List.of(cards.first().getValue())));
    }

    private Optional<Hand> getFourOfKind(HandDispatched handDispatched) {
        return handDispatched.getFours().stream().findFirst()
                .map(four -> new Hand(FOUR, getValues(handDispatched.getSortedCardsValues(), List.of(four), FOUR_VALUES_SIZE)));
    }

    private Optional<Hand> getFullHouse(HandDispatched handDispatched) {
        SortedSet<Integer> threes = handDispatched.getThrees();
        SortedSet<Integer> pairs = handDispatched.getPairs();
        return threes.isEmpty() || pairs.isEmpty() ? Optional.empty() : Optional.of(new Hand(FULL_HOUSE,
                getValues(handDispatched.getSortedCardsValues(), List.of(threes.first(), pairs.first()), FULL_HOUSE_VALUES_SIZE)));
    }

    private Optional<Hand> getFlush(HandDispatched handDispatched) {
        return handDispatched.getSuitedCards().values().stream()
                .filter(this::isFlush).findFirst()
                .map(cards -> new Hand(FLUSH, cards.stream().map(Card::getValue).collect(Collectors.toList())));
    }

    private boolean isFlush(SortedSet<Card> cards) {
        return cards.stream().map(Card::getColor).distinct().count() == 1;
    }

    private Optional<Hand> getStraight(HandDispatched handDispatched) {
        return handDispatched.getStraightCards().stream()
                .filter(this::isStraight).map(cards -> cards.first().getValue()).max(Integer::compareTo)
                .map(maxCard -> new Hand(STRAIGHT, List.of(maxCard)));
    }

    private boolean isStraight(SortedSet<Card> cards) {
        return cards.size() == 5;
    }

    private Optional<Hand> getThreeOfKind(HandDispatched handDispatched) {
        return handDispatched.getThrees().stream().findFirst()
                .map(three -> new Hand(THREE, getValues(handDispatched.getSortedCardsValues(), List.of(three), THREE_VALUES_SIZE)));
    }

    private Optional<Hand> getTwoPairs(HandDispatched handDispatched) {
        SortedSet<Integer> pairs = handDispatched.getPairs();
        SortedSet<Integer> allCards = handDispatched.getSortedCardsValues();
        return pairs.size() >= 2 ? Optional.of(new Hand(TWO_PAIRS, getValues(allCards, new ArrayList<>(pairs).subList(0, 2), TWO_PAIRS_VALUES_SIZE)))
                : Optional.empty();
    }

    private Optional<Hand> getTwoOfKind(HandDispatched handDispatched) {
        return handDispatched.getPairs().stream().findFirst()
                .map(pair -> new Hand(PAIR, getValues(handDispatched.getSortedCardsValues(), List.of(pair), PAIR_VALUES_SIZE)));
    }
}
