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
import java.awt.image.BufferedImage;


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
    private final int ANIMATION_DELAY_MS = 20;

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

    public static class DestinationItem {
        String name;
        String country;
        String description;
        double rating;
        String imagePath;
        String startDate;
        String endDate;
        String[] tags;

        public DestinationItem(String name, String country, String description, double rating, String imagePath, String startDate, String endDate, String[] tags) {
            this.name = name;
            this.country = country;
            this.description = description;
            this.rating = rating;
            this.imagePath = imagePath;
            this.startDate = startDate;
            this.endDate = endDate;
            this.tags = tags;
        }
    }

    private List<DestinationItem> tripList = new ArrayList<>();
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
                    ((Timer)e.getSource()).stop();
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

        panel.add(sidebarItem("👤", "친구 관리", sideFont));
        panel.add(Box.createVerticalStrut(10));
        panel.add(sidebarItem("🗓", "일정 관리", sideFont));
        panel.add(Box.createVerticalStrut(10));
        panel.add(sidebarItem("⚙", "유저 정보", sideFont));

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel sidebarItem(String icon, String text, Font font) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        itemPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 18));
        iconLabel.setForeground(TITLE_COLOR);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(font);
        textLabel.setForeground(TITLE_COLOR);

        itemPanel.add(iconLabel);
        itemPanel.add(textLabel);

        return itemPanel;
    }


    public void addTrip(DestinationItem item) {
        tripList.add(item);
        refreshTripListUI();
    }

    public void addScheduleItem(ScheduleItem item) {
        refreshTripListUI();
    }

    public void refreshTripListUI() {
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
                toggleSidebar();
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

        addBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            AddTripDialog dialog = new AddTripDialog(parentFrame, CUSTOM_FONT_NAME, this);
            dialog.setVisible(true);
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

        final int CARD_HEIGHT = 150;
        final int IMAGE_WIDTH = 200;
        final int IMAGE_HEIGHT = 40;
        final int CARD_INNER_MARGIN = 12;

        // 최대 크기 설정으로 늘어남 방지 (세로 고정)
        card.setPreferredSize(new Dimension(1000, CARD_HEIGHT));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, CARD_HEIGHT));

        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(CARD_INNER_MARGIN, 10, CARD_INNER_MARGIN, 10)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                Main.tdm.setCurrentTrip(item);

                MainPane.this.card.show(root, "여행지 검색");

                if (travelSearchPanelRef != null) {
                    travelSearchPanelRef.setTravelSearchPaneTitle();
                    travelSearchPanelRef.refreshPlanListUI();
                }
            }
        });

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        imagePanel.setBackground(new Color(220, 220, 220));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

        if (item.imagePath != null && !item.imagePath.equals("default.jpg")) {
            try {
                ImageIcon originalIcon = new ImageIcon(item.imagePath);
                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } catch (Exception ex) {
                // 이미지 로드 실패 시 빈 상태 유지
            }
        }


        imagePanel.add(imageLabel, BorderLayout.CENTER);

        card.add(imagePanel, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        textPanel.setBorder(new EmptyBorder(0, 5, 0, 5));

        JLabel nameLabel = new JLabel(item.name);
        nameLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        nameLabel.setForeground(TITLE_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel locationLabel = new JLabel("📍 " + item.country);
        locationLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 10));
        locationLabel.setForeground(new Color(90, 90, 110));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String dateRange = item.endDate.isEmpty() || item.startDate.equals(item.endDate) ? item.startDate : item.startDate + " - " + item.endDate;
        JLabel dateLabel = new JLabel(dateRange);
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
                    item.name + " 여행을 정말 삭제하시겠습니까?",
                    "여행 삭제 확인",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                tripList.remove(item);
                refreshTripListUI();
            }
        });

        controlPanel.add(deleteBtn);
        card.add(controlPanel, BorderLayout.EAST);

        return card;
    }

    private ImageIcon loadImage(String imagePath, int width, int height) {
        return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
    }
}