package ru.nsu.basargina.players;

import ru.nsu.basargina.deck.Deck;

import java.util.Scanner;

/**
 * Class for the human player from Player.
 */
public class Human extends Player {
    Scanner inp = new Scanner(System.in);

    /**
     * Create human player.
     */
    public Human() {
        super.setPlayerName("You");
    }

    public void makeMove(Deck deck, Deck droppedDeck, Dealer dealer) {
        int move;

        System.out.println("Input '1', to take a card, and '0', to stop...");
        move = inp.nextInt();
        System.out.println(move);

        if (move == 1) {

            this.take(deck, droppedDeck, false, false); // take card
            printHumanHand();
            dealer.printDealerHand(dealer.ifHasClosed());

            if(this.getPlayerHand().countHandSum() >= 21) {
                return;
            }
            else {
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
        System.out.println("Your's cards: " + super.getPlayerHand().showHand(false));
    }
}
