package atm;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Bank {
	Scanner scanner;
	Random random;

	private final int EXIT = 0;
	private String name;
	private UserManager usermanager;
	private AccountManager accountmanager;
	private int log;

	// 0 ������ �ʱ�ȭ
	public Bank(String name) {
		this.scanner = new Scanner(System.in);
		this.random = new Random();
		this.name = name;
		this.usermanager = new UserManager();
		this.accountmanager = new AccountManager();
		this.log = -1;
	}

	public String inputString(String message) {
		System.out.printf("%s : ", message);
		String data = this.scanner.next();
		return data;
	}

	public int inputNumber(String message) {
		int data = -1;
		while (true) {
			try {
				System.out.printf("%s : ", message);
				data = this.scanner.nextInt();
			} catch (InputMismatchException e) {
				e.printStackTrace();
				System.out.println("��ȿ�� ������ �Է��� �ƴմϴ�.");
				scanner.nextLine();
			}
			if (data < 1)
				continue;
			return data;
		}
	}

	private boolean isLogged() {
		return log != -1;
	}

	// 1. printMenu()
	private void printMenu() {
		System.out.printf("==== %s ====\n", this.name);
		System.out.println("1. ȸ������\n2. ȸ��Ż��\n3. ���µ��");
		System.out.println("4. ����öȸ\n5. �α���\n6. �α׾ƿ�");
	}

	// 2. ȸ������ [1]
	private void joinMembership() {
		if (!isLogged()) {
			String id = idDuplicationCheck();
			String password = inputString("��й�ȣ : ");
			String name = inputString("�̸� : ");

			User user = new User(id, password, name);

			this.usermanager.createUser(user);
		}
	}

	// 2-1 ���̵� �ߺ��˻�
	private String idDuplicationCheck() {
		while (true) {
			String id = inputString("���̵� : ");

			boolean duplication = false;
			for (User i : this.usermanager.getList()) {
				String compareId = i.getId();
				if (id.equals(compareId))
					duplication = true;
			}

			if (!duplication)
				return id;
		}
	}

	// 3. ȸ�� Ż�� [2]
	private void leaveMembership() {
		if (isLogged()) {
			String password = inputString("��й�ȣ : ");
			this.usermanager.deleteUser(this.log, password);
		}
	}

	// 4. ���� ��� [3]
	private void joinAccount() {
		if (isLogged()) {
			String acc = accountDuplicationCheck();
			String id = this.usermanager.getUser(this.log).getId();
			Account account = new Account(id, acc);

			this.accountmanager.createAccount(account);
			User user = this.usermanager.getUser(this.log);
			user.addAccount(account);
			this.usermanager.setUser(this.log, user);
		}
	}

	// 4-1 ���� �ߺ� ����
	private String accountDuplicationCheck() {
		while (true) {
			String account = "";
			int frontNum = random.nextInt(8999) + 1000;
			int middleNum = random.nextInt(8999) + 1000;
			int endNum = random.nextInt(899) + 100;

			account += frontNum + "-" + middleNum + "-" + endNum + "";

			boolean duplication = false;
			for (Account i : this.accountmanager.getList()) {
				if (i.getAccount().equals(account))
					duplication = true;
			}

			if (!duplication) {
				return account;
			}
		}
	}

	// 5. ����öȸ [4]
	private void deleteAccount() {
		User user = this.usermanager.getList().get(this.log);
		int index = 0;
		for (Account i : user.getAccs()) {
			System.out.printf("%d. %s", ++index, i.toString());
		}
		
		int select = selectListNumber(user);
		System.out.printf("������ ���·� %d�� ���� �����ϼ˽��ϴ�.\n",select + 1);
		
		String password = inputString("��й�ȣ");
		if(user.getPassWord().equals(password)) {
//			user.getAccs().get(select)
//			user.getAccs().remove(select);
			Account delAcc = this.accountmanager.getAccount(select);
			
			this.accountmanager.deleteAccount(delAcc);
//			user.deleteAccount(delAcc);
		}else {
			System.out.println("��й�ȣ�� �ٽ� Ȯ�����ּ���");
		}
		
		
	}
	
	private int selectListNumber(User user) {
		int select = -1;
		while(true) {
			select = inputNumber("������ ����");
			if(select > user.getAccs().size())
				continue;
			return select-1;
		}	
	}

	// 6. �α��� [5]
	private void logIn() {
		if (!isLogged()) {
			String id = inputString("���̵�");
			String password = inputString("��й�ȣ");

			int index = -1;
			for (int i = 0; i < this.usermanager.getList().size(); i++) {
				User user = this.usermanager.getList().get(i);
				if (user.getId().equals(id) && user.getPassWord().equals(password)) {
					index = i;
					break;
				}
			}

			if (index == -1)
				System.out.println("���̵� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
			else {
				this.log = index;
				System.out.println("�α��� �Ǿ����ϴ�.");
			}
		}
	}

	// 7. �α׾ƿ� [6]
	private void logOut() {
		this.log = -1;
		System.out.println("�α׾ƿ� �Ǿ����ϴ�.");
	}

	public void run() {

		while (true) {
			if (isLogged())
				System.out.println(this.usermanager.getList().get(this.log).getId());

			printMenu();
			int select = inputNumber("�޴�") -1;
			if (select == EXIT) {
				System.out.println("���α׷��� �����մϴ�.");
				break;
			}

			if (select == 1)
				joinMembership();
			else if (select == 2)
				leaveMembership();
			else if (select == 3)
				joinAccount();
			else if (select == 4) {
			} else if (select == 5)
				logIn();
			else if (select == 6) {
			}

		}
	}

	// ATM ������Ʈ

	// 1. ȸ������ / 2. Ż��
	// 3. ���� ��û / 4. öȸ ( 1�� 3���±��� )
	// 5. �α���
}
