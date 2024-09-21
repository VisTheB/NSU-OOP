package ru.nsu.basargina.players;

import ru.nsu.basargina.Hand;
import ru.nsu.basargina.deck.Deck;

/**
 * Class for the human and the dealer.
 */
public abstract class Player {
    private Hand hand;
    private String playerName;
    private boolean isDealer;

    /**
     * Create a new player.
     */
    public Player() {
        this.hand = new Hand();
        this.playerName = "";
        this.isDealer = false;
    }

    /**
     * Sets that player is a dealer or not.
     * 
     * @param isDealer - is dealer or not
     */
    public void setDealerOrNot(boolean isDealer) {
        this.isDealer = isDealer;
    }

    /**
     * Sets player's name.
     *
     * @param name - name
     */
    public void setPlayerName(String name) {
        this.playerName = name;
    }

    /**
     * Returns player's current hand.
     *
     * @return hand
     */
    public Hand getPlayerHand() {
        return this.hand;
    }

    /**
     * Checks if player has blackjack.
     *
     * @return has or not
     */
    public boolean hasBlackjack() {
        return this.getPlayerHand().countHandSum() == 21;
    }

    /**
     * Make move - i.e. take card from the deck to player's hand.
     *
     * @param deck - deck
     * @param droppedDeck - deck with dropped cards
     */
    public void take(Deck deck, Deck droppedDeck) {
        if (!deck.hasCards()) {
            deck.remakeDeck(droppedDeck);
        }

        if (this.isDealer) {
            Dealer dealer = (Dealer) this;
            if (dealer.ifHasClosed()) {
                String playerLastCard = this.hand.getLastCard().getStringCard();
                System.out.println("Dealer opens closed card " + playerLastCard);
                dealer.setDealerClosedCard(false);

            } else {
                this.hand.takeCard(deck);
                String playerLastCard = this.hand.getLastCard().getStringCard();
                System.out.println("Dealer opens card " + playerLastCard);
            }
        } else {
            this.hand.takeCard(deck);
            String playerLastCard = this.hand.getLastCard().getStringCard();
            System.out.println("You opened card " + playerLastCard);
        }
    }
}
