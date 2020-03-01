package com.dd.poker.service;

import com.dd.poker.model.Card;
import com.dd.poker.model.Hand;
import com.dd.poker.model.HandDispatched;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dd.poker.model.HandType.*;

@Service
class HandFactory {

    private List<Function<HandDispatched, Optional<Hand>>> handsChain = List.of(
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
        return handsChain.stream().map(f -> f.apply(handDispatched)).filter(Optional::isPresent)
                .findFirst().map(Optional::get)
                .orElse(new Hand(HIGHCARD, String.valueOf(handDispatched.getSortedCardsValues().first())));
    }

    private Optional<Hand> getRoyalFlush(HandDispatched handDispatched) {
        return handDispatched.getStraightCards().stream()
                .filter(cards -> cards.first().getValue() == 15 && isStraight(cards) && isFlush(cards)).findAny()
                .map(cards -> new Hand(ROYAL_FLUSH, "R O Y A L  F L U S H"));
    }

    private Optional<Hand> getStraightFlush(HandDispatched handDispatched) {
        return handDispatched.getStraightCards().stream()
                .filter(cards -> isStraight(cards) && isFlush(cards)).findAny()
                .map(cards -> new Hand(STRAIGHT_FLUSH, String.valueOf(cards.first().getValue())));
    }

    private boolean isStraight(SortedSet<Card> cards) {
        return cards.size() == 5;
    }

    private boolean isFlush(SortedSet<Card> cards) {
        return cards.stream().map(Card::getColor).distinct().count() == 1;
    }

    private Optional<Hand> getFourOfKind(HandDispatched handDispatched) {
        return handDispatched.getFours().isEmpty() ? Optional.empty() :
                Optional.of(new Hand(FOUR, String.valueOf(handDispatched.getFours().first())));
    }

    private Optional<Hand> getFullHouse(HandDispatched handDispatched) {
        SortedSet<Integer> threes = handDispatched.getThrees();
        SortedSet<Integer> pairs = handDispatched.getPairs();
        return threes.isEmpty() || pairs.isEmpty() ? Optional.empty() : Optional.of(new Hand(FULL_HOUSE,
                String.join(":", List.of(String.valueOf(threes.first()), String.valueOf(pairs.first())))));
    }

    private Optional<Hand> getFlush(HandDispatched handDispatched) {
        return handDispatched.getSuitedCards().values().stream()
                .filter(cards -> cards.size() >= 5).findFirst()
                .map(cards -> new Hand(FLUSH, String.valueOf(cards.first().getValue())));
    }

    private Optional<Hand> getStraight(HandDispatched handDispatched) {
        return handDispatched.getStraightCards().stream()
                .filter(cards -> cards.size() == 5).map(cards -> cards.first().getValue()).max(Integer::compareTo)
                .map(maxCard -> new Hand(STRAIGHT, String.valueOf(maxCard)));
    }

    private Optional<Hand> getThreeOfKind(HandDispatched handDispatched) {
        return handDispatched.getThrees().isEmpty() ? Optional.empty() :
                Optional.of(new Hand(THREE, String.valueOf(handDispatched.getThrees().first())));
    }

    private Optional<Hand> getTwoPairs(HandDispatched handDispatched) {
        SortedSet<Integer> pairs = handDispatched.getPairs();
        return pairs.size() >= 2 ? Optional.of(new Hand(TWO_PAIRS, new ArrayList<>(pairs).subList(0,2).stream().map(String::valueOf)
                        .collect(Collectors.joining(":")))) : Optional.empty();
    }

    private Optional<Hand> getTwoOfKind(HandDispatched handDispatched) {
        return handDispatched.getPairs().isEmpty() ? Optional.empty() :
                Optional.of(new Hand(PAIR, String.valueOf(handDispatched.getPairs().first())));
    }
}
