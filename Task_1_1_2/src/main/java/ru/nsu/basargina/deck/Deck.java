package ru.nsu.basargina.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Class dor game deck of 52 cards.
 */
public class Deck {
    private final ArrayList<Card> deck;

    /**
     * Create deck.
     *
     * @param createDeck - should it be empty or not
     */
    public Deck(boolean createDeck) {
        deck = new ArrayList<Card>();

        if (createDeck) { // Creates 52 card deck
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    deck.add(new Card(suit, rank));
                }
            }

            shuffle();
        }
    }

    /**
     * Shuffles deck.
     */
    private void shuffle() {
        Collections.shuffle(deck, new Random()); // collections utility method to shuffle the deck
    }

    /**
     * Returns player's deck.
     *
     * @return deck
     */
    public ArrayList<Card> getCards() {
        return deck;
    }

    /**
     * Add card to the deck.
     *
     * @param card - card to be added
     */
    public void deckAddCard(Card card) {
        deck.add(card);
    }

    /**
     * Function that takes card from the top of deck.
     *
     * @return card
     */
    public Card takeCard() {
        return deck.remove(0);
    }

    /**
     * Returns how many cards are left int the deck.
     *
     * @return deck size
     */
    public int cardsLeftCnt() {
        return deck.size();
    }

    /**
     * Checks if deck is not empty.
     *
     * @return is empty or not
     */
    public boolean hasCards() {
        return deck.size() > 0;
    }

    /**
     * Clears the deck.
     */
    public void clearDeck() {
        deck.clear();
    }

    /**
     * Adds given cards to the deck.
     *
     * @param cards - cards to be added
     */
    public void addCards(ArrayList<Card> cards) {
        deck.addAll(cards);
    }

    /**
     * If deck is empty, then take cards from droppedDeck.
     *
     * @param droppedDeck - deck with dropped cards
     */
    public void remakeDeck(Deck droppedDeck) {
        this.addCards(droppedDeck.getCards()); // add cards from droppedDeck

        this.shuffle();

        droppedDeck.clearDeck();

        System.out.println("The cards are out, shuffling...");
    }
}
