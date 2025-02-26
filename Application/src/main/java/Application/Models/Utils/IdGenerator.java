package Application.Models.Utils;


public class IdGenerator {
    private int userId = 0;
    private int bankAccountId = 0;

    public synchronized int generateUserId() {
        return ++userId;
    }

    public synchronized int generateBankAccountId() {
        return ++bankAccountId;
    }
}
