package atm;

public class Account {
	public static int LIMIT = 3;

	private String userId, account;
	private int balance;

	public Account(String userId, String account) {
		this.userId = userId;
		this.account = account;
		this.balance = 0;
	}

	public String getId() {
		return this.userId;
	}

	public String getAccount() {
		return this.account;
	}

	public int getBalance() {
		return this.balance;
	}

	public void setBalance(int money) {
		this.balance += money;
	}

	@Override
	public String toString() {
		return String.format("%s[%s] : %d\n", this.account, this.userId, this.balance);
	}
}
