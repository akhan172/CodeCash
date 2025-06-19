package org.example;

import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class BANKING_APP {
    private static final String url = "jdbc:mysql://localhost:3306/banking_sys";
    private static final String username = "root";
    private static final String password = "toor";
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers Loaded");

        } catch (ClassNotFoundException e) {
            System.err.println("error in loading driving");

        }
        try{
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection to DB established");
            Scanner sc = new Scanner(System.in);
            USER user = new USER(con, sc);
            ACCOUNTS accounts = new ACCOUNTS(con, sc);
            ACCOUNT_MANAGER account_manager = new ACCOUNT_MANAGER(con, sc);

            String email;
            long acc_no;

            while(true){
                System.out.println("=======================================================");
                System.out.println("Welcome to KHAN'S Banking System");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                int choice1 = sc.nextInt();
                sc.nextLine();
                switch(choice1){
                    case 1: user.register(); break;

                    case 2:
                        email = user.login();
                        if(email != null){
                            System.out.println("=====================");
                            System.out.println("User logged in successful");
                            if(!accounts.acc_exist(email)){
                                sc.nextLine();
                                System.out.println("1. Create a new Account");
                                System.out.println("2. exit");
                                if(sc.nextInt() == 1){
                                    acc_no = accounts.open_accont(email);
                                    System.out.println("Your Account Number is: "+acc_no);
                                }else{
                                    break;
                                }
                            }
                            acc_no = accounts.get_acc_num(email);
                            int choice2 = 0;
                            while(choice2 != 5){
                                System.out.println();

                                System.out.println("1. Credit Money");
                                System.out.println("2. Debit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log-Out");
                                System.out.println("Enter you Choice");
                                choice2 = sc.nextInt();
                                switch (choice2){
                                    case 1:
                                        account_manager.credit_money(acc_no);
                                        break;
                                    case 2:
                                        account_manager.debit_money(acc_no);
                                        break;
                                    case 3:
                                        account_manager.transfer_money(acc_no);
                                        break;
                                    case 4:
                                        account_manager.check_balance(acc_no);
                                        break;
                                    case 5: break;

                                    default:
                                        System.out.println("Enter correct choice");
                                        break;
                                }
                            }
                        }else{
                            System.out.println("Enter Correct Details");

                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING THIS SYSTEM");
                        System.out.println("Exiting the system......");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }

            }

        } catch (SQLException e) {
            System.out.println("DB not connected");

        }
    }
}