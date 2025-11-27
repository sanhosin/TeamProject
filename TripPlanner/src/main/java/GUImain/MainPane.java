package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.*;

import main.java.domain.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MainPane extends JPanel {

    JPanel root;
    CardLayout card;

    private final String CUSTOM_FONT_NAME = "맑은 고딕";

    private final Color BG_COLOR = new Color(240, 247, 255);
    private final Color CARD_COLOR = new Color(255, 255, 255);
    private final Color BORDER_COLOR = new Color(200, 200, 200);
    private final Color TITLE_COLOR = new Color(40, 60, 90);
    private final Color BLUE_ACCENT = new Color(80, 140, 255);

    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel sidebar;
    private boolean isSidebarVisible = false;
    private final int SLIDE_WIDTH = 180;
    private final int ANIMATION_STEPS = 20;
    private final int ANIMATION_DELAY_MS = 5;

    private JPanel listContainer;
    private TravelSearchPane travelSearchPanelRef;

    public MainPane(JPanel root, CardLayout card) {
        this.root = root;
        this.card = card;
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        mainPanel = setupMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    public void setTravelSearchPanel(TravelSearchPane travelSearchPanel) {
        this.travelSearchPanelRef = travelSearchPanel;
    }

    public JPanel setupMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel header = buildHeader();
        header.setBackground(Color.WHITE);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        sidebar = buildSidebar();
        sidebar.setPreferredSize(new Dimension(0, 0));

        JPanel center = buildTripListArea();

        contentPanel.add(sidebar, BorderLayout.WEST);
        contentPanel.add(center, BorderLayout.CENTER);

        panel.add(header, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private void toggleSidebar() {
        final int targetWidth = isSidebarVisible ? 0 : SLIDE_WIDTH;
        final int startWidth = sidebar.getPreferredSize().width;
        final int delta = (targetWidth - startWidth) / ANIMATION_STEPS;

        Timer timer = new Timer(ANIMATION_DELAY_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentWidth = sidebar.getPreferredSize().width;
                int newWidth = currentWidth + delta;

                boolean shouldStop = (delta > 0 && newWidth >= targetWidth) || (delta < 0 && newWidth <= targetWidth);

                if (shouldStop) {
                    newWidth = targetWidth;
                    ((Timer) e.getSource()).stop();
                    isSidebarVisible = !isSidebarVisible;
                }

                sidebar.setPreferredSize(new Dimension(newWidth, sidebar.getHeight()));
                contentPanel.revalidate();
            }
        });

        if (timer.isRunning()) {
            timer.stop();
        }
        timer.start();
    }

    private JPanel buildSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 20, 50, 20));

        panel.setOpaque(true);
        panel.setBackground(CARD_COLOR);

        Font sideFont = new Font(CUSTOM_FONT_NAME, Font.BOLD, 14);

        panel.add(sidebarItem(Main.tdm.rootPath+"gachi.png", "친구 관리", sideFont));
        panel.add(Box.createVerticalStrut(10));
        panel.add(sidebarItem(Main.tdm.rootPath+"honja.png", "유저 정보", sideFont));

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // >>수정 : 아미지 삽입
    private JPanel sidebarItem(String icon, String text, Font font) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        itemPanel.setOpaque(false);

        JPanel iconBox = new JPanel(new GridBagLayout());
        iconBox.setPreferredSize(new Dimension(24, 24));
        iconBox.setOpaque(false);

        ImageIcon raw = new ImageIcon(icon); // ← 첫 번째 파라미터를 이미지 경로로 사용

        iconBox.add(new JLabel(new ImageIcon(raw.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH))));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(font);
        textLabel.setForeground(TITLE_COLOR);

        itemPanel.add(iconBox);
        itemPanel.add(textLabel);

        return itemPanel;
    }

    public void refreshTripListUI() {
        if (listContainer == null)
            return;

        listContainer.removeAll();

        List<Trip> tripList = Main.tdm.getTripList();

        if (tripList != null) {
            for (Trip t : tripList) {
                listContainer.add(buildTripCard(t));
                // 카드 사이 간격 15픽셀
                listContainer.add(Box.createVerticalStrut(15));
            }
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 10, 15, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JPanel searchBar = buildSearchBar();

        // 수정>> 이미지삽입
        JPanel menuBox = new JPanel(new GridBagLayout()); // 가운데 정렬용
        menuBox.setPreferredSize(new Dimension(40, 40)); // 직사각형 크기
        menuBox.setOpaque(false); // 배경 투명

        ImageIcon headerRaw = new ImageIcon(Main.tdm.rootPath+"menu.png");
        Image headerImg = headerRaw.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel menuIcon = new JLabel(new ImageIcon(headerImg));

        menuBox.add(menuIcon);

        menuBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSidebar();
                menuBox.add(menuIcon);
            }
        });

        JLabel title = new JLabel("여행가요");
        title.setForeground(TITLE_COLOR);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 26));

        leftPanel.add(menuBox);
        leftPanel.add(title);

        JButton addBtn = new JButton("여행추가");
        addBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        addBtn.setFocusPainted(false);
        addBtn.setBackground(BLUE_ACCENT);
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addBtn.setPreferredSize(new Dimension(150, 40));

        addBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            AddTripDialog dialog = new AddTripDialog(parentFrame, CUSTOM_FONT_NAME, this);
            dialog.setVisible(true);
        });

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(searchBar, BorderLayout.CENTER);
        panel.add(addBtn, BorderLayout.EAST);

        return panel;
    }

    private JPanel buildTripListArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // 수정>> 나의여행
        ImageIcon rawIcon2 = new ImageIcon(Main.tdm.rootPath+"dal1.png");
        Image img2 = rawIcon2.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        ImageIcon icon2 = new ImageIcon(img2);

        JLabel myTripTitle = new JLabel("나의 여행"); // ← 앞에 공백만 남기기
        myTripTitle.setIcon(icon2);
        myTripTitle.setIconTextGap(10);
        myTripTitle.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 28));
        myTripTitle.setForeground(new Color(40, 60, 90));

        listContainer = new JPanel();
        listContainer.setOpaque(false);
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(myTripTitle);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel buildTripCard(Trip t) {
        JPanel card = new JPanel(new BorderLayout(10, 0));

        final int CARD_HEIGHT = 120;
        final int IMAGE_WIDTH = 200;
        final int IMAGE_HEIGHT = 150;
        final int CARD_INNER_MARGIN = 12;

        // 최대 크기 설정으로 늘어남 방지 (세로 고정)
        card.setPreferredSize(new Dimension(0, CARD_HEIGHT));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, CARD_HEIGHT));

        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(CARD_INNER_MARGIN, 10, CARD_INNER_MARGIN, 10)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Main.tdm.setCurrentTrip(t);
                if (travelSearchPanelRef != null) {
                    travelSearchPanelRef.setTravelSearchPaneTitle();
                    travelSearchPanelRef.refreshPlanListUI();
                }
                MainPane.this.card.show(root, "여행지 검색");
            }
        });

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        imagePanel.setBackground(new Color(220, 220, 220));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

        Object[] uidata = t.getUidata();
        String tripTitle = (String) uidata[Trip.TRIP_TITLE];

        String tripImgPath = t.getTripImagePath();

        // 🔹 imageLabel 에 아이콘 세팅
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        if (tripImgPath != null && !tripImgPath.isEmpty() && !tripImgPath.equals("default.jpg")) {
            try {
                ImageIcon rawIcon = new ImageIcon(tripImgPath);
                Image scaledImg = rawIcon.getImage().getScaledInstance(
                        IMAGE_WIDTH,
                        IMAGE_HEIGHT,
                        Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
            } catch (Exception ex) {
                System.err.println("Error loading image from path: " + tripImgPath);
            }
        } else {
            ImageIcon rawIcon = new ImageIcon(Main.tdm.rootPath + "default.png");
            Image scaledImg = rawIcon.getImage().getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImg));
        }

        // 🔹 회색 박스(imagePanel) 가운데에 imageLabel 올리기
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        card.add(imagePanel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        textPanel.setBorder(new EmptyBorder(0, 5, 0, 5));

        JLabel nameLabel = new JLabel(tripTitle);
        nameLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        nameLabel.setForeground(TITLE_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String toLocationLabel = (t.getPlanList().isEmpty()) ? "미정" : t.getLocationDetail();
        // 수정>> 이미지 삽입 대신
        ImageIcon pinRaw = new ImageIcon("TripPlanner/src/main/resources/images/wich.png");
        Image pinScaled = pinRaw.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        ImageIcon pinIcon = new ImageIcon(pinScaled);

        JLabel locationLabel = new JLabel(" " + toLocationLabel); // 텍스트만
        locationLabel.setIcon(pinIcon);
        locationLabel.setIconTextGap(2); // 이미지와 텍스트 간격 조절

        locationLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 10));
        locationLabel.setForeground(new Color(90, 90, 110));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String toDateLabel = (t.getPlanList().isEmpty()) ? "미정" : t.getDateDuration();
        JLabel dateLabel = new JLabel(toDateLabel);
        dateLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 10));
        dateLabel.setForeground(new Color(90, 90, 110));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(locationLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(dateLabel);

        card.add(textPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controlPanel.setOpaque(false);

        JButton deleteBtn = new JButton("🗑");
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    MainPane.this.root.getParent(),
                    tripTitle + " 여행을 정말 삭제하시겠습니까?",
                    "여행 삭제 확인",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                Main.tdm.deleteTrip(t);
                refreshTripListUI();
            }

        });

        controlPanel.add(deleteBtn);
        card.add(controlPanel, BorderLayout.EAST);

        return card;
    }

    private JPanel buildSearchBar() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setMaximumSize(new Dimension(300, 40)); // 검색창 최대 너비 제한

        JTextField searchField = new JTextField("검색어를 입력하세요");
        searchField.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        searchField.setForeground(new Color(150, 150, 150)); // Placeholder 색상
        searchField.setBackground(new Color(240, 240, 240));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));

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

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchIcon, BorderLayout.EAST);

        return searchPanel;
    }

    private ImageIcon loadImage(String imagePath, int width, int height) {
        return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
    }
}