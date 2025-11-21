package main.java.GUImain;

import java.awt.*;
import java.util.Scanner;
import javax.swing.*;
import main.java.domain.TestDataManage;
// Manager와 domain 클래스는 현재 GUImain.Main에서 직접 사용되지 않아 import 제거 (clean code)

public class Main {
    // TestDataManage는 static 필드로 선언되어 초기화 시 파일 로드를 시도합니다.
    public static TestDataManage tdm = new TestDataManage();

    Scanner scan = new Scanner(System.in);

    JFrame frame = new JFrame("여행가요");
    // CardLayout과 JPanel은 Swing GUI의 기본 구성 요소입니다. [cite: 623, 624]
    public static CardLayout card = new CardLayout();
    public static JPanel root = new JPanel(card); // 모든 화면을 담는 컨테이너

    LoginPane loginPanel = new LoginPane(root, card);

    // MainPane과 TravelSearchPane은 순서가 중요합니다.
    MainPane mainPanel; // 일정 관리 화면 (MainPane)
    TravelSearchPane travelSearchPanel; // 여행지 검색 화면 (새로 추가)
    JoinPane joinPanel; // 회원가입 화면

    // JPanel searchPanel, schedulePanel; // 사용되지 않아 제거

    void mymain(){
        // 1. MainPane을 먼저 인스턴스화합니다.
        mainPanel = new MainPane(root, card);
        // 2. TravelSearchPane을 인스턴스화할 때, mainPanel의 참조를 넘겨줍니다.
        travelSearchPanel = new TravelSearchPane(root, card, mainPanel);
        // 3. JoinPane 인스턴스 생성
        joinPanel = new JoinPane(root, card);
        //TravelSearchPane을 여행별로 다르게 나타내기 위해 참조를 넘겨줍니다.
        mainPanel.setTravelSearchPanel(travelSearchPanel);
       
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
        root.add(joinPanel.setupJoinPanel(), "회원가입"); // 회원가입 화면 추가
        root.add(mainPanel.setupMainPanel(), "메인");
        root.add(travelSearchPanel.setupMainPanel(), "여행지 검색"); // "여행지 검색" 키로 등록

        card.show(root, "로그인"); // 초기 화면을 '로그인'으로 설정

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); // 프레임을 보이게 합니다. [cite: 1664, 1720]
    }

    public static void main(String[] args) {
        // System.out.println(new File(".").getAbsolutePath()); // 경로 확인용 코드 제거

        Main main = new Main();
        main.mymain();
        main.scan.close();
    }
}