package game.tiles;

import game.Position;

public abstract class Tile  {
    protected char tile;
    protected Position pos;

    public Tile(char tile) {
        this.tile = tile;
    }
    public char getTile() {
        return tile;
    }
    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public void interact() {
        System.out.println("Interacting with tile at " + pos.toString());
    }

    public void interact(T object){
        System.out.println("Interacting with tile at " + pos.toString() + " with object: " + object);
    }

}
