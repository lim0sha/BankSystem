package Application.Contracts.ResultTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

public abstract sealed class BankAccountResult
        permits BankAccountResult.Success, BankAccountResult.BankAccountCreationError,
        BankAccountResult.BankAccountUpdateError, BankAccountResult.BankAccountDeletionError {

    public static final class Success extends BankAccountResult {
    }

    @Getter
    @AllArgsConstructor
    public static final class BankAccountCreationError extends BankAccountResult {
        private final String message;
    }

    @Getter
    @AllArgsConstructor
    public static final class BankAccountUpdateError extends BankAccountResult {
        private final String message;
    }

    @Getter
    @AllArgsConstructor
    public static final class BankAccountDeletionError extends BankAccountResult {
        private final String message;
    }
}
