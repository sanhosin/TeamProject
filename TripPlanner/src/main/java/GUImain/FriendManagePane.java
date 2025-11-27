package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import main.java.domain.User;

public class FriendManagePane extends JPanel {
    private final String CUSTOM_FONT_NAME = "맑은 고딕";
    private final Color MAIN_BLUE = new Color(90, 80, 255);
    private final Color BG_COLOR = new Color(240, 247, 255);
    private final Color TITLE_COLOR = new Color(40, 60, 90);

    private JPanel listContainer; // 친구 목록 패널
    private JButton requestBoxBtn; // 받은 요청함 버튼

    public FriendManagePane() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // 1. 헤더
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 15, 10, 15));
        header.setPreferredSize(new Dimension(0, 50));

        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 20));
        backBtn.setForeground(new Color(150, 150, 150));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> Main.card.show(Main.root, "메인"));

        JLabel titleLabel = new JLabel("친구 목록");
        titleLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 18));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 버튼들을 담을 오른쪽 패널
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightPanel.setBackground(Color.WHITE);

        // [New] 받은 요청 버튼 (편지 봉투 모양)
        requestBoxBtn = new JButton("📩");
        requestBoxBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 16));
        requestBoxBtn.setForeground(MAIN_BLUE);
        requestBoxBtn.setBorderPainted(false);
        requestBoxBtn.setContentAreaFilled(false);
        requestBoxBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        requestBoxBtn.setToolTipText("받은 친구 요청 확인");
        requestBoxBtn.addActionListener(e -> showRequestBoxDialog());

        // 친구 추가 버튼
        JButton addBtn = new JButton(" + ");
        addBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 20));
        addBtn.setForeground(MAIN_BLUE);
        addBtn.setBorderPainted(false);
        addBtn.setContentAreaFilled(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> showSearchDialog());

        rightPanel.add(requestBoxBtn);
        rightPanel.add(addBtn);

        header.add(backBtn, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);
        header.add(rightPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // 2. 친구 목록 리스트
        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(BG_COLOR);
        listContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);

        // 화면 갱신 리스너
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshFriendList();
                updateRequestBtnState(); // 요청이 있으면 버튼 색깔 바꾸기 등
            }
        });
    }

    private void refreshFriendList() {
        listContainer.removeAll();
        User currentUser = Main.tdm.getLoginedUser();
        
        if (currentUser != null) {
            ArrayList<User> friends = currentUser.getFriendsList();
            
            if (friends == null || friends.isEmpty()) {
                JLabel emptyLabel = new JLabel("등록된 친구가 없습니다.");
                emptyLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
                emptyLabel.setForeground(Color.GRAY);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                listContainer.add(Box.createVerticalStrut(50));
                listContainer.add(emptyLabel);
            } else {
                for (User f : friends) {
                    listContainer.add(createFriendCard(f, currentUser));
                    listContainer.add(Box.createVerticalStrut(10));
                }
            }
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    // 요청 버튼 상태 업데이트 (요청이 있으면 빨간점 표시 같은 효과)
    private void updateRequestBtnState() {
        User me = Main.tdm.getLoginedUser();
        if (me != null && !me.getReceivedRequests().isEmpty()) {
            requestBoxBtn.setText("📩 (" + me.getReceivedRequests().size() + ")");
            requestBoxBtn.setForeground(Color.RED); // 요청이 있으면 빨간색
        } else {
            requestBoxBtn.setText("📩");
            requestBoxBtn.setForeground(MAIN_BLUE);
        }
    }

    private JPanel createFriendCard(User friend, User me) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(friend.getNick());
        nameLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 16));
        
        JLabel idLabel = new JLabel("@" + friend.getID());
        idLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 13));
        idLabel.setForeground(Color.GRAY);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(idLabel);

        JButton deleteBtn = new JButton("🗑");
        deleteBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 18));
        deleteBtn.setForeground(new Color(255, 100, 100));
        deleteBtn.setBorderPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                friend.getNick() + "님을 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                me.removeFriend(friend); 
                refreshFriendList();
            }
        });

        card.add(textPanel, BorderLayout.CENTER);
        card.add(deleteBtn, BorderLayout.EAST);
        return card;
    }

    // ★★★ [1] 친구 검색 및 요청 보내기 팝업 ★★★
    private void showSearchDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "친구 요청 보내기", true);
        dialog.setSize(400, 220);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel guide = new JLabel("아이디 또는 닉네임 검색");
        guide.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        guide.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField searchField = new JTextField();
        searchField.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton searchBtn = new JButton("요청 보내기"); // 버튼 이름 변경
        searchBtn.setBackground(MAIN_BLUE);
        searchBtn.setForeground(Color.WHITE);

        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) return;

            User found = Main.tdm.findUser(keyword); 
            User me = Main.tdm.getLoginedUser();

            if (found == null) {
                JOptionPane.showMessageDialog(dialog, "사용자를 찾을 수 없습니다.");
            } else if (found.getID().equals(me.getID())) {
                JOptionPane.showMessageDialog(dialog, "본인에게는 요청할 수 없습니다.");
            } else if (me.getFriendsList().contains(found)) {
                JOptionPane.showMessageDialog(dialog, "이미 등록된 친구입니다.");
            } else if (found.hasReceivedRequestFrom(me)) { // 이미 요청 보냈는지 확인
                JOptionPane.showMessageDialog(dialog, "이미 요청을 보냈습니다. 수락 대기 중입니다.");
            } else {
                int choice = JOptionPane.showConfirmDialog(dialog, 
                    "'" + found.getNick() + "'님에게 친구 요청을 보내시겠습니까?", 
                    "친구 요청", JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    // ★ 핵심: 바로 추가가 아니라 요청 보내기
                    found.receiveFriendRequest(me); 
                    
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "요청을 보냈습니다!");
                }
            }
        });

        btnPanel.add(searchBtn);
        content.add(guide);
        content.add(Box.createVerticalStrut(10));
        content.add(searchField);
        content.add(Box.createVerticalStrut(10));
        content.add(btnPanel);

        dialog.add(content);
        dialog.setVisible(true);
    }

    // ★★★ [2] 받은 요청 확인 팝업 (수락/거절) ★★★
    private void showRequestBoxDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "받은 친구 요청", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);

        JPanel requestListPanel = new JPanel();
        requestListPanel.setLayout(new BoxLayout(requestListPanel, BoxLayout.Y_AXIS));
        requestListPanel.setBackground(Color.WHITE);
        requestListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        User me = Main.tdm.getLoginedUser();
        ArrayList<User> requests = me.getReceivedRequests();

        if (requests.isEmpty()) {
            JLabel empty = new JLabel("받은 요청이 없습니다.");
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            requestListPanel.add(Box.createVerticalStrut(50));
            requestListPanel.add(empty);
        } else {
            for (User sender : requests) {
                // 요청 카드 UI
                JPanel item = new JPanel(new BorderLayout());
                item.setBackground(new Color(245, 245, 250));
                item.setBorder(new EmptyBorder(10, 10, 10, 10));
                item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

                JLabel name = new JLabel(sender.getNick() + " (@" + sender.getID() + ")");
                name.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));

                JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                btns.setOpaque(false);

                JButton accBtn = new JButton("수락");
                accBtn.setBackground(MAIN_BLUE);
                accBtn.setForeground(Color.WHITE);
                accBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
                
                JButton rejBtn = new JButton("거절");
                rejBtn.setBackground(Color.LIGHT_GRAY);
                rejBtn.setForeground(Color.BLACK);
                rejBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));

                // 수락 로직
                accBtn.addActionListener(e -> {
                    me.acceptFriendRequest(sender);
                    refreshFriendList(); // 메인 목록 갱신
                    updateRequestBtnState();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, sender.getNick() + "님과 친구가 되었습니다!");
                });

                // 거절 로직
                rejBtn.addActionListener(e -> {
                    me.rejectFriendRequest(sender);
                    updateRequestBtnState();
                    dialog.dispose(); // 간단히 닫고 갱신 (실제론 리스트만 갱신해도 됨)
                    JOptionPane.showMessageDialog(this, "요청을 거절했습니다.");
                });

                btns.add(accBtn);
                btns.add(rejBtn);

                item.add(name, BorderLayout.CENTER);
                item.add(btns, BorderLayout.EAST);
                
                requestListPanel.add(item);
                requestListPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(requestListPanel);
        scroll.setBorder(null);
        dialog.add(scroll);
        dialog.setVisible(true);
    }
}