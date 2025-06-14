package game.tiles;

import game.Position;
import game.messages.MoveResult;

public class Wall extends Tile {

    public Wall(Position pos) {
        super('#');
        this.pos = pos;
    }

    @Override
    public MoveResult accept(InteractionVisitor visitor) {
        return visitor.visit(this);  // double dispatch
    }

    @Override
    public String toString() {
        return "Wall at " + pos.toString();
    }

    @Override
    public  boolean isUnit(){
        return false;
    }


    @Override
    public MoveResult interact(Tile unit) {
        return accept((InteractionVisitor) unit);
    }
}
