package game.tiles;

import game.Position;
import game.messages.MoveResult;

public class Empty extends Tile {
    public Empty(Position pos) {
        super('.');
        this.pos = pos;
    }

    @Override
    public MoveResult accept(InteractionVisitor visitor) {
        return visitor.visit(this);  // double dispatch
    }

    @Override
    public String toString() {
        return "Empty tile at " + pos.toString();
    }

    @Override
    public  boolean isUnit(){
        return false;
    }

    public MoveResult interact(Tile unit) {
        return accept((InteractionVisitor) unit); //TODO casting
    }
}
