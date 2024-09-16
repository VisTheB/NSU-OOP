package ru.nsu.basargina;

import ru.nsu.basargina.deck.Deck;
import ru.nsu.basargina.players.Dealer;
import ru.nsu.basargina.players.Human;

/**
 * Class where game logic is described.
 */
public class Game {

    private Dealer dealer;
    private Human human;
    private Deck deck;
    private Deck droppedDeck; // a deck where used cards go

    private int winsCnt;
    private int losesCnt;
    private int tiesCnt;
    private int roundCnt;

    /**
     *  Start new game.
     */
    public Game() {
        winsCnt = 0;
        losesCnt = 0;
        tiesCnt = 0;
        roundCnt = 0;

        dealer = new Dealer();
        human = new Human();

        deck = new Deck(true);
        droppedDeck = new Deck(false);

        startRound();
    }

    /**
     * Prints score in addition to "you won/lost ...".
     */
    private void printScore() {
        System.out.print("Score " + winsCnt + ":" + losesCnt);

        if (winsCnt > losesCnt) {
            System.out.println(" in your favor.");
        } else if (winsCnt < losesCnt) {
            System.out.println(" in dealer's favor.");
        } else {
            System.out.println(" is equal.");
        }
    }

    /**
     * Checks if players have blackjack.
     */
    private void hasBlackJack() {
        if (dealer.hasBlackjack()) {

            if(human.hasBlackjack()) {
                System.out.print("Both have 21. Tie! ");
                printScore();
                tiesCnt++;
                startRound();
            }
            else {
                System.out.print("You lost the round! ");
                printScore();
                losesCnt++;
                startRound();
            }
        }

        if(human.hasBlackjack()) {
            System.out.print("You won the round! ");
            printScore();
            winsCnt++;
            startRound();
        }
    }

    /**
     * Checks who won in the end of the round.
     */
    public void whoWonInTheRound() {
        if (dealer.getPlayerHand().countHandSum() > 21) {
            System.out.print("Dealer lost the round! ");
            winsCnt++;
        }
        else if (dealer.getPlayerHand().countHandSum() > human.getPlayerHand().countHandSum()) {
            System.out.print("You lost the round! ");
            losesCnt++;
        }
        else if (human.getPlayerHand().countHandSum() > dealer.getPlayerHand().countHandSum()) {
            System.out.print("You won the round! ");
            winsCnt++;
        }
        else {
            System.out.print("Both have 21. Tie! ");
        }

        printScore();

        startRound();
    }

    /**
     * Drops player's hands to droppedDeck in the beginning of each round.
     */
    private void dropPlayersHands() {
        if (winsCnt > 0 || losesCnt > 0 || tiesCnt > 0) {

            dealer.getPlayerHand().dropHand(droppedDeck);
            human.getPlayerHand().dropHand(droppedDeck);
        }

        if (deck.cardsLeftCnt() < 4) { // check if at least 4 cards left
            deck.remakeDeck(droppedDeck);
        }
    }

    /**
     * Describes round logic.
     */
    private void startRound() {
        dropPlayersHands();
        dealer.setDealerClosedCard(true);

        roundCnt++;
        System.out.println("Round " + roundCnt);
        System.out.println("Dealer dealt the cards");

        dealer.getPlayerHand().takeCard(deck);
        dealer.getPlayerHand().takeCard(deck);

        human.getPlayerHand().takeCard(deck);
        human.getPlayerHand().takeCard(deck);

        human.printHumanHand();
        dealer.printDealerHand(dealer.ifHasClosed());

        hasBlackJack();

        System.out.println("Your's turn\n" + "-------");
        human.makeMove(deck, droppedDeck, dealer);

        if (human.getPlayerHand().countHandSum() > 21) { // if human gets more than 21 in total after all moves
            System.out.println("You lost the round! ");
            printScore();

            losesCnt ++;

            startRound();
        }

        System.out.println("Dealer's turn\n" + "-------");
        while (dealer.getPlayerHand().countHandSum() < 17) {

            dealer.take(deck, droppedDeck, true, dealer.ifHasClosed());

            human.printHumanHand();
            dealer.printDealerHand(dealer.ifHasClosed());
        }

        whoWonInTheRound();
    }

}
