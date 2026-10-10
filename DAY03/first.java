import java.util.*;

class first {
    // Static inner class
     static class BankAccount {
        private String accountHolderName;
        private double balance;

        public BankAccount(String accountHolderName, double balance) {
            this.accountHolderName = accountHolderName;
            this.balance = balance;
        }

        public String getAccountHolderName() {
            return accountHolderName;
        }

        public void setAccountHolderName(String accountHolderName) {
            this.accountHolderName = accountHolderName;
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
            } else {
                System.out.println("Deposit amount must be positive.");
            }
        }

        public void withdraw(double amount) {
            if (amount > balance) {
                System.out.println("Insufficient funds.");
            } else {
                balance -= amount;
            }
        }
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount("John Doe", 5000);

        System.out.println("Account Holder: " + account.getAccountHolderName());
        System.out.println("Balance: " + account.getBalance());

        account.deposit(1500);
        System.out.println("Updated Balance: " + account.getBalance());

        account.withdraw(2000);
        System.out.println("Balance after Withdrawal: " + account.getBalance());
    }
}