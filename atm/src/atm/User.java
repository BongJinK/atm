package atm;

import java.util.ArrayList;

public class User {

	private String id, password, name;
	// new 객체가 아님 -> AccountManager.list 안에 있는 인스턴스
	private ArrayList<Account> accs;

	public User(String id, String password, String name) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.accs = new ArrayList<Account>();
	}

	public User(String id, String password, String name, ArrayList<Account> accs) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.accs = accs;
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

	public int getAccountSize() {
		return this.accs.size();
	}

	public void addAccount(Account account) {
		this.accs.add(account);
	}

	public Account getAccount(int index) {
		return this.accs.get(index);
	}

	public void deleteAccount(Account account) {
		int index = indexOf(account);
		if (index != -1)
			deleteAccount(index);
	}

	private int indexOf(Account account) {
		int index = -1;
		if (account != null) {
			for (int i = 0; i < this.accs.size(); i++) {
				String saveAccount = this.accs.get(i).getAccNum();
				if (account.getAccNum().equals(saveAccount))
					return i;
			}
		}
		return index;
	}

	public void deleteAccount(int index) {
		this.accs.remove(index);
	}

	@Override
	public String toString() {
		return String.format("%s[%s, %s]\n", this.name, this.id, this.password);
	}

	public ArrayList<Account> getAccountList() {
		return (ArrayList<Account>) this.accs.clone();
	}
}
