package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class MainPane extends JPanel {
    JPanel root;
    CardLayout card;
    
    JPanel mainPanel;
    JPanel mainHeaderPanel;
    JPanel mainCenterPanel;

    public MainPane(JPanel root, CardLayout card) {
        this.root = root;
        this.card = card;
    }
    public JPanel setupMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        mainHeaderPanel = buildMainHeader();
        mainCenterPanel = buildMainCenter();
        mainCenterPanel.setOpaque(false);

        mainPanel.add(mainHeaderPanel, BorderLayout.NORTH);
        mainPanel.add(mainCenterPanel, BorderLayout.CENTER);

        return mainPanel;
    }
    JPanel buildMainHeader() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel left = new JPanel(new FlowLayout());
        JButton menu = new JButton("MENU");
        left.add(menu);

        JPanel center = new JPanel(new FlowLayout());
        JLabel title = new JLabel("여행가요");
        center.add(title);

        JPanel right = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        right.add(searchField);

        panel.add(left, BorderLayout.WEST);
        panel.add(center, BorderLayout.CENTER);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    JPanel buildMainCenter() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 100, 20, 100));

        JPanel forCalendar = new JPanel();
        forCalendar.add(new JLabel("달력 추가 공간")); // 수정

        JPanel forSchedules = new JPanel();
        forSchedules.add(new JLabel("일정리스트 표시 공간")); // 수정

        panel.add(forCalendar);
        panel.add(forSchedules);

        return panel;
    }

}
