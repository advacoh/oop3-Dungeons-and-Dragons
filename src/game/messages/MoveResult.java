package game.messages;
import game.Position;
import game.tiles.Unit;

import java.util.ArrayList;
import java.util.List;

public class MoveResult {
    private final boolean hasMoved;
    private final List<Position> newPosition; // Nullable
    private final String message;
    private final boolean castingAbility;

    // Private constructor
    private MoveResult(boolean hasMoved, Position newPosition, String message) {
        this.hasMoved = hasMoved;
        this.newPosition = new ArrayList<>();
        this.newPosition.add(newPosition);
        this.message = message;
        this.castingAbility = false; // Default value, can be overridden if needed
    }

    private MoveResult(boolean hasMoved, String message, Position pos, boolean castingAbility) {
        this.hasMoved = hasMoved;
        this.newPosition = new ArrayList<>();
        this.newPosition.add(pos);
        this.message = message;
        this.castingAbility = castingAbility;
    }

    private MoveResult(boolean hasMoved, String message, List<Position> pos, boolean castingAbility) {
        this.hasMoved = hasMoved;
        this.newPosition = pos != null ? new ArrayList<>(pos) : new ArrayList<>();
        this.message = message;
        this.castingAbility = castingAbility;
    }

    // Move to a new position (successful movement)
    public static MoveResult moveTo(Position newPos) {
        return new MoveResult(true, newPos, "Moved successfully to:" + newPos);
    }

    // Move with defeated enemy (optional)
    public static MoveResult moveToWithDefeat(Position newPos, Unit defeated) {
        return new MoveResult(true, newPos, defeated.getName() + " was defeated.");
    }

    // No move with custom reason
    public static MoveResult noMove(String reason) {
        return new MoveResult(false, null, reason);
    }
    public static MoveResult abilityCasting(boolean hasMoved, String message, List<Position> pos,boolean castingAbility) {
        return new MoveResult(hasMoved, message,pos, castingAbility);
    }

    public boolean hadCastingAbility() {
        return castingAbility;
    }
    public List<Position> getNewPosition() {
        return newPosition;
    }

    public String getMessage() {
        return message;
    }

    public List<Position> getPosition() {
        return newPosition != null ? newPosition : null;
    }

    public boolean didMove() {
        return hasMoved;
    }

    public static MoveResult defeated(Unit defeated, String reason ) {
        return new MoveResult(false, null, reason);
    }

}
