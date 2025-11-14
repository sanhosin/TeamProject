package main.java.GUImain;



import java.awt.*;
import java.util.Scanner;


import javax.swing.*;


import main.java.domain.TestDataManage;

public class Main {
    TestDataManage tdm=new TestDataManage();
    
    Scanner scan=new Scanner(System.in);

    JFrame frame = new JFrame("여행가요");
    public static CardLayout card = new CardLayout();
    public static JPanel root = new JPanel(card);

    LoginPane loginPanel=new LoginPane(root, card);
    MainPane mainPanel=new MainPane(root, card);
    JPanel searchPanel;
    JPanel schedulePanel;

    void mymain(){
        createAndShowGUI();
    }

    void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.getContentPane().add(root);
        frame.getContentPane().setBackground(new Color(220, 235, 255));
        root.setOpaque(false);


        root.add(loginPanel.setupLoginPanel(), "로그인");
        root.add(mainPanel.setupMainPanel(), "메인");

        card.show(root, "로그인");

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.mymain();
        main.scan.close();
    }
}
