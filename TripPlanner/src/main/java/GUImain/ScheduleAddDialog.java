package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import main.java.domain.TripPlan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ScheduleAddDialog extends JDialog {

    private final String CUSTOM_FONT_NAME = "맑은 고딕";
    private final Color MAIN_BLUE = new Color(90, 80, 255);
    private final Color INPUT_BG = new Color(245, 245, 245);
    private final Color LIGHT_GRAY = new Color(220, 220, 220);
    private final Color TITLE_COLOR = new Color(40, 60, 90);

    private final Color[] COLOR_OPTIONS = { // Color 배열: Collection의 한 형태입니다.
            new Color(153, 102, 255),
            new Color(255, 102, 178),
            new Color(255, 153, 0),
            new Color(255, 204, 51),
            new Color(51, 204, 153),
            new Color(51, 153, 255),
            new Color(255, 51, 51)
    };
    private TravelSearchPane travelSearchPanel;
    private JTextField titleField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField locationField;
    private JTextField descriptionField;
    private Color selectedColor = COLOR_OPTIONS[0];
    private TripPlan toModify=null;
    private Object[] uidata;
    private String dialogTitle;

    public ScheduleAddDialog(Frame owner, String fontName, TravelSearchPane travelSearchPanel) {
        super(owner, true);
        this.travelSearchPanel=travelSearchPanel;
        dialogTitle="일정 추가";
        setTitle(dialogTitle);
        setSize(480, 580);
        setResizable(false);
        setLocationRelativeTo(owner);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1));

        setContentPane(buildPanel());
    }

    //생성자 추가로 일정 수정기능과 일정추가 구분, 메서드에서는 toModify의 값이 null인지 아닌지에 따라 판단하도록 추가함
    public ScheduleAddDialog(Frame owner, String fontName, TravelSearchPane travelSearchPanel,TripPlan toModify) {
        super(owner, true);
        this.travelSearchPanel=travelSearchPanel;
        this.toModify=toModify;
        uidata=toModify.getUidata();
        dialogTitle="일정 수정";
        setTitle(dialogTitle);
        setSize(480, 580);
        setResizable(false);
        setLocationRelativeTo(owner);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1));

        setContentPane(buildPanel());
    }

    public void preFillScheduleData(String title, String location, String description, String date, String time) {
        if (titleField != null) titleField.setText(title);
        if (locationField != null) locationField.setText(location);
        if (descriptionField != null) descriptionField.setText(description);
        if (dateField != null) dateField.setText(date);
        if (timeField != null) timeField.setText(time);
    }

    private JPanel buildPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        mainPanel.add(buildTitle(dialogTitle));
        mainPanel.add(Box.createVerticalStrut(25));

        if(toModify==null){
            titleField = buildInputField("일정 제목을 입력하세요", 40);
            locationField = buildInputField("위치를 입력하세요", 40);
            descriptionField = buildInputField("설명을 입력하세요", 40);
        }
        else{
            titleField = buildInputField((String)uidata[TripPlan.PLAN_TITLE], 40);
            locationField = buildInputField((String)uidata[TripPlan.PLAN_LOC], 40);
            descriptionField = buildInputField((String)uidata[TripPlan.PLAN_REVIEW], 40);
        }
        mainPanel.add(buildLabel("제목"));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(titleField);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(buildDateTimeRow());
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(buildLabel("장소"));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(locationField);
        mainPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(buildLabel("설명"));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(descriptionField);
        mainPanel.add(Box.createVerticalStrut(30));

        mainPanel.add(buildColorSelection());
        mainPanel.add(Box.createVerticalGlue());

        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buildButtonRow());

        return mainPanel;
    }

    private JLabel buildTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 22));
        title.setForeground(TITLE_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        return title;
    }

    private JLabel buildLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        label.setForeground(TITLE_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JTextField buildInputField(String placeholder, int height) {
        JTextField inputField = new JTextField(placeholder);
        inputField.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        inputField.setPreferredSize(new Dimension(400, height));
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        inputField.setBackground(INPUT_BG);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        return inputField;
    }

    private JPanel buildDateTimeRow() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel labelRow = new JPanel(new GridLayout(1, 2, 0, 0));
        labelRow.setOpaque(false);
        labelRow.setMaximumSize(new Dimension(400, 20));

        JLabel dateLabel = new JLabel("날짜", SwingConstants.CENTER);
        dateLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        dateLabel.setForeground(TITLE_COLOR);
        JLabel timeLabel = new JLabel("시간", SwingConstants.CENTER);
        timeLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        timeLabel.setForeground(TITLE_COLOR);

        labelRow.add(dateLabel);
        labelRow.add(timeLabel);

        JPanel inputRow = new JPanel(new GridLayout(1, 2, 10, 0));
        inputRow.setOpaque(false);
        inputRow.setMaximumSize(new Dimension(400, 40));

        if(toModify==null){
            dateField = buildInputField("25/11/15", 40);
            timeField = buildInputField("10:00", 40);
        }
        else{
            dateField = buildInputField((String)uidata[TripPlan.PLAN_DATE], 40);
            timeField = buildInputField((String)uidata[TripPlan.PLAN_TIME], 40);
        }
        dateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        timeField.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputRow.add(dateField);
        inputRow.add(timeField);

        panel.add(labelRow);
        panel.add(Box.createVerticalStrut(5));
        panel.add(inputRow);

        return panel;
    }

    private JPanel buildColorSelection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(400, 100));

        JLabel label = new JLabel("색상", SwingConstants.CENTER);
        label.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        label.setForeground(TITLE_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel colorGrid = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        colorGrid.setOpaque(false);

        for (Color color : COLOR_OPTIONS) {
            JButton colorButton = new JButton();
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.setBackground(color);
            colorButton.setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1, true));
            colorButton.putClientProperty("color", color);

            if (color.equals(selectedColor)) {
                colorButton.setBorder(BorderFactory.createLineBorder(MAIN_BLUE, 3, true));
            }

            // ActionListener를 무명 클래스로 구현
            colorButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedColor = (Color) colorButton.getClientProperty("color");
                    for (Component comp : colorGrid.getComponents()) {
                        if (comp instanceof JButton) {
                            ((JButton) comp).setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1, true));
                        }
                    }
                    colorButton.setBorder(BorderFactory.createLineBorder(MAIN_BLUE, 3, true));
                }
            });
            colorGrid.add(colorButton);
        }

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(colorGrid);

        return panel;
    }

    private JPanel buildButtonRow() {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        rowPanel.setOpaque(false);
        rowPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rowPanel.setMaximumSize(new Dimension(400, 40));

        JButton cancelBtn = new JButton("취소");
        styleButton(cancelBtn, Color.WHITE, TITLE_COLOR, new Dimension(80, 35));
        cancelBtn.setBorder(BorderFactory.createLineBorder(LIGHT_GRAY));
        cancelBtn.addActionListener(e -> dispose());

        JButton saveBtn = new JButton("저장");
        styleButton(saveBtn, MAIN_BLUE, Color.WHITE, new Dimension(80, 35));
        saveBtn.addActionListener(e -> {
            String title = titleField.getText();
            String date = dateField.getText();
            String time = timeField.getText();
            String location = locationField.getText();
            String description = descriptionField.getText();

            // 1. TestDataManage에 데이터 저장 (백엔드 저장)
            uidata=new Object[]{title,date,time,location,description,selectedColor};
            if(toModify==null){
                Main.tdm.addPlan(uidata);
            }
            else{
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