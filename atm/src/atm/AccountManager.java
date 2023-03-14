package atm;

import java.util.ArrayList;

public class AccountManager {

	private static ArrayList<Account> list = new ArrayList<Account>();

	// Create
	public void createAccount(Account account) {
		list.add(account);
	}

	// Read
	public Account getAccount(int index) {
		Account acc = list.get(index);

		String id = acc.getId();
		String account = acc.getAccount();
		int balance = acc.getBalance();

		Account request = new Account(id, account);
		request.setBalance(balance);

		return request;
	}

	// Update
	public void setAccount(int index, Account account) {
		list.set(index, account);
	}

	// Delete
	public void deleteAccount(Account account) {
		int index = indexOf(account);
		if( index != -1)
			deleteAccount(index);
		else
			System.out.println("존재하지 않습니다. 다시 확인해주세요.");
	}
	
	private int indexOf(Account account) {
		int index = -1;
		
		return index;
	}
	
	public void deleteAccount(int index) {
		list.remove(index);
		System.out.println("삭제되었습니다.");
	}
	

	


	public ArrayList<Account> getList() {
		return list;
	}
}
