package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ACCOUNTS {
    private Connection con;
    private Scanner sc;

    public ACCOUNTS(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public long open_accont(String email) throws SQLException{
        if(!acc_exist(email)){
            System.out.println("Enter the full name");
            String name = sc.nextLine();
            sc.nextLine();
            System.out.println("Enter the Initial balance");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.println("Enter the PIN");
            String pin = sc.nextLine();
            String sql = "INSERT INTO ACCOUNTS(FULL_NAME, EMAIL, BALANCE, PIN) VALUES (?, ?, ?, ?)";
            try{
                PreparedStatement ptsm = con.prepareStatement(sql);
                ptsm.setString(1,name);
                ptsm.setString(2, email);
                ptsm.setDouble(3,balance);
                ptsm.setString(4, pin);

                int rowAffect = ptsm.executeUpdate();
                if(rowAffect>0){
                    System.out.println("Account Created Successfully");
                    String getACC = "SELECT ACC_NUM FROM ACCOUNTS WHERE EMAIL = ?";
                    PreparedStatement getPtsm = con.prepareStatement(getACC);
                    getPtsm.setString(1, email);
                    ResultSet rs = getPtsm.executeQuery();
                    if(rs.next()){
                        return rs.getLong("ACC_NUM");
                    }


                }else{
                    System.err.println("Error in Creating Account");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account already Exist");
    }

    public long get_acc_num(String email){
        String getACC = "SELECT ACC_NUM FROM ACCOUNTS WHERE EMAIL = ?";
        try {
            PreparedStatement getPtsm = con.prepareStatement(getACC);
            getPtsm.setString(1,email);
            ResultSet rs = getPtsm.executeQuery();
            if (rs.next()) {
                return rs.getLong("ACC_NUM");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Does Not Exist");
    }

    public boolean acc_exist(String email) throws SQLException {
        String query = "SELECT * FROM ACCOUNTS WHERE EMAIL = ?";
        try{
            PreparedStatement ptsm = con.prepareStatement(query);
            ptsm.setString(1,email);
            ResultSet rs = ptsm.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
