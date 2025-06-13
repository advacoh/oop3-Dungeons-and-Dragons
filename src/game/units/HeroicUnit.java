package game.units;

import game.board.GameContext;
import game.messages.MoveResult;

public interface HeroicUnit {
    MoveResult castAbility(GameContext context);
    boolean abilityReady(GameContext context);
}
