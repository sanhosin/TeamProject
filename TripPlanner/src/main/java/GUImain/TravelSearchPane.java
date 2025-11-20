package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TravelSearchPane extends JPanel {

    JPanel root;
    CardLayout card;
    private MainPane mainPaneRef; // MainPane (여행 목록)으로 돌아가기 위해 필요

    private final String CUSTOM_FONT_NAME = "맑은 고딕";

    private final Color BG_COLOR = new Color(240, 247, 255);
    private final Color CARD_COLOR = new Color(255, 255, 255);
    private final Color BORDER_COLOR = new Color(200, 200, 200);
    private final Color TITLE_COLOR = new Color(40, 60, 90);
    private final Color BLUE_ACCENT = new Color(80, 140, 255);
    private final Color DOT_COLOR = new Color(255, 60, 60);

    // 여행지 목록을 저장할 리스트 (임시로 추가)
    private List<DestinationItem> destinations = new ArrayList<>();

    public TravelSearchPane(JPanel root, CardLayout card, MainPane mainPaneRef) {
        this.root = root;
        this.card = card;
        this.mainPaneRef = mainPaneRef;
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        add(setupMainPanel(), BorderLayout.CENTER);
    }

    // **오류 해결을 위해 추가된 메서드**
    /**
     * MainPane의 참조를 반환합니다. DestinationDetailDialog에서 필요합니다.
     */
    public MainPane getMainPaneRef() {
        return this.mainPaneRef;
    }

    /**
     * DestinationAddDialog에서 새로운 여행지를 추가할 때 호출됩니다.
     */
    public void addDestination(DestinationItem item) {
        this.destinations.add(item);
        // 실제로는 리스트를 업데이트하고 화면을 새로고침하는 로직이 추가되어야 합니다.
        System.out.println("새로운 여행지가 추가되었습니다: " + item.name);
    }
    // **********************************

    public JPanel setupMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel header = buildHeader();
        header.setBackground(Color.WHITE);

        JPanel center = buildScheduleDetailArea();

        panel.add(header, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 10, 15, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        // 뒤로가기 버튼 (MainPane, 즉 여행 목록으로 돌아감)
        JLabel backIcon = new JLabel("⬅");
        backIcon.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 26));
        backIcon.setForeground(TITLE_COLOR);
        backIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                card.show(root, "메인"); // 여행 목록 화면(MainPane)으로 복귀
            }
        });

        JLabel title = new JLabel("여행가요");
        title.setForeground(TITLE_COLOR);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 26));

        leftPanel.add(backIcon);
        leftPanel.add(title);

        JButton addScheduleBtn = new JButton("+ 일정 추가"); // Image 2의 일정 추가 버튼
        addScheduleBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        addScheduleBtn.setFocusPainted(false);
        addScheduleBtn.setBackground(BLUE_ACCENT);
        addScheduleBtn.setForeground(Color.WHITE);
        addScheduleBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addScheduleBtn.setPreferredSize(new Dimension(150, 40));

        addScheduleBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            // Image 3의 일정 추가 다이얼로그 호출
            ScheduleAddDialog dialog = new ScheduleAddDialog(parentFrame, CUSTOM_FONT_NAME, mainPaneRef);
            dialog.setVisible(true);
        });


        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(addScheduleBtn, BorderLayout.EAST);

        return panel;
    }

    private JPanel buildScheduleDetailArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel tripTitle = new JLabel("파리 여행"); // 여행 상세 타이틀
        tripTitle.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 28));
        tripTitle.setForeground(TITLE_COLOR);
        tripTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel listContainer = new JPanel();
        listContainer.setOpaque(false);
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        listContainer.setBorder(new EmptyBorder(20, 0, 0, 0));

        // 샘플 일정 카드 (Image 2 참고)
        listContainer.add(buildScheduleCard("에펠탑 방문", "2025-11-15 10:00", "파리, 프랑스", "에펠탑 관람 및 사진 촬영", new Color(0, 150, 136)));
        listContainer.add(Box.createVerticalStrut(15));
        listContainer.add(buildScheduleCard("루브르 박물관", "2025-11-15 14:00", "파리, 프랑스", "모나리자 감상", new Color(255, 60, 60)));
        listContainer.add(Box.createVerticalStrut(15));
        listContainer.add(buildScheduleCard("센 강 유람선", "2025-11-16 18:00", "파리, 프랑스", "센 강 야경 투어", new Color(20, 120, 255)));

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(tripTitle);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel buildScheduleCard(String title, String datetime, String location, String description, Color dotColor) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(CARD_COLOR);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // 좌측 도트
        JLabel dot = new JLabel("●");
        dot.setForeground(dotColor);
        dot.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 10));

        // 중앙 텍스트 영역
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 16));
        t.setForeground(TITLE_COLOR);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dt = new JLabel("🕒 " + datetime + "  © " + location);
        dt.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 13));
        dt.setForeground(new Color(90, 90, 110));
        dt.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel d = new JLabel(description);
        d.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 13));
        d.setForeground(new Color(100, 100, 100));
        d.setBorder(new EmptyBorder(5, 0, 0, 0));
        d.setAlignmentX(Component.LEFT_ALIGNMENT);

        inner.add(t);
        inner.add(Box.createVerticalStrut(6));
        inner.add(dt);
        inner.add(d);

        // 우측 버튼 (수정/삭제)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btnPanel.setOpaque(false);

        JButton editBtn = new JButton("✏");
        JButton deleteBtn = new JButton("🗑");

        // 버튼 스타일링
        editBtn.setFocusPainted(false); editBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        deleteBtn.setFocusPainted(false); deleteBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);

        card.add(dot, BorderLayout.WEST);
        card.add(inner, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.EAST);

        return card;
    }
}