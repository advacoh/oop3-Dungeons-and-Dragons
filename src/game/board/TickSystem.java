package game.board;

import game.tiles.Unit;
import game.units.players.Player;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class TickSystem {
    private final Scanner scanner;
    private final Player player;
    private final File[] levelFiles;
    private int currentLevelIndex;
    private Board board;

    public TickSystem(Player player, String levelsDirectoryPath) {
        this.player = player;
        this.scanner = new Scanner(System.in);
        File dir = new File(levelsDirectoryPath);

        this.levelFiles = Arrays.stream(dir.listFiles((d, name) -> name.matches("level\\d+\\.txt")))
                .sorted(Comparator.comparingInt(f -> Integer.parseInt(f.getName().replaceAll("\\D+", ""))))
                .toArray(File[]::new);

        if (levelFiles.length == 0) {
            throw new IllegalArgumentException("No level files found in directory: " + levelsDirectoryPath);
        }

        this.currentLevelIndex = 0;
        loadCurrentLevel();
    }

    private void loadCurrentLevel() {
        if (currentLevelIndex >= levelFiles.length) {
            try {
                WinnersAnimation();
            } catch (InterruptedException e) {
                System.err.println("Error during winners animation: " + e.getMessage());
            }
            System.exit(0);
        }
        File file = levelFiles[currentLevelIndex];
        System.out.println("Loading level: " + (currentLevelIndex+1));
        this.board = new Board( player, file.getPath());
    }

    public void WinnersAnimation() throws InterruptedException {
        String[] fireworks = {
                "        *",
                "       ***",
                "      *****",
                "     *******",
                "    *********",
                "   ***********",
                "    *********",
                "     *******",
                "      *****",
                "       ***",
                "        *"
        };

        for (int round = 0; round < 3; round++) {
            for (String line : fireworks) {
                System.out.println(line);
                Thread.sleep(100);
            }

            System.out.println("\n\n🎉 Congratulations! 🎉\n\n");
            Thread.sleep(800);


            for (int i = 0; i < 20; i++) {
                System.out.println();
            }
        }
    }

    public void startGameLoop() {
        while (true) {
            board.printBoard();
            player.tick();

            if (!player.isAlive()) {
                System.out.println("💀 You have died. Game over.");
                break;
            }

            System.out.println("Enter your move (w/a/s/d/e=ability) q = skip");
            char input = scanner.nextLine().charAt(0);
            while (input == 'e' && !player.abilityReady(board)) {
                System.out.println("Ability not ready, please choose another action.");
                input = scanner.nextLine().charAt(0);
            }

            tick(input);
            checkLevelCompletion();
        }
    }

    public void tick(char input) {
        if (input == 'q') {
            System.out.println("Skipping turn...");
        } else {
            board.tryMovePlayer(input);
        }

        runEnemiesTurn();
    }

    public void runEnemiesTurn() {
        System.out.println("Player turn ended, Performing enemies actions:");
        board.enemiesTurn();
    }

    private void checkLevelCompletion() {
        if (board.getEnemies().stream().noneMatch(Unit::isAlive)) {
            System.out.println("✅ Level completed!");
            currentLevelIndex++;
            loadCurrentLevel();
        }
    }
}
