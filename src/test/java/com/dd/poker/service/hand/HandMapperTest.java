package com.dd.poker.service.hand;


import com.dd.poker.model.*;
import com.dd.poker.service.hand.HandMapper;
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

class HandMapperTest {

    private final HandMapper handMapper;

    private DispatchedHand dispatchedHand;

    private HandMapperTest() {
        handMapper = new HandMapper();
        dispatchedHand = mock(DispatchedHand.class);
    }

    @BeforeEach
    void setUp() {
        TreeSet<Integer> cards = new TreeSet<>(Collections.reverseOrder());
        cards.addAll(List.of(15, 12, 9, 8, 5, 4));
        when(dispatchedHand.getSortedCardsValues()).thenReturn(cards);
        when(dispatchedHand.getPairs()).thenReturn(new TreeSet<>());
        when(dispatchedHand.getThrees()).thenReturn(new TreeSet<>());
        when(dispatchedHand.getStraightCards()).thenReturn(List.of());
        when(dispatchedHand.getSuitedCards()).thenReturn(Map.of());
        when(dispatchedHand.getFours()).thenReturn(new TreeSet<>());
    }

    @Test
    void shouldGetFullHandHighCard() {
        Hand hand = handMapper.getHand(dispatchedHand);
        assertEquals(HandType.HIGHCARD, hand.getType());
        assertEquals(5, hand.getValues().size());
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
    }

    @Test
    void shouldGetTwoCarHighCard() {
        TreeSet<Integer> cards = new TreeSet<>(Collections.reverseOrder());
        cards.addAll(List.of(15, 12));
        when(dispatchedHand.getSortedCardsValues()).thenReturn(cards);
        Hand hand = handMapper.getHand(dispatchedHand);
        assertEquals(HandType.HIGHCARD, hand.getType());
        assertEquals(2, hand.getValues().size());
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
    }

    @Test
    void shouldGetPair() {
        when(dispatchedHand.getPairs()).thenReturn(new TreeSet<>(List.of(15)));
        Hand hand = handMapper.getHand(dispatchedHand);
        assertEquals(HandType.PAIR, hand.getType());
        assertEquals(4, hand.getValues().size());
        assertTrue(hand.getValues().containsAll(dispatchedHand.getPairs()));
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
        assertEquals(Integer.valueOf(12), hand.getValues().get(1));

    }

    @Test
    void shouldGetTwoPairs() {
        TreeSet<Integer> cards = new TreeSet<>(Collections.reverseOrder());
        cards.addAll(List.of(15, 8));
        when(dispatchedHand.getPairs()).thenReturn(cards);
        Hand hand = handMapper.getHand(dispatchedHand);
        assertEquals(3, hand.getValues().size());
        assertTrue(hand.getValues().containsAll(dispatchedHand.getPairs()));
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
        assertEquals(Integer.valueOf(8), hand.getValues().get(1));
        assertEquals(Integer.valueOf(12), hand.getValues().get(2));
    }

    @Test
    void shouldGetThree() {
        when(dispatchedHand.getThrees()).thenReturn(new TreeSet<>(List.of(15)));
        Hand hand = handMapper.getHand(dispatchedHand);
        assertEquals(HandType.THREE, hand.getType());
        assertEquals(3, hand.getValues().size());
        assertTrue(hand.getValues().containsAll(dispatchedHand.getThrees()));
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
        assertEquals(Integer.valueOf(12), hand.getValues().get(1));
        assertEquals(Integer.valueOf(9), hand.getValues().get(2));
    }

    @Test
    void shouldGetStraight() {
        when(dispatchedHand.getStraightCards()).thenReturn(List.of(new TreeSet<>(List.of(
                new Card(DIAMONDS, 12), new Card(SPADES, 11), new Card(DIAMONDS, 10), new Card(CLUBS, 9), new Card(DIAMONDS, 8)))));
        Hand hand = handMapper.getHand(dispatchedHand);
        assertEquals(HandType.STRAIGHT, hand.getType());
        assertEquals(1, hand.getValues().size());
        assertEquals(Integer.valueOf(12), hand.getValues().get(0));
    }

    @Test
    void shouldGetFlush() {
        when(dispatchedHand.getSuitedCards()).thenReturn(Map.of(DIAMONDS, new TreeSet<>(List.of(
                new Card(DIAMONDS, 14), new Card(DIAMONDS, 11), new Card(DIAMONDS, 10), new Card(DIAMONDS, 9), new Card(DIAMONDS, 4)))));
        Hand hand = handMapper.getHand(dispatchedHand);
        assertEquals(HandType.FLUSH, hand.getType());
        assertEquals(5, hand.getValues().size());
        assertEquals(Integer.valueOf(14), hand.getValues().get(0));
    }

    @Test
    void shouldGetFullHouse() {
        when(dispatchedHand.getPairs()).thenReturn(new TreeSet<>(List.of(8)));
        when(dispatchedHand.getThrees()).thenReturn(new TreeSet<>(List.of(3)));
        Hand hand = handMapper.getHand(dispatchedHand);
        assertEquals(HandType.FULL_HOUSE, hand.getType());
        assertEquals(2, hand.getValues().size());
        assertTrue(hand.getValues().containsAll(dispatchedHand.getThrees()));
        assertTrue(hand.getValues().containsAll(dispatchedHand.getPairs()));
        assertEquals(Integer.valueOf(3), hand.getValues().get(0));
        assertEquals(Integer.valueOf(8), hand.getValues().get(1));
    }

    @Test
    void shouldGetFour() {
        when(dispatchedHand.getFours()).thenReturn(new TreeSet<>(List.of(15)));
        Hand hand = handMapper.getHand(dispatchedHand);
        assertEquals(HandType.FOUR, hand.getType());
        assertEquals(2, hand.getValues().size());
        assertTrue(hand.getValues().containsAll(dispatchedHand.getFours()));
        assertEquals(Integer.valueOf(15), hand.getValues().get(0));
        assertEquals(Integer.valueOf(12), hand.getValues().get(1));
    }

    @Test
    void shouldGetStraightFlush() {
        when(dispatchedHand.getStraightCards()).thenReturn(List.of(new TreeSet<>(List.of(
                new Card(DIAMONDS, 12), new Card(DIAMONDS, 11), new Card(DIAMONDS, 10), new Card(DIAMONDS, 9), new Card(DIAMONDS, 8)))));
        Hand hand = handMapper.getHand(dispatchedHand);
        assertEquals(HandType.STRAIGHT_FLUSH, hand.getType());
        assertEquals(1, hand.getValues().size());
        assertEquals(Integer.valueOf(12), hand.getValues().get(0));
    }

    @Test
    void shouldGetRoyalFlush() {
        when(dispatchedHand.getStraightCards()).thenReturn(List.of(new TreeSet<>(List.of(
                new Card(DIAMONDS, 15), new Card(DIAMONDS, 14), new Card(DIAMONDS, 13), new Card(DIAMONDS, 12), new Card(DIAMONDS, 11)))));
        Hand hand = handMapper.getHand(dispatchedHand);
        assertEquals(HandType.ROYAL_FLUSH, hand.getType());
    }
}