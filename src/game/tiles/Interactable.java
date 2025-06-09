package game.tiles;

import game.messages.MoveResult;

public interface Interactable {
    MoveResult interact(Tile actor);              // entry point for interaction
    MoveResult accept(InteractionVisitor visitor); // double dispatch resolution
}