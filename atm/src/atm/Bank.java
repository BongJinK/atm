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

		User administrator = new User("admin", "admin", "관리자");
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
				System.err.println("유효한 범주의 입력이 아닙니다.");
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
		System.out.printf("[%s 님]\n", name);
		System.out.println("1. 로그아웃\n2. 회원정보 조회\n3. 파일");
		System.out.println("0. 종료");
	}

	private void printMainMenu() {
		System.out.printf("==== %s ====\n", this.name);
		System.out.println("1. 회원가입\n2. 로그인");
		System.out.println("0. 종료");
	}

	private void isLoggedPrintMainMenu() {
		String name = this.usermanager.getUser(this.log).getName();
		System.out.printf("==== %s ====\n", this.name);
		System.out.printf("[%s 님]\n", name);

		System.out.println("1. 로그아웃\n2. 개인정보");
		System.out.println("3. 계좌생성\n4. 계좌철회");
		System.out.println("5. 뱅킹\n0. 종료");
	}

	private void printAccountInfo() {
		System.out.println("1. 회원정보\n2. 회원탈퇴\n0. 뒤로가기");
	}

	private void printBankingMenu() {
		System.out.println("1. 입금\n2. 출금\n3. 조회\n4. 이체");
		System.out.println("0. 뒤로가기");
	}

	private void fileMenu() {
		System.out.println("1. 저장\n2. 로드\n0. 뒤로가기");
	}

	private void queryAllInfo() {
		while (true) {
			System.out.println("1. 회원정보");
			System.out.println("2. 회원계좌정보");
			System.out.println("0. 뒤로가기");

			int select = inputNumber("메뉴");
			if (select == EXIT) {
				System.out.println("초기화면으로 돌아갑니다.");
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
		String id = inputString("아이디");
		String password = inputString("비밀번호");
		String name = inputString("이름");

		User user = new User(id, password, name);

		if (this.usermanager.addUser(user) != null)
			System.out.println("회원가입 성공");
		else
			System.err.println("중복된 아이디가 존재합니다.");
	}

	private void infoRun() {
		while (true) {
			printAccountInfo();

			int select = inputNumber("메뉴");
			if (select == EXIT) {
				System.out.println("초기화면으로 돌아갑니다.");
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
		System.out.println("[ 회원정보 ]");
		System.out.printf("이름 : %s\n", user.getName());
		System.out.printf("아이디 : %s\n", user.getId());
		System.out.printf("비밀번호 : %s\n", user.getPassWord());

		if (user.getAccountSize() != 0) {
			System.out.println("[계좌정보]");
			for (Account i : user.getAccountList()) {
				System.out.printf("계좌 : %s\n", i.getAccNum());
				System.out.printf("잔액 : %d\n", i.getBalance());
			}
		}
	}

	private void leaveMembership() {
		String password = inputString("비밀번호");
		this.usermanager.deleteUserByPassword(this.log, password);
		this.log = -1;
	}

	private void logIn() {
		String id = inputString("아이디");
		String password = inputString("비밀번호");

		int index = -1;
		for (int i = 0; i < this.usermanager.size(); i++) {
			User user = this.usermanager.getUser(i);
			if (user.getId().equals(id) && user.getPassWord().equals(password)) {
				index = i;
				break;
			}
		}

		if (index == -1)
			System.err.println("아이디나 비밀번호가 일치하지 않습니다.");
		else {
			this.log = index;
			System.out.println("로그인 되었습니다.");
		}
	}

	private void logOut() {
		this.log = -1;
		System.out.println("로그아웃 되었습니다.");
	}

	private void bankingRun() {
		while (true) {
			printBankingMenu();

			int select = inputNumber("메뉴");
			if (select == EXIT) {
				System.out.println("초기화면으로 돌아갑니다.");
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
		int inputMoney = inputNumber("금액");

		user.getAccount(select).setBalance(inputMoney + oldBalance);
	}

	private void withdraw() {
		User user = this.usermanager.getUser(this.log);
		int select = selectAccount(user);

		int oldBalance = user.getAccount(select).getBalance();
		int outputMoney = inputNumber("금액");
		if (oldBalance >= outputMoney) {
			user.getAccount(select).setBalance(oldBalance - outputMoney);
			System.out.printf("%d원 출금되었습니다.\n", outputMoney);
			System.out.printf("잔액 : %d원\n", user.getAccount(select).getBalance());
		} else {
			System.err.println("잔액이 부족합니다.");
			System.out.printf("잔액 : %d원\n", oldBalance);
		}
	}

	private void query() {
		User user = this.usermanager.getUser(this.log);
		System.out.printf("==== %s님 계좌 내역 ====\n", user.getName());
		for (int i = 0; i < user.getAccountList().size(); i++) {
			Account account = user.getAccountList().get(i);
			System.out.printf("%d. %s\n", i + 1, account.toString());
		}
	}

	private void transfer() {
		User user = this.usermanager.getUser(this.log);
		int select = selectAccount(user);

		String transferAccount = inputString("이체할 계좌번호");

		String accNum = "";
		accNum += transferAccount.substring(0, 4) + "-";
		accNum += transferAccount.substring(4, 8) + "-";
		accNum += transferAccount.substring(8, 11);
		Account account = this.accountmanager.getAccountByAccNum(accNum);

		if (transferAccount.length() != 11 || account == null) {
			System.err.println("유효한 계좌번호가 아닙니다.");
			return;
		}

		String selectAccount = user.getAccount(select).getAccNum();

		if (selectAccount.equals(account.getAccNum())) {
			System.err.println("동일 계좌로는 이체가 불가능합니다.");
			return;
		}

		int oldBalance = user.getAccount(select).getBalance();
		int transferMoney = inputNumber("금액");

		if (oldBalance < transferMoney) {
			System.err.println("잔액이 부족합니다.");
			System.out.printf("잔액 : %d원", oldBalance);
			return;
		}
		int newBalance = oldBalance - transferMoney;

		int receivingOldBalance = account.getBalance();
		int receiveBalance = receivingOldBalance + transferMoney;

		user.getAccount(select).setBalance(newBalance);
		account.setBalance(receiveBalance);
		System.out.println("이체가 완료되었습니다.");

		int myBalance = user.getAccount(select).getBalance();
		System.out.printf("이체 후 잔액은 %d원 입니다.\n", myBalance);
	}

	private void joinAccount() {
		if (isLogged()) {
			User user = this.usermanager.getUser(this.log);
			String id = user.getId();

			if (user.getAccountSize() >= Account.LIMIT) {
				System.err.println("계좌는 3개까지 보유 가능합니다.");
				return;
			}

			Account account = this.accountmanager.createAccount(new Account(id));
			this.usermanager.setUser(user, account, Account.ADD);

			System.out.println("계좌가 생성되었습니다.");
			System.out.printf("계좌번호는 %s 입니다.\n", account.getAccNum());
		}
	}

	private void deleteAccount() {
		if (isLogged()) {
			User user = this.usermanager.getUser(this.log);
			int select = selectAccount(user);

			String password = inputString("비밀번호");
			if (user.getPassWord().equals(password)) {
				Account deleteAcc = user.getAccount(select);

				this.accountmanager.deleteAccount(deleteAcc);
				user.deleteAccount(deleteAcc);

				this.usermanager.setUser(user, deleteAcc, Account.DELETE);
			} else {
				System.err.println("비밀번호를 다시 확인해주세요");
			}
		}
	}

	private int selectAccount(User user) {
		for (int i = 0; i < user.getAccountList().size(); i++) {
			Account account = user.getAccountList().get(i);
			System.out.printf("%d. %s", i + 1, account.toString());
		}

		int select = selectListNumber(user) - 1;
		System.out.printf("%d번 계좌 선택하셧습니다.\n", select + 1);

		return select;
	}

	private int selectListNumber(User user) {
		int max = user.getAccountList().size();
		int select;
		while (true) {
			select = inputNumber("계좌 리스트 번호");
			if (select >= 1 && select <= max)
				return select;
			System.err.printf("1부터 %d 사이의 값을 입력해주세요.\n", max);
		}
	}

	private void fileRun() {
		FileManager fileManager = new FileManager();
		while (true) {
			fileMenu();

			int select = inputNumber("메뉴");
			if (select == EXIT) {
				System.out.println("뒤로 돌아갑니다.");
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
			System.err.println("로드할 계좌 데이터가 존재하지 않습니다.");
			return;
		}
		this.accountmanager.setAccount(fileManager.loadAccountData());
		ArrayList<Account> loadList = this.accountmanager.getAccountList();

		if (fileManager.loadUserData(loadList) == null) {
			System.err.println("로드할 유저 데이터가 존재하지 않습니다.");
			return;
		}
		this.usermanager.setUser(fileManager.loadUserData(loadList));
	}

	public void run() {
		while (true) {
			printMenu();

			int select = inputNumber("메뉴");
			if (select == EXIT) {
				System.out.println("프로그램을 종료합니다.");
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
