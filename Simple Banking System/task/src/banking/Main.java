package banking;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:" + args[1];
        BankingInfo newBank = new BankingInfo(url);
        newBank.chooseAction();
    }
}

    class BankingInfo {
        Scanner sc = new Scanner(System.in);
       SQLiteDataSource dataSource = new SQLiteDataSource();
       private static Connection con;
       private static Statement statement;
       private static ResultSet resultSet;



        public BankingInfo(String url) throws SQLException {
            dataSource.setUrl(url);
            con = dataSource.getConnection();
            statement = con.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id      INTEGER," +
                        "number  TEXT," +
                        "pin     TEXT," +
                        "balance INTEGER DEFAULT 0)");

        }

        public void chooseAction() throws SQLException {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            switch (sc.nextInt()) {
                case 1:
                    creatingAccount();
                    chooseAction();
                    break;
                case 2:
                    loginInAccount();
                    chooseAction();
                    break;
                default:
                    System.out.println("Bye!");
                    System.exit(0);
            }

        }

        public void creatingAccount() throws SQLException {
            final String CONST = "400000";
            Random random = new Random();
            String finalCardNumber = CONST;
            for (int i = 0; i < 10; i++) {
                int cardNumber = random.nextInt(10);
                String s = Integer.toString(cardNumber);
                finalCardNumber = finalCardNumber + s;
            }
            if (validateCreditCardNumber(finalCardNumber)){
                System.out.println("Your card has been created");
                System.out.println(finalCardNumber);

                String PIN = "";
                for (int i = 0; i < 4; i++) {
                    int digits = random.nextInt(10);
                    String s = Integer.toString(digits);
                    PIN = PIN + s;
                }
                System.out.println("Your card PIN");
                System.out.println(PIN);


                // Insert into database
                try {
                    statement.executeUpdate("INSERT INTO card (number, pin) VALUES "
                            + String.format("('%s', '%s')", finalCardNumber, PIN));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                chooseAction();
            } else {
                creatingAccount();
            }
        }
        //luhn algorithm
        public boolean validateCreditCardNumber(String input){
            int[] cardNumber = new int[input.length()];

            for (int i = 0; i < input.length(); i++){
                cardNumber[i] = Integer.parseInt(input.substring(i, i + 1));
            }
            for (int i = cardNumber.length - 2; i >= 0; i = i -2) {
                int tempValue = cardNumber[i];
                tempValue = tempValue * 2;
                if (tempValue > 9) {
                    tempValue = tempValue % 10 + 1;
                }
                cardNumber[i] = tempValue;
            }

            int total = 0;
            for (int i = 0; i < cardNumber.length; i++){
                total += cardNumber[i];
            }

            if(total % 10 == 0){
                return true;
            } else {
                return false;
            }

        }

        public void loginInAccount() throws SQLException {
            System.out.println("Enter your card number:");
            String logCardNumb = sc.next();
            System.out.println("Enter your PIN:");
            String logPIN = sc.next();
            boolean f = true;
            try (final Statement statement = con.createStatement();
                 final ResultSet resultSet = statement.executeQuery("SELECT * FROM card")) {
                while (resultSet.next()) {
                    if (resultSet.getString("number").equals(logCardNumb) && resultSet.getString("pin").equals(logPIN)) {
                        f = false;
                        System.out.println("You have successfully logged in!");
                        manageInAccount(logCardNumb);
                        break;
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (f) {
                System.out.println("Wrong card number or PIN!");
                chooseAction();
            }


        }
        public void addIncome(String logCardNumb) throws SQLException {
            String input = "UPDATE card SET balance = balance + ? WHERE number = ?";
            System.out.println("Enter income:");
            int add = sc.nextInt();
            try(    PreparedStatement preparedStatement = con.prepareStatement(input)) {
                    preparedStatement.setInt(1,add);
                    preparedStatement.setString(2,logCardNumb);

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Income was added!");
            manageInAccount(logCardNumb);
        }

        public void closeAccount(String logCardNumb) throws SQLException {
            statement.executeUpdate("DELETE FROM card WHERE number = '" + logCardNumb + "';");
            System.out.println("The account has been closed!");
        }
        public void doTransfer(String logCardNumb) throws SQLException{
            System.out.println("Transfer");
            System.out.println("Enter card number:");

            String input = sc.next();
            if (!validateCreditCardNumber(input)) {
                System.out.println("Probably you made a mistake in the card number. Please try again!");
                manageInAccount(logCardNumb);
            } else if (input.equals(logCardNumb)){
                System.out.println("You can't transfer money to the same account!");
                manageInAccount(logCardNumb);
            } else if (!ifExists(input)) {
                System.out.println("Such a card does not exist.");
                manageInAccount(logCardNumb);
            } else {
                System.out.println("Enter how much money you want to transfer:");
                long moneyToTransfer = sc.nextLong();
                resultSet = statement.executeQuery("SELECT * FROM card WHERE balance >= " + moneyToTransfer +
                                                       " AND number LIKE " + logCardNumb );
                if (resultSet.next()) {
                try {

                    con.setAutoCommit(false);

                    try {

                        statement.executeUpdate("UPDATE card SET balance = balance - " + moneyToTransfer +
                                " WHERE number = " + logCardNumb);
                        statement.executeUpdate("UPDATE card SET balance = balance +" + moneyToTransfer +
                                " WHERE number = " + input);
                        // con.setAutoCommit(true);
                        con.commit();
                        con.setAutoCommit(true);
                        // спросить тут
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                    System.out.println("Success!");
                    manageInAccount(logCardNumb);
                } else {
                    System.out.println("Not enough money!");
                    manageInAccount(logCardNumb);
                }
            }
        }
        public boolean ifExists(String input) throws SQLException {
            resultSet = statement.executeQuery("SELECT * FROM card WHERE number = " + input);
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        }
        public void manageInAccount(String logCardNumb) throws SQLException {
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            int action = sc.nextInt();
            switch (action) {
                case 1:
                    // спросить про файнал
                    try (final Statement statement = con.createStatement();
                         final ResultSet resultSet = statement.executeQuery("SELECT * FROM card")) {
                        while (resultSet.next()) {
                            if (resultSet.getString("number").equals(logCardNumb)) {
                                System.out.println("Balance: " + resultSet.getString("balance"));
                                manageInAccount(logCardNumb);
                                break;
                            }
                        }
                        manageInAccount(logCardNumb);
                    }

                case 2:
                    //add income
                    addIncome(logCardNumb);
                    break;
                case 3:
                    //do transfer
                    doTransfer(logCardNumb);
                    break;
                case 4:
                    // close account
                    closeAccount(logCardNumb);
                    break;
                case 5:
                    System.out.println("You have successfully logged out!");
                    chooseAction();
                    break;
                default:
                    System.out.println("Bye!");
                    System.exit(0);
            }
        }
    }