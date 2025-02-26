package Application.Contracts.ResultTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

public abstract sealed class UserResult
        permits UserResult.Success, UserResult.UserCreationError, UserResult.UserUpdateError, UserResult.UserDeletionError {

    public static final class Success extends UserResult {
    }

    @Getter
    @AllArgsConstructor
    public static final class UserCreationError extends UserResult {
        private final String message;
    }

    @Getter
    @AllArgsConstructor
    public static final class UserUpdateError extends UserResult {
        private final String message;
    }

    @Getter
    @AllArgsConstructor
    public static final class UserDeletionError extends UserResult {
        private final String message;
    }
}
