package game.messages;
import game.Position;
import game.tiles.Unit;

import java.util.ArrayList;
import java.util.List;

public class MoveResult {
    private boolean hasMoved;
    private final List<Position> newPosition; // Nullable
    private final String message;
    private final boolean castingAbility;
    private boolean toPrint = false;

    private MoveResult(boolean hasMoved, Position newPosition, String message) {
        this.hasMoved = hasMoved;
        this.newPosition = new ArrayList<>();
        this.newPosition.add(newPosition);
        this.message = message;
        this.castingAbility = false; // Default value, can be overridden if needed
    }

    private MoveResult(boolean hasMoved, String message, List<Position> pos, boolean castingAbility) {
        this.hasMoved = hasMoved;
        this.newPosition = pos != null ? new ArrayList<>(pos) : new ArrayList<>();
        this.message = message;
        this.castingAbility = castingAbility;
    }

    public static MoveResult moveTo(Position newPos) {
        return new MoveResult(true, newPos, "Moved successfully to:" + newPos);
    }

    public static MoveResult moveToWithDefeat(Position newPos, Unit defeated, String message) {
        return new MoveResult(true, newPos, message);
    }

    public static MoveResult noMove(String reason) {
        return new MoveResult(false, null, reason);
    }

    public static MoveResult abilityCasting(boolean hasMoved, String message, List<Position> pos,boolean castingAbility) {
        return new MoveResult(hasMoved, message,pos, castingAbility);
    }

    public boolean hadCastingAbility() {
        return castingAbility;
    }

    public String getMessage() {
        return toPrint ? message : "" ;
    }

    public void setPrint(boolean print) {
        toPrint = print;
    }

    public List<Position> getPosition() {
        return newPosition;
    }

    public boolean didMove() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public static MoveResult defeated(Unit defeated, String reason ) {
        return new MoveResult(false, null, reason);
    }

}
