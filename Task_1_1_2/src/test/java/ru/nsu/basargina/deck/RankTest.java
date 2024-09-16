package ru.nsu.basargina.deck;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Class with rank tests.
 */
class RankTest {

    @Test
    void testGetRankName() {
        assertEquals("Ace", Rank.ACE.getRankName());
        assertEquals("King", Rank.KING.getRankName());
        assertEquals("Ten", Rank.TEN.getRankName());
        assertEquals("Two", Rank.TWO.getRankName());
    }

    @Test
    void testGetRankValue() {
        assertEquals(11, Rank.ACE.rankValue);
        assertEquals(10, Rank.KING.rankValue);
        assertEquals(2, Rank.TWO.rankValue);
        assertEquals(9, Rank.NINE.rankValue);
    }
}