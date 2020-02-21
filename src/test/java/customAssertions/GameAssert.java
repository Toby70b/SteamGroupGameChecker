package customAssertions;

import models.Game;
import org.assertj.core.api.AbstractAssert;

public class GameAssert extends AbstractAssert<GameAssert, Game> {

    public GameAssert(Game game) {
        super(game, GameAssert.class);
    }

    public static GameAssert assertThat(Game actual) {
        return new GameAssert(actual);
    }

    public GameAssert hasDetails() {
        isNotNull();
        if (actual.getId() == 0) {
            failWithMessage(
                    "Expected game to have a id, but it was null"
            );
        }
        if (actual.getName() == null) {
            failWithMessage(
                    "Expected game to have a name, but it was null"
            );
        }
        return this;
    }

}