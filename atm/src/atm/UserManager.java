package atm;

import java.util.ArrayList;

public class UserManager {
	private static ArrayList<User> list = new ArrayList<User>();

	// Create
	public User addUser(User user) {
		// ���� �� add
		User check = getUserById(user.getId());
		if (check == null) {
			list.add(user);
			return user;
		}
		return null;
	}

	// Read
	public User getUser(int index) {
		User user = list.get(index);

		User request = new User(user.getId(), user.getPassWord(), user.getName());
		return request;
	}

	public User getUserById(String id) {
		User user = null;

		int index = indexOfById(id);
		if (index != -1)
			user = getUser(index);
		return user;
	}
	
	private int indexOfById(String id) {
		int index = -1;
		for (User user : list) {
			if (user.getId().equals(id))
				index = list.indexOf(user);
		}
		return index;
	}

	// Update
	public void setUser(int index, User user) {
		list.set(index, user);
	}
	
	public void setUser(User user, Account account) {
		int index = indexOfById(user.getId());
		
		list.get(index).addAccount(account);
	}

	// Delete
	public void deleteUserByPassword(int log, String password) {
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
	
	public int size() {
		return list.size();
	}

}
