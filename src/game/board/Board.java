package game.board;

import game.Position;
import game.units.enemies.Enemy;
import game.units.enemies.EnemyLibrary;
import game.messages.MoveResult;
import game.units.players.Player;
import game.tiles.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Board implements GameContext {
    private final int width;
    private final int height;
    private final Tile[][] tiles;
    private Player player;
    private List<Enemy> enemiesList = new ArrayList<>();
    private static Board instance = null;

    public static Board getInstance(Player player, String path) {
        if (instance == null) {
            instance = new Board(player,path);
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    private Board(Player player, String path) {
        this.player = player;
        try {
            Tile[][] board = readLevel(path);
            this.height = board.length;
            this.width = board[0].length;
            this.tiles = new Tile[height][width];
            copyBoard(board);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage());
        }
    }

    private Tile[][] readLevel(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        int rows = lines.size();
        int cols = lines.stream().mapToInt(String::length).max().orElse(0);

        Tile[][] board = new Tile[rows][cols];
        for (int y = 0; y < rows; y++) {
            String line = lines.get(y);
            for (int x = 0; x < cols; x++) {
                char c = (x < line.length()) ? line.charAt(x) : ' ';
                Position pos = new Position(x, y);
                Tile tile;
                if (c == '#') tile = new Wall(pos);
                else if (c == '.') tile = new Empty(pos);
                else if (c == '@') { tile = player; player.setPos(pos); }
                else if (Character.isLetter(c)) {
                    try {
                        Enemy e = EnemyLibrary.getEnemyByTile(c, pos);
                        tile = e;
                        enemiesList.add(e);
                    } catch (IllegalArgumentException ex) {
                        tile = new Empty(pos);
                    }
                } else {
                    tile = new Empty(pos);
                }
                board[y][x] = tile;
            }
        }
        return board;
    }
    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemiesList;
    }

    private void copyBoard(Tile[][] board) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                this.tiles[y][x] = board[y][x];
            }
        }
    }

    @Override
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Position out of bounds");
        }
        return tiles[y][x];
    }

    public void enemiesTurn(){
        for(Enemy e : enemiesList){
            if (!e.isAlive()) continue;
//            System.out.println("now making a move with:" + e);
            Position currPos = e.getPos();
            Position targetPos = e.onEnemyTurn(player);

            Tile target = getTile(targetPos.getX(), targetPos.getY());
            applyChanges(e.interact(target),targetPos,currPos);
            if(!e.isAlive())
                tiles[currPos.getY()][currPos.getX()] = new Empty(currPos);
        }
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
        if (input == 'e'){
            MoveResult result = player.castAbility(this);
            for(Position pos : result.getPosition()){
                applyChanges(result, pos, player.getPos());
                result.setPrint(false);
            }
            return;
        }
        Position curr = player.getPos();
        Position target = curr.shiftBy(input);

        if (!inBounds(target)) {
            System.out.println("Cannot move out of bounds!");
            return;
        }

        Tile targetTile = getTile(target.getX(), target.getY());
        MoveResult result = player.interact(targetTile);

        if (target.equals(curr)) {
            System.out.println("You are already here.");
            return;
        }
        applyChanges(result, target, curr);
    }

    private void applyChanges(MoveResult result, Position target, Position from) {
        if(result.getToPrint())
            System.out.println(result.getMessage());
        if (result.hadCastingAbility()) {
            if (result.didMove()) {
                tiles[target.getY()][target.getX()] = new Empty(target);
            }
        }
        else if (result.didMove()){
            Tile mover = tiles[from.getY()][from.getX()];
            tiles[target.getY()][target.getX()] = mover;
            tiles[from.getY()][from.getX()] = new Empty(from);
        }
    }

    public boolean inBounds(Position p) {
        return p.getX() >= 0 && p.getX() < width && p.getY() >= 0 && p.getY() < height;
    }

    @Override
    public Stream<Tile> getTilesInRange(Position center, int range) {
        int startX = Math.max(0, center.getX() - range);
        int endX = Math.min(width - 1, center.getX() + range);
        int startY = Math.max(0, center.getY() - range);
        int endY = Math.min(height - 1, center.getY() + range);

        return IntStream.rangeClosed(startY, endY)
                .boxed()
                .flatMap(y -> IntStream.range(startX, endX)
                        .mapToObj(x -> getTile(x, y)));
    }

    @Override
    public List<Enemy> getEnemiesInRange(int range) {
        return getTilesInRange(player.getPos(), range)
                .filter(Tile::isUnit)
                .filter(Tile::isEnemy)
                .map(tile -> (Enemy) tile)
                .filter(Enemy::isAlive)
                .collect(Collectors.toList());
    }

    public void setEnemies(List<Enemy> es) {
        this.enemiesList = es;

        for (Enemy e : es){
            Position p = e.getPos();
            tiles[p.getY()][p.getX()] = e;
        }
    }
}
