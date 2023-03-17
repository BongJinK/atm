package atm;
 
import java.util.ArrayList;

public class UserManager {
	private static ArrayList<User> list = new ArrayList<User>();

	// Create
	public User addUser(User user) {
		// 검증 후 add
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

		User request = new User(user.getId(), user.getPassWord(), user.getName(), user.getAccountList());
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

	public void setUser(User user, Account account, int order) {
		int index = indexOfById(user.getId());

		if (order == Account.ADD)
			list.get(index).addAccount(account);
		else if (order == Account.DELETE)
			list.get(index).deleteAccount(account);
	}
	
	public void setUser(ArrayList<User> userList) {
		list.clear();
		for(User i : userList) {
			list.add(i);
		}
	}

	// Delete
	public void deleteUserByPassword(int log, String password) {
		String pin = list.get(log).getPassWord();
		if (!pin.equals(password))
			System.err.println("비밀번호를 다시 확인해주세요.");
		else {
			deleteUser(log);
			System.out.println("삭제되었습니다.");
		}
	}

	public void deleteUser(int index) {
		list.remove(index);
	}

	public int size() {
		return list.size();
	}
	
	public ArrayList<User> getUserList(){
		return (ArrayList<User>)list.clone();
	}

}
