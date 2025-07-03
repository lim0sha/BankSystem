package Application.ResultTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Абстрактный класс, представляющий result-type операций с пользователями.
 * Включает различные типы результатов, такие как успешное выполнение операции или ошибка при создании, обновлении или удалении пользователя.
 */
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
