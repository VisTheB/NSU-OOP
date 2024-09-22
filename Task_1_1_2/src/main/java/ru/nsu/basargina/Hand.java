package ru.nsu.basargina;

import java.util.ArrayList;
import ru.nsu.basargina.deck.Card;
import ru.nsu.basargina.deck.Deck;

/**
 * Class for player's and dealer's hands.
 */
public class Hand {
    private ArrayList<Card> hand;
    private int sumOfCardsValues;

    /**
     * Create a hand.
     */
    public Hand() {
        hand = new ArrayList<Card>();
        sumOfCardsValues = 0;
    }

    /**
     * Take card from the top and put in the hand.
     *
     * @param deck - deck from where to take card
     */
    public void takeCard(Deck deck) {
        hand.add(deck.takeCard());
    }

    /**
     * Add card to the hand.
     *
     * @param card - card to be added
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Get last taken card.
     *
     * @return last card
     */
    public Card getLastCard() {
        return hand.getLast();
    }

    /**
     * Returns size of the hand.
     *
     * @return hand size
     */
    public int handSize() {
        return hand.size();
    }

    /**
     * Drops cards from main deck to droppedDeck.
     *
     * @param droppedDeck - deck where to drop
     */
    public void dropHand(Deck droppedDeck) {
        droppedDeck.addCards(hand);

        hand.clear();
    }

    /**
     * Count card values in the hand and handle aces situations.
     *
     * @return sum
     */
    public int countHandSum() {
        int aceCount = 0;
        sumOfCardsValues = 0;

        for (Card card : hand) {
            sumOfCardsValues += card.getValue();

            if (card.getValue() == 11) {
                aceCount++;
            }
        }

        if (sumOfCardsValues > 21 && aceCount > 0) {
            // If there are > 0 aces and sum > 21, then set each ace to 1 until sum > 21
            while (sumOfCardsValues > 21 && aceCount > 0) {
                aceCount--;
                sumOfCardsValues -= 10;
            }
        }
        return sumOfCardsValues;
    }

    /**
     * Returns hand converted to string.
     *
     * @param isClosed - if dealer has closed card
     * @return - string hand
     */
    public String showHand(boolean isClosed) {
        String out = "[";

        if (isClosed) {
            for (int i = 0; i < hand.size() - 1; i++) {
                Card card = hand.get(i);
                out += card.getStringCard() + ", ";
            }
            out += "<closed card>]";
        } else {
            for (Card card : hand) {
                out += card.getStringCard() + ", ";
            }

            out = out.substring(0, out.length() - 2); // removes ", " from the end
            out += "]";
            out += " => ";
            out += countHandSum();
        }

        return out;
    }
}
