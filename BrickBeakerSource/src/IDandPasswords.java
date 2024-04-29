import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class IDandPasswords {

	// stores usernames and passwords
	Map<String, String> loginInfo = new HashMap<>();
	
	// this reads the usernames and passwords from users.txt and puts them in the map.
	// (Assumes users.txt is formatted as "username password" on each line)
	public IDandPasswords() {
		String userName;
		String password;
		try {
			Scanner fileRead = new Scanner(new File("users.txt"));
			while (fileRead.hasNext()) {
				userName = fileRead.next();
				password = fileRead.next();
				loginInfo.put(userName, password);
			}
			fileRead.close();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file for reading");
		}
		System.out.println(loginInfo);
	}
	
	// returns the map of all usernames and passwords
	public Map<String, String> getLoginInfo() {
		return loginInfo;
	}
	
}
