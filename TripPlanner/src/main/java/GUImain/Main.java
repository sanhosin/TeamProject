package main.java.GUImain;

import java.awt.*;
import java.util.Scanner;
import javax.swing.*;
import main.java.domain.*;

public class Main {
    public static TestDataManage tdm=new TestDataManage();

    Scanner scan=new Scanner(System.in);

    JFrame frame = new JFrame("여행가요");
    public static CardLayout card = new CardLayout();
    public static JPanel root = new JPanel(card);

    LoginPane loginPanel=new LoginPane(root, card);

    // MainPane과 TravelSearchPane은 순서가 중요합니다.
    MainPane mainPanel; // 일정 관리 화면 (MainPane)
    TravelSearchPane travelSearchPanel; // 여행지 검색 화면 (새로 추가)
    JPanel searchPanel, schedulePanel;

    void mymain(){
        // 1. MainPane을 먼저 인스턴스화합니다.
        mainPanel = new MainPane(root, card);
        // 2. TravelSearchPane을 인스턴스화할 때, mainPanel의 참조를 넘겨줍니다.
        travelSearchPanel = new TravelSearchPane(root, card, mainPanel);

        tdm.printAllLocation();
        tdm.printAllUser();
        createAndShowGUI();
    }

    void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.getContentPane().add(root);
        frame.getContentPane().setBackground(new Color(220, 235, 255));
        root.setOpaque(false);

        // CardLayout에 모든 패널을 등록합니다.
        root.add(loginPanel.setupLoginPanel(), "로그인");
        root.add(mainPanel.setupMainPanel(), "메인");
        root.add(travelSearchPanel.setupMainPanel(), "여행지 검색"); // "여행지 검색" 키로 등록

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