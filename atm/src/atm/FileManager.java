package atm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

	private String userFileName;
	private String accountFileName;
	private File userfile;
	private File accountfile;
	private FileWriter fileWriter;
	private FileReader fileReader;
	private BufferedReader bufferedReader;

	public FileManager() {
		this.userFileName = "user.txt";
		this.accountFileName = "account.txt";

		this.userfile = new File(userFileName);
		this.accountfile = new File(accountFileName);
	}

	// id/password/name
	public void saveUserData(ArrayList<User> list) {
		if (list != null) {
			String data = "";
			System.out.println("유저데이터 리스트 길이 : " + list.size());
			for (int i = 0; i < list.size(); i++) {
				User user = list.get(i);
				data += user.getId() + "/";
				data += user.getPassWord() + "/";
				data += user.getName();
				if (i != list.size() - 1)
					data += "\n";
			}

			try {
				this.fileWriter = new FileWriter(this.userfile);
				fileWriter.write(data);
				fileWriter.close();
				System.out.println("유저 데이터 저장 성공");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("유저 데이터 저장 실패");
			}
		}
	}

	public ArrayList<User> loadUserData(ArrayList<Account> list) {
		ArrayList<User> userData = new ArrayList<User>();

		if (userfile.exists()) {
//			userData = new ArrayList<User>();
			try {
				this.fileReader = new FileReader(this.userfile);
				this.bufferedReader = new BufferedReader(this.fileReader);

				int index = 0;
				while (this.bufferedReader.ready()) {
					String[] line = this.bufferedReader.readLine().split("/");

					User user = new User(line[0], line[1], line[2]);
					for (Account i : list) {
						if (line[0].equals(i.getId())) {
							user.addAccount(i);
							System.out.println(i.toString());
						}
					}
					userData.add(user);

					System.out.println(userData.get(index).getId());
					System.out.println(userData.get(index).getPassWord());
					System.out.println(userData.get(index++).getName());
				}

				this.fileReader.close();
				this.bufferedReader.close();
				System.out.println("계좌 데이터 로드 성공");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("계좌 데이터 로드 실패");
			}
			return userData;
		}
		return userData;
	}

	// userId/accNum/balance
	public void saveAccountData(ArrayList<Account> list) {
		if (list != null) {
			String data = "";
			for (int i = 0; i < list.size(); i++) {
				Account account = list.get(i);
				data += account.getId() + "/";
				data += account.getAccNum() + "/";
				data += account.getBalance();
				if (i != list.size() - 1)
					data += "\n";
			}

			try {
				this.fileWriter = new FileWriter(accountfile);
				this.fileWriter.write(data);
				this.fileWriter.close();
				System.out.println("계좌 데이터 저장 성공");

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("계좌 데이터 저장 실패");
			}
		}
	}

	public ArrayList<Account> loadAccountData() {
		ArrayList<Account> accountData = null;
		int index = 0;

		if (this.accountfile.exists()) {
			accountData = new ArrayList<Account>();
			try {
				this.fileReader = new FileReader(this.accountfile);
				this.bufferedReader = new BufferedReader(this.fileReader);

				while (this.bufferedReader.ready()) {
					String[] line = this.bufferedReader.readLine().split("/");

					Account account = new Account(line[0], line[1], Integer.parseInt(line[2]));

					accountData.add(account);
					System.out.println(accountData.get(index).getId());
					System.out.println(accountData.get(index).getAccNum());
					System.out.println(accountData.get(index++).getBalance());
				}

				this.fileReader.close();
				this.bufferedReader.close();
				System.out.println("계좌 데이터 로드 성공");
				return accountData;
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("계좌 데이터 로드 실패");
			}
		}
		return accountData;

		// null 이 아닐때만 저장
	}
}
