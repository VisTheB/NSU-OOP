package ru.nsu.basargina.players;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.basargina.deck.Card;
import ru.nsu.basargina.deck.Deck;
import ru.nsu.basargina.deck.Rank;
import ru.nsu.basargina.deck.Suit;

/**
 * Class with dealer methods tests.
 */
class DealerTest {
    private Dealer dealer;
    private Deck deck;
    private Deck droppedDeck;
    PrintStream originalOut;

    @BeforeEach
    void setUp() {
        dealer = new Dealer();
    }

    @Test
    void testDealerInitialization() {
        assertTrue(dealer.ifHasClosed());
    }

    @Test
    void testSetDealerClosedCard() {
        dealer.setDealerClosedCard(false);
        assertFalse(dealer.ifHasClosed());
    }

    @Test
    void testPrintDealerHand() {

        dealer.getPlayerHand().addCard(new Card(Suit.DIAMOND, Rank.QUEEN));
        dealer.getPlayerHand().addCard(new Card(Suit.HEART, Rank.THREE));

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        dealer.printDealerHand(false);

        String output = outputStream.toString();
        String expectedOutput = "Dealer cards: [Queen Diamonds (10), Three Hearts (3)] => 13\n";

        boolean isEqual = expectedOutput.equals(output);
        assertTrue(isEqual);

        // Restore the standard output
        System.setOut(originalOut);
    }

    @Test
    void testGetBlackJack() {
        dealer.getPlayerHand().addCard(new Card(Suit.DIAMOND, Rank.QUEEN));
        dealer.getPlayerHand().addCard(new Card(Suit.HEART, Rank.THREE));
        dealer.getPlayerHand().addCard(new Card(Suit.HEART, Rank.EIGHT));

        assertTrue(dealer.hasBlackjack());
    }

    @Test
    void testTakeWithClosedCard() {
        deck = new Deck(true);
        droppedDeck = new Deck(false);

        dealer.getPlayerHand().addCard(new Card(Suit.HEART, Rank.THREE));

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        dealer.take(deck, droppedDeck);

        String output = outputStream.toString();
        String expectedOut = "Dealer opens closed card Three Hearts (3)\n";

        boolean isEqual = expectedOut.equals(output);
        assertTrue(isEqual);

        // Restore the standard output
        System.setOut(originalOut);
    }

    @Test
    void testTakeWithOpenedCard() {
        deck = new Deck(true);
        deck.clearDeck();
        deck.deckAddCard(new Card(Suit.SPADE, Rank.FOUR));
        droppedDeck = new Deck(false);

        dealer.setDealerClosedCard(false);

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        dealer.take(deck, droppedDeck);

        String output = outputStream.toString();
        String expectedOut = "Dealer opens card Four Spades (4)\n";

        boolean isEqual = expectedOut.equals(output);
        assertTrue(isEqual);

        // Restore the standard output
        System.setOut(originalOut);
    }
}