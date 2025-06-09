package game.messages;
import game.Position;
import game.tiles.Unit;

public class MoveResult {
    private final boolean hasMoved;
    private final Position newPosition; // Nullable
    private final String message;

    // Private constructor
    private MoveResult(boolean hasMoved, Position newPosition, String message) {
        this.hasMoved = hasMoved;
        this.newPosition = newPosition;
        this.message = message;
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

    public Position getNewPosition() {
        return newPosition;
    }

    public String getMessage() {
        return message;
    }

    public boolean didMove() {
        return hasMoved;
    }

    public static MoveResult defeated(Unit defeated, String reason ) {
        return new MoveResult(false, null, reason);
    }

}
