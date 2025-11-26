package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.plaf.basic.BasicTextFieldUI;

public class JoinPane extends JPanel {
    JPanel root;
    CardLayout card;

    // 디자인 상수 (LoginPane 및 MainPane과 동일)
    private final String CUSTOM_FONT_NAME = "맑은 고딕";
    private final Color MAIN_BLUE = new Color(90, 80, 255);
    private final Color LIGHT_BLUE = new Color(220, 235, 255);
    private final Color INPUT_BG = new Color(245, 245, 245);
    private final Color TITLE_COLOR = new Color(40, 60, 90);

    public JoinPane(JPanel root, CardLayout card) {
        this.root = root;
        this.card = card;
    }

    public JPanel setupJoinPanel() {
        JPanel joinPanel = new JPanel(new GridBagLayout());
        joinPanel.setBackground(LIGHT_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel center = buildJoinCenter();
        joinPanel.add(center, gbc);

        return joinPanel;
    }

    JPanel buildJoinCenter() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(380, 560));
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 카드 외곽 스타일 적용
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)));

        JLabel title = new JLabel("회원가입", SwingConstants.CENTER);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 24));
        title.setForeground(TITLE_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 1. 닉네임 필드
        JLabel nicknameLabel = new JLabel("닉네임");
        nicknameLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 15));
        JTextField nicknameField = new PlaceholderTextField("닉네임을 입력하세요");
        stylizeTextField(nicknameField);
        nicknameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nicknameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. 아이디 필드
        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 15));
        JTextField idField = new PlaceholderTextField("사용할 아이디를 입력하세요");
        stylizeTextField(idField);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        idField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. 비밀번호 필드
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 15));
        JPasswordField pwField = new PlaceholderPasswordField("비밀번호를 입력하세요");
        stylizeTextField(pwField);
        pwLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pwField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 회원가입 버튼
        JButton joinBtn = new JButton("회원가입");
        joinBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinBtn.setBackground(MAIN_BLUE);
        joinBtn.setForeground(Color.WHITE);
        joinBtn.setFocusPainted(false);
        joinBtn.setPreferredSize(new Dimension(310, 40));
        joinBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 15));

        // ActionListener를 무명 클래스로 구현 [cite: 1917, 1921]
        joinBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 실제 회원가입 로직 (데이터 저장)을 여기에 추가합니다.
                String id = idField.getText();
                String pwd = new String(pwField.getPassword());
                String nickname = nicknameField.getText();
                Object[] uidata = { id, pwd, nickname };
                if (id.equals("") || pwd.equals("") || nickname.equals("")) {
                    System.out.println("모두 입력해주세요.");
                    return;
                }

                if (Main.tdm.isExistUserID(id)) {
                    // 여기에 아이디 중복 경고 메시지 출력 부분 추가
                    System.out.println("존재하는 아이디 입니다!");
                } else {
                    Main.tdm.addUser(uidata);
                    card.show(root, "로그인"); // 성공 시 로그인 화면으로 복귀
                }
            }
        });

        // 취소 버튼 (로그인 화면으로 복귀)
        JButton cancelBtn = new JButton("취소");
        cancelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelBtn.setPreferredSize(new Dimension(310, 40));
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setForeground(TITLE_COLOR);
        cancelBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        cancelBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 15));

        cancelBtn.addActionListener(e -> {
            card.show(root, "로그인");
        });

        // 요소 배치
        panel.add(title);
        panel.add(Box.createVerticalStrut(30));

        panel.add(nicknameLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(nicknameField);
        panel.add(Box.createVerticalStrut(20));

        panel.add(idLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(idField);
        panel.add(Box.createVerticalStrut(20));

        panel.add(pwLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(pwField);
        panel.add(Box.createVerticalStrut(30));

        panel.add(joinBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(cancelBtn);

        return panel;
    }

    void stylizeTextField(JTextField field) {
        field.setPreferredSize(new Dimension(310, 40));
        field.setMaximumSize(new Dimension(310, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        field.setBackground(INPUT_BG);
        field.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
    }

    // Placeholder를 위한 커스텀 JTextField (코드 간소화를 위해 paintComponent는 생략)
    class PlaceholderTextField extends JTextField {
        private String placeholder;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
        }
    }

    // Placeholder를 위한 커스텀 JPasswordField (코드 간소화를 위해 paintComponent는 생략)
    class PlaceholderPasswordField extends JPasswordField {
        private String placeholder;

        public PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setEchoChar((char) 0);
        }
    }
}