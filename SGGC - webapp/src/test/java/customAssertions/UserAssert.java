package customAssertions;

import com.app.demo.model.User;
import org.assertj.core.api.AbstractAssert;

public class UserAssert extends AbstractAssert<UserAssert, User> {

    public UserAssert(User user) {
        super(user, UserAssert.class);
    }

    public static UserAssert assertThat(User actual) {
        return new UserAssert(actual);
    }

    public UserAssert hasDetails() {
        isNotNull();
        if (actual.getId() == null) {
            failWithMessage(
                    "Expected user to have a id, but it was null"
            );
        }
        if (actual.getOwnedGameIds() == null) {
            failWithMessage(
                    "Expected user to have a list of owned game ids, but it was null"
            );
        }
        return this;
    }

}