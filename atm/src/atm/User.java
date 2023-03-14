package atm;

import java.util.ArrayList;

public class User {
	private String id, password, name;
	private ArrayList<Account> accs;

	public User(String id, String password, String name, ArrayList<Account> list) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.accs = list;
	}

	public User(String id, String password, String name) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.accs = new ArrayList<Account>();
	}

	public String getId() {
		return this.id;
	}

	public String getPassWord() {
		return this.password;
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<Account> getAccs() {
		ArrayList<Account> list = new ArrayList<Account>();
		int index = 0;
		for (Account i : this.accs) {
			String id = i.getId();
			String password = i.getAccount();
			int balance = i.getBalance();

			Account account = new Account(id, password);
			account.setBalance(balance);
			list.add(index++, account);
		}
		return list;
	}

	public void addAccount(Account account) {
		if (this.accs.size() < Account.LIMIT - 1)
			this.accs.add(account);
		else
			System.out.println("계좌는 1인당 3개까지 보유가능합니다.");
	}

	public void deleteAccount(int index) {
		accs.remove(index);
	}

	public void setPassWord(String Password) {
		this.password = Password;
	}

	@Override
	public String toString() {
		return String.format("%s[%s, %s]\n", this.name, this.id, this.password);
	}
}
