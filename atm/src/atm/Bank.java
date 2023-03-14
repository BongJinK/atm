package atm;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Bank {
	private Scanner scanner;
	private Random random;

	private final int EXIT = 0;
	private String name;
	private UserManager usermanager;
	private AccountManager accountmanager;
	private int log;

	public Bank(String name) {
		this.scanner = new Scanner(System.in);
		this.random = new Random();
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
				System.out.println("유효한 범주의 입력이 아닙니다.");
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
		System.out.printf("==== %s ====\n", this.name);
		System.out.println("1. 회원가입\n2. 회원탈퇴\n3. 계좌등록");
		System.out.println("4. 계좌철회\n5. 로그인\n6. 로그아웃");
		System.out.println("0. 종료");
	}

	// 1. 회원가입 [1]
	private void joinMembership() {
		if (!isLogged()) {
			String id = idDuplicationCheck();
			String password = inputString("비밀번호 : ");
			String name = inputString("이름 : ");

			User user = new User(id, password, name);

			this.usermanager.createUser(user);
		}
	}

	// 1-1 아이디 중복검사
	private String idDuplicationCheck() {
		while (true) {
			String id = inputString("아이디 : ");

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

	// 2. 회원 탈퇴 [2]
	private void leaveMembership() {
		if (isLogged()) {
			String password = inputString("비밀번호 : ");
			this.usermanager.deleteUser(this.log, password);
		}
	}

	// 3. 계좌 등록 [3]
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

	// 3-1 계좌 중복 검증
	private String accountDuplicationCheck() {
		while (true) {
			String account = "";
			int frontNum = random.nextInt(8999) + 1000;
			int middleNum = random.nextInt(8999) + 1000;
			int endNum = random.nextInt(899) + 100;

			account += frontNum + "-" + middleNum + "-" + endNum + "";
			System.out.println(account);

			boolean duplication = false;
			for (Account i : this.accountmanager.getList()) {
				if (i.getAccount().equals(account))
					duplication = true;
			}

			if (!duplication)
				return account;
		}
	}

	// 4. 계좌철회 [4]
	private void deleteAccount() {
		if(isLogged()) {
			User user = this.usermanager.getList().get(this.log);
			for (int i = 0; i < user.getAccs().size(); i++) {
				Account account = user.getAccs().get(i);
				System.out.printf("%d. %s\n", i + 1, account.toString());
			}

			int select = selectListNumber(user) - 1;
			System.out.printf("삭제할 계좌로 %d번 계좌 선택하셧습니다.\n", select + 1);

			String password = inputString("비밀번호");
			if (user.getPassWord().equals(password)) {
				Account delAcc = this.accountmanager.getAccount(select);

				this.accountmanager.deleteAccount(delAcc);
				user.deleteAccount(select);
			} else {
				System.out.println("비밀번호를 다시 확인해주세요");
			}
		}
	}

	private int selectListNumber(User user) {
		int max = user.getAccs().size();
		int select;
		while (true) {
			select = inputNumber("삭제할 계좌");
			if (select >= 1 && select <= max)
				return select;
			System.out.printf("1부터 %d 사이의 값을 입력해주세요.\n", max);
		}
	}

	// 5. 로그인 [5]
	private void logIn() {
		if (!isLogged()) {
			String id = inputString("아이디");
			String password = inputString("비밀번호");

			int index = -1;
			for (int i = 0; i < this.usermanager.getList().size(); i++) {
				User user = this.usermanager.getList().get(i);
				if (user.getId().equals(id) && user.getPassWord().equals(password)) {
					index = i;
					break;
				}
			}

			if (index == -1)
				System.out.println("아이디나 비밀번호가 일치하지 않습니다.");
			else {
				this.log = index;
				System.out.println("로그인 되었습니다.");
			}
		}
	}

	// 6. 로그아웃 [6]
	private void logOut() {
		if (isLogged()) {
			this.log = -1;
			System.out.println("로그아웃 되었습니다.");
		}
	}

	public void run() {
		while (true) {
			if (isLogged())
				System.out.println(this.usermanager.getList().get(this.log).getId());

			printMenu();
			int select = inputNumber("메뉴");
			if (select == EXIT) {
				System.out.println("프로그램을 종료합니다.");
				break;
			}

			if (select == 1)
				joinMembership();
			else if (select == 2)
				leaveMembership();
			else if (select == 3)
				joinAccount();
			else if (select == 4)
				deleteAccount();
			else if (select == 5)
				logIn();
			else if (select == 6)
				logOut();
		}
	}
}
