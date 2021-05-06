package com.sjsu;

import javax.swing.*;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class GUI extends JFrame {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    // JDBC Objects
    private static Connection con;
    private static Statement stmt;

    public static void main(String[] args) throws SQLException {
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
            }
        }

        welcomeScreen();


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

    public static void adminView(){
        JFrame frame = new JFrame();
        JTextField name, gender, age, pin;
        JLabel userName, userGender, userAge, userPin;
        JPanel panel = new JPanel();

        userName = new JLabel("Name");
        userName.setBounds(10, 20, 80, 25);
        panel.add(userName);

        name = new JTextField(20);
        name.setBounds(110, 20, 300, 25);
        panel.add(name);

        userGender = new JLabel("Gender");
        userGender.setBounds(10, 50, 80, 25);
        panel.add(userGender);

        gender = new JTextField(20);
        gender.setBounds(110, 50, 300, 25);
        panel.add(gender);

        userAge = new JLabel("Age");
        panel.add(userAge);

        age = new JTextField(20);
        panel.add(age);

        userPin = new JLabel("User Pin");
        panel.add(userPin);

        age = new JTextField(20);
        panel.add(age);


        JButton next = new JButton("Next");
        panel.add(next);

        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
        frame.setSize(500, 500);

    }

    public static void welcomeScreen(){
        JFrame frame = new JFrame("Welcome Screen");

        // create a new buttons
        JButton newCustomer = new JButton("New Customer");
        JButton cutomerLogin = new JButton("Customer Login");
        JButton exit = new JButton("Exit");

        // create a panel to add buttons
        JPanel panel = new JPanel();

        // add buttons and textfield to panel
        panel.add(newCustomer);
        panel.add(cutomerLogin);
        panel.add(exit);


        // add panel to frame
        frame.add(panel);

        frame.pack();
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        newCustomer.addActionListener( e -> {
            newCustomerScreen();
            frame.dispose();
        });

    }

    public static void newCustomerScreen(){


        JTextField name, gender, age, pin;
        JLabel userName, userGender, userAge, userPin;
        JButton next;

        JPanel panel = new JPanel();
        JFrame frame = new JFrame("New Customer Screen");


        userName = new JLabel("Name");
        userName.setBounds(10, 20, 80, 25);
        panel.add(userName);

        name = new JTextField(20);
        name.setBounds(110, 20, 300, 25);
        panel.add(name);

        userGender = new JLabel("Gender");
        userGender.setBounds(10, 50, 80, 25);
        panel.add(userGender);

        gender = new JTextField(20);
        gender.setBounds(110, 50, 300, 25);
        panel.add(gender);

        userAge = new JLabel("Age");
        panel.add(userAge);

        age = new JTextField(20);
        panel.add(age);

        userPin = new JLabel("User Pin");
        panel.add(userPin);

        age = new JTextField(20);
        panel.add(age);


        next = new JButton("Next");
        panel.add(next);

        frame.add(panel);

        frame.pack();
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);


    }
}
