package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*; // 이벤트 처리를 위해 필요
import main.java.domain.User;

public class UserInfoPane extends JPanel {
    private final String CUSTOM_FONT_NAME = "맑은 고딕";
    private final Color MAIN_BLUE = new Color(90, 80, 255);
    private final Color LOGOUT_RED = new Color(220, 20, 60);
    private final Color BG_COLOR = new Color(240, 247, 255);
    
    // 텍스트 필드를 전역 변수로 빼서 언제든 내용을 바꿀 수 있게 함
    private JTextField nickField;
    private JTextField idField;
    private JTextField pwField;

    public UserInfoPane() {
        setLayout(new GridBagLayout()); 
        setBackground(BG_COLOR);

        //화면이 보일 때마다 데이터를 새로고침하는 이벤트 추가
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshData(); // 화면이 뜰 때 이 함수가 실행됩니다.
            }
        });

        // --- 디자인(UI) 부분은 기존과 동일 ---
        JPanel contentCard = new JPanel();
        contentCard.setLayout(new BoxLayout(contentCard, BoxLayout.Y_AXIS));
        contentCard.setBackground(Color.WHITE);
        contentCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(40, 40, 40, 40)
        ));
        contentCard.setPreferredSize(new Dimension(450, 550)); 

        // 1. 헤더
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 20));
        backBtn.setForeground(new Color(150, 150, 150));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> Main.card.show(Main.root, "메인"));

        JLabel titleLabel = new JLabel(" 유저 정보 관리");
        titleLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 22));
        titleLabel.setForeground(MAIN_BLUE);

        header.add(backBtn, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);
        
        contentCard.add(header);
        contentCard.add(Box.createVerticalStrut(30));

        // 2. 입력 필드 (처음엔 빈 칸으로 생성)
        
        // (1) 닉네임
        contentCard.add(createLabel("닉네임"));
        contentCard.add(Box.createVerticalStrut(5));
        nickField = createInputField("", true); 
        contentCard.add(nickField);
        contentCard.add(Box.createVerticalStrut(20));

        // (2) 아이디
        contentCard.add(createLabel("아이디"));
        contentCard.add(Box.createVerticalStrut(5));
        idField = createInputField("", false); // 수정 불가
        contentCard.add(idField);
        contentCard.add(Box.createVerticalStrut(20));

        // (3) 비밀번호
        contentCard.add(createLabel("비밀번호"));
        contentCard.add(Box.createVerticalStrut(5));
        pwField = createInputField("", true); 
        contentCard.add(pwField);
        
        contentCard.add(Box.createVerticalGlue());

        // 3. 버튼 영역
        JButton saveBtn = new JButton("변경사항 저장");
        styleButton(saveBtn, MAIN_BLUE, Color.WHITE);
        saveBtn.addActionListener(e -> {
            String newNick = nickField.getText().trim();
            String newPw = pwField.getText().trim();

            if (newNick.isEmpty() || newPw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "닉네임과 비밀번호를 입력해주세요.");
                return;
            }
            User u = Main.tdm.getLoginedUser();
            if (u != null) {
                u.setNick(newNick);
                u.setPwd(newPw);
                JOptionPane.showMessageDialog(this, "정보가 성공적으로 수정되었습니다.");
            }
        });
        contentCard.add(saveBtn);
        contentCard.add(Box.createVerticalStrut(10));

        JButton logoutBtn = new JButton("로그아웃");
        styleButton(logoutBtn, LOGOUT_RED, Color.WHITE);
        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, 
                    "로그아웃 하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION) {
                Main.tdm.setLoginedUser(null);
                Main.card.show(Main.root, "로그인");
            }
        });
        contentCard.add(logoutBtn);

        add(contentCard);
    }

    // 데이터를 최신으로 불러오는 함수
    private void refreshData() {
        User user = Main.tdm.getLoginedUser();
        if (user != null) {
            nickField.setText(user.getNick());
            idField.setText(user.getID()); 
            pwField.setText(user.getPwd());
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 13));
        label.setForeground(new Color(100, 100, 100));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createInputField(String text, boolean isEditable) {
        JTextField field = new JTextField(text);
        field.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 15));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)), 
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        if (isEditable) {
            field.setBackground(Color.WHITE);
            field.setEditable(true);
        } else {
            // 수정 불가 시 스타일
            field.setBackground(new Color(245, 245, 245)); 
            field.setForeground(Color.DARK_GRAY); 
            field.setEditable(false);
            field.setFocusable(false);
        }
        return field;
    }

    private void styleButton(JButton btn, Color bgColor, Color fgColor) {
        btn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 15));
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}