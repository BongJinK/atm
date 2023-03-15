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

	private void printMainMenu() {
		System.out.printf("==== %s ====\n", this.name);
		System.out.println("1. 회원가입\n2. 회원탈퇴\n3. 로그인");
		System.out.println("4. 로그아웃\n5. 뱅킹\n6. 파일");
		System.out.println("0. 종료");
	}
	
	private void printBankingMenu() {
		System.out.println("1. 입금\n2. 출금\n3. 조회\n4. 이체");
		System.out.println("5. 계좌 생성\n6. 계좌 철회\n0. 뒤로가기");
	}
	
	private void fileMenu() {
		System.out.println("1. 저장\n2. 로드\n0. 뒤로가기");
	}

	// 1. 회원가입
	private void joinMembership() {
		if (!isLogged()) {
			String id = inputString("아이디 : ");
			String password = inputString("비밀번호 : ");
			String name = inputString("이름 : ");

			User user = new User(id, password, name);

			if (this.usermanager.addUser(user) != null)
				System.out.println("회원가입 성공");
			else
				System.out.println("중복된 아이디가 존재합니다.");
		}
	}

	// 2. 회원 탈퇴
	private void leaveMembership() {
		if (isLogged()) {
			String password = inputString("비밀번호 : ");
			this.usermanager.deleteUserByPassword(this.log, password);
		}
	}
	
	// 3. 로그인
	private void logIn() {
		if (!isLogged()) {
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
				System.out.println("아이디나 비밀번호가 일치하지 않습니다.");
			else {
				this.log = index;
				System.out.println("로그인 되었습니다.");
			}
		}
	}

	// 4. 로그아웃
	private void logOut() {
		if (isLogged()) {
			this.log = -1;
			System.out.println("로그아웃 되었습니다.");
		}
	}
	
	

	
	// 5. 뱅킹
	private void bankingRun() {
		while(true) {
			printBankingMenu();
			
			int select = inputNumber("메뉴");
			if( select == EXIT) {
				System.out.println("초기화면으로 돌아갑니다.");
				break;
			}
		}
	}

	// 3. 계좌 등록 [3]
	private void joinAccount() {
		if (isLogged()) {
			// 복제본 반환 받음 (로그인 되어 있는 회원)
			User user = this.usermanager.getUser(this.log);
			String id = user.getId();

			if (user.getAccountSize() < Account.LIMIT) {
				Account account = this.accountmanager.createAccount(new Account(id));
				this.usermanager.setUser(user, account);
			} else
				System.out.println("계좌는 3개까지 보유 가능합니다.");
		}
	}

	// 4. 계좌철회 [4]
	private void deleteAccount() {
		if (isLogged()) {
			User user = this.usermanager.getUser(this.log);
			for (int i = 0; i < user.getAccs().size(); i++) {
				Account account = user.getAccs().get(i);
				System.out.printf("%d. %s\n", i + 1, account.toString());
			}

			int select = selectListNumber(user) - 1;
			System.out.printf("삭제할 계좌로 %d번 계좌 선택하셧습니다.\n", select + 1);

			String password = inputString("비밀번호");
			if (user.getPassWord().equals(password)) {
				Account deleteAcc = user.getAccount(select);
//				Account delAcc = this.accountmanager.getAccount(select);

				this.accountmanager.deleteAccount(deleteAcc);
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

	public void run() {
		while (true) {
			if (isLogged())
				System.out.println(this.usermanager.getUser(this.log).getId());

			printMainMenu();
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
