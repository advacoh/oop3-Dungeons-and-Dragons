package game;


import game.board.Board;
import game.players.Player;
import game.players.Warrior;

import java.io.File;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a level file path.");
            return;
        }
        Warrior warrior = new Warrior('@', "Hero", 100, 20, 10,  5, 0);
        Board board = new Board(14, 6,warrior, args[0]);


    }
}
