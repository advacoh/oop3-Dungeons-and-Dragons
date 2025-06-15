package game.units.players;

import java.util.*;

public class PlayerSelector {

    public static Player selectPlayer() {
        List<Player> players = new ArrayList<>(List.of(
                PlayerLibrary.getPlayerByName("Jon Snow"),
                PlayerLibrary.getPlayerByName("The Hound"),
                PlayerLibrary.getPlayerByName("Melisandre"),
                PlayerLibrary.getPlayerByName("Thoros of Myr"),
                PlayerLibrary.getPlayerByName("Arya Stark"),
                PlayerLibrary.getPlayerByName("Bronn")
        ));

        System.out.println("Choose your player:\n");
        System.out.println("+----+--------------------------------------------------------------+");
        System.out.println("| ID | Player Info                                                  |");
        System.out.println("+----+--------------------------------------------------------------+");

        for (int i = 0; i < players.size(); i++) {
            System.out.printf("| %-2d | %-60s |\n", i + 1, players.get(i).toString());
        }

        System.out.println("+----+--------------------------------------------------------------+");

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice < 1 || choice > players.size()) {
            System.out.print("Enter the ID of your chosen player: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > players.size()) {
                    System.out.println("Invalid choice. Please try again.");
                }
            } else {
                scanner.next(); // discard invalid input
                System.out.println("Please enter a number.");
            }
        }

        return players.get(choice - 1);
    }
}
