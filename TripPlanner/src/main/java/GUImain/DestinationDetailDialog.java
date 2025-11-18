package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class DestinationDetailDialog extends JDialog {

    private final String CUSTOM_FONT_NAME;
    private final Color MAIN_BLUE = new Color(90, 80, 255);
    private final Color LIGHT_GRAY = new Color(220, 220, 220);
    private final Color TITLE_COLOR = new Color(40, 60, 90);
    private final Color BLUE_ACCENT = new Color(80, 140, 255);

    private final TravelSearchPane parentPane;
    private final TravelSearchPane.DestinationItem destination;

    private final String[] attractions = {"에펠탑", "루브르 박물관", "샹젤리제 거리", "노트르담 대성당"};
    private final String travelPeriod = "봄(3-5월), 가을(9-11월)";
    private final String avgCost = "₩150만원 - ₩300만원 (3박 4일 기준)";

    public DestinationDetailDialog(Frame owner, String fontName, TravelSearchPane parentPane, TravelSearchPane.DestinationItem destination) {
        super(owner, true);
        CUSTOM_FONT_NAME = fontName;
        this.parentPane = parentPane;
        this.destination = destination;
        setTitle(destination.name + ", " + destination.country);
        setSize(450, 700);
        setResizable(false);
        setLocationRelativeTo(owner);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1));

        setContentPane(buildPanel());
    }

    private JPanel buildPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(buildContent());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(buildHeader(), BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buildButtonPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel(destination.name + ", " + destination.country);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 18));
        title.setForeground(TITLE_COLOR);

        JLabel closeBtn = new JLabel("❌");
        closeBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 18));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        panel.add(title, BorderLayout.CENTER); // 제목을 가운데로
        panel.add(closeBtn, BorderLayout.EAST); // X 버튼을 오른쪽으로
        return panel;
    }

    private JPanel buildContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        contentPanel.add(buildImageAndRating());
        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(buildSectionTitle("소개"));
        contentPanel.add(buildTextBlock(destination.description));
        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(buildSectionTitle("주요 관광지"));
        contentPanel.add(buildAttractionsGrid(attractions));
        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(buildSectionTitle("여행 정보"));
        contentPanel.add(buildInfoGrid());
        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(buildSectionTitle("태그"));
        contentPanel.add(buildTagPanel(destination.tags));

        return contentPanel;
    }

    private JPanel buildImageAndRating() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(410, 200));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        panel.setBackground(new Color(220, 220, 220)); // 회색 배경
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 이미지 로딩 (Placeholder)
        ImageIcon icon = loadImage(destination.imagePath, panel.getPreferredSize().width, panel.getPreferredSize().height);
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);

        JLabel ratingLabel = new JLabel("⭐ " + String.format("%.1f", destination.rating), SwingConstants.CENTER);
        ratingLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        ratingLabel.setForeground(Color.WHITE);
        ratingLabel.setBackground(new Color(0, 0, 0, 150));
        ratingLabel.setOpaque(true);
        ratingLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JPanel ratingWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        ratingWrapper.setOpaque(false);
        ratingWrapper.add(ratingLabel);
        panel.add(ratingWrapper, BorderLayout.NORTH);

        return panel;
    }

    // 이미지 로딩 도우미 메서드 (placeholder)
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


    private JLabel buildSectionTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 16));
        title.setForeground(TITLE_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        return title;
    }

    private JPanel buildTextBlock(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel("<html><body style='width: 380px; font-size: 13px; color: #555555;'>" + text + "</body></html>");
        label.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 13));
        label.setForeground(new Color(85, 85, 85));
        panel.add(label, BorderLayout.WEST);
        return panel;
    }

    private JPanel buildAttractionsGrid(String[] attractions) {
        JPanel grid = new JPanel(new GridLayout(0, 2, 10, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String item : attractions) {
            JLabel label = new JLabel("ⓘ " + item);
            label.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
            label.setForeground(new Color(90, 90, 110));
            label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            grid.add(label);
        }
        return grid;
    }

    private JPanel buildInfoGrid() {
        JPanel grid = new JPanel(new GridLayout(1, 2, 10, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        grid.add(buildInfoItem("최적 여행 시기", "🕑", travelPeriod));
        grid.add(buildInfoItem("평균 여행 경비", "$", avgCost));

        return grid;
    }

    private JPanel buildInfoItem(String title, String icon, String content) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(icon + " " + title);
        titleLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel contentLabel = new JLabel("<html><body style='width: 170px;'>" + content + "</body></html>");
        contentLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
        contentLabel.setForeground(new Color(85, 85, 85));
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(contentLabel);
        return panel;
    }

    private JPanel buildTagPanel(String[] tags) {
        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        tagPanel.setOpaque(false);
        tagPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String tag : tags) {
            JLabel label = new JLabel(tag);
            label.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
            label.setForeground(BLUE_ACCENT);
            label.setBackground(new Color(230, 230, 250));
            label.setOpaque(true);
            label.setBorder(new EmptyBorder(3, 8, 3, 8));
            tagPanel.add(label);
        }
        return tagPanel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        panel.setBackground(Color.WHITE);

        JButton addBtn = new JButton("일정 추가");
        addBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 16));
        addBtn.setFocusPainted(false);
        addBtn.setBackground(MAIN_BLUE);
        addBtn.setForeground(Color.WHITE);
        addBtn.setPreferredSize(new Dimension(410, 45));

        addBtn.addActionListener(e -> {
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            MainPane mainPane = parentPane.getMainPaneRef();

            ScheduleAddDialog dialog = new ScheduleAddDialog(mainFrame, CUSTOM_FONT_NAME, mainPane);

            dialog.preFillScheduleData(destination.name, destination.country, destination.description,"","");

            dispose();
            dialog.setVisible(true);
        });

        panel.add(addBtn);
        return panel;
    }
}
