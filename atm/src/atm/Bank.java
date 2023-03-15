package atm;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Bank {
	private Scanner scanner;

	private final int EXIT = 0;
	private String name;
	private UserManager usermanager;
	private AccountManager accountmanager;
	private int log;
	private String userFile;
	private String accountFile;
	private File file;

	public Bank(String name) {
		this.scanner = new Scanner(System.in);
		this.name = name;
		this.usermanager = new UserManager();
		this.accountmanager = new AccountManager();
		this.log = -1;
	}

	private String inputString(String message) {
		System.out.printf("%s : ", message);
		String data = this.scanner.next();
		return data;
	}

	private int inputNumber(String message) {
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
			if (data < 0)
				continue;
			return data;
		}
	}

	private boolean isLogged() {
		return log != -1;
	}

	private void printMainMenu() {
		System.out.printf("==== %s ====\n", this.name);
		System.out.println("1. ȸ������\n2. ȸ��Ż��\n3. �α���");
		System.out.println("4. �α׾ƿ�\n5. ��ŷ\n6. ����");
		System.out.println("0. ����");
	}
	
	private void printBankingMenu() {
		System.out.println("1. �Ա�\n2. ���\n3. ��ȸ\n4. ��ü");
		System.out.println("5. ���� ����\n6. ���� öȸ\n0. �ڷΰ���");
	}
	
	private void fileMenu() {
		System.out.println("1. ����\n2. �ε�\n0. �ڷΰ���");
	}

	// 1. ȸ������
	private void joinMembership() {
		if (!isLogged()) {
			String id = inputString("���̵� : ");
			String password = inputString("��й�ȣ : ");
			String name = inputString("�̸� : ");

			User user = new User(id, password, name);

			if (this.usermanager.addUser(user) != null)
				System.out.println("ȸ������ ����");
			else
				System.out.println("�ߺ��� ���̵� �����մϴ�.");
		}
	}

	// 2. ȸ�� Ż��
	private void leaveMembership() {
		if (isLogged()) {
			String password = inputString("��й�ȣ : ");
			this.usermanager.deleteUserByPassword(this.log, password);
		}
	}
	
	// 3. �α���
	private void logIn() {
		if (!isLogged()) {
			String id = inputString("���̵�");
			String password = inputString("��й�ȣ");

			int index = -1;
			for (int i = 0; i < this.usermanager.size(); i++) {
				User user = this.usermanager.getUser(i);
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

	// 4. �α׾ƿ�
	private void logOut() {
		if (isLogged()) {
			this.log = -1;
			System.out.println("�α׾ƿ� �Ǿ����ϴ�.");
		}
	}
	
	

	
	// 5. ��ŷ
	private void bankingRun() {
		while(true) {
			printBankingMenu();
			
			int select = inputNumber("�޴�");
			if( select == EXIT) {
				System.out.println("�ʱ�ȭ������ ���ư��ϴ�.");
				break;
			}
		}
	}

	// 3. ���� ��� [3]
	private void joinAccount() {
		if (isLogged()) {
			// ������ ��ȯ ���� (�α��� �Ǿ� �ִ� ȸ��)
			User user = this.usermanager.getUser(this.log);
			String id = user.getId();

			if (user.getAccountSize() < Account.LIMIT) {
				Account account = this.accountmanager.createAccount(new Account(id));
				this.usermanager.setUser(user, account);
			} else
				System.out.println("���´� 3������ ���� �����մϴ�.");
		}
	}

	// 4. ����öȸ [4]
	private void deleteAccount() {
		if (isLogged()) {
			User user = this.usermanager.getUser(this.log);
			for (int i = 0; i < user.getAccs().size(); i++) {
				Account account = user.getAccs().get(i);
				System.out.printf("%d. %s\n", i + 1, account.toString());
			}

			int select = selectListNumber(user) - 1;
			System.out.printf("������ ���·� %d�� ���� �����ϼ˽��ϴ�.\n", select + 1);

			String password = inputString("��й�ȣ");
			if (user.getPassWord().equals(password)) {
				Account deleteAcc = user.getAccount(select);
//				Account delAcc = this.accountmanager.getAccount(select);

				this.accountmanager.deleteAccount(deleteAcc);
				user.deleteAccount(select);
			} else {
				System.out.println("��й�ȣ�� �ٽ� Ȯ�����ּ���");
			}
		}
	}

	private int selectListNumber(User user) {
		int max = user.getAccs().size();
		int select;
		while (true) {
			select = inputNumber("������ ����");
			if (select >= 1 && select <= max)
				return select;
			System.out.printf("1���� %d ������ ���� �Է����ּ���.\n", max);
		}
	}

	public void run() {
		while (true) {
			if (isLogged())
				System.out.println(this.usermanager.getUser(this.log).getId());

			printMainMenu();
			int select = inputNumber("�޴�");
			if (select == EXIT) {
				System.out.println("���α׷��� �����մϴ�.");
				break;
			}

			if (select == 1)
				joinMembership();
			else if (select == 2)
				leaveMembership();
			else if (select == 3)
				logIn();
			else if (select == 4)
				logOut();
			else if (select == 5)
				joinAccount();
			else if (select == 6)
				deleteAccount();

		}
	}
}
