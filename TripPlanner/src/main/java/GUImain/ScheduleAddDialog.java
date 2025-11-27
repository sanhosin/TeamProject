package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import main.java.domain.TripPlan;

public class ScheduleAddDialog extends JDialog {

    private final String CUSTOM_FONT_NAME = "맑은 고딕";
    private final Color MAIN_BLUE = new Color(90, 80, 255);
    private final Color INPUT_BG = new Color(245, 245, 245);
    private final Color LIGHT_GRAY = new Color(220, 220, 220);
    private final Color TITLE_COLOR = new Color(40, 60, 90);
    private final Color TEXT_GRAY = new Color(150, 150, 150);

    private final Color[] COLOR_OPTIONS = {
            new Color(51, 153, 255), // 파랑
            new Color(255, 51, 51), // 빨강
            new Color(51, 204, 153), // 초록
            new Color(255, 153, 0), // 주황
            new Color(153, 102, 255), // 보라
            new Color(255, 204, 51), // 노랑
            new Color(255, 102, 178) // 분홍
    };
    private TravelSearchPane travelSearchPanel;
    private JTextField titleField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField locationField;
    private JTextField descriptionField;
    private Color selectedColor = COLOR_OPTIONS[0];
    private TripPlan toModify = null;
    private Object[] uidata;
    private String dialogTitle;

    private final String PLACEHOLDER_TITLE = "일정 제목을 입력하세요";
    private final String PLACEHOLDER_DATE = "25/11/15"; // 이미지처럼 연도-월-일만 남김
    private final String PLACEHOLDER_TIME = "10:00";
    private final String PLACEHOLDER_LOCATION = "위치를 입력하세요";
    private final String PLACEHOLDER_DESCRIPTION = "설명을 입력하세요";

    public ScheduleAddDialog(Frame owner, String fontName, TravelSearchPane travelSearchPanel) {
        super(owner, true);
        this.travelSearchPanel = travelSearchPanel;
        dialogTitle = "일정 추가";
        initDialog(owner);
    }

    public ScheduleAddDialog(Frame owner, String fontName, TravelSearchPane travelSearchPanel, TripPlan toModify) {
        super(owner, true);
        this.travelSearchPanel = travelSearchPanel;
        this.toModify = toModify;
        uidata = toModify.getUidata();
        dialogTitle = "일정 수정";
        initDialog(owner);
    }

    private void initDialog(Frame owner) {
        setTitle(dialogTitle);
        setSize(480, 580);
        setResizable(false);
        setLocationRelativeTo(owner);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1));

        setContentPane(buildPanel());
    }

    private JPanel buildPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(25, 40, 20, 40));

        mainPanel.add(buildHeader(dialogTitle));
        mainPanel.add(Box.createVerticalStrut(25));

        boolean isModify = (toModify != null);

        String initialTitle = isModify ? (String) uidata[TripPlan.PLAN_TITLE] : PLACEHOLDER_TITLE;
        titleField = buildInputField(initialTitle, PLACEHOLDER_TITLE, 35, !isModify);

        String initialLocation = isModify ? (String) uidata[TripPlan.PLAN_LOC] : PLACEHOLDER_LOCATION;
        locationField = buildInputField(initialLocation, PLACEHOLDER_LOCATION, 35, !isModify);

        String initialDescription = isModify ? (String) uidata[TripPlan.PLAN_REVIEW] : PLACEHOLDER_DESCRIPTION;
        descriptionField = buildInputField(initialDescription, PLACEHOLDER_DESCRIPTION, 35, !isModify);

        // UI 구성
        mainPanel.add(buildSection("일정 제목"));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(titleField);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(buildDateTimeSection(isModify));
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(buildSection("위치"));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(locationField);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(buildSection("설명"));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(descriptionField);
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(buildColorSelection(isModify ? (Color) uidata[TripPlan.PLAN_COLOR] : COLOR_OPTIONS[0]));
        mainPanel.add(Box.createVerticalGlue());

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buildButtonRow());

        return mainPanel;
    }

    private JPanel buildHeader(String text) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel title = new JLabel(text);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 22));
        title.setForeground(TITLE_COLOR);

        ImageIcon rawClose = new ImageIcon("TripPlanner/src/main/resources/images/x.png");
        Image scaledClose = rawClose.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        JLabel closeBtn = new JLabel(new ImageIcon(scaledClose));

        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        header.add(title, BorderLayout.WEST);
        header.add(closeBtn, BorderLayout.EAST);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        return header;
    }

    private JLabel buildSection(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        label.setForeground(TITLE_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField buildInputField(String initialText, String placeholder, int height, boolean usePlaceholder) {
        JTextField inputField = new JTextField(initialText);
        inputField.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        inputField.setBackground(INPUT_BG);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GRAY),
                new EmptyBorder(8, 10, 8, 10)));
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (usePlaceholder) {
            inputField.setForeground(TEXT_GRAY);
        } else {
            inputField.setForeground(TITLE_COLOR);
        }

        if (usePlaceholder) {
            inputField.addFocusListener(new PlaceholderFocusListener(inputField, placeholder));
        }

        return inputField;
    }

    private class PlaceholderFocusListener extends FocusAdapter {
        private JTextField field;
        private String placeholder;

        public PlaceholderFocusListener(JTextField field, String placeholder) {
            this.field = field;
            this.placeholder = placeholder;
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (field.getText().equals(placeholder)) {
                field.setText("");
                field.setForeground(TITLE_COLOR);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (field.getText().trim().isEmpty()) {
                field.setText(placeholder);
                field.setForeground(TEXT_GRAY);
            }
        }
    }

    private JPanel buildDateTimeSection(boolean isModify) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 1. 연도-월-일
        JLabel dateLabel = buildSection("일정 시간");
        panel.add(dateLabel);
        panel.add(Box.createVerticalStrut(5));

        boolean usePlaceholder = !isModify;

        String initialDate = isModify ? (String) uidata[TripPlan.PLAN_DATE] : PLACEHOLDER_DATE;
        dateField = buildInputField(initialDate, PLACEHOLDER_DATE, 35, usePlaceholder);

        String initialTime = isModify ? (String) uidata[TripPlan.PLAN_TIME] : PLACEHOLDER_TIME;
        timeField = buildInputField(initialTime, PLACEHOLDER_TIME, 35, usePlaceholder);

        // ⭐ 2. 날짜 필드와 시간 레이블/필드를 담을 패널 (수평 정렬)
        JPanel customRow = new JPanel(new GridBagLayout());
        customRow.setOpaque(false);
        customRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        customRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 10);

        // A. 날짜 필드 (크기 비중 1.5)
        gbc.gridx = 0;
        gbc.weightx = 1.5;
        customRow.add(dateField, gbc);

        // B. 시간 레이블 ("시간")
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(0, 0, 0, 5);
        JLabel timeLabelText = buildSection("");

        // timeLabelText.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        timeLabelText.setPreferredSize(new Dimension(15, 35));
        timeLabelText.setMaximumSize(new Dimension(15, 35));
        timeLabelText.setHorizontalAlignment(SwingConstants.RIGHT); // 우측 정렬
        timeLabelText.setVerticalAlignment(SwingConstants.CENTER);
        customRow.add(timeLabelText, gbc);

        // C. 시간 필드 (크기 비중 1)
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        customRow.add(timeField, gbc);

        panel.add(customRow);

        return panel;
    }

    private JPanel buildColorSelection(Color initialColor) {
        this.selectedColor = initialColor;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel label = buildSection("색상");
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));

        JPanel colorGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        colorGrid.setOpaque(false);

        for (Color color : COLOR_OPTIONS) {
            RoundButton colorButton = new RoundButton(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.putClientProperty("color", color);
            colorButton.setFocusPainted(false);

            if (color.equals(initialColor)) {
                colorButton.setBorder(BorderFactory.createLineBorder(MAIN_BLUE, 3, true));
            } else {
                colorButton.setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1, true));
            }

            colorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedColor = (Color) colorButton.getClientProperty("color");
                    for (Component comp : colorGrid.getComponents()) {
                        if (comp instanceof RoundButton) {
                            ((RoundButton) comp).setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1, true));
                        }
                    }
                    colorButton.setBorder(BorderFactory.createLineBorder(MAIN_BLUE, 3, true));
                }
            });
            colorGrid.add(colorButton);
        }

        panel.add(colorGrid);

        return panel;
    }

    private static class RoundButton extends JButton {
        private Color buttonColor;

        public RoundButton(Color color) {
            this.buttonColor = color;
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(buttonColor);
            g2.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            if (getBorder() != null) {
                getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
            }
        }

        @Override
        public boolean contains(int x, int y) {
            int radius = getWidth() / 2;
            return Math.pow(x - radius, 2) + Math.pow(y - radius, 2) < Math.pow(radius, 2);
        }
    }

    private JPanel buildButtonRow() {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rowPanel.setOpaque(false);
        rowPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton cancelBtn = new JButton("취소");
        styleButton(cancelBtn, Color.WHITE, TITLE_COLOR, new Dimension(80, 35));
        cancelBtn.setBorder(BorderFactory.createLineBorder(LIGHT_GRAY));
        cancelBtn.addActionListener(e -> dispose());

        JButton saveBtn = new JButton("추가");
        if (toModify != null)
            saveBtn.setText("저장");

        styleButton(saveBtn, MAIN_BLUE, Color.WHITE, new Dimension(80, 35));

        saveBtn.addActionListener(e -> {
            String title = titleField.getText().equals(PLACEHOLDER_TITLE) ? "" : titleField.getText();
            String date = dateField.getText().equals(PLACEHOLDER_DATE) ? "" : dateField.getText();
            String time = timeField.getText().equals(PLACEHOLDER_TIME) ? "" : timeField.getText();
            String location = locationField.getText().equals(PLACEHOLDER_LOCATION) ? "" : locationField.getText();
            String description = descriptionField.getText().equals(PLACEHOLDER_DESCRIPTION) ? ""
                    : descriptionField.getText();

            if (title.trim().isEmpty() || date.trim().isEmpty() || location.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "제목, 날짜, 위치는 필수 입력 항목입니다.", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            final int PLAN_TITLE = 0;
            final int PLAN_DATE = 1;
            final int PLAN_TIME = 2;
            final int PLAN_LOC = 3;
            final int PLAN_REVIEW = 4;
            final int PLAN_COLOR = 5;

            uidata = new Object[6];
            uidata[PLAN_TITLE] = title;
            uidata[PLAN_DATE] = date;
            uidata[PLAN_TIME] = time;
            uidata[PLAN_LOC] = location;
            uidata[PLAN_REVIEW] = description;
            uidata[PLAN_COLOR] = selectedColor;

            if (toModify == null) {
                Main.tdm.addPlan(uidata);
            } else {
                toModify.set(uidata);
                Main.tdm.modifyPlan();
            }
            travelSearchPanel.refreshPlanListUI();

            dispose();
        });

        rowPanel.add(cancelBtn);
        rowPanel.add(saveBtn);

        return rowPanel;
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor, Dimension size) {
        button.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
    }
}