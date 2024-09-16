package ru.nsu.basargina.deck;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class with suit tests.
 */
class SuitTest {

    @Test
    void testGetSuitName() {
        assertEquals("Clubs", Suit.CLUB.getSuitName());
        assertEquals("Diamonds", Suit.DIAMOND.getSuitName());
        assertEquals("Hearts", Suit.HEART.getSuitName());
        assertEquals("Spades", Suit.SPADE.getSuitName());
    }
}