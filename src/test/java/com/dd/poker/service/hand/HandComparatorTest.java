package com.dd.poker.service.hand;

import com.dd.poker.model.Hand;
import com.dd.poker.model.HandType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class HandComparatorTest {

    private final HandComparator handComparator = new HandComparator();

    @Test
    void shouldHigherPriorityHandBeGreater() {
        Hand twoPairs = new Hand(HandType.TWO_PAIRS, List.of(13,5,9,8,3));
        Hand onePair = new Hand(HandType.PAIR, List.of(13,5,9,8,3));
        assertTrue(handComparator.compare(twoPairs, onePair) > 0);
    }

    @Test
    void shouldHigherKickerHandBeGreater() {
        Hand twoPairs = new Hand(HandType.PAIR, List.of(13,9,8,3,2));
        Hand onePair = new Hand(HandType.PAIR, List.of(13,15, 9,8,3));
        assertTrue(handComparator.compare(twoPairs, onePair) < 0);
    }

    @Test
    void shouldBeEqualWhenHandTypesAndValuesAreTheSame() {
        Hand twoPairs = new Hand(HandType.PAIR, List.of(13,9));
        Hand onePair = new Hand(HandType.PAIR, List.of(13,9));
        assertEquals(0, handComparator.compare(twoPairs, onePair));
    }
}