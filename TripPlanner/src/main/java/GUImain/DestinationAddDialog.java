package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter; // 👈 이 import가 추가되었습니다.
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class DestinationAddDialog extends JDialog {

    private final String CUSTOM_FONT_NAME;
    private final Color MAIN_BLUE = new Color(90, 80, 255);
    private final Color LIGHT_GRAY = new Color(220, 220, 220);
    private final Color TITLE_COLOR = new Color(40, 60, 90);
    private final Color BLUE_ACCENT = new Color(80, 140, 255);

    private JTextField nameField;
    private JTextField countryField;
    private JTextArea descriptionArea;
    private JTextField ratingField;
    private JTextField tagsField;

    private TravelSearchPane travelSearchPane;

    public DestinationAddDialog(Frame owner, String fontName, TravelSearchPane parentPane) {
        super(owner, true);
        CUSTOM_FONT_NAME = fontName;
        this.travelSearchPane = parentPane;

        setTitle("새로운 여행지 추가");
        setSize(400, 550);
        setResizable(false);
        setLocationRelativeTo(owner);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1));

        setContentPane(buildPanel());
    }

    private JPanel buildPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(buildHeader(), BorderLayout.NORTH);
        mainPanel.add(buildContent(), BorderLayout.CENTER);
        mainPanel.add(buildButtonPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("새로운 여행지 추가");
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

        panel.add(title, BorderLayout.WEST);
        panel.add(closeBtn, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        contentPanel.add(createFormField("여행지 이름:", nameField = new JTextField()));
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(createFormField("국가:", countryField = new JTextField()));
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(createFormTextArea("설명:", descriptionArea = new JTextArea(3, 20)));
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(createFormField("평점 (예: 4.5):", ratingField = new JTextField()));
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(createFormField("태그 (쉼표로 구분):", tagsField = new JTextField()));

        return contentPanel;
    }

    private JPanel createFormField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        label.setForeground(TITLE_COLOR);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));

        textField.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GRAY, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormTextArea(String labelText, JTextArea textArea) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        label.setForeground(TITLE_COLOR);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));

        textArea.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GRAY, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        panel.setBackground(Color.WHITE);

        JButton addBtn = new JButton("여행지 추가");
        addBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 16));
        addBtn.setFocusPainted(false);
        addBtn.setBackground(MAIN_BLUE);
        addBtn.setForeground(Color.WHITE);
        addBtn.setPreferredSize(new Dimension(340, 45));

        addBtn.addActionListener(e -> {
            String name = nameField.getText();
            String country = countryField.getText();
            String description = descriptionArea.getText();
            String ratingStr = ratingField.getText();
            String tagsStr = tagsField.getText();

            if (name.isEmpty() || country.isEmpty() || description.isEmpty() || ratingStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "모든 필드를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double rating;
            try {
                rating = Double.parseDouble(ratingStr);
                if (rating < 0 || rating > 5) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "평점은 0.0에서 5.0 사이의 숫자로 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] tags = tagsStr.isEmpty() ? new String[]{} : Arrays.stream(tagsStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);

            TravelSearchPane.DestinationItem newItem = new TravelSearchPane.DestinationItem(name, country, description, rating, "default.jpg", tags);
            travelSearchPane.addDestination(newItem);
            dispose();
        });

        panel.add(addBtn);
        return panel;
    }
}