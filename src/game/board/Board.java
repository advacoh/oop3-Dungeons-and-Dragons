package game.board;

import game.Position;
import game.enemies.Enemy;
import game.enemies.EnemyLibrary;
import game.messages.MoveResult;
import game.players.Player;
import game.tiles.*;

import java.io.*;
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

    public Board(int width, int height, Player player, String path) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[height][width];
        this.player = player;
        File file = new File(path);
        try {
            Tile[][] board = readLevel(file);
            copyBoard(board);
            printBoard();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
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

    public Tile[][] readLevel(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int rows = 0;
        int cols = 0;

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

                if (c == '#') {
                    tile = new Wall(pos);
                } else if (c == '.') {
                    tile = new Empty(pos);
                } else if (c == '@') {
                    tile = player;
                    player.setPos(pos);
                } else if (Character.isLetter(c)) {
                    try {
                        tile = EnemyLibrary.getEnemyByTile(c, pos);
                        enemiesList.add((Enemy) tile);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Warning: Unknown enemy tile '" + c + "' at " + pos + ". Using Empty tile.");
                        tile = new Empty(pos);
                    }
                } else {
                    tile = new Empty(pos);
                }

                board[y][x] = tile;
            }
            y++;
        }

        reader.close();
        return board;
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
        Position curr = player.getPos();
        Position target = curr.shiftBy(input);


        if (!inBounds(target)) {
            System.out.println("Cannot move out of bounds!");
            return;
        }

        Tile targetTile = getTile(target.getX(), target.getY());
        applyChanges(player.interact(targetTile), target, curr);
    }

    private void applyChanges(MoveResult result, Position target, Position from) {
        System.out.println(result.getMessage());
        if (result.didMove()){
            Tile mover = tiles[from.getY()][from.getX()];
            tiles[target.getY()][target.getX()] = mover;
            tiles[from.getY()][from.getX()] = new Empty(from);
        } else {
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
}
