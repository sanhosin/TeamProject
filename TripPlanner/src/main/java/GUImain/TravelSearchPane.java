package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TravelSearchPane extends JPanel {

    JPanel root;
    CardLayout card;
    private MainPane mainPaneRef;

    private final String CUSTOM_FONT_NAME = "맑은 고딕";

    private JPanel mainPanel;
    private JPanel sidebar;

    private boolean isSidebarVisible = false;
    private final int SLIDE_WIDTH = 180;
    private final int ANIMATION_STEPS = 20;
    private final int ANIMATION_DELAY_MS = 20;

    private final Color BG_COLOR = new Color(240, 247, 255);
    private final Color CARD_COLOR = new Color(255, 255, 255);
    private final Color BORDER_COLOR = new Color(200, 200, 200);
    private final Color TITLE_COLOR = new Color(40, 60, 90);
    private final Color BLUE_ACCENT = new Color(80, 140, 255);
    private final Color TAG_BG_COLOR = new Color(230, 230, 250);

    public static class DestinationItem {
        String name;
        String country;
        String description;
        double rating;
        String imagePath;
        String[] tags;

        public DestinationItem(String name, String country, String description, double rating, String imagePath, String[] tags) {
            this.name = name;
            this.country = country;
            this.description = description;
            this.rating = rating;
            this.imagePath = imagePath;
            this.tags = tags;
        }
    }

    private List<DestinationItem> userAddedDestinations = new ArrayList<>();
    private JPanel listContainer;
    private JScrollPane scrollPane;

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

    public JPanel setupMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel header = buildHeader();
        header.setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        sidebar = buildSidebar();
        sidebar.setPreferredSize(new Dimension(0, 0));

        JPanel center = buildSearchListArea();

        panel.add(header, BorderLayout.NORTH);
        contentPanel.add(sidebar, BorderLayout.WEST);
        contentPanel.add(center, BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    public void addDestination(DestinationItem item) {
        userAddedDestinations.add(item);
        refreshDestinationListUI();
    }

    private void refreshDestinationListUI() {
        listContainer.removeAll();

        if (userAddedDestinations.isEmpty()) {
            listContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 100));
            listContainer.add(buildEmptyDestinationMessage());
        } else {
            listContainer.setLayout(new GridLayout(0, 3, 20, 20));
            for (DestinationItem item : userAddedDestinations) {
                listContainer.add(buildDestinationCard(item));
            }
        }

        listContainer.revalidate();
        listContainer.repaint();
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    private void toggleSidebar() {
        final int startWidth = isSidebarVisible ? SLIDE_WIDTH : 0;
        final int endWidth = isSidebarVisible ? 0 : SLIDE_WIDTH;
        final int delta = (endWidth - startWidth) / ANIMATION_STEPS;

        Timer timer = new Timer(ANIMATION_DELAY_MS, null);

        timer.addActionListener(e -> {
            int currentWidth = sidebar.getPreferredSize().width;
            int newWidth = currentWidth + delta;

            if ((delta > 0 && newWidth >= endWidth) || (delta < 0 && newWidth <= endWidth)) {
                newWidth = endWidth;
                ((Timer)e.getSource()).stop();
                isSidebarVisible = !isSidebarVisible;
            }

            sidebar.setPreferredSize(new Dimension(newWidth, sidebar.getHeight()));
            sidebar.revalidate();
            mainPanel.repaint();
        });

        if (timer.isRunning()) {
            timer.stop();
        }
        timer.start();
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

        JButton addBtn = new JButton("여행지 추가하기");
        addBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        addBtn.setFocusPainted(false);
        addBtn.setBackground(BLUE_ACCENT);
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addBtn.setPreferredSize(new Dimension(150, 40));

        addBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(TravelSearchPane.this);
            DestinationAddDialog dialog = new DestinationAddDialog(parentFrame, CUSTOM_FONT_NAME, TravelSearchPane.this);
            dialog.setVisible(true);
        });


        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(addBtn, BorderLayout.EAST);

        return panel;
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

    private JPanel buildSearchListArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel searchBarPanel = buildSearchBar();
        searchBarPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchBarPanel.setMaximumSize(new Dimension(800, 40));

        JLabel title = new JLabel("내가 추가한 여행지를 찾아봐요!");
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 22));
        title.setForeground(TITLE_COLOR);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        listContainer = new JPanel();
        listContainer.setOpaque(false);
        listContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        refreshDestinationListUI();

        panel.add(searchBarPanel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel buildSearchBar() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JTextField searchField = new JTextField("여행지를 검색하세요...");
        searchField.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 16));
        searchField.setBackground(CARD_COLOR);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 16));

        panel.add(searchIcon, BorderLayout.WEST);
        panel.add(searchField, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildDestinationCard(DestinationItem item) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setPreferredSize(new Dimension(250, 300));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(TravelSearchPane.this);
                DestinationDetailDialog dialog = new DestinationDetailDialog(parentFrame, CUSTOM_FONT_NAME, TravelSearchPane.this, item);
                dialog.setVisible(true);
            }
        });

        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(220, 150));
        imagePanel.setBackground(new Color(220, 220, 220));
        imagePanel.setLayout(new BorderLayout());

        ImageIcon icon = loadImage(item.imagePath, imagePanel.getPreferredSize().width, imagePanel.getPreferredSize().height);
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JLabel ratingLabel = new JLabel("⭐ " + String.format("%.1f", item.rating), SwingConstants.CENTER);
        ratingLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        ratingLabel.setForeground(Color.WHITE);
        ratingLabel.setBackground(new Color(0, 0, 0, 150));
        ratingLabel.setOpaque(true);
        ratingLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JPanel ratingWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        ratingWrapper.setOpaque(false);
        ratingWrapper.add(ratingLabel);
        imagePanel.add(ratingWrapper, BorderLayout.NORTH);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JLabel nameLabel = new JLabel(item.name);
        nameLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 18));
        nameLabel.setForeground(TITLE_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel countryLabel = new JLabel("📍 " + item.country);
        countryLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 13));
        countryLabel.setForeground(new Color(90, 90, 110));
        countryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // String.format()을 사용하여 호환성 문제를 해결합니다.
        JLabel descLabel = new JLabel(String.format("<html><body style='width: %dpx;'>%s</body></html>", 220, item.description));
        descLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        tagPanel.setOpaque(false);
        tagPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String tag : item.tags) {
            tagPanel.add(buildTag(tag));
        }

        textPanel.add(nameLabel);
        textPanel.add(countryLabel);
        textPanel.add(descLabel);
        textPanel.add(tagPanel);

        card.add(imagePanel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private ImageIcon loadImage(String imagePath, int width, int height) {
        ImageIcon imageIcon = null;
        try {
            java.net.URL imgURL = getClass().getClassLoader().getResource("res/images/" + imagePath);
            if (imgURL != null) {
                imageIcon = new ImageIcon(imgURL);
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imagePath + " - " + e.getMessage());
        }
        return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
    }

    private JLabel buildTag(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
        label.setForeground(BLUE_ACCENT);
        label.setBackground(TAG_BG_COLOR);
        label.setOpaque(true);
        label.setBorder(new EmptyBorder(3, 8, 3, 8));
        label.putClientProperty("flat-border", true);
        return label;
    }

    private JPanel buildEmptyDestinationMessage() {
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(50, 0, 50, 0));

        JLabel emptyIcon = new JLabel("✈");
        emptyIcon.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 40));
        emptyIcon.setForeground(new Color(180, 180, 180));
        emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msg1 = new JLabel("아직 추가된 여행지가 없어요.", SwingConstants.CENTER);
        msg1.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        msg1.setForeground(new Color(100, 100, 100));
        msg1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msg2 = new JLabel("여행지 추가하기 버튼을 눌러 새로운 여행지를 등록해보세요!", SwingConstants.CENTER);
        msg2.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        msg2.setForeground(new Color(100, 100, 100));
        msg2.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(emptyIcon);
        center.add(Box.createVerticalStrut(10));
        center.add(msg1);
        center.add(Box.createVerticalStrut(5));
        center.add(msg2);

        return center;
    }

    public void showMainSchedulePanel() {
        card.show(root, "메인");
    }
}