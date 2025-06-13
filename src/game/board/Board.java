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

    public Board(Player player, String path) {
        this.player = player;
        try {
            Tile[][] board = readLevel(path);
            this.height = board.length;
            this.width = board[0].length;
            this.tiles = new Tile[height][width];
            copyBoard(board);
            printBoard();
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

//    public Tile[][] readLevel(File file) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//        String line;
//        int rows = 0;
//        int cols = 0;
//
//        while ((line = reader.readLine()) != null) {
//            cols = Math.max(cols, line.strip().length());
//            rows++;
//        }
//        reader.close();
//
//        Tile[][] board = new Tile[rows][cols];
//        reader = new BufferedReader(new FileReader(file));
//        int y = 0;
//
//        while ((line = reader.readLine()) != null) {
//            line = line.strip();
//            for (int x = 0; x < cols; x++) {
//                char c = (x < line.length()) ? line.charAt(x) : ' ';
//                Position pos = new Position(x, y);
//                Tile tile;
//
//                if (c == '#') {
//                    tile = new Wall(pos);
//                } else if (c == '.') {
//                    tile = new Empty(pos);
//                } else if (c == '@') {
//                    tile = player;
//                    player.setPos(pos);
//                } else if (Character.isLetter(c)) {
//                    try {
//                        tile = EnemyLibrary.getEnemyByTile(c, pos);
//                        enemiesList.add((Enemy) tile);
//                    } catch (IllegalArgumentException e) {
//                        System.out.println("Warning: Unknown enemy tile '" + c + "' at " + pos + ". Using Empty tile.");
//                        tile = new Empty(pos);
//                    }
//                } else {
//                    tile = new Empty(pos);
//                }
//
//                board[y][x] = tile;
//            }
//            y++;
//        }
//
//        reader.close();
//        return board;
//    }

    @Override
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Position out of bounds");
        }
        return tiles[y][x];
    }

    public void enemiesTurn(){
        for(Enemy e : enemiesList){
            if (!e.isAlive()) continue; // Skip dead enemies
            System.out.println("now making a move with:" + e);
            Position currPos = e.getPos();
            Position targetPos = e.onEnemyTurn(player);

            Tile target = getTile(targetPos.getX(), targetPos.getY());
            applyChanges(e.interact(target),targetPos,currPos);
            if(!e.isAlive())
                tiles[currPos.getY()][currPos.getX()] = new Empty(currPos);
        }
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
        if (input == 'e'){
            MoveResult result = player.castAbility(this);
            for(Position pos : result.getPosition()){
                applyChanges(result, pos, player.getPos());
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
        // 💡 Check if movement is allowed

        if (target.equals(curr)) {
            System.out.println("You are already here.");
            return;
        }
        applyChanges(result, target, curr);
    }

    private void applyChanges(MoveResult result, Position target, Position from) {
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

    private boolean inBounds(Position p) {
        return p.getX() >= 0 && p.getX() < width && p.getY() >= 0 && p.getY() < height;
    }

    @Override
    public boolean isWalkable(Position pos) {
//        if (!inBounds(pos)) return false;
//        return getTile(pos).isWalkable();
        return false;
    }

    @Override
    public void moveEnemy(Position from, Position to) {
        if (!inBounds(from) || !inBounds(to)) return;
        Tile fromTile = getTile(from.getX(), from.getY());
        Tile toTile = getTile(to.getX(), to.getY());

        //TODO find way to get rid of casting
        MoveResult result = fromTile.interact(toTile);
        if (result.didMove()) {
            setTile(to.getX(), to.getY(), fromTile);
            setTile(from.getX(), from.getY(), new Empty(from));
            fromTile.setPos(to);
        }

        if (!result.getMessage().isEmpty()) {
            System.out.println(result.getMessage());
        }
    }

    public void killPlayer(){
        System.out.println("You died. Game Over.");
        player.die();
    }

    public void killEnemies(){
        for(Enemy enemy : enemiesList) {
            if (enemy.isAlive()) {
                enemy.die();
//                tiles[enemy.getPos().getY()][enemy.getPos().getX()] = new Empty(enemy.getPos());
            }
        }
    }

    @Override
    public Stream<Tile> getTilesInRange(Position center, int range) {
        int startX = Math.max(0, center.getX() - range);
        int endX = Math.min(width - 1, center.getX() + range);
        int startY = Math.max(0, center.getY() - range);
        int endY = Math.min(height - 1, center.getY() + range);

        return IntStream.rangeClosed(startY, endY)
                .boxed()
                .flatMap(y -> IntStream.rangeClosed(startX, endX)
                        .mapToObj(x -> getTile(x, y)));
    }

    @Override
    public List<Unit> getUnitsAround(Position center, int range) {
        return getTilesInRange(center, range)
                .filter(Tile::isUnit)
                .map(tile -> (Unit) tile) //TODO casting
                .collect(Collectors.toList());
    }

    @Override
    public List<Enemy> getEnemiesInRange(int range) {
        return getTilesInRange(player.getPos(), range)
                .filter(Tile::isUnit)
                .filter(tile -> tile instanceof Enemy)
                .map(tile -> (Enemy) tile)
                .filter(Enemy::isAlive)
                .collect(Collectors.toList());
    }
}
