package atm;

import java.util.ArrayList;
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

		User administrator = new User("admin", "admin", "������");
		this.usermanager.addUser(administrator);
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
				System.err.println("��ȿ�� ������ �Է��� �ƴմϴ�.");
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

	private void printMenu() {
		if (isLogged()) {
			if (this.log == 0)
				administratorPrintMenu();
			else
				isLoggedPrintMainMenu();
		} else {
			printMainMenu();
		}
	}

	private void administratorPrintMenu() {
		String name = this.usermanager.getUser(this.log).getName();
		System.out.printf("==== %s ====\n", this.name);
		System.out.printf("[%s ��]\n", name);
		System.out.println("1. �α׾ƿ�\n2. ȸ������ ��ȸ\n3. ����");
		System.out.println("0. ����");
	}

	private void printMainMenu() {
		System.out.printf("==== %s ====\n", this.name);
		System.out.println("1. ȸ������\n2. �α���");
		System.out.println("0. ����");
	}

	private void isLoggedPrintMainMenu() {
		String name = this.usermanager.getUser(this.log).getName();
		System.out.printf("==== %s ====\n", this.name);
		System.out.printf("[%s ��]\n", name);

		System.out.println("1. �α׾ƿ�\n2. ��������");
		System.out.println("3. ���»���\n4. ����öȸ");
		System.out.println("5. ��ŷ\n0. ����");
	}

	private void printAccountInfo() {
		System.out.println("1. ȸ������\n2. ȸ��Ż��\n0. �ڷΰ���");
	}

	private void printBankingMenu() {
		System.out.println("1. �Ա�\n2. ���\n3. ��ȸ\n4. ��ü");
		System.out.println("0. �ڷΰ���");
	}

	private void fileMenu() {
		System.out.println("1. ����\n2. �ε�\n0. �ڷΰ���");
	}

	private void queryAllInfo() {
		while (true) {
			System.out.println("1. ȸ������");
			System.out.println("2. ȸ����������");
			System.out.println("0. �ڷΰ���");

			int select = inputNumber("�޴�");
			if (select == EXIT) {
				System.out.println("�ʱ�ȭ������ ���ư��ϴ�.");
				break;
			}

			if (select == 1)
				queryUserInfo();
			else if (select == 2)
				queryAccountInfo();
		}
	}

	private void queryUserInfo() {
		for (int i = 0; i < this.usermanager.size(); i++) {
			System.out.printf("%d. %s", i + 1, this.usermanager.getUser(i));
		}
		System.out.println("===============");
	}

	private void queryAccountInfo() {
		for (int i = 0; i < this.accountmanager.size(); i++) {
			System.out.printf("%d. %s", i + 1, this.accountmanager.getAccount(i));
		}
		System.out.println("===============");
	}

	private void joinMembership() {
		String id = inputString("���̵�");
		String password = inputString("��й�ȣ");
		String name = inputString("�̸�");

		User user = new User(id, password, name);

		if (this.usermanager.addUser(user) != null)
			System.out.println("ȸ������ ����");
		else
			System.err.println("�ߺ��� ���̵� �����մϴ�.");
	}

	private void infoRun() {
		while (true) {
			printAccountInfo();

			int select = inputNumber("�޴�");
			if (select == EXIT) {
				System.out.println("�ʱ�ȭ������ ���ư��ϴ�.");
				break;
			}

			if (select == 1)
				showAccountInfo();
			else if (select == 2)
				leaveMembership();
		}
	}

	private void showAccountInfo() {
		User user = this.usermanager.getUser(this.log);
		System.out.println("[ ȸ������ ]");
		System.out.printf("�̸� : %s\n", user.getName());
		System.out.printf("���̵� : %s\n", user.getId());
		System.out.printf("��й�ȣ : %s\n", user.getPassWord());

		if (user.getAccountSize() != 0) {
			System.out.println("[��������]");
			for (Account i : user.getAccountList()) {
				System.out.printf("���� : %s\n", i.getAccNum());
				System.out.printf("�ܾ� : %d\n", i.getBalance());
			}
		}
	}

	private void leaveMembership() {
		String password = inputString("��й�ȣ");
		this.usermanager.deleteUserByPassword(this.log, password);
		this.log = -1;
	}

	private void logIn() {
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

	private void logOut() {
		this.log = -1;
		System.out.println("�α׾ƿ� �Ǿ����ϴ�.");
	}

	private void bankingRun() {
		while (true) {
			printBankingMenu();

			int select = inputNumber("�޴�");
			if (select == EXIT) {
				System.out.println("�ʱ�ȭ������ ���ư��ϴ�.");
				break;
			}

			if (select == 1)
				deposit();
			else if (select == 2)
				withdraw();
			else if (select == 3)
				query();
			else if (select == 4)
				transfer();
		}
	}

	private void deposit() {
		User user = this.usermanager.getUser(this.log);
		int select = selectAccount(user);

		int oldBalance = user.getAccount(select).getBalance();
		int inputMoney = inputNumber("�ݾ�");

		user.getAccount(select).setBalance(inputMoney + oldBalance);
	}

	private void withdraw() {
		User user = this.usermanager.getUser(this.log);
		int select = selectAccount(user);

		int oldBalance = user.getAccount(select).getBalance();
		int outputMoney = inputNumber("�ݾ�");
		if (oldBalance >= outputMoney) {
			user.getAccount(select).setBalance(oldBalance - outputMoney);
			System.out.printf("%d�� ��ݵǾ����ϴ�.\n", outputMoney);
			System.out.printf("�ܾ� : %d��\n", user.getAccount(select).getBalance());
		} else {
			System.err.println("�ܾ��� �����մϴ�.");
			System.out.printf("�ܾ� : %d��\n", oldBalance);
		}
	}

	private void query() {
		User user = this.usermanager.getUser(this.log);
		System.out.printf("==== %s�� ���� ���� ====\n", user.getName());
		for (int i = 0; i < user.getAccountList().size(); i++) {
			Account account = user.getAccountList().get(i);
			System.out.printf("%d. %s\n", i + 1, account.toString());
		}
	}

	private void transfer() {
		User user = this.usermanager.getUser(this.log);
		int select = selectAccount(user);

		String transferAccount = inputString("��ü�� ���¹�ȣ");

		String accNum = "";
		accNum += transferAccount.substring(0, 4) + "-";
		accNum += transferAccount.substring(4, 8) + "-";
		accNum += transferAccount.substring(8, 11);
		Account account = this.accountmanager.getAccountByAccNum(accNum);

		if (transferAccount.length() != 11 || account == null) {
			System.err.println("��ȿ�� ���¹�ȣ�� �ƴմϴ�.");
			return;
		}

		String selectAccount = user.getAccount(select).getAccNum();

		if (selectAccount.equals(account.getAccNum())) {
			System.err.println("���� ���·δ� ��ü�� �Ұ����մϴ�.");
			return;
		}

		int oldBalance = user.getAccount(select).getBalance();
		int transferMoney = inputNumber("�ݾ�");

		if (oldBalance < transferMoney) {
			System.err.println("�ܾ��� �����մϴ�.");
			System.out.printf("�ܾ� : %d��", oldBalance);
			return;
		}
		int newBalance = oldBalance - transferMoney;

		int receivingOldBalance = account.getBalance();
		int receiveBalance = receivingOldBalance + transferMoney;

		user.getAccount(select).setBalance(newBalance);
		account.setBalance(receiveBalance);
		System.out.println("��ü�� �Ϸ�Ǿ����ϴ�.");

		int myBalance = user.getAccount(select).getBalance();
		System.out.printf("��ü �� �ܾ��� %d�� �Դϴ�.\n", myBalance);
	}

	private void joinAccount() {
		if (isLogged()) {
			User user = this.usermanager.getUser(this.log);
			String id = user.getId();

			if (user.getAccountSize() >= Account.LIMIT) {
				System.err.println("���´� 3������ ���� �����մϴ�.");
				return;
			}

			Account account = this.accountmanager.createAccount(new Account(id));
			this.usermanager.setUser(user, account, Account.ADD);

			System.out.println("���°� �����Ǿ����ϴ�.");
			System.out.printf("���¹�ȣ�� %s �Դϴ�.\n", account.getAccNum());
		}
	}

	private void deleteAccount() {
		if (isLogged()) {
			User user = this.usermanager.getUser(this.log);
			int select = selectAccount(user);

			String password = inputString("��й�ȣ");
			if (user.getPassWord().equals(password)) {
				Account deleteAcc = user.getAccount(select);

				this.accountmanager.deleteAccount(deleteAcc);
				user.deleteAccount(deleteAcc);

				this.usermanager.setUser(user, deleteAcc, Account.DELETE);
			} else {
				System.err.println("��й�ȣ�� �ٽ� Ȯ�����ּ���");
			}
		}
	}

	private int selectAccount(User user) {
		for (int i = 0; i < user.getAccountList().size(); i++) {
			Account account = user.getAccountList().get(i);
			System.out.printf("%d. %s", i + 1, account.toString());
		}

		int select = selectListNumber(user) - 1;
		System.out.printf("%d�� ���� �����ϼ˽��ϴ�.\n", select + 1);

		return select;
	}

	private int selectListNumber(User user) {
		int max = user.getAccountList().size();
		int select;
		while (true) {
			select = inputNumber("���� ����Ʈ ��ȣ");
			if (select >= 1 && select <= max)
				return select;
			System.err.printf("1���� %d ������ ���� �Է����ּ���.\n", max);
		}
	}

	private void fileRun() {
		FileManager fileManager = new FileManager();
		while (true) {
			fileMenu();

			int select = inputNumber("�޴�");
			if (select == EXIT) {
				System.out.println("�ڷ� ���ư��ϴ�.");
				break;
			}
			if (select == 1)
				save(fileManager);
			else if (select == 2)
				load(fileManager);
		}
	}

	private void save(FileManager fileManager) {
		ArrayList<Account> account = this.accountmanager.getAccountList();
		ArrayList<User> user = this.usermanager.getUserList();

		if (account.size() != 0 && user.size() != 0) {
			fileManager.saveAccountData(account);
			fileManager.saveUserData(user);
		}
	}

	private void load(FileManager fileManager) {
		if (fileManager.loadAccountData() == null) {
			System.err.println("�ε��� ���� �����Ͱ� �������� �ʽ��ϴ�.");
			return;
		}
		this.accountmanager.setAccount(fileManager.loadAccountData());
		ArrayList<Account> loadList = this.accountmanager.getAccountList();

		if (fileManager.loadUserData(loadList) == null) {
			System.err.println("�ε��� ���� �����Ͱ� �������� �ʽ��ϴ�.");
			return;
		}
		this.usermanager.setUser(fileManager.loadUserData(loadList));
	}

	public void run() {
		while (true) {
			printMenu();

			int select = inputNumber("�޴�");
			if (select == EXIT) {
				System.out.println("���α׷��� �����մϴ�.");
				break;
			}

			selectMenu(select);
		}
	}

	private void selectMenu(int select) {
		if (isLogged()) {
			if (this.log == 0) {
				if (select == 1)
					logOut();
				else if (select == 2) {
					queryAllInfo();
				} else if (select == 3) {
					fileRun();
				}
			} else {
				if (select == 1)
					logOut();
				else if (select == 2)
					infoRun();
				else if (select == 3)
					joinAccount();
				else if (select == 4)
					deleteAccount();
				else if (select == 5)
					bankingRun();
			}
		} else {
			if (select == 1)
				joinMembership();
			else if (select == 2)
				logIn();
		}
	}
}
