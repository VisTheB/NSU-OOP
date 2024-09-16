package ru.nsu.basargina.deck;

/**
 * Cards' suits.
 */
public enum Suit {
    CLUB("Clubs"),
    DIAMOND("Diamonds"),
    HEART("Hearts"),
    SPADE("Spades");

    final String suitName;

    /**
     * Create card's suit.
     *
     * @param suitName - suit name
     */
    Suit(String suitName) {
        this.suitName = suitName;
    }

    /**
     * Returns name of the suit.
     *
     * @return suit name
     */
    public String getSuitName() {
        return suitName;
    }
}
