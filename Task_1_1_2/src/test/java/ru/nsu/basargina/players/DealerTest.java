package ru.nsu.basargina.players;

import ru.nsu.basargina.deck.Card;
import ru.nsu.basargina.deck.Rank;
import ru.nsu.basargina.deck.Suit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class with dealer methods tests.
 */
class DealerTest {
    private Dealer dealer;

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
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        dealer.printDealerHand(false);

        String output = outputStream.toString();

        assertEquals("Dealer cards: [Queen Diamonds (10), Three Hearts (3)] => 13\r\n", output,
                "output should be correct");

        // Restore the standard output
        System.setOut(originalOut);
    }
}