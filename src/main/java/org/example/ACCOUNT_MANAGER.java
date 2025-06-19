package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ACCOUNT_MANAGER {
    Connection con;
    Scanner sc;
    //constructor to get connection and scanner instance;
     public ACCOUNT_MANAGER(Connection con, Scanner sc){
        this.con=con;
        this.sc=sc;
    }

     public void credit_money(long acc_no) throws SQLException {
        System.out.print("Enter The Amount: ");
        double amt = sc.nextDouble();
        if(amt<0){
            System.err.println("Enter a positive value");
            return;

        }
        sc.nextLine();
        System.out.print("Enter the security Pin");
        String pin = sc.nextLine();
        try{
            con.setAutoCommit(false);
            String sql = "Select balance from accounts where acc_num = ? and pin = ?";
            PreparedStatement ptsm = con.prepareStatement(sql);
            ptsm.setLong(1, acc_no);
            ptsm.setString(2, pin);
            ResultSet rs = ptsm.executeQuery();
            if (rs.next()) {
                String credit_query = "update accounts set balance = balance+? where acc_num = ?";
                PreparedStatement crptsm = con.prepareStatement(credit_query);
                crptsm.setDouble(1, amt);
                crptsm.setLong(2, acc_no);
                int rowAffected = crptsm.executeUpdate();
                if (rowAffected > 0) {
                    con.commit();
                    System.out.println("Transaction sucessful");
                    con.setAutoCommit(true);
                } else {
                    con.rollback();
                    System.out.println("Transaction failed");
                    con.setAutoCommit(true);
                }
            } else {
                System.out.println("Incorrect Pin or Account Number");

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
     }

     public void debit_money(long acc_no) throws SQLException{

        System.out.print("Enter the amount to debit: ");
        double amt = sc.nextDouble();
        sc.nextLine();
         if (amt < 0) {
             System.err.println("Enter a positive Value");
             return;

         }
        System.out.print("Enter the PIN: ");
        String pin = sc.nextLine();
        try{
            con.setAutoCommit(false);
            String sql = "Select Balance from accounts where acc_num = ? and pin = ?";
            PreparedStatement check_statement = con.prepareStatement(sql);
            check_statement.setLong(1,acc_no);
            check_statement.setString(2, pin);
            ResultSet rs = check_statement.executeQuery();
            if(rs.next() ){
                double acc_balance = rs.getDouble("BALANCE");
                if(acc_balance>amt){
                    String debit_query = "update accounts set balance = balance - ? where acc_num = ?";
                    PreparedStatement debitStatement = con.prepareStatement(debit_query);
                    debitStatement.setDouble(1, amt);
                    debitStatement.setLong(2, acc_no);
                    int rowAffected = debitStatement.executeUpdate();
                    if(rowAffected>0){
                        con.commit();
                        System.out.println("Transaction Sucessful");
                        con.setAutoCommit(true);
                    }else{
                        con.rollback();
                        System.out.println("Transaction Failed");
                        con.setAutoCommit(true);
                    }
                }else{
                    System.err.println("INSUFFICIENT BALANCE ");

                }
            }else{
                System.out.println("Enter correct credentials");

            }
        }catch (SQLException e ){
            e.printStackTrace();
        }
     }

     public void transfer_money(long senderAccNo) throws SQLException{
        System.out.println("Enter The Receiver Account Number");
        long recvAccNo = sc.nextLong();
        sc.nextLine();
        System.out.println("enter the amount");
        double amt = sc.nextDouble();
        if(amt < 0){
            System.err.println("Enter positive value");
            return;

        }
        System.out.println("Enter the PIN");
        sc.nextLine();
        String pin = sc.nextLine();
        try {
            con.setAutoCommit(false);
            String checkQuery = "SELECT BALANCE FROM ACCOUNTS WHERE ACC_NUM = ? AND PIN = ?";
            PreparedStatement checkStatement = con.prepareStatement(checkQuery);
            checkStatement.setLong(1,senderAccNo);
            checkStatement.setString(2,pin);
            String recvCheckQuery = "SELECT * FROM ACCOUNTS WHERE ACC_NUM = ?";
            PreparedStatement recvCheckStatement = con.prepareStatement(recvCheckQuery);
            recvCheckStatement.setLong(1, recvAccNo);
            ResultSet rs1 = recvCheckStatement.executeQuery();
            ResultSet rs = checkStatement.executeQuery();
            if(rs.next() && rs1.next()){
                double avail_bal = rs.getDouble("BALANCE");
                if(avail_bal>amt) {
                    String creQuery = "UPDATE ACCOUNTS SET BALANCE = BALANCE + ? WHERE ACC_NUM = ?";
                    String debQuery = "UPDATE ACCOUNTS SET BALANCE = BALANCE - ? WHERE ACC_NUM = ?";
                    PreparedStatement creStm = con.prepareStatement(creQuery);
                    PreparedStatement debStm = con.prepareStatement(debQuery);
                    creStm.setDouble(1, amt);
                    creStm.setLong(2, recvAccNo);
                    debStm.setDouble(1, amt);
                    debStm.setLong(2, senderAccNo);
                    int recvConn = creStm.executeUpdate();
                    int sendConn = debStm.executeUpdate();
                    if (recvConn > 0 && sendConn > 0) {
                        con.commit();
                        System.out.println("Transaction Successful");
                        con.setAutoCommit(true);
                    } else {
                        con.rollback();
                        System.err.println("Transaction Failed!!!!");
                        con.setAutoCommit(true);
                    }
                }else{
                    System.err.println("BKL AUKAAT ME REHLE");

                }
            }else{
                System.out.println("BAKCHODI MAT KAR LAWDE");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
     }

     public void check_balance(long acc_no){
        System.out.println("Enter the security PIN");
        sc.nextLine();
        String pin = sc.nextLine();
        try{
            String query = "SELECT BALANCE FROM ACCOUNTS WHERE ACC_NUM = ? and PIN = ?";
            PreparedStatement ptsm = con.prepareStatement(query);
            ptsm.setLong(1,acc_no);
            ptsm.setString(2, pin);
            ResultSet rs = ptsm.executeQuery();
            if(rs.next()){
                double amt = rs.getDouble("BALANCE");
                System.out.println("Your Account Balance is "+ amt);

            }else{
                System.out.println("Incorrect Credentials");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

     }
}
