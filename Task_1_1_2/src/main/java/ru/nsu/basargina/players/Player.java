package ru.nsu.basargina.players;

import ru.nsu.basargina.Hand;
import ru.nsu.basargina.deck.Deck;

/**
 * Class for the human and the dealer.
 */
public abstract class Player {
    private Hand hand;
    private String playerName;

    /**
     * Create a new player.
     */
    public Player() {
        this.hand = new Hand();
        this.playerName = "";
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
    public void take(Deck deck, Deck droppedDeck, boolean isDealer, boolean isHasClosed) {
        if (!deck.hasCards()) {
            deck.remakeDeck(droppedDeck);
        }


        if (isDealer) {
            if (isHasClosed) {
                System.out.println("Dealer opens closed card " + this.hand.getLastCard().getStringCard());
                Dealer dealer = (Dealer) this;
                dealer.setDealerClosedCard(false);

            } else {
                this.hand.takeCard(deck);
                System.out.println("Dealer opens card " + this.hand.getLastCard().getStringCard());
            }
        } else {
            this.hand.takeCard(deck);
            System.out.println("You opened card " + this.hand.getLastCard().getStringCard());
        }
    }
}
