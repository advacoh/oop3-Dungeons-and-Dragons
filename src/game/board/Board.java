package game.board;

import game.Position;
import game.players.Player;
import game.players.Warrior;
import game.tiles.Empty;
import game.tiles.Tile;
import game.tiles.Wall;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Board {
    private final int width;
    private final int height;
    private final Tile[][] tiles;
    private Player player;

    public Board(int width, int height,Player player, String path) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[height][width];
        this.player = player;
        File file = new File(path);
        try {
            Tile[][] board = readLevel(file);
            copyBoard(board); // Save into this.tiles
            printBoard();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private void copyBoard(Tile[][] board) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                this.tiles[y][x] = board[y][x];
            }
        }
    }

    public Tile[][] readLevel(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int rows = 0;
        int cols = 0;

        // First pass: determine dimensions
        while ((line = reader.readLine()) != null) {
            cols = Math.max(cols, line.strip().length());
            rows++;
        }
        reader.close();

        Tile[][] board = new Tile[rows][cols];
        reader = new BufferedReader(new FileReader(file));
        int y = 0;

        while ((line = reader.readLine()) != null) {
            line = line.strip();
            for (int x = 0; x < cols; x++) {
                char c = (x < line.length()) ? line.charAt(x) : ' ';
                Position pos = new Position(x, y);
                Tile tile;

                switch (c) {
                    case '#':
                        tile = new Wall(pos);
                        break;
                    case '.':
                        tile = new Empty(pos);
                        break;
                    case '@':
                        tile = player;
                        player.setPos(new Position(x, y));
                        break;
                    default:
                        tile = new Empty(pos);
                        break;
                }

                board[y][x] = tile;
            }
            y++;
        }

        reader.close();
        return board;
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Position out of bounds");
        }
        return tiles[y][x];
    }

    public void setTile(int x, int y, Tile tile) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Position out of bounds");
        }
        tiles[y][x] = tile;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void printBoard() {
        System.out.println(player.toString());
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                System.out.print(tile == null ? '?' : tile.getTile());
            }
            System.out.println();
        }
    }

    public void tryMovePlayer(char input) {
        Position curr = player.getPos();
        Position target = curr.shiftBy(input);
        Tile targetTile = getTile(target.getX(), target.getY());

        targetTile.interact(player);


    }


}
