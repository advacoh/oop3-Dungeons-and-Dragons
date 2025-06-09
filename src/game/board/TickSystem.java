package game.board;

import game.enemies.Enemy;
import game.players.Player;

import java.io.BufferedReader;
import java.util.List;
import java.util.Scanner;

public class TickSystem {
    private final Board board;
    private final Scanner scanner;

    public TickSystem(Board board) {
        this.board = board;
        this.scanner = new Scanner(System.in);
    }

    public void startGameLoop(){
        while (true){
            board.printBoard();
            System.out.println("Enter your move (w/a/s/d/e=ability) q = skip");
            char input = scanner.nextLine().charAt(0);

            tick(input);
        }
    }

    public void tick(char input) {
        if (input == 'e') {
            board.getPlayer().abilityAttempt();
        } else if (input == 'q') {
            System.out.println("Skipping turn...");
        } else {
            board.tryMovePlayer(input);
        }

        // After player acts, do enemy turn
        runEnemiesTurn();
    }

    public void runEnemiesTurn() {
        System.out.println("acting with enemies");
        board.enemiesTurn();
    }
}
