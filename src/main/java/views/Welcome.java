package views;

import dao.UserDAO;
import model.User;
import service.GenerateOTP;
import service.SendOTPService;
import service.UserService;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {

    public JFrame jf;
    public JLabel jl;

    public static void main(String[] args){
        Welcome w = new Welcome();
        w.welcomeScreen();
    }
    public void welcomeScreen() {

        jf = new JFrame("File Hider");
        jf.setLayout(null);
        jf.setSize(400,300);
        jf.setLocationRelativeTo(null);

        jl = new JLabel("Welcome to the App");
        jl.setBounds(140,40,200,30);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(130,90,120,30);

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(130,130,120,30);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBounds(130,170,120,30);

        jf.add(jl);
        jf.add(loginBtn);
        jf.add(signupBtn);
        jf.add(exitBtn);

        jf.setVisible(true);

        loginBtn.addActionListener(e -> login());
        signupBtn.addActionListener(e -> signUp());
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private void login() {

        String email = JOptionPane.showInputDialog(jf,"Enter Email:");

        try{
            if(UserDAO.isExists(email)){

                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email,genOTP);

                String otp = JOptionPane.showInputDialog(jf,"Enter OTP:");

                if(otp.equals(genOTP)){
                    jf.dispose();
                    new UserView(email).home();
                }else{
                    JOptionPane.showMessageDialog(jf,"Wrong OTP");
                }

            }else{
                JOptionPane.showMessageDialog(jf,"User not found");
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    private void signUp() {

        String name = JOptionPane.showInputDialog(jf,"Enter Name:");
        String email = JOptionPane.showInputDialog(jf,"Enter Email:");

        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email,genOTP);

        String otp = JOptionPane.showInputDialog(jf,"Enter OTP:");

        if(otp.equals(genOTP)){

            User user = new User(name,email);
            int response = UserService.saveUser(user);

            switch(response){
                case 0 -> JOptionPane.showMessageDialog(jf,"User Registered");
                case 1 -> JOptionPane.showMessageDialog(jf,"User Already exists");
            }
        }
        else{
            JOptionPane.showMessageDialog(jf,"Wrong OTP");
        }
    }
}