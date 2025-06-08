package game.tiles;

import game.Position;

public class Wall extends Tile {

    public Wall(Position pos) {
        super('#');
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Wall at " + pos.toString();
    }
    @Override
    public void interact() {
        System.out.println("You hit a wall at " + pos.toString());
        // No movement allowed through walls
    }
}
