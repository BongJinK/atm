package atm;

public class Account {
	
	public static int ADD = 1;
	public static int DELETE = 2;
	public static int LIMIT = 3;

	private String userId, accNum;
	private int balance;

	
	public Account(String userId) {
		this.userId = userId;
	}
	
	public Account(String userId, String accNum, int balance) {
		this.userId = userId;
		this.accNum = accNum;
		this.balance = balance;
	}
	
	public String getId() {
		return this.userId;
	}

	public String getAccNum() {
		return this.accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public int getBalance() {
		return this.balance;
	}

	public void setBalance(int money) {
		this.balance = money;
//		this.balance += money;
	}

	@Override
	public String toString() {
		return String.format("%s[%s] : %d\n", this.accNum, this.userId, this.balance);
	}
}
