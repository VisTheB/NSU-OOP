package ru.nsu.basargina.players;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import ru.nsu.basargina.deck.Card;
import ru.nsu.basargina.deck.Deck;
import ru.nsu.basargina.deck.Rank;
import ru.nsu.basargina.deck.Suit;

/**
 * Class with human methods tests.
 */
class HumanTest {
    private Human human;
    private Deck deck;
    private Deck droppedDeck;
    PrintStream originalOut;
    InputStream originalIn;

    @Test
    void testMakeMoveInput1() {
        String input = "1\n0\n"; // choose '1' then '0' to stop
        originalIn = System.in; // original input stream

        // Test input '1'
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        human = new Human(new Scanner(System.in));
        deck = new Deck(true);
        droppedDeck = new Deck(false);

        human.makeMove(deck, droppedDeck, new Dealer());

        assertTrue(human.getPlayerHand().handSize() > 0);

        System.setIn(originalIn); // restore original input
    }

    @Test
    void testMakeMoveInput0() {
        String input = "0\n"; // choose '1' then '0' to stop
        originalIn = System.in; // original input stream

        // Test input '0'
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        human = new Human(new Scanner(System.in));
        deck = new Deck(true);
        droppedDeck = new Deck(false);

        human.makeMove(deck, droppedDeck, new Dealer());

        assertFalse(human.getPlayerHand().handSize() > 0);

        System.setIn(originalIn); // restore original input
    }

    @Test
    void testPrintHumanHand() {
        human = new Human(new Scanner(System.in));
        deck = new Deck(true);
        droppedDeck = new Deck(false);

        human.getPlayerHand().addCard(new Card(Suit.HEART, Rank.ACE));
        human.getPlayerHand().addCard(new Card(Suit.SPADE, Rank.KING));

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        human.printHumanHand();

        String output = outputStream.toString();
        String expectedOutput = "Yours cards: [Ace Hearts (11), King Spades (10)] => 21\n";

        boolean isEqual = expectedOutput.equals(output);
        assertTrue(isEqual);

        // Restore the standard output
        System.setOut(originalOut);
    }

    @Test
    void testTake() {
        human = new Human(new Scanner(System.in));
        deck = new Deck(true);
        deck.clearDeck();
        deck.deckAddCard(new Card(Suit.DIAMOND, Rank.SEVEN));
        droppedDeck = new Deck(false);

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        human.take(deck, droppedDeck);

        String output = outputStream.toString();
        String expectedOut = "You opened card Seven Diamonds (7)\n";

        boolean isEqual = expectedOut.equals(output);
        assertTrue(isEqual);

        // Restore the standard output
        System.setOut(originalOut);
    }
}