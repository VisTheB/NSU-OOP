package ru.nsu.basargina.deck;

/**
 * Class for the card.
 */
public class Card {
    private final Suit suit;
    private final Rank rank;

    /**
     * Create card.
     *
     * @param suit - card's suit
     * @param rank - card's rank
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Creates string representation of the card.
     *
     * @return string card
     */
    public String getStringCard() {
        return rank.getRankName() + " " + suit.getSuitName() + " (" + this.getValue() + ")";
    }

    public int getValue(){
        return rank.rankValue;
    }
}
