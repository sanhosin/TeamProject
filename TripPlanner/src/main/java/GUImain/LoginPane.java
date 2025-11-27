package main.java.GUImain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.FocusManager;
import javax.swing.plaf.basic.BasicTextFieldUI;
import main.java.domain.User;

public class LoginPane extends JPanel{
    JPanel root;
    CardLayout card;
    JPanel loginPanel;
    MainPane mainpanel;

    private final String CUSTOM_FONT_NAME = "맑은 고딕";
    private final Color MAIN_BLUE = new Color(90, 80, 255);
    private final Color LIGHT_BLUE = new Color(220, 235, 255);

    public LoginPane(JPanel root, CardLayout card) {
        this.root = root;
        this.card = card;
    }

    public JPanel setupLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(LIGHT_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel center = buildLoginCenter();
        loginPanel.add(center, gbc);

        return loginPanel;
    }

    JPanel buildLoginCenter() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(380, 460));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)));

        // 수정>> 여행가요 이미지 삽입
        System.out.println(Main.tdm.rootPath);
        ImageIcon rawIcon = new ImageIcon(Main.tdm.rootPath+"logo.png");
        Image scaledImage = rawIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel icon = new JLabel(scaledIcon);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("여행가요", SwingConstants.CENTER);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 15));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField idField = new PlaceholderTextField("아이디를 입력하세요");
        stylizeTextField(idField);
        idField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 15));
        pwLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField pwField = new PlaceholderPasswordField("비밀번호를 입력하세요");
        stylizeTextField(pwField);
        pwField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginBtn = new JButton("로그인");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setBackground(MAIN_BLUE);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(310, 40));
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loginBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 15));

        loginBtn.addActionListener(e -> {
            String id = idField.getText();
            String pwd = new String(pwField.getPassword());
            User loginedUser = Main.tdm.isExistUser(id, pwd);
            if (loginedUser == null) {
                // 여기에 아이디/비번 틀렸을 때 안내메시지 띄우는 기능 추가
                System.out.println("존재하지 않는 아이디/비밀번호 입니다.");
            } else {
                Main.tdm.setLoginedUser(loginedUser);
                Main.tdm.readData();
                mainpanel.refreshTripListUI();

                card.show(root, "메인");
            }
        });

        JButton joinBtn = new JButton("회원가입");
        joinBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinBtn.setPreferredSize(new Dimension(310, 40));
        joinBtn.setBackground(Color.WHITE);
        joinBtn.setForeground(Color.BLACK);
        joinBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        joinBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 15));

        // 🚨 수정된 부분: 회원가입 버튼 클릭 시 "회원가입" 화면으로 전환
        joinBtn.addActionListener(e -> {
            card.show(root, "회원가입");
        });

        panel.add(icon);
        panel.add(title);
        panel.add(Box.createVerticalStrut(30));
        panel.add(idLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(idField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(pwLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(pwField);
        panel.add(Box.createVerticalStrut(25));
        panel.add(loginBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(joinBtn);

        return panel;
    }

    void stylizeTextField(JTextField field) {
        field.setPreferredSize(new Dimension(310, 40));
        field.setMaximumSize(new Dimension(310, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        field.setBackground(new Color(245, 245, 245));
        field.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
    }

    public void getMainPane(MainPane mainpanel) {
        this.mainpanel = mainpanel;
    }

    class PlaceholderTextField extends JTextField {
        private String placeholder;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setUI(new BasicTextFieldUI());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !(FocusManager.getCurrentManager().getFocusOwner() == this)) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(180, 180, 180));
                g2.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
                int h = g2.getFontMetrics().getHeight();
                g2.drawString(placeholder, getInsets().left, (getHeight() - h) / 2 + h - 2);
                g2.dispose();
            }
        }
    }

    class PlaceholderPasswordField extends JPasswordField {
        private String placeholder;

        public PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setEchoChar((char) 0);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getPassword().length == 0 && !(FocusManager.getCurrentManager().getFocusOwner() == this)) {
                setEchoChar((char) 0);
            }
            super.paintComponent(g);

            if (getPassword().length == 0 && !(FocusManager.getCurrentManager().getFocusOwner() == this)) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(180, 180, 180));
                g2.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
                int h = g2.getFontMetrics().getHeight();
                g2.drawString(placeholder, getInsets().left, (getHeight() - h) / 2 + h - 2);
                g2.dispose();
            }

            setEchoChar('*');
        }
    }

    
}