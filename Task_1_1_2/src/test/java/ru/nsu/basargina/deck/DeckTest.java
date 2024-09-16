package ru.nsu.basargina.deck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(52, deck.cardsLeftCnt(), "Deck should contain 52 cards initially.");
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

        assertEquals(initSize + 51, deck.cardsLeftCnt(), "Deck should have cards added from dropped deck.");
        assertFalse(droppedDeck.hasCards(), "Dropped deck should be empty.");
    }
}