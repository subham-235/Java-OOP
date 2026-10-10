public class second{
    private class BankAccount{
        private String accountNumber;
        private double balance;

        BankAccount(String accountNumber, double balance) {
            this.accountNumber = accountNumber;
            this.balance = balance;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public double getBalance() {
            return balance;
        }
    }

    public static void main(String[] args) {
       BankAccount account = new second().new BankAccount("123456789", 1000.0);
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Balance: " + account.getBalance());
    }
}