package game.tiles;

import game.Position;
import game.messages.MoveResult;

public abstract class Tile implements Interactable {
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

    public abstract MoveResult interact(Tile unit);

    public abstract boolean isUnit();

    @Override
    public abstract MoveResult accept(InteractionVisitor visitor);


    @Override
    public String toString() {
        return String.valueOf(tile);
    }

}
