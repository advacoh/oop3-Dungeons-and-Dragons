package game;

import game.board.TickSystem;
import game.units.players.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a level file path.");
            return;
        }
        Player chosen = PlayerSelector.selectPlayer();
        TickSystem tickSystem = new TickSystem(chosen, args[0]);
        tickSystem.startGameLoop();
    }
}
