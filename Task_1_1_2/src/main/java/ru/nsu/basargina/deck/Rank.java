package ru.nsu.basargina.deck;

/**
 *  Cards' ranks.
 */
public enum Rank {
    ACE("Ace", 11),
    JACK("Jack",10),
    QUEEN("Queen",10),
    KING("King",10),
    TEN("Ten", 10),
    NINE("Nine", 9),
    EIGHT("Eight", 8),
    SEVEN("Seven", 7),
    SIX("Six", 6),
    FIVE("Five", 5),
    FOUR("Four", 4),
    THREE("Three", 3),
    TWO("Two", 2);

    final String rankName;
    final int rankValue;

    /**
     * Create card's rank.
     *
     * @param rankName - rank name
     * @param rankValue - rank value
     */
    Rank(String rankName, int rankValue) {
        this.rankName = rankName;
        this.rankValue = rankValue;
    }

    /**
     * Returns name of the rank.
     *
     * @return rank name
     */
    public String getRankName() {
        return rankName;
    }
}
