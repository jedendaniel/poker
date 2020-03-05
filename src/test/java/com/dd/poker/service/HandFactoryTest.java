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
import static org.junit.Assert.assertTrue;
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
        cards.addAll(List.of(15, 12, 9, 8, 5, 4));
        when(handDispatched.getSortedCardsValues()).thenReturn(cards);
        when(handDispatched.getPairs()).thenReturn(new TreeSet<>());
        when(handDispatched.getThrees()).thenReturn(new TreeSet<>());
        when(handDispatched.getStraightCards()).thenReturn(List.of());
        when(handDispatched.getSuitedCards()).thenReturn(Map.of());
        when(handDispatched.getFours()).thenReturn(new TreeSet<>());
    }

    @Test
    void getFullHandHighCard() {
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.HIGHCARD, hand.getType());
        assertEquals(5, hand.getValues().size());
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
    }

    @Test
    void getTwoCarHighCard() {
        TreeSet<Integer> cards = new TreeSet<>(Collections.reverseOrder());
        cards.addAll(List.of(15, 12));
        when(handDispatched.getSortedCardsValues()).thenReturn(cards);
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.HIGHCARD, hand.getType());
        assertEquals(2, hand.getValues().size());
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
    }

    @Test
    void getPair() {
        when(handDispatched.getPairs()).thenReturn(new TreeSet<>(List.of(15)));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.PAIR, hand.getType());
        assertEquals(4, hand.getValues().size());
        assertTrue(hand.getValues().containsAll(handDispatched.getPairs()));
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
        assertEquals(Integer.valueOf(12), hand.getValues().get(1));

    }

    @Test
    void getTwoPairs() {
        TreeSet<Integer> cards = new TreeSet<>(Collections.reverseOrder());
        cards.addAll(List.of(15, 8));
        when(handDispatched.getPairs()).thenReturn(cards);
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(3, hand.getValues().size());
        assertTrue(hand.getValues().containsAll(handDispatched.getPairs()));
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
        assertEquals(Integer.valueOf(8), hand.getValues().get(1));
        assertEquals(Integer.valueOf(12), hand.getValues().get(2));
    }

    @Test
    void getThree() {
        when(handDispatched.getThrees()).thenReturn(new TreeSet<>(List.of(15)));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.THREE, hand.getType());
        assertEquals(3, hand.getValues().size());
        assertTrue(hand.getValues().containsAll(handDispatched.getThrees()));
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
        assertEquals(Integer.valueOf(12), hand.getValues().get(1));
        assertEquals(Integer.valueOf(9), hand.getValues().get(2));
    }

    @Test
    void getStraight() {
        when(handDispatched.getStraightCards()).thenReturn(List.of(new TreeSet<>(List.of(
                new Card(DIAMONDS, 12), new Card(SPADES, 11), new Card(DIAMONDS, 10), new Card(CLUBS, 9), new Card(DIAMONDS, 8)))));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.STRAIGHT, hand.getType());
        assertEquals(1, hand.getValues().size());
        assertEquals(Integer.valueOf(12), hand.getValues().get(0));
    }

    @Test
    void getFlush() {
        when(handDispatched.getSuitedCards()).thenReturn(Map.of(DIAMONDS, new TreeSet<>(List.of(
                new Card(DIAMONDS, 14), new Card(DIAMONDS, 11), new Card(DIAMONDS, 10), new Card(DIAMONDS, 9), new Card(DIAMONDS, 4)))));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.FLUSH, hand.getType());
        assertEquals(5, hand.getValues().size());
        assertEquals(Integer.valueOf(14), hand.getValues().get(0));
    }

    @Test
    void getFullHouse() {
        when(handDispatched.getPairs()).thenReturn(new TreeSet<>(List.of(8)));
        when(handDispatched.getThrees()).thenReturn(new TreeSet<>(List.of(3)));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.FULL_HOUSE, hand.getType());
        assertEquals(2, hand.getValues().size());
        assertTrue(hand.getValues().containsAll(handDispatched.getThrees()));
        assertTrue(hand.getValues().containsAll(handDispatched.getPairs()));
        assertEquals(Integer.valueOf(3), hand.getValues().get(0));
        assertEquals(Integer.valueOf(8), hand.getValues().get(1));
    }

    @Test
    void getFour() {
        when(handDispatched.getFours()).thenReturn(new TreeSet<>(List.of(15)));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.FOUR, hand.getType());
        assertEquals(2, hand.getValues().size());
        assertTrue(hand.getValues().containsAll(handDispatched.getFours()));
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
        assertEquals(Integer.valueOf(12), hand.getValues().get(1));
    }

    @Test
    void getStraightFlush() {
        when(handDispatched.getStraightCards()).thenReturn(List.of(new TreeSet<>(List.of(
                new Card(DIAMONDS, 12), new Card(DIAMONDS, 11), new Card(DIAMONDS, 10), new Card(DIAMONDS, 9), new Card(DIAMONDS, 8)))));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.STRAIGHT_FLUSH, hand.getType());
        assertEquals(1, hand.getValues().size());
        assertEquals(Integer.valueOf(12), hand.getValues().get(0));
    }

    @Test
    void getRoyalFlush() {
        when(handDispatched.getStraightCards()).thenReturn(List.of(new TreeSet<>(List.of(
                new Card(DIAMONDS, 15), new Card(DIAMONDS, 14), new Card(DIAMONDS, 13), new Card(DIAMONDS, 12), new Card(DIAMONDS, 11)))));
        Hand hand = handFactory.getHand(handDispatched);
        assertEquals(HandType.ROYAL_FLUSH, hand.getType());
    }
}