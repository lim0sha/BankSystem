package Application.ResultTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract sealed class OperationResult permits OperationResult.Success, OperationResult.OperationError {

    @NoArgsConstructor
    public static final class Success extends OperationResult {
    }

    @Getter
    @AllArgsConstructor
    public static final class OperationError extends OperationResult {
        private final String message;
    }
}
