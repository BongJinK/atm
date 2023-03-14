package atm;

import java.util.ArrayList;

public class UserManager {
	private static ArrayList<User> list = new ArrayList<User>();

	// Create
	public void createUser(User user) {
		list.add(user);
	}

	// Read
	public User getUser(int index) {
		User user = list.get(index);

		String id = user.getId();
		String password = user.getPassWord();
		String name = user.getName();
		ArrayList<Account> list = user.getAccs();

		User request = new User(id, password, name, list);
		return request;
	}

	// Update
	public void setUser(int index, User user) {
		list.set(index, user);
	}

	// Delete
	public void deleteUser(int log, String password) {
		String pin = list.get(log).getPassWord();
		if (!pin.equals(password))
			System.err.println("��й�ȣ�� �ٽ� Ȯ�����ּ���.");
		else {
			deleteUser(log);
			System.out.println("�����Ǿ����ϴ�.");
		}
	}

	public void deleteUser(int index) {
		list.remove(index);
	}

	public ArrayList<User> getList() {
		return list;
	}
}
