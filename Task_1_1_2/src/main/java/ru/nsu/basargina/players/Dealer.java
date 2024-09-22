package ru.nsu.basargina.players;

/**
 * Class for the dealer from Player.
 */
public class Dealer extends Player {
    private boolean hasClosed;

    /**
     * Create dealer.
     */
    public Dealer() {
        super.setPlayerName("Dealer");
        hasClosed = true; // if dealer has a closed card
        super.setDealerOrNot(true);
    }

    /**
     * Sets dealer's closed card to has or not.
     *
     * @param isClosed - is card closed or not
     */
    public void setDealerClosedCard(boolean isClosed) {
        this.hasClosed = isClosed;
    }

    /**
     * Checks if dealer's card is closed.
     *
     * @return closed or not
     */
    public boolean ifHasClosed() {
        return this.hasClosed;
    }

    /**
     * Prints dealer's hand.
     */
    public void printDealerHand(boolean hasClosed) {
        String stringHand = super.getPlayerHand().showHand(hasClosed);

        System.out.println("Dealer cards: " + stringHand);
    }
}
