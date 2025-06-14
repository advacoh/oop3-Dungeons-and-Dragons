package game.tiles;

import game.messages.MoveResult;

public interface Interactable {
    MoveResult interact(Tile actor);
    MoveResult accept(InteractionVisitor visitor);
}