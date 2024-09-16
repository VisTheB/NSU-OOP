package ru.nsu.basargina.deck;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class for card tests.
 */
class CardTest {

    @Test
    void testCardInStringView() {
        Card card = new Card(Suit.HEART, Rank.ACE);
        assertEquals("Ace Hearts (11)", card.getStringCard());

        Card anotherCard = new Card(Suit.SPADE, Rank.QUEEN);
        assertEquals("Queen Spades (10)", anotherCard.getStringCard());
    }

    @Test
    void testGetValue() {
        Card card = new Card(Suit.DIAMOND, Rank.TEN);
        assertEquals(10, card.getValue());

        Card aceCard = new Card(Suit.CLUB, Rank.ACE);
        assertEquals(11, aceCard.getValue());
    }
}