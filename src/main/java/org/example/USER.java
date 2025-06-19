package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class USER {
    private Connection con;
    private Scanner sc;

    public USER(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public void register(){
        System.out.print("Enter the full name: ");
        String name = sc.nextLine();
        System.out.print("Enter the email address: ");
        String email = sc.nextLine();
        System.out.print("Enter the password: ");
        String pass = sc.nextLine();
        if(user_exists(email)){
            return;
        }
        String reg_query = "INSERT INTO USER_INFO(FULL_NAME, EMAIL, PASS) VALUES(?, ?, ?)";
        try {
            PreparedStatement reg_stm = con.prepareStatement(reg_query);
            reg_stm.setString(1, name);
            reg_stm.setString(2, email);
            reg_stm.setString(3, pass);
            int regRowsAffec = reg_stm.executeUpdate();
            if(regRowsAffec>0){
                System.out.println("USER registration successfull");
            }else{
                System.out.println("registration unsuccessfull");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public String login() throws SQLException{
        System.out.print("Enter the email address: ");
        String email = sc.nextLine();
        System.out.print("Enter the password: ");
        String pass = sc.nextLine();
        String log_query = "SELECT * FROM USER_INFO WHERE EMAIL = ? AND PASS = ?";
        try{
            PreparedStatement logStm = con.prepareStatement(log_query);
            logStm.setString(1,email);
            logStm.setString(2, pass);
            ResultSet rs = logStm.executeQuery();
            if(rs.next()){
                return email;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;

    }

    boolean user_exists(String email){
            String sql = "SELECT FULL_NAME FROM USER_INFO WHERE EMAIL = ?";
        try{
            PreparedStatement ptsm= con.prepareStatement(sql);
            ptsm.setString(1, email);
            ResultSet rs = ptsm.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
