package atm;

import java.util.ArrayList;
import java.util.Random;

public class AccountManager {

	private static ArrayList<Account> list = new ArrayList<Account>();

	// Create
	public Account createAccount(Account account) {
		// ���¹�ȣ ���� �ο�
		String accountNum = accNumGenerator();
		account.setAccNum(accountNum);
		list.add(account);
		return account;
	}

	private String accNumGenerator() {
		String account = "";

		Random random = new Random();
		while (true) {
			int frontNum = random.nextInt(8999) + 1000;
			int middleNum = random.nextInt(8999) + 1000;
			int endNum = random.nextInt(899) + 100;
			account += frontNum + "-" + middleNum + "-" + endNum + "";

			Account check = getAccountByAccNum(account);
			if (check == null)
				return account;
		}
	}

	// Read
	public Account getAccount(int index) {
		Account account = list.get(index);

		Account reqObj = new Account(account.getId(), account.getAccNum(), account.getBalance());
		return reqObj;
	}

	public Account getAccountByAccNum(String accountNum) {
		Account account = null;

		for (Account object : list) {
			if (object.getAccNum().equals(accountNum))
				account = object;
		}
		return account;
	}

	// Update
	public void setAccount(int index, Account account) {
		list.set(index, account);
	}
	
	public void setAccount(ArrayList<Account> accountList) {
		list.clear();
		for(Account i : accountList) {
			list.add(i);
		}
	}

	// Delete
	public void deleteAccount(Account account) {
		int index = indexOf(account);
		if (index != -1)
			deleteAccount(index);
	}

	private int indexOf(Account account) {
		int index = -1;
		if (account != null) {
			for (int i = 0; i < list.size(); i++) {
				String saveAccount = list.get(i).getAccNum();
				if (account.getAccNum().equals(saveAccount))
					return i;
			}
		}
		return index;
	}

	public void deleteAccount(int index) {
		list.remove(index);
		System.out.println("�����Ǿ����ϴ�.");
	}

	public int size() {
		return list.size();
	}
	
	public ArrayList<Account> getAccountList(){
		return (ArrayList<Account>) list.clone();
	}
}
