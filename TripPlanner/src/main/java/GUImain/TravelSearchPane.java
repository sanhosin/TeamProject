package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import main.java.domain.*;

public class TravelSearchPane extends JPanel {

    JPanel root;
    CardLayout card;
    private MainPane mainPaneRef;

    private final String CUSTOM_FONT_NAME = "맑은 고딕";

    private final Color BG_COLOR = new Color(240, 247, 255);
    private final Color CARD_COLOR = new Color(255, 255, 255);
    private final Color BORDER_COLOR = new Color(200, 200, 200);
    private final Color TITLE_COLOR = new Color(40, 60, 90);
    private final Color BLUE_ACCENT = new Color(80, 140, 255);
    private final Color DOT_COLOR = new Color(255, 60, 60);

    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel sidebar;
    private boolean isSidebarVisible = false;
    private final int SLIDE_WIDTH = 180;
    private final int ANIMATION_STEPS = 20;
    private final int ANIMATION_DELAY_MS = 20;

    private ArrayList<TripPlan> planListToShow=null;

    JPanel listContainer;
    JLabel tripTitle;

    public TravelSearchPane(JPanel root, CardLayout card, MainPane mainPaneRef) {
        this.root = root;
        this.card = card;
        this.mainPaneRef = mainPaneRef;
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        mainPanel = setupMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    public MainPane getMainPaneRef() {
        return this.mainPaneRef;
    }

    public void refreshPlanListUI() {
        listContainer.removeAll();

        if(planListToShow==null){
            planListToShow = (ArrayList<TripPlan>) Main.tdm.getPlanList();
        }

        // 날짜 기준 정렬 (없는 날짜는 맨 뒤)
        for (int i = 0; i < planListToShow.size() - 1; i++) {
            for (int j = 0; j < planListToShow.size() - 1 - i; j++) {
                String s1 = planListToShow.get(j).getDateDetail();
                String s2 = planListToShow.get(j + 1).getDateDetail();

                long date1 = s1.contains("?") ? Long.MAX_VALUE : Long.parseLong(s1);
                long date2 = s2.contains("?") ? Long.MAX_VALUE : Long.parseLong(s2);

                if (date1 > date2) {
                    TripPlan temp = planListToShow.get(j);
                    planListToShow.set(j, planListToShow.get(j + 1));
                    planListToShow.set(j + 1, temp);
                }
            }
        }

        for (TripPlan tp : planListToShow) {
            String[] uidata = tp.getUidata();
            listContainer.add(buildScheduleCard(tp, uidata));
            listContainer.add(Box.createVerticalStrut(15));
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    public void setTravelSearchPaneTitle() {
        tripTitle.setText(Main.tdm.getCurrentTripTitle());
    }

    public JPanel setupMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel header = buildHeader();
        header.setBackground(Color.WHITE);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);


        JPanel center = buildScheduleDetailArea();

        contentPanel.add(center, BorderLayout.CENTER);

        panel.add(header, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 10, 15, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JPanel searchBar = buildSearchBar();
        
        JLabel backIcon = new JLabel("<-"); // 텍스트 제거해서 사각형 공간 제거

        backIcon.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 26));
        backIcon.setForeground(TITLE_COLOR);
        backIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // ⭐ 수정: null을 Trip 타입으로 명시적 캐스팅하여 모호성 제거 (NPE 방지)
                Main.tdm.setCurrentTrip((main.java.domain.Trip) null);

                card.show(root, "메인");
                mainPaneRef.refreshTripListUI();
            }
        });

        JLabel title = new JLabel("여행가요");
        title.setForeground(TITLE_COLOR);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 26));
        ImageIcon raw = new ImageIcon(Main.tdm.rootPath+"logo.png");
        Image img = raw.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        title.setIcon(new ImageIcon(img));
        title.setIconTextGap(10);

        leftPanel.add(backIcon);
        leftPanel.add(title);

        JButton addScheduleBtn = new JButton("+ 일정 추가");
        addScheduleBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        addScheduleBtn.setFocusPainted(false);
        addScheduleBtn.setBackground(BLUE_ACCENT);
        addScheduleBtn.setForeground(Color.WHITE);
        addScheduleBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addScheduleBtn.setPreferredSize(new Dimension(150, 40));

        addScheduleBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ScheduleAddDialog dialog = new ScheduleAddDialog(parentFrame, CUSTOM_FONT_NAME, this);
            dialog.setVisible(true);
        });

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(searchBar, BorderLayout.CENTER);
        panel.add(addScheduleBtn, BorderLayout.EAST);

        return panel;
    }

    private JPanel buildScheduleDetailArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        tripTitle = new JLabel();
        tripTitle.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 28));
        tripTitle.setForeground(TITLE_COLOR);
        tripTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        listContainer = new JPanel();
        listContainer.setOpaque(false);
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        listContainer.setBorder(new EmptyBorder(0, 0, 0, 0));

        if (Main.tdm.getLoginedUser() != null)
            refreshPlanListUI();

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(tripTitle);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scrollPane);

        return panel;
    }

    private static final int PLAN_COLOR = 5;
    private static final int PLAN_TITLE = 0;
    private static final int PLAN_DATE = 1;
    private static final int PLAN_TIME = 2;
    private static final int PLAN_LOC = 3;
    private static final int PLAN_REVIEW = 4;

    private JPanel buildScheduleCard(TripPlan tp, String[] uidata) {

        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(CARD_COLOR);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        // uidata의 타입을 안전하게 확인하고 사용
        Color planColor = (uidata.length > PLAN_COLOR && uidata[PLAN_COLOR] instanceof String)
                ? stringToColor(uidata[PLAN_COLOR])
                : new Color(150, 150, 150);
        String planTitle = (uidata.length > PLAN_TITLE && uidata[PLAN_TITLE] instanceof String)
                ? uidata[PLAN_TITLE]
                : "제목 미정";
        String planDate = (uidata.length > PLAN_DATE && uidata[PLAN_DATE] instanceof String)
                ?  uidata[PLAN_DATE]
                : "";
        String planTime = (uidata.length > PLAN_TIME && uidata[PLAN_TIME] instanceof String)
                ?  uidata[PLAN_TIME]
                : "00:00";
        String planLoc = (uidata.length > PLAN_LOC && uidata[PLAN_LOC] instanceof String) ?  uidata[PLAN_LOC]
                : "위치 미정";
        String planReview = (uidata.length > PLAN_REVIEW && uidata[PLAN_REVIEW] instanceof String)
                ?  uidata[PLAN_REVIEW]
                : "설명 없음";

        JLabel dot = new JLabel("●");
        dot.setForeground(planColor);
        dot.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 10));

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);

        JLabel t = new JLabel(planTitle);
        t.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 16));
        t.setForeground(TITLE_COLOR);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 🔹 수정>>시간 아이콘 이미지 삽입
        ImageIcon timeRaw = new ImageIcon(Main.tdm.rootPath+"dal2.png");
        Image timeImg = timeRaw.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        ImageIcon timeIcon = new ImageIcon(timeImg);

        // 🔹 위치 아이콘 이미지 삽입
        ImageIcon locRaw = new ImageIcon(Main.tdm.rootPath+"wich.png");
        Image locImg = locRaw.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        ImageIcon locIcon = new ImageIcon(locImg);

        // 🔹 라벨 하나만 사용해서, 아이콘 + 글자 조합
        // 🔹 날짜/시간 라벨
        JLabel dateLabel = new JLabel(planDate + "  " + planTime);
        dateLabel.setIcon(timeIcon); // 달력(또는 시간) 이미지
        dateLabel.setIconTextGap(4);
        dateLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 13));
        dateLabel.setForeground(new Color(90, 90, 110));

        // 🔹 위치 라벨
        JLabel locLabel = new JLabel("  " + planLoc); // 앞에 공백 하나
        locLabel.setIcon(locIcon); // 위치 이미지
        locLabel.setIconTextGap(4);
        locLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 13));
        locLabel.setForeground(new Color(90, 90, 110));

        // 🔹 둘을 한 줄에 넣을 패널
        JPanel dtPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dtPanel.setOpaque(false);
        dtPanel.add(dateLabel);
        dtPanel.add(Box.createHorizontalStrut(12)); // 날짜/위치 사이 간격
        dtPanel.add(locLabel);
        dtPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel d = new JLabel(planReview);
        d.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 13));
        d.setForeground(new Color(100, 100, 100));
        d.setBorder(new EmptyBorder(5, 0, 0, 0));
        d.setAlignmentX(Component.LEFT_ALIGNMENT);

        inner.add(t);
        inner.add(Box.createVerticalStrut(6));
        inner.add(dtPanel); // ✅ 새로 만든 패널
        inner.add(d);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btnPanel.setOpaque(false);

        JButton editBtn = new JButton("✏");
        editBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ScheduleAddDialog dialog = new ScheduleAddDialog(parentFrame, CUSTOM_FONT_NAME, this, tp);
            dialog.setVisible(true);
        });
        JButton deleteBtn = new JButton("🗑");
        deleteBtn.addActionListener(e -> {
            Main.tdm.deletePlan(tp);
            refreshPlanListUI();
        });

        editBtn.setFocusPainted(false);
        editBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);

        card.add(dot, BorderLayout.WEST);
        card.add(inner, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.EAST);

        return card;
    }

    private JPanel buildSearchBar() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setMaximumSize(new Dimension(300, 40));

        JTextField searchField = new JTextField("검색어를 입력하세요");
        searchField.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        searchField.setForeground(new Color(150, 150, 150));
        searchField.setBackground(new Color(240, 240, 240));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));

        // Placeholder 기능
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("검색어를 입력하세요")) {
                    searchField.setText("");
                    searchField.setForeground(TITLE_COLOR);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("검색어를 입력하세요");
                    searchField.setForeground(new Color(150, 150, 150));
                }
            }
        });

        // 돋보기 아이콘 추가
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 16));
        searchIcon.setBorder(new EmptyBorder(0, 5, 0, 5));
        searchIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(searchField.getText().isEmpty()){
                    planListToShow=null;
                }
                else{
                    planListToShow=(ArrayList<TripPlan>)(Main.tdm.findAllPlans(searchField.getText()));
                }
                refreshPlanListUI();
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchIcon, BorderLayout.EAST);

        return searchPanel;
    }
    private Color stringToColor(String colorRGB){
        String[] rgb=colorRGB.split(",");
        int red=Integer.parseInt(rgb[0]);
        int green=Integer.parseInt(rgb[1]);
        int blue=Integer.parseInt(rgb[2]);
        return new Color(red,green,blue);
    }
}