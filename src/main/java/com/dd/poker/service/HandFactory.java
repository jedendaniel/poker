package com.dd.poker.service;

import com.dd.poker.model.Card;
import com.dd.poker.model.Hand;
import com.dd.poker.model.HandDispatched;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dd.poker.model.HandType.*;

@Service
public class HandFactory {

    private List<Function<HandDispatched, Optional<Hand>>> handChain = List.of(
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

    public Hand getHand(HandDispatched handDispatched) {
        return handChain.stream().map(f -> f.apply(handDispatched)).filter(Optional::isPresent)
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
        return Optional.ofNullable(handDispatched.getFours())
                .map(cards -> new Hand(FOUR, String.valueOf(cards.first())));
    }

    private Optional<Hand> getFullHouse(HandDispatched handDispatched) {
        return Optional.ofNullable(handDispatched).map(hd -> new Hand(FULL_HOUSE, String.join(":",
                String.valueOf(hd.getThrees().first()) , String.valueOf(hd.getPairs().first()))));
    }

    private Optional<Hand> getFlush(HandDispatched handDispatched) {
        return handDispatched.getSuitedCards().values().stream()
                .filter(cards -> cards.size() >= 5).findFirst()
                .map(cards -> new Hand(FLUSH, String.valueOf(cards.first())));
    }

    private Optional<Hand> getStraight(HandDispatched handDispatched) {
        return handDispatched.getStraightCards().stream()
                .filter(cards -> cards.size() == 5).map(cards -> cards.first().getValue()).max(Integer::compareTo)
                .map(maxCard -> new Hand(STRAIGHT, String.valueOf(maxCard)));
    }

    private Optional<Hand> getThreeOfKind(HandDispatched handDispatched) {
        return Optional.ofNullable(handDispatched.getThrees())
                .map(cards -> new Hand(THREE, String.valueOf(cards.first())));
    }

    private Optional<Hand> getTwoPairs(HandDispatched handDispatched) {
        SortedSet<Integer> pairs = handDispatched.getPairs();
        return pairs.size() >= 2 ? Optional.of(new Hand(TWO_PAIRS, pairs.subSet(0,1).stream().map(String::valueOf)
                        .collect(Collectors.joining(":")))) : Optional.empty();
    }

    private Optional<Hand> getTwoOfKind(HandDispatched handDispatched) {
        return Optional.ofNullable(handDispatched.getPairs())
                .map(cards -> new Hand(PAIR, String.valueOf(cards.first())));
    }
}
