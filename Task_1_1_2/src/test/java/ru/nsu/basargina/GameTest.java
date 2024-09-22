package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.basargina.deck.Card;
import ru.nsu.basargina.deck.Rank;
import ru.nsu.basargina.deck.Suit;

/**
 * Class with game methods tests.
 */
class GameTest {
    private Game game;
    PrintStream originalOut;
    InputStream originalIn;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void testWhoWonInTheRound() {
        String input = "3\n"; // choose '3' to end the game
        originalIn = System.in; // original input stream
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        game = new Game();
        // Set wins and loses for example
        game.winsCnt = 2;
        game.losesCnt = 1;

        game.dealer.getPlayerHand().addCard(new Card(Suit.HEART, Rank.TEN));
        game.dealer.getPlayerHand().addCard(new Card(Suit.CLUB, Rank.SEVEN));

        game.human.getPlayerHand().addCard(new Card(Suit.SPADE, Rank.JACK));
        game.human.getPlayerHand().addCard(new Card(Suit.DIAMOND, Rank.NINE));

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        game.whoWonInTheRound();

        String output = outputStream.toString();

        assertEquals("You won the round! " + "Score " + game.winsCnt
                +
                ":" + game.losesCnt + " in your favor.\n" +
                "Input: 2 to continue or enter: 3 to stop\n", output);

        System.setOut(originalOut); // Restore original output
        System.setIn(originalIn); // Restore original input
    }

    @Test
    void testPrintScore() {
        // Set wins and loses for example
        game.winsCnt = 1;
        game.losesCnt = 2;

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        game.printScore();

        String output = outputStream.toString();

        assertEquals("Score " + game.winsCnt +
                ":" + game.losesCnt + " in dealer's favor.\n", output);

        // Restore the standard output
        System.setOut(originalOut);
    }

    @Test
    void testHasBlackJack() {
        String input = "3\n"; // choose '3' to end the game
        originalIn = System.in; // original input stream
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        game = new Game();
        // Set wins and loses for example
        game.winsCnt = 2;
        game.losesCnt = 2;

        game.dealer.getPlayerHand().addCard(new Card(Suit.HEART, Rank.TEN));
        game.dealer.getPlayerHand().addCard(new Card(Suit.CLUB, Rank.SEVEN));
        game.dealer.getPlayerHand().addCard(new Card(Suit.CLUB, Rank.FOUR));

        game.human.getPlayerHand().addCard(new Card(Suit.SPADE, Rank.JACK));
        game.human.getPlayerHand().addCard(new Card(Suit.DIAMOND, Rank.NINE));

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        game.hasBlackJack();

        String output = outputStream.toString();

        assertEquals("You lost the round! " + "Score " + "2" +
                ":" + "2" + " is equal.\n" +
                "Input: 2 to continue or enter: 3 to stop\n", output);

        System.setOut(originalOut); // Restore original output
        System.setIn(originalIn); // Restore original input
    }

    @Test
    void testDropHands() {
        game.dealer.getPlayerHand().addCard(new Card(Suit.HEART, Rank.TEN));
        game.dealer.getPlayerHand().addCard(new Card(Suit.CLUB, Rank.SEVEN));
        game.dealer.getPlayerHand().addCard(new Card(Suit.CLUB, Rank.FOUR));

        game.human.getPlayerHand().addCard(new Card(Suit.SPADE, Rank.JACK));
        game.human.getPlayerHand().addCard(new Card(Suit.DIAMOND, Rank.NINE));

        // Set wins for example
        game.winsCnt = 2;

        game.dropPlayersHands();

        assertEquals(0, game.dealer.getPlayerHand().handSize());
        assertEquals(0, game.human.getPlayerHand().handSize());
        assertTrue(game.droppedDeck.cardsLeftCnt() > 0);
    }

    @Test
    void testStartRound() {
        String input = "2\n0\n3\n"; // choose '3' to end the game
        originalIn = System.in; // original input stream
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        game = new Game();
        game.deck.clearDeck();
        game.deck.deckAddCard(new Card(Suit.DIAMOND, Rank.KING));
        game.deck.deckAddCard(new Card(Suit.DIAMOND, Rank.TWO));
        game.deck.deckAddCard(new Card(Suit.SPADE, Rank.QUEEN));
        game.deck.deckAddCard(new Card(Suit.HEART, Rank.JACK));
        game.deck.deckAddCard(new Card(Suit.CLUB, Rank.FIVE));

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        game.startRound();

        String output = outputStream.toString();
        String expectedOut =
                "Input: 2 to continue or enter: 3 to stop\n" +
                "Round 1\n" + "Dealer dealt the cards\n" +
                "Yours cards: [Queen Spades (10), Jack Hearts (10)] => 20\n" +
                "Dealer cards: [King Diamonds (10), <closed card>]\n" +
                "Your's turn\n" + "-------\n" +
                "Input '1', to take a card, and '0', to stop...\n" + "0\n\n" +
                "Dealer's turn\n" + "-------\n" +
                "Dealer opens closed card Two Diamonds (2)\n" +
                "Yours cards: [Queen Spades (10), Jack Hearts (10)] => 20\n" +
                "Dealer cards: [King Diamonds (10), Two Diamonds (2)] => 12\n" +
                "Dealer opens card Five Clubs (5)\n" +
                "Yours cards: [Queen Spades (10), Jack Hearts (10)] => 20\n" +
                "Dealer cards: [King Diamonds (10), Two Diamonds (2), Five Clubs (5)] => 17\n" +
                "You won the round! " + "Score " + game.winsCnt +
                ":" + game.losesCnt + " in your favor.\n" +
                "Input: 2 to continue or enter: 3 to stop\n";

        assertEquals(expectedOut, output);

        System.setOut(originalOut); // Restore original output
        System.setIn(originalIn); // Restore original input
    }
}