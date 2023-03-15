package atm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
	
	// id/password/name/accs.size()/
	public void saveUserData(ArrayList<User> list) {

	}

	public void loadUserData() {

	}
	
	// userId/accNum/balance
	public void saveAccountData(ArrayList<Account> list) {
		
	}
	
	public void loadAccountData() {
		
	}
}
