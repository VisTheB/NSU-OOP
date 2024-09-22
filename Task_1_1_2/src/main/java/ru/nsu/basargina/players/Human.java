package ru.nsu.basargina.players;

import java.util.Scanner;
import ru.nsu.basargina.deck.Deck;

/**
 * Class for the human player from Player.
 */
public class Human extends Player {
    Scanner inp;

    /**
     * Create human player.
     */
    public Human(Scanner inp) {
        super.setPlayerName("You");
        this.inp = inp;
    }

    /**
     * Logic for human's move.
     *
     * @param deck - human's deck
     * @param droppedDeck - deck with dropped cards
     * @param dealer - used to print dealer's hand
     */
    public void makeMove(Deck deck, Deck droppedDeck, Dealer dealer) {
        int move;

        System.out.println("Input '1', to take a card, and '0', to stop...");
        move = inp.nextInt();
        System.out.println(move);

        if (move == 1) {

            this.take(deck, droppedDeck); // take card
            printHumanHand();
            dealer.printDealerHand(dealer.ifHasClosed());

            if (this.getPlayerHand().countHandSum() >= 21) {
                return;
            } else {
                this.makeMove(deck, droppedDeck, dealer);
            }
        } else {
            System.out.print("\n");
        }
    }

    /**
     * Prints human's hand.
     */
    public void printHumanHand() {
        System.out.println("Yours cards: " + super.getPlayerHand().showHand(false));
    }
}
