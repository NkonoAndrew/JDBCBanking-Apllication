package com.sjsu;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.sql.*;
import java.util.Scanner;

/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	
	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	private static Scanner in = new Scanner(System.in);
	private static Scanner input = new Scanner(System.in);
	private  static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Need database properties filename");
		} else {
			init(args[0]);
			try {
				Class.forName(driver);
				con = null;
				stmt = null;
				con = DriverManager.getConnection(url, username, password);
				welcomeScreen();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initialize database connection given properties file.
	 * @param filename name of properties file
	 */
	public static void init(String filename) {
		try {
			Properties props = new Properties();						// Create a new Properties object
			FileInputStream input = new FileInputStream(filename);	// Create a new FileInputStream object using our filename parameter
			props.load(input);										// Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver");				// Load the driver
			url = props.getProperty("jdbc.url");						// Load the url
			username = props.getProperty("jdbc.username");			// Load the username
			password = props.getProperty("jdbc.password");			// Load the password
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		System.out.println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
			} catch (Exception e) {
				System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
				e.printStackTrace();
			}
	}

	/**
	 * User Welcome Screen
	 */
	public static void welcomeScreen() throws Exception {
		try {
		int input;
		boolean again = true;

		while (again) {
			System.out.println("Welcome!" +
					"\n 1. New Customer" +
					"\n 2. Login" +
					"\n 3. Cancel");
			System.out.print("Please make a choice:");
			input = Math.abs(in.nextInt());


			switch (input) {
				case 1:	//Prompt for Name, Gender, Age, and Pin.  System will return a customer ID if successful.

					//java -cp ";./db2jcc4.jar" BankingSystem.java ./db.properties
//					System.out.println("Enter a name:");
					String name;

					do {
						System.out.println("Enter a name:");
						name = in.next();
					} while (isNumeric(name));

					System.out.println("Enter a gender (M) or (F):");
					String gender = in.next();

					while ((!"M".equals(gender)) && (!"F".equals(gender))){
						System.out.println("Gender:");
						gender = in.next();
					}

					System.out.println("Enter age:");
					String enterAge = in.next();
					int Age = isNumber(enterAge);

					while (Age < 0){
						System.out.println("Enter age:");
						enterAge = in.next();
						Age = isNumber(enterAge);
					}
					String age = String.valueOf(Age);



					System.out.println("Enter pin:");
					String enterPin = in.next();
					int Pin = isNumber(enterPin);

					while (Pin < 0){
						System.out.println("Enter pin:");
						enterPin = in.next();
						Pin = isNumber(enterPin);
					}

					String pin = String.valueOf(Pin);

					newCustomer(name, gender, age, pin);
					break;

				case 2: //, prompt for customer ID and pin to authenticate the customer.
						// If user enters 0 for both customer ID & pin, then you will go straight to Screen #4.

					System.out.println("Enter the ID:");
					int enterID = Math.abs(in.nextInt());


					System.out.println("Enter your pin:");
					int enterPin2 = in.nextInt();


					int returnedPassword = userAuthentification(enterID);

					if (enterID == 0 && enterPin2 == 0){
						adminView();
					}else if (returnedPassword == enterPin2) {
						customerMenu(Integer.valueOf(enterID));
					}
					else {
						System.out.println("The password or userID is incorrect. Try again");
					}
					break;
				case 3:
					again = false;
					break;
			}
		}
		} catch (Exception e){
			System.out.println("Please enter correct input");
		}

	}

	public static int isNumber(String age){
		int number = Integer.valueOf(age);
		boolean isNumber;
		do {
			System.out.println("Enter number:");

			if (in.hasNextInt()){
				number = in.nextInt();
				isNumber = true;
			} else {
				System.out.println("Enter number again:");
				isNumber = false;
				in.next();
			}

		} while (!(isNumber));
		return number;
	}


	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static void customerMenu(int ID) throws SQLException, Exception {
		try{
		int input2;
		boolean again = true;

		while (again) {
			System.out.println("Welcome!" +
					"\n 1. Open Account" +
					"\n 2. Close Account" +
					"\n 3. Deposit" +
					"\n 4. Withdraw" +
					"\n 5. Transfer" +
					"\n 6. Account Summary" +
					"\n 7. Exit");
			System.out.print("Please make a choice:");
			Scanner in = new Scanner(System.in);
			input2 = in.nextInt();


			switch (input2) {
				//new user needs to insert a name gender and pin
				case 1:  //prompt for customer ID, account type, and balance (Initial deposit).
					//System will return an account number if successful.
					System.out.println("Enter your ID:");
					String customerID = in.next();


					System.out.println("Enter the account type C for Checking and S for Savings:");
					String accountType = in.next();

					while ((!"S".equals(accountType)) && (!"C".equals(accountType))){
						System.out.println("Gender:");
						accountType = in.next();
					}


					System.out.println("Enter amount you wish to deposit:");
					String initialAmount = in.next();


					openAccount(customerID, initialAmount, accountType);
					break;
				case 2:
					System.out.println("Enter the account to close:");
					String closeThis = in.next();

					if (ID != isOwner(closeThis)){
						System.out.println("Sorry, this account belongs to another user");
					}
					else {
						closeAccount(closeThis);
						System.out.println("Account " + closeThis + " closed!");
					}

					break;
				case 3:    //prompt for account # and deposit amount
					System.out.println("Enter the account number you wish to deposit to:");
					String accountNo = in.next();

					System.out.println("Enter amount you wish to deposit:");
					String depositAmount = in.next();

					deposit(accountNo, depositAmount);
					break;
				case 4:	//prompt for account # and withdraw amount
					System.out.println("Enter the account number you wish to withdraw from:");
					String withdrawFrom = in.next();

					System.out.println("Enter amount you wish to withdraw:");
					String withdrawAmount = in.next();

					if (ID != isOwner(withdrawFrom)){
						System.out.println("Can only withdraw from your account!");
					} else {
						withdraw(withdrawFrom, withdrawAmount);
					}
					break;

				case 5:	//prompt for the source and destination account #s and transfer amount.
					System.out.println("Enter the account number you wish to withdraw from:");
					String withdrawFromAcc = in.next();


					System.out.println("Enter the account number you wish to transfer to:");
					String transferTo = in.next();

					System.out.println("Enter amount you wish to transfer:");
					String transferAmount = in.next();


					if (ID != isOwner(withdrawFromAcc)){
						System.out.println("This account belongs to another user or does not exist");
					}
					else {
						transfer(withdrawFromAcc, transferTo, transferAmount);
					}
					break;
				case 6:	//display each account # and its balance for same customer
						// and the total balance of all accounts.
						accountSummary(String.valueOf(ID));
					break;
				case 7:
					again = false;
					break;
				default:
					System.out.println("Enter a valid number or enter 7 to exit");
					break;
			}
		}
		} catch (Exception e){
			System.out.println("Please enter data correctly:");
		}

	}

	public static void adminView() throws SQLException, Exception {
		try {
		int input3;
		boolean again = true;

		while (again) {
			System.out.println("Welcome!" +
					"\n 1. Account Summary for a customer" +
					"\n 2. Report A :: Customer Information wiht Total Balance in Decreasing Order" +
					"\n 3. Find the Average Total Balance Between Age Groups" +
					"\n 4. Exit");
			System.out.print("Please make a choice:");
			Scanner in = new Scanner(System.in);
			input3 = in.nextInt();


			switch (input3) {
				//new user needs to insert a name gender and pin
				case 1:
					System.out.println("Enter customer ID to view summary:");
					String userID = in.next();
					accountSummary(userID);
					break;
				case 2:
					reportA();
					break;
				case 3:
					System.out.println("Enter a minimum age");
					String min = in.next();

					System.out.println("Enter the maximum age");
					String max = in.next();

					reportB(min, max);
					break;
				case 4:
					again = false;
					break;
				default:
					System.out.println("Enter a valid number or enter 4 to exit");
					break;

			}
		}
		} catch (Exception e){
			System.out.println("Please enter correct input:");
		}
	}


	/**
	 * Create a new customer.
	 * @param name customer name
	 * @param gender customer gender
	 * @param age customer age
	 * @param pin customer pin
	 */
	public static void newCustomer(String name, String gender, String age, String pin) throws SQLException {
		try {
			System.out.println(":: CREATE NEW CUSTOMER - RUNNING");
			PreparedStatement preparedStatement = con.prepareStatement
					("INSERT INTO p1.customer(name, gender, age, pin) VALUES (?, ?, ?, ?)");
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, gender);
			preparedStatement.setString(3, age);
			preparedStatement.setString(4, pin);
			preparedStatement.executeUpdate();

			preparedStatement = con.prepareStatement
					("SELECT ID FROM p1.customer WHERE name = ? AND gender = ? AND age = ?");
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, gender);
			preparedStatement.setString(3, age);

			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.print("Your auto-generated customer id is: ");
			while (resultSet.next()) {
				System.out.print(resultSet.getInt("ID"));
			}
			resultSet.close();
			System.out.println();
			System.out.println(":: CREATE NEW CUSTOMER - SUCCESS");

		}
		catch (SQLException exception){
			System.out.println(exception);
		}

	}

	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 */
	public static void openAccount(String id, String amount, String type) throws SQLException {
		try {
			System.out.println(":: OPEN ACCOUNT - RUNNING");
			PreparedStatement preparedStatement = con.prepareStatement
					("INSERT INTO p1.account (id, balance, type, status) VALUES (?, ?, ?, 'A')");
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, amount);
			preparedStatement.setString(3, type);
			preparedStatement.executeUpdate();
		}
		catch (SQLException exception) {
			System.out.println("Failed to open ACCOUNT correctly :" + exception);
		}
			PreparedStatement preparedStatement = con.prepareStatement("SELECT number FROM p1.account WHERE id = ?");
			preparedStatement.setString(1, id);

		System.out.print("Your auto-generated account# is: ");
		try {
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				System.out.println(resultSet.getInt("number"));
			}
			resultSet.close();
			System.out.println("If you have opened more than one account, your new account number is the last one");

			} catch (Exception exception){
			System.out.println("Failed to get inserted ID ");
			}
			System.out.println(":: OPEN ACCOUNT - SUCCESS");
	}


	public static int userAuthentification(int id) throws SQLException{

			PreparedStatement preparedStatement = con.prepareStatement("SELECT PIN from p1.customer WHERE id = ?");
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();

			int password = 0;
			while (resultSet.next()){
				password = resultSet.getInt("PIN");
			}
			resultSet.close();
			return password;
	}

	/**
	 * Close an account.
	 * @param accNum account number
	 */
	public static void closeAccount(String accNum) throws SQLException {
		System.out.println(":: CLOSE ACCOUNT - RUNNING");
				PreparedStatement preparedStatement = con.prepareStatement
						("UPDATE p1.account SET status = 'I', balance = 0 WHERE number = ?");
				preparedStatement.setString(1, accNum);
				preparedStatement.executeUpdate();
		System.out.println(":: CLOSE ACCOUNT - SUCCESS");
	}

	public static int isOwner(String accNum) throws SQLException {
		PreparedStatement preparedStatement = con.prepareStatement
				("SELECT id from p1.account where number = ?");

		preparedStatement.setString(1, accNum);
		ResultSet resultSet = preparedStatement.executeQuery();

		int id = 0;
		while (resultSet.next()){
			id = resultSet.getInt("id");
		}
		resultSet.close();
		return id;

	}


	/**
	 * Deposit into an account.
	 * @param accNum account number
	 * @param amount deposit amount
	 */
	public static void deposit(String accNum, String amount) throws SQLException {
		System.out.println(":: DEPOSIT - RUNNING");
				PreparedStatement preparedStatement = con.prepareStatement
						("UPDATE p1.account SET balance = balance + ? WHERE number = ?");
		preparedStatement.setString(1, amount);
		preparedStatement.setString(2, accNum);
		preparedStatement.executeUpdate();

		//"UPDATE TableName SET TableField = TableField + 1 WHERE SomeFilterField = @ParameterID"
		System.out.println(":: OPEN ACCOUNT - SUCCESS");
	}

	/**
	 * Withdraw from an account.
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdraw(String accNum, String amount) throws SQLException {
		System.out.println(":: WITHDRAW - RUNNING");

		try {
		PreparedStatement preparedStatement = con.prepareStatement
				("UPDATE p1.account SET balance = balance - ? WHERE number = ?");
		preparedStatement.setString(1, amount);
		preparedStatement.setString(2, accNum);
		preparedStatement.executeUpdate();
		System.out.println(":: WITHDRAW - SUCCESS");
		} catch (SQLException exception){
			System.out.println("Account cannot be over drafted. Please check how much money you have" +
								"by checking Account Summary" + exception);
		}
	}

	/**
	 * Transfer amount from source account to destination account. 
	 * @param srcAccNum source account number
	 * @param destAccNum destination account number
	 * @param amount transfer amount
	 */
	public static void transfer(String srcAccNum, String destAccNum, String amount) throws SQLException {
		try {
			System.out.println(":: TRANSFER - RUNNING");
			withdraw(srcAccNum, amount);
			deposit(destAccNum, amount);
			System.out.println(":: TRANSFER - SUCCESS");
		} catch (SQLException exception){
			System.out.println("Transfer not successful");
		}
	}

	/**
	 * Display account summary.
	 * @param cusID customer ID
	 */
	public static void accountSummary(String cusID) throws SQLException {
		System.out.println(":: ACCOUNT SUMMARY - RUNNING");
				try {
					PreparedStatement preparedStatement = con.prepareStatement
							("SELECT number, balance from p1.account WHERE id = ? AND status = 'A' ");
					preparedStatement.setString(1, cusID);
					ResultSet resultSet = preparedStatement.executeQuery();


					System.out.println("The account number(s) and balance of the current customer(" + cusID + ")");
					while (resultSet.next()) {
						System.out.print(resultSet.getInt("number") + "  ");
						System.out.print(resultSet.getInt("balance"));
						System.out.println();
					}
					resultSet.close();
				} catch (SQLException e){
					System.out.println("Cannot find the customer ID or the ID does not exist. Try again");
				}


				try {
					PreparedStatement preparedStatement = con.prepareStatement
							("SELECT sum(balance) as totalBalance from p1.account WHERE id = ? AND status = 'A' ");

					preparedStatement.setString(1, cusID);

					int total = 0;
					ResultSet resultSet1 = preparedStatement.executeQuery();

					System.out.print("Total balance of the current customer(" + cusID + ") =");

					while (resultSet1.next()) {
						total = resultSet1.getInt("totalBalance");
					}
					resultSet1.close();
					System.out.print(total);
				} catch (SQLException e){
					System.out.println("Cannot find the user balance");
				}
				System.out.println();
				System.out.println(":: ACCOUNT SUMMARY - SUCCESS");
	}

	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing Order.
	 */
	public static void reportA() throws SQLException {
		System.out.println(":: REPORT A - RUNNING");
		try {
			PreparedStatement preparedStatement = con.prepareStatement
					("SELECT C.ID, name, age, gender, sum(balance) as totalBalance FROM p1.customer C, p1.account A WHERE C.id = A.id and STATUS = 'A' GROUP BY C.ID, name, name, age, gender ORDER BY totalBalance DESC");
			ResultSet resultSet = preparedStatement.executeQuery();

			String name, gender;
			int total = 0, age, id = 0;
			System.out.println("ID \t NAME \t AGE \t GENDER \t TOTAL BALANCE");
			while (resultSet.next()) {
				id = resultSet.getInt("ID");
				name = resultSet.getString("NAME");
				age = resultSet.getInt("AGE");
				gender = resultSet.getString("GENDER");
				total = resultSet.getInt("totalBalance");
				System.out.println(id + "\t" + name + "\t" + age + "\t" + gender + "\t\t\t" + total);

			}
			resultSet.close();
		} catch (SQLException e){
			System.out.println(e);
		}
		System.out.println(":: REPORT A - SUCCESS");
	}

	/**
	 * Display Report B - Customer Information with Total Balance in Decreasing Order.
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static void reportB(String min, String max) throws SQLException
	{
		try {
			PreparedStatement preparedStatement = con.prepareStatement
					("SELECT age, avg(balance) as averageBalance FROM p1.account A, p1.customer C WHERE A.status ='A' AND C.age >= 44 AND C.age <= 45 GROUP BY age");
			preparedStatement.setString(1, min);
					preparedStatement.setString(2, max);
					ResultSet resultSet = preparedStatement.executeQuery();

					int age, balance;
					while (resultSet.next()){
						age = resultSet.getInt("age");
						balance = resultSet.getInt("averageBalance");
						System.out.println(age + "\t\t" + balance);
					}
					resultSet.close();
		} catch (SQLException exception){

		}
	}
}
