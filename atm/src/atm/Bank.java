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

	// 0 생성자 초기화
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
				System.out.println("유효한 범주의 입력이 아닙니다.");
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
		System.out.println("1. 회원가입\n2. 회원탈퇴\n3. 계좌등록");
		System.out.println("4. 계좌철회\n5. 로그인\n6. 로그아웃");
	}

	// 2. 회원가입 [1]
	private void joinMembership() {
		if (!isLogged()) {
			String id = idDuplicationCheck();
			String password = inputString("비밀번호 : ");
			String name = inputString("이름 : ");

			User user = new User(id, password, name);

			this.usermanager.createUser(user);
		}
	}

	// 2-1 아이디 중복검사
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

	// 3. 회원 탈퇴 [2]
	private void leaveMembership() {
		if (isLogged()) {
			String password = inputString("비밀번호 : ");
			this.usermanager.deleteUser(this.log, password);
		}
	}

	// 4. 계좌 등록 [3]
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

	// 4-1 계좌 중복 검증
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

	// 5. 계좌철회 [4]
	private void deleteAccount() {
		User user = this.usermanager.getList().get(this.log);
		int index = 0;
		for (Account i : user.getAccs()) {
			System.out.printf("%d. %s", ++index, i.toString());
		}
		
		int select = selectListNumber(user);
		System.out.printf("삭제할 계좌로 %d번 계좌 선택하셧습니다.\n",select + 1);
		
		String password = inputString("비밀번호");
		if(user.getPassWord().equals(password)) {
//			user.getAccs().get(select)
//			user.getAccs().remove(select);
			Account delAcc = this.accountmanager.getAccount(select);
			
			this.accountmanager.deleteAccount(delAcc);
//			user.deleteAccount(delAcc);
		}else {
			System.out.println("비밀번호를 다시 확인해주세요");
		}
		
		
	}
	
	private int selectListNumber(User user) {
		int select = -1;
		while(true) {
			select = inputNumber("삭제할 계좌");
			if(select > user.getAccs().size())
				continue;
			return select-1;
		}	
	}

	// 6. 로그인 [5]
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

	// 7. 로그아웃 [6]
	private void logOut() {
		this.log = -1;
		System.out.println("로그아웃 되었습니다.");
	}

	public void run() {

		while (true) {
			if (isLogged())
				System.out.println(this.usermanager.getList().get(this.log).getId());

			printMenu();
			int select = inputNumber("메뉴") -1;
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
			else if (select == 4) {
			} else if (select == 5)
				logIn();
			else if (select == 6) {
			}

		}
	}

	// ATM 프로젝트

	// 1. 회원가입 / 2. 탈퇴
	// 3. 계좌 신청 / 4. 철회 ( 1인 3계좌까지 )
	// 5. 로그인
}
