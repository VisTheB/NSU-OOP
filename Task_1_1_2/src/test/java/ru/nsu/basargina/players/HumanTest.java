package ru.nsu.basargina.players;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import ru.nsu.basargina.deck.Card;
import ru.nsu.basargina.deck.Deck;
import ru.nsu.basargina.deck.Rank;
import ru.nsu.basargina.deck.Suit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class with human methods tests.
 */
class HumanTest {
    private Human human;
    private Deck deck;
    private Deck droppedDeck;

    @Test
    void testMakeMoveInput1() {
        String input = "1\n0\n"; // choose '1' then '0' to stop
        final InputStream originalIn = System.in; // original input stream

        // Test input '1'
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        human = new Human();
        deck = new Deck(true);
        droppedDeck = new Deck(false);

        human.makeMove(deck, droppedDeck, new Dealer());

        assertTrue(human.getPlayerHand().handSize() > 0);

        System.setIn(originalIn); // restore original input
    }

    @Test
    void testMakeMoveInput0() {
        String input = "0\n"; // choose '1' then '0' to stop
        final InputStream originalIn = System.in; // original input stream

        // Test input '0'
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        human = new Human();
        deck = new Deck(true);
        droppedDeck = new Deck(false);

        human.makeMove(deck, droppedDeck, new Dealer());

        assertFalse(human.getPlayerHand().handSize() > 0);

        System.setIn(originalIn); // restore original input
    }

    @Test
    void testPrintHumanHand() {
        human = new Human();
        deck = new Deck(true);
        droppedDeck = new Deck(false);

        human.getPlayerHand().addCard(new Card(Suit.HEART, Rank.ACE));
        human.getPlayerHand().addCard(new Card(Suit.SPADE, Rank.KING));

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        human.printHumanHand();

        String output = outputStream.toString();

        assertTrue("Yours cards: [Ace Hearts (11), King Spades (10)] => 21\r\n".equals(output));

        // Restore the standard output
        System.setOut(originalOut);
    }
}