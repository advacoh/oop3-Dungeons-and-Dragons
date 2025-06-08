package game.tiles;

import game.Position;

public class Empty extends Tile {
    public Empty(Position pos) {
        super('.');
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Empty tile at " + pos.toString();
    }
    @Override
    public void interact() {
        System.out.println("You step on an empty tile at " + pos.toString());
        moveTo();
    }
    public void moveTo() {

    }
}
