package ru.nsu.basargina.deck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class with deck tests.
 */
class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck(true); // Create 52 cards deck
    }

    @Test
    void testDeckCreation() {
        assertEquals(52, deck.cardsLeftCnt());
    }

    @Test
    void testTakeCard() {
        Card card = deck.takeCard();
        assertNotNull(card, "Should return a card.");
        assertEquals(51, deck.cardsLeftCnt(), "Deck should have 51 cards left after taking one.");
    }

    @Test
    void testShuffle() {
        ArrayList<Card> initialD = new ArrayList<>(deck.getCards());
        deck.takeCard(); // Delete one card
        deck = new Deck(true); // Create new deck
        ArrayList<Card> shuffledD = new ArrayList<>(deck.getCards());

        assertNotEquals(initialD, shuffledD, "Deck should be shuffled.");
    }

    @Test
    void testClearDeck() {
        deck.clearDeck();
        assertEquals(0, deck.cardsLeftCnt(), "Deck should be empty after clearing.");
    }

    @Test
    void testAddCards() {
        deck.clearDeck();
        ArrayList<Card> newCards = new ArrayList<>();
        newCards.add(new Card(Suit.HEART, Rank.ACE));
        newCards.add(new Card(Suit.SPADE, Rank.KING));

        deck.addCards(newCards);
        assertEquals(2, deck.cardsLeftCnt(), "Deck should have 2 cards.");
    }

    @Test
    void testRemakeDeck() {
        Deck droppedDeck = new Deck(true);
        droppedDeck.takeCard();  // Убираем одну карту из сброшенной колоды

        deck.clearDeck();
        int initSize = deck.cardsLeftCnt();
        deck.remakeDeck(droppedDeck);

        assertEquals(initSize + 51, deck.cardsLeftCnt());
        assertFalse(droppedDeck.hasCards(), "Dropped deck should be empty.");
    }

    @Test
    void testDeckAddCard() {
        deck.clearDeck();
        deck.deckAddCard(new Card(Suit.SPADE, Rank.FOUR));
        assertEquals(1, deck.cardsLeftCnt());
    }
}