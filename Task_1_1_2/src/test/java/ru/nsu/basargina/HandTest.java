package ru.nsu.basargina;

import ru.nsu.basargina.deck.Deck;
import ru.nsu.basargina.deck.Card;
import ru.nsu.basargina.deck.Rank;
import ru.nsu.basargina.deck.Suit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class with hand methods tests.
 */
class HandTest {
    private Hand hand;
    private Deck deck;

    @BeforeEach
    void setUp() {
        hand = new Hand();
        deck = new Deck(true); // Create deck with 52 cards
    }

    @Test
    void testTakeCardFromDeck() {
        int initSize = 0;
        hand.takeCard(deck);
        assertEquals(initSize + 1, hand.handSize());
        assertEquals(51, deck.cardsLeftCnt());
    }

    @Test
    void testAddCard() {
        Card card = new Card(Suit.HEART, Rank.ACE);
        hand.addCard(card);
        assertEquals(card, hand.getLastCard());
    }

    @Test
    void testGetLastCard() {
        Card card1 = new Card(Suit.CLUB, Rank.SEVEN);
        Card card2 = new Card(Suit.SPADE, Rank.KING);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(card2, hand.getLastCard());
    }

    @Test
    void testHandSize() {
        int initSize = 0;
        Card card = new Card(Suit.CLUB, Rank.TWO);
        hand.addCard(card);
        assertEquals(initSize + 1, hand.handSize());
    }

    @Test
    void testDropHand() {
        Card card = new Card(Suit.SPADE, Rank.THREE);
        hand.addCard(card);

        Deck droppedDeck = new Deck(false);
        hand.dropHand(droppedDeck);

        assertEquals(0, hand.handSize());
        assertEquals(1, droppedDeck.cardsLeftCnt());
    }

    @Test
    void testCountHandSumWithoutAce() {
        hand.addCard(new Card(Suit.DIAMOND, Rank.TWO));
        hand.addCard(new Card(Suit.HEART, Rank.THREE));

        assertEquals(5, hand.countHandSum());
    }

    @Test
    void testCountHandSumWithAce() {
        hand.addCard(new Card(Suit.SPADE, Rank.ACE)); // 11 by default
        hand.addCard(new Card(Suit.CLUB, Rank.EIGHT));
        hand.addCard(new Card(Suit.HEART, Rank.SIX));

        // Ace should be transformed to 1, because 11+8+6 > 21
        assertEquals(15, hand.countHandSum());
    }

    @Test
    void testShowHandOpen() {
        hand.addCard(new Card(Suit.HEART, Rank.TEN));
        hand.addCard(new Card(Suit.SPADE, Rank.JACK));

        String expectedOut = "[Ten Hearts (10), Jack Spades (10)] => 20";
        assertEquals(expectedOut, hand.showHand(false));
    }

    @Test
    void testShowHandClosed() {
        hand.addCard(new Card(Suit.HEART, Rank.TEN));
        hand.addCard(new Card(Suit.SPADE, Rank.JACK));

        String expectedOut = "[Ten Hearts (10), <closed card>]";
        assertEquals(expectedOut, hand.showHand(true));
    }
}