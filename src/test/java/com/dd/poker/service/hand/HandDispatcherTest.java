package com.dd.poker.service.hand;

import com.dd.poker.model.Card;
import com.dd.poker.model.Color;
import com.dd.poker.model.DispatchedHand;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.dd.poker.model.Color.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class HandDispatcherTest {

    private final HandDispatcher handDispatcher = new HandDispatcher();

    @Test
    void shouldSortCards() {
        List<Card> cards = List.of(new Card(DIAMONDS, 1), new Card(CLUBS, 14), new Card(SPADES, 7));
        DispatchedHand dispatchedHand = handDispatcher.getHandPossibilities(new DispatchedHand(), cards);
        assertEquals(3, dispatchedHand.getSortedCardsValues().size());
        assertEquals(Integer.valueOf(14), dispatchedHand.getSortedCardsValues().first());
        assertEquals(Integer.valueOf(1), dispatchedHand.getSortedCardsValues().last());
    }

    @Test
    void shouldCreateSameValuesCardTypes() {
        List<Card> cards = List.of(
                new Card(CLUBS, 4), new Card(CLUBS, 13), new Card(CLUBS, 2),
                new Card(DIAMONDS, 4), new Card(HEARTS, 13), new Card(DIAMONDS, 2),
                new Card(SPADES, 4), new Card(DIAMONDS, 13),
                new Card(HEARTS, 4), new Card(HEARTS, 8), new Card(CLUBS, 8));
        List<Integer> cardsValues = cards.stream().flatMap(c -> c.getValues().stream()).collect(Collectors.toList());
        assertEquals(2, handDispatcher.getNCardOfKind(cardsValues, 2).size());
        assertEquals(1, handDispatcher.getNCardOfKind(cardsValues, 3).size());
        assertEquals(1, handDispatcher.getNCardOfKind(cardsValues, 4).size());
    }

    @Test
    void shouldCreateStraightSets() {
        List<Card> cards = List.of(new Card(DIAMONDS, 15), new Card(CLUBS, 12), new Card(CLUBS, 13), new Card(CLUBS, 11), new Card(DIAMONDS, 8));
        List<SortedSet<Card>> straightVariations = handDispatcher.getStraightVariations(cards);

        assertEquals(5, straightVariations.size());
        assertTrue(straightVariations.containsAll(List.of(
                new TreeSet<>(List.of(new Card(DIAMONDS, 15), new Card(CLUBS, 12), new Card(CLUBS, 13), new Card(CLUBS, 11))),
                new TreeSet<>(List.of(new Card(CLUBS, 12), new Card(CLUBS, 13), new Card(CLUBS, 11))),
                new TreeSet<>(List.of(new Card(CLUBS, 12), new Card(CLUBS, 11), new Card(DIAMONDS, 8))),
                new TreeSet<>(List.of(new Card(DIAMONDS, 15))),
                new TreeSet<>(List.of(new Card(DIAMONDS, 8)))
        )));
    }

    @Test
    void shouldCreateStraightFlush() {
        List<Card> cards = List.of(new Card(CLUBS, 15), new Card(CLUBS, 14), new Card(CLUBS, 13), new Card(CLUBS, 12), new Card(CLUBS, 11), new Card(DIAMONDS, 10));
        List<SortedSet<Card>> straightVariations = handDispatcher.getStraightVariations(cards);
        assertTrue(straightVariations.contains(new TreeSet<>(List.of(new Card(CLUBS, 15), new Card(CLUBS, 14), new Card(CLUBS, 13), new Card(CLUBS, 12), new Card(CLUBS, 11)))));
    }

    @Test void shouldCreateFlushSets() {
        List<Card> flushCards = List.of(new Card(DIAMONDS, 15), new Card(CLUBS, 12), new Card(CLUBS, 3), new Card(CLUBS, 11), new Card(DIAMONDS, 8));
        Map<Color, SortedSet<Card>> cardsIntoFlush = handDispatcher.groupCardsPerColor(flushCards);

        assertEquals(2, cardsIntoFlush.size());
        assertTrue(cardsIntoFlush.containsKey(DIAMONDS));
        assertTrue(cardsIntoFlush.containsKey(CLUBS));
        assertEquals(2, cardsIntoFlush.get(DIAMONDS).size());
        assertEquals(3, cardsIntoFlush.get(CLUBS).size());
    }

}