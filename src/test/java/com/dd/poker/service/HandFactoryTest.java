package com.dd.poker.service;


import com.dd.poker.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static com.dd.poker.model.Color.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandFactoryTest {

    private final HandFactory handFactory;

    private HandDispatched handDispatched;

    private HandFactoryTest() {
        handFactory = new HandFactory();
        handDispatched = mock(HandDispatched.class);
    }

    @BeforeEach
    void setUp() {
        TreeSet<Integer> cards = new TreeSet<>(Collections.reverseOrder());
        cards.addAll(List.of(15, 9, 4));
        when(handDispatched.getSortedCardsValues()).thenReturn(cards);
        when(handDispatched.getPairs()).thenReturn(new TreeSet<>());
        when(handDispatched.getThrees()).thenReturn(new TreeSet<>());
        when(handDispatched.getStraightCards()).thenReturn(List.of());
        when(handDispatched.getSuitedCards()).thenReturn(Map.of());
        when(handDispatched.getFours()).thenReturn(new TreeSet<>());
    }

    @Test
    void getHighCard() {
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.HIGHCARD, hand.getHandType());
        assertEquals("15", hand.getValue());
    }

    @Test
    void getPair() {
        when(handDispatched.getPairs()).thenReturn(new TreeSet<>(List.of(15)));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.PAIR, hand.getHandType());
        assertEquals("15", hand.getValue());
    }

    @Test
    void getTwoPairs() {
        TreeSet<Integer> cards = new TreeSet<>(Collections.reverseOrder());
        cards.addAll(List.of(15, 8));
        when(handDispatched.getPairs()).thenReturn(cards);
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.TWO_PAIRS, hand.getHandType());
        assertEquals("15:8", hand.getValue());
    }

    @Test
    void getThree() {
        when(handDispatched.getThrees()).thenReturn(new TreeSet<>(List.of(15)));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.THREE, hand.getHandType());
        assertEquals("15", hand.getValue());
    }

    @Test
    void getStraight() {
        when(handDispatched.getStraightCards()).thenReturn(List.of(new TreeSet<>(List.of(
                new Card(DIAMONDS, 12), new Card(SPADES, 11), new Card(DIAMONDS, 10), new Card(CLUBS, 9), new Card(DIAMONDS, 8)))));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.STRAIGHT, hand.getHandType());
        assertEquals("12", hand.getValue());
    }

    @Test
    void getFlush() {
        when(handDispatched.getSuitedCards()).thenReturn(Map.of(DIAMONDS, new TreeSet<>(List.of(
                new Card(DIAMONDS, 14), new Card(DIAMONDS, 11), new Card(DIAMONDS, 10), new Card(DIAMONDS, 9), new Card(DIAMONDS, 4)))));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.FLUSH, hand.getHandType());
        assertEquals("14", hand.getValue());
    }

    @Test
    void getFullHouse() {
        when(handDispatched.getPairs()).thenReturn(new TreeSet<>(List.of(8)));
        when(handDispatched.getThrees()).thenReturn(new TreeSet<>(List.of(15)));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.FULL_HOUSE, hand.getHandType());
        assertEquals("15:8", hand.getValue());
    }

    @Test
    void getFour() {
        when(handDispatched.getFours()).thenReturn(new TreeSet<>(List.of(15)));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.FOUR, hand.getHandType());
        assertEquals("15", hand.getValue());
    }

    @Test
    void getStraightFlush() {
        when(handDispatched.getStraightCards()).thenReturn(List.of(new TreeSet<>(List.of(
                new Card(DIAMONDS, 12), new Card(DIAMONDS, 11), new Card(DIAMONDS, 10), new Card(DIAMONDS, 9), new Card(DIAMONDS, 8)))));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.STRAIGHT_FLUSH, hand.getHandType());
        assertEquals("12", hand.getValue());
    }

    @Test
    void getRoyalFlush() {
        when(handDispatched.getStraightCards()).thenReturn(List.of(new TreeSet<>(List.of(
                new Card(DIAMONDS, 15), new Card(DIAMONDS, 14), new Card(DIAMONDS, 13), new Card(DIAMONDS, 12), new Card(DIAMONDS, 11)))));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.ROYAL_FLUSH, hand.getHandType());
    }
}