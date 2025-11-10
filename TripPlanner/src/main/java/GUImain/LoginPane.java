package main.java.GUImain;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;

public class LoginPane extends JPanel {
    JPanel root;
    CardLayout card;
    
    JPanel loginPanel;
    JPanel loginCenterPanel;

    public LoginPane(JPanel root, CardLayout card) {
        this.root = root;
        this.card = card;
    }

    public JPanel setupLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginCenterPanel = buildLoginCenter();
        loginPanel.add(loginCenterPanel);

        loginPanel.setOpaque(false);

        return loginPanel;
    }

    JPanel buildLoginCenter() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setPreferredSize(new Dimension(300, 400));

        panel.add(new JLabel("여행가요"));
        panel.add(new JLabel("아이디"));
        panel.add(new JTextField(20));
        panel.add(new JLabel("비밀번호"));
        panel.add(new JTextField(20));

        JButton loginBtn = new JButton("로그인");
        loginBtn.addActionListener(e -> {
            card.show(root, "메인");
        });
        panel.add(loginBtn);

        return panel;
    }

}
