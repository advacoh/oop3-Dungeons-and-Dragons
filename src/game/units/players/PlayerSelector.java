package game.units.players;

import game.Position;
import java.util.*;

public class PlayerSelector {

    public static Player selectPlayer() {
        List<String> playerNames = new ArrayList<>(List.of(
                "Jon Snow", "The Hound",
                "Melisandre", "Thoros of Myr",
                "Arya Stark", "Bronn"
        ));

        System.out.println("Choose your player:\n");
        System.out.println("+----+-------------------+");
        System.out.println("| ID | Name              |");
        System.out.println("+----+-------------------+");

        for (int i = 0; i < playerNames.size(); i++) {
            System.out.printf("| %-2d | %-17s |\n", i + 1, playerNames.get(i));
        }

        System.out.println("+----+-------------------+");

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice < 1 || choice > playerNames.size()) {
            System.out.print("Enter the ID of your chosen player: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice < 1 || choice > playerNames.size()) {
                    System.out.println("Invalid choice. Please try again.");
                }
            } else {
                scanner.next(); // discard invalid input
                System.out.println("Please enter a number.");
            }
        }

        String selectedName = playerNames.get(choice - 1);
        return PlayerLibrary.getPlayerByName(selectedName);
    }
}
