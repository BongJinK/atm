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
				System.err.println("�ߺ��� ���̵� �����մϴ�.");
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
				System.err.println("���̵� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
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
		while (true && isLogged()) {
			printBankingMenu();

			int select = inputNumber("�޴�");
			if (select == EXIT) {
				System.out.println("�ʱ�ȭ������ ���ư��ϴ�.");
				break;
			}

			if (select == 1)
				deposit(); // �Ա�
			else if (select == 2)
				withdraw(); // ���
			else if (select == 3)
				query(); // ��ȸ
			else if (select == 4)
				transfer(); // ��ü
			else if (select == 5)
				joinAccount();
			else if (select == 6)
				deleteAccount();
		}
	}

	// 5-1 �Ա�
	private void deposit() {
		User user = this.usermanager.getUser(this.log);
		int select = selectAccount(user);

		int oldBalance = user.getAccount(select).getBalance();
		int inputMoney = inputNumber("�ݾ�");

		user.getAccount(select).setBalance(inputMoney + oldBalance);
	}

	// 5-2 ���
	private void withdraw() {
		User user = this.usermanager.getUser(this.log);
		int select = selectAccount(user);

		int oldBalance = user.getAccount(select).getBalance();
		int outputMoney = inputNumber("�ݾ�");
		if (oldBalance >= outputMoney)
			user.getAccount(select).setBalance(oldBalance - outputMoney);
		else {
			System.err.println("�ܾ��� �����մϴ�.");
			System.out.printf("�ܾ� : %d��", oldBalance);
		}
	}

	// 5-3 ��ȸ
	private void query() {
		User user = this.usermanager.getUser(this.log);
		for (int i = 0; i < user.getAccountList().size(); i++) {
			Account account = user.getAccountList().get(i);
			System.out.printf("%d. %s\n", i + 1, account.toString());
		}
	}

	// 5-4 ��ü
	private void transfer() {
		User user = this.usermanager.getUser(this.log);
		int select = selectAccount(user);

		String transferAccount = inputString("��ü�� ���¹�ȣ");

		if (transferAccount.length() != 11)
			System.err.println("��ȿ�� ���¹�ȣ�� �ƴմϴ�.");
		else {
			String accNum = "";
			accNum += transferAccount.substring(0, 4) + "-";
			accNum += transferAccount.substring(4, 8) + "-";
			accNum += transferAccount.substring(8, 11);

			Account account = this.accountmanager.getAccountByAccNum(accNum);
			if (account == null)
				System.err.println("��ġ�ϴ� ���°� �������� �ʽ��ϴ�.");
			else {
				int oldBalance = user.getAccount(select).getBalance();
				int receivingOldBalance = account.getBalance();

				int transferMoney = inputNumber("�ݾ�");
				if (oldBalance >= transferMoney) {
					user.getAccount(select).setBalance(oldBalance - transferMoney);
					account.setBalance(receivingOldBalance + transferMoney);
				} else {
					System.err.println("�ܾ��� �����մϴ�.");
					System.out.printf("�ܾ� : %d��", oldBalance);
				}
			}
		}
	}

	// 5-5. ���� ����
	private void joinAccount() {
		if (isLogged()) {
			// ������ ��ȯ ���� (�α��� �Ǿ� �ִ� ȸ��)
			User user = this.usermanager.getUser(this.log);
			String id = user.getId();

			if (user.getAccountSize() < Account.LIMIT) {
				Account account = this.accountmanager.createAccount(new Account(id));
				this.usermanager.setUser(user, account);
			} else
				System.err.println("���´� 3������ ���� �����մϴ�.");
		}
	}

	// 5-6. ����öȸ
	private void deleteAccount() {
		if (isLogged()) {
			User user = this.usermanager.getUser(this.log);
			int select = selectAccount(user);

			String password = inputString("��й�ȣ");
			if (user.getPassWord().equals(password)) {
				Account deleteAcc = user.getAccount(select);
//				Account delAcc = this.accountmanager.getAccount(select);

				this.accountmanager.deleteAccount(deleteAcc);
				user.deleteAccount(select);
			} else {
				System.err.println("��й�ȣ�� �ٽ� Ȯ�����ּ���");
			}
		}
	}

	// 5-0. ���� ����
	private int selectAccount(User user) {
		for (int i = 0; i < user.getAccountList().size(); i++) {
			Account account = user.getAccountList().get(i);
			System.out.printf("%d. %s\n", i + 1, account.toString());
		}

		int select = selectListNumber(user) - 1;
		System.out.printf("%d�� ���� �����ϼ˽��ϴ�.\n", select + 1);

		return select;
	}

	private int selectListNumber(User user) {
		int max = user.getAccountList().size();
		int select;
		while (true) {
			select = inputNumber("������ ����");
			if (select >= 1 && select <= max)
				return select;
			System.err.printf("1���� %d ������ ���� �Է����ּ���.\n", max);
		}
	}
	
	// 6. ����
	private void fileRun() {
		while(true) {
			fileMenu();
			
			int select = inputNumber("�޴�");
			if( select == EXIT ) {
				System.out.println("�ڷ� ���ư��ϴ�.");
				break;
			}
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
				bankingRun();
			else if (select == 6)
				fileRun();
		}
	}
}
