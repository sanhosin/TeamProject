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
import java.util.Collections;
import java.awt.image.BufferedImage;
import java.awt.Container;

public class MainPane extends JPanel {

    JPanel root;
    CardLayout card;

    private final String CUSTOM_FONT_NAME = "맑은 고딕";

    private final Color BG_COLOR = new Color(240, 247, 255);
    private final Color CARD_COLOR = new Color(255, 255, 255);
    private final Color BORDER_COLOR = new Color(200, 200, 200);
    private final Color TITLE_COLOR = new Color(40, 60, 90);
    private final Color BLUE_ACCENT = new Color(80, 140, 255);

    public static class ScheduleItem {
        String title;
        String date;
        String time;
        String location;
        String description;
        Color color;

        public ScheduleItem(String title, String date, String time, String location, String description, Color color) {
            this.title = title;
            this.date = date;
            this.time = time;
            this.location = location;
            this.description = description;
            this.color = color;
        }
    }

    private List<DestinationItem> tripList = new ArrayList<>();
    private JPanel listContainer;
    private JScrollPane scrollPane;
    private JPanel scheduleCardContainer;
    private JPanel scheduleEmptyMessagePanel;
    private JPanel scheduleListAreaCard;


    public MainPane(JPanel root, CardLayout card) {
        this.root = root;
        this.card = card;
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // DestinationItem 초기화 코드
        tripList.add(new DestinationItem("파리 여행", "프랑스, 파리", "로맨틱 여행", 4.8, "paris.jpg", "2025.11.15", "2025.11.20", new String[]{"로맨틱", "문화"}));
        tripList.add(new DestinationItem("도쿄 여행", "일본, 도쿄", "미식과 문화", 4.9, "tokyo.jpg", "2025.12.01", "2025.12.07", new String[]{"미식", "문화"}));
        tripList.add(new DestinationItem("서울 여행", "대한민국, 서울", "야경 투어", 4.7, "seoul.jpg", "2025.12.20", "2025.12.25", new String[]{"야경", "도시"}));
        tripList.add(new DestinationItem("윈도우", "위치 미정", "여행 기간 미정", 0.0, "windows.jpg", "여행 기간 미정", "", new String[]{"미정"}));

        add(setupMainPanel(), BorderLayout.CENTER);
    }

    public JPanel setupMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel header = buildHeader();
        header.setBackground(Color.WHITE);

        JPanel center = buildTripListArea();

        panel.add(header, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    public void addTrip(DestinationItem item) {
        tripList.add(item);
        refreshTripListUI();
    }

    // 👈 오류 발생 메소드: 인자는 받으나, 내부에서 refreshTripListUI()를 호출하도록 수정합니다.
    public void addScheduleItem(ScheduleItem item) {
        // TestDataManage에서 이미 데이터 저장을 담당한다고 가정하고, 여기서는 화면 갱신만 요청합니다.
        // 현재 MainPane은 TripList를 보여주므로, TripList 갱신을 요청합니다.
        refreshTripListUI(); // 👈 refreshScheduleListUI 대신 refreshTripListUI를 호출합니다.
    }

    private void refreshTripListUI() {
        listContainer.removeAll();
        for (DestinationItem item : tripList) {
            listContainer.add(buildTripCard(item));
            listContainer.add(Box.createVerticalStrut(15));
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 10, 15, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JLabel menuIcon = new JLabel("☰");
        menuIcon.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 26));
        menuIcon.setForeground(TITLE_COLOR);

        menuIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 사이드바 토글 로직
            }
        });

        JLabel title = new JLabel("여행가요");
        title.setForeground(TITLE_COLOR);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 26));

        leftPanel.add(menuIcon);
        leftPanel.add(title);

        JButton addBtn = new JButton("여행추가");
        addBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        addBtn.setFocusPainted(false);
        addBtn.setBackground(BLUE_ACCENT);
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addBtn.setPreferredSize(new Dimension(150, 40));

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(MainPane.this);
                AddTripDialog dialog = new AddTripDialog(parentFrame, CUSTOM_FONT_NAME, MainPane.this);
                dialog.setVisible(true);
            }
        });


        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(addBtn, BorderLayout.EAST);

        return panel;
    }

    private JPanel buildTripListArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel listTitle = new JLabel("✈ 나의 여행");
        listTitle.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 20));
        listTitle.setForeground(TITLE_COLOR);
        listTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        listContainer = new JPanel();
        listContainer.setOpaque(false);
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        refreshTripListUI();

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(listTitle);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel buildTripCard(DestinationItem item) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setPreferredSize(new Dimension(800, 100));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainPane.this.card.show(root, "여행지 검색");
            }
        });

        // 1. 좌측 이미지 영역
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(100, 80));
        imagePanel.setBackground(new Color(220, 220, 220));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(100, 80));

        imagePanel.add(imageLabel, BorderLayout.CENTER);

        card.add(imagePanel, BorderLayout.WEST);

        // 2. 중앙 텍스트 영역
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(item.name);
        nameLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 16));
        nameLabel.setForeground(TITLE_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel locationLabel = new JLabel("📍 " + item.country);
        locationLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
        locationLabel.setForeground(new Color(90, 90, 110));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String dateRange = item.endDate.isEmpty() || item.startDate.equals(item.endDate) ? item.startDate : item.startDate + " - " + item.endDate;
        JLabel dateLabel = new JLabel(dateRange);
        dateLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
        dateLabel.setForeground(new Color(90, 90, 110));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(locationLabel);
        textPanel.add(dateLabel);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private ImageIcon loadImage(String imagePath, int width, int height) {
        return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
    }
}