package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainPane extends JPanel {

    JPanel root;
    CardLayout card;

    private final String CUSTOM_FONT_NAME = "맑은 고딕";

    private JPanel mainPanel;
    private JPanel sidebar;
    private JPanel contentPanel;

    private boolean isSidebarVisible = false;
    private final int SLIDE_WIDTH = 180;
    private final int ANIMATION_STEPS = 20;
    private final int ANIMATION_DELAY_MS = 20;

    private final Color BG_COLOR = new Color(240, 247, 255);
    private final Color CARD_COLOR = new Color(255, 255, 255);
    private final Color BORDER_COLOR = new Color(200, 200, 200);
    private final Color TITLE_COLOR = new Color(40, 60, 90);
    private final Color BLUE_ACCENT = new Color(80, 140, 255);

    private List<ScheduleItem> scheduleList = new ArrayList<>();
    private JPanel scheduleCardContainer;
    private JPanel scheduleEmptyMessagePanel;
    private JPanel scheduleListAreaCard;

    public static class ScheduleItem {
        String title;
        String date;
        String time;
        String location;
        String description;
        Color color;

        public ScheduleItem(String title, String date, String time, String location, String description, Color color) {
            this.title = title;
            this.date = date;
            this.time = time;
            this.location = location;
            this.description = description;
            this.color = color;
        }
    }

    public MainPane(JPanel root, CardLayout card) {
        this.root = root;
        this.card = card;
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        mainPanel = setupMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // 기존에 샘플 데이터를 넣던 부분을 제거했습니다. (초기 리스트는 비어있음)
    }

    public JPanel setupMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel header = buildHeader();
        header.setBackground(Color.WHITE);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        sidebar = buildSidebar();
        sidebar.setPreferredSize(new Dimension(0, 0));

        JPanel center = buildCalendarAndScheduleArea();

        panel.add(header, BorderLayout.NORTH);
        contentPanel.add(sidebar, BorderLayout.WEST);
        contentPanel.add(center, BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    public void addScheduleItem(ScheduleItem item) {
        scheduleList.add(item);
        refreshScheduleListUI();
    }

    private void refreshScheduleListUI() {
        scheduleCardContainer.removeAll();

        if (scheduleList.isEmpty()) {
            scheduleCardContainer.add(scheduleEmptyMessagePanel);
        } else {
            for (ScheduleItem item : scheduleList) {
                scheduleCardContainer.add(buildScheduleCard(item));
                scheduleCardContainer.add(Box.createVerticalStrut(15));
            }
            if (scheduleCardContainer.getComponentCount() > 0) {
                scheduleCardContainer.remove(scheduleCardContainer.getComponentCount() - 1);
            }
        }

        scheduleCardContainer.revalidate();
        scheduleCardContainer.repaint();
    }

    private void toggleSidebar() {
        final int startWidth = isSidebarVisible ? SLIDE_WIDTH : 0;
        final int endWidth = isSidebarVisible ? 0 : SLIDE_WIDTH;
        final int delta = (endWidth - startWidth) / ANIMATION_STEPS;

        Timer timer = new Timer(ANIMATION_DELAY_MS, null);

        timer.addActionListener(e -> {
            int currentWidth = sidebar.getPreferredSize().width;
            int newWidth = currentWidth + delta;

            if ((delta > 0 && newWidth >= endWidth) || (delta < 0 && newWidth <= endWidth)) {
                newWidth = endWidth;
                ((Timer)e.getSource()).stop();
                isSidebarVisible = !isSidebarVisible;
            }

            sidebar.setPreferredSize(new Dimension(newWidth, sidebar.getHeight()));
            sidebar.revalidate();
            mainPanel.repaint();
        });

        if (timer.isRunning()) {
            timer.stop();
        }
        timer.start();
    }


    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 10, 15, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JLabel menuIcon = new JLabel("☰");
        menuIcon.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 26));
        menuIcon.setForeground(TITLE_COLOR);

        menuIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSidebar();
            }
        });

        JLabel title = new JLabel("여행가요");
        title.setForeground(TITLE_COLOR);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 26));

        leftPanel.add(menuIcon);
        leftPanel.add(title);

        JButton addBtn = new JButton("여행지 구경하기");
        addBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        addBtn.setFocusPainted(false);
        addBtn.setBackground(BLUE_ACCENT);
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addBtn.setPreferredSize(new Dimension(150, 40));

        addBtn.addActionListener(e -> {
            card.show(root, "여행지 검색");
        });


        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(addBtn, BorderLayout.EAST);

        return panel;
    }

    private JPanel buildSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 20, 50, 20));

        panel.setOpaque(true);
        panel.setBackground(CARD_COLOR);

        Font sideFont = new Font(CUSTOM_FONT_NAME, Font.BOLD, 14);

        panel.add(sidebarItem("👤", "친구 관리", sideFont));
        panel.add(Box.createVerticalStrut(10));
        panel.add(sidebarItem("🗓", "일정 관리", sideFont));
        panel.add(Box.createVerticalStrut(10));
        panel.add(sidebarItem("⚙", "유저 정보", sideFont));

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel sidebarItem(String icon, String text, Font font) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        itemPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 18));
        iconLabel.setForeground(TITLE_COLOR);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(font);
        textLabel.setForeground(TITLE_COLOR);

        itemPanel.add(iconLabel);
        itemPanel.add(textLabel);

        return itemPanel;
    }

    private JPanel buildCalendarAndScheduleArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel calendarCard = buildCalendarCard();
        calendarCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendarCard.setMaximumSize(new Dimension(800, 450));

        JPanel scheduleCard = buildScheduleCardArea();
        scheduleCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        scheduleCard.setMaximumSize(new Dimension(800, 300));

        panel.add(calendarCard);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scheduleCard);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel buildCalendarCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 30, 20, 30)
        ));

        Font boldFont = new Font(CUSTOM_FONT_NAME, Font.BOLD, 15);
        Font plainFont = new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14);

        JPanel monthHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        monthHeader.setOpaque(false);
        JLabel yearLabel = new JLabel("2025");
        yearLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 22));

        JPanel monthNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        monthNav.setOpaque(false);
        JLabel prevMonth = new JLabel("◀ November");
        prevMonth.setFont(boldFont);
        JLabel nextMonth = new JLabel("►");
        nextMonth.setFont(boldFont);
        monthNav.add(prevMonth);
        monthNav.add(nextMonth);

        JPanel topHeader = new JPanel();
        topHeader.setLayout(new BoxLayout(topHeader, BoxLayout.Y_AXIS));
        topHeader.setOpaque(false);
        yearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        monthNav.setAlignmentX(Component.CENTER_ALIGNMENT);
        topHeader.add(yearLabel);
        topHeader.add(monthNav);

        JPanel calendarGrid = new JPanel(new GridLayout(7, 7, 5, 10));
        calendarGrid.setOpaque(false);

        String[] days = {"월", "화", "수", "목", "금", "토", "일"};
        for (int i = 0; i < 7; i++) {
            JLabel dayLabel = new JLabel(days[i], SwingConstants.CENTER);
            dayLabel.setFont(boldFont);
            if (i >= 5) {
                dayLabel.setForeground(i == 5 ? new Color(255, 100, 100) : new Color(255, 60, 60));
            } else {
                dayLabel.setForeground(TITLE_COLOR);
            }
            calendarGrid.add(dayLabel);
        }

        for (int i = 0; i < 42; i++) {
            JLabel dateLabel = new JLabel("26", SwingConstants.CENTER);
            dateLabel.setFont(plainFont);
            if (i == 24) {
                dateLabel.setForeground(Color.WHITE);
                dateLabel.setOpaque(true);
                dateLabel.setBackground(BLUE_ACCENT);
                dateLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            } else if ((i + 1) % 7 == 0) {
                dateLabel.setForeground(new Color(255, 60, 60));
            } else if (i == 25 || i == 32) {
                dateLabel.setForeground(new Color(255, 100, 100));
            } else {
                dateLabel.setForeground(TITLE_COLOR);
            }
            calendarGrid.add(dateLabel);
        }

        card.add(topHeader, BorderLayout.NORTH);
        card.add(calendarGrid, BorderLayout.CENTER);

        return card;
    }

    private JPanel buildScheduleCardArea() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(20, 30, 20, 30)
        ));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel dateTitle = new JLabel("🗓 2025년 11월 6일");
        dateTitle.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 15));
        dateTitle.setForeground(TITLE_COLOR);

        JButton addBtn = new JButton("일정 추가");
        addBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        addBtn.setFocusPainted(false);
        addBtn.setBackground(BLUE_ACCENT);
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        addBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ScheduleAddDialog dialog = new ScheduleAddDialog(parentFrame, CUSTOM_FONT_NAME, this);
            dialog.setVisible(true);
        });

        top.add(dateTitle, BorderLayout.WEST);
        top.add(addBtn, BorderLayout.EAST);

        scheduleCardContainer = new JPanel();
        scheduleCardContainer.setLayout(new BoxLayout(scheduleCardContainer, BoxLayout.Y_AXIS));
        scheduleCardContainer.setOpaque(false);
        scheduleCardContainer.setBorder(new EmptyBorder(20, 0, 0, 0));

        scheduleEmptyMessagePanel = buildEmptyScheduleMessage();

        JScrollPane scrollPane = new JScrollPane(scheduleCardContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_COLOR);

        card.add(top, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        this.scheduleListAreaCard = card;

        refreshScheduleListUI(); // 초기 상태: 비어있는 메시지 표시

        return card;
    }

    private JPanel buildScheduleCard(ScheduleItem item) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(CARD_COLOR);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel dot = new JLabel("●");
        dot.setForeground(item.color);
        dot.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 10));

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t = new JLabel(item.title);
        t.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 17));
        t.setForeground(TITLE_COLOR);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dt = new JLabel("🕒 " + item.date + " " + item.time + " · " + item.location);
        dt.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 13));
        dt.setForeground(new Color(90, 90, 110));
        dt.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel d = new JLabel("ⓘ " + item.description);
        d.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 13));
        d.setForeground(new Color(90, 90, 110));
        d.setAlignmentX(Component.LEFT_ALIGNMENT);

        inner.add(t);
        inner.add(Box.createVerticalStrut(6));
        inner.add(dt);
        inner.add(Box.createVerticalStrut(3));
        inner.add(d);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btnPanel.setOpaque(false);

        JButton editBtn = createIconButton("✏");
        JButton deleteBtn = createIconButton("❌");

        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);

        card.add(dot, BorderLayout.WEST);
        card.add(inner, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.EAST);

        return card;
    }

    private JButton createIconButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(150, 150, 150));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return btn;
    }

    private JPanel buildEmptyScheduleMessage() {
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(50, 0, 50, 0));

        JLabel emptyIcon = new JLabel("🗓");
        emptyIcon.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 40));
        emptyIcon.setForeground(new Color(180, 180, 180));
        emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msg1 = new JLabel("이 날짜에 예정된 일정이 없습니다.", SwingConstants.CENTER);
        msg1.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        msg1.setForeground(new Color(100, 100, 100));
        msg1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msg2 = new JLabel("새로운 여행 일정을 추가해보세요!", SwingConstants.CENTER);
        msg2.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        msg2.setForeground(new Color(100, 100, 100));
        msg2.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(emptyIcon);
        center.add(Box.createVerticalStrut(10));
        center.add(msg1);
        center.add(msg2);

        return center;
    }
}