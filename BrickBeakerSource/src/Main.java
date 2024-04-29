
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		/*
		 *  creates instance of IDandPasswords, which reads in the known usernames and passwords
		 *  from users.txt and stores them in a HashMap that maps the usernames to their passwords.
		 */
		IDandPasswords idandpasswords = new IDandPasswords();
		
		// creates the JFrame for the login page and passes the Map of usernames and passwords to
		// the JFrame.
		LoginPage login = new LoginPage(idandpasswords.getLoginInfo());
	}
}