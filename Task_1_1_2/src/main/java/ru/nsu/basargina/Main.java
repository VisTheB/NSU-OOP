package ru.nsu.basargina;

/**
 * Main class for the game.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to BlackJack!");

        Game blackJack = new Game();

        blackJack.startRound();
    }
}