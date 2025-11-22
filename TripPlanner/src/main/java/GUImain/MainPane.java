package main.java.GUImain;

import main.java.domain.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.awt.image.BufferedImage;
import java.io.File;

public class MainPane extends JPanel {

    JPanel root;
    CardLayout card;

    private final String CUSTOM_FONT_NAME = "맑은 고딕";

    private final Color BG_COLOR = new Color(240, 247, 255);
    private final Color CARD_COLOR = new Color(255, 255, 255);
    private final Color BORDER_COLOR = new Color(200, 200, 200);
    private final Color TITLE_COLOR = new Color(40, 60, 90);
    private final Color BLUE_ACCENT = new Color(80, 140, 255);

    private TravelSearchPane travelSearchPanel;
    private JPanel listContainer;
    private JScrollPane scrollPane;
    private JPanel scheduleCardContainer;
    private JPanel scheduleEmptyMessagePanel;
    private JPanel scheduleListAreaCard;


    public MainPane(JPanel root, CardLayout card) {
        this.root = root;
        this.card = card;
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        add(setupMainPanel(), BorderLayout.CENTER);
    }

    public void setTravelSearchPanel(TravelSearchPane travelSearchPanel){
        this.travelSearchPanel=travelSearchPanel;
    }

    public JPanel setupMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel header = buildHeader();
        header.setBackground(Color.WHITE);

        JPanel center = buildTripListArea();

        panel.add(header, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    
    public void refreshTripListUI() {
        if (listContainer == null) return;

        listContainer.removeAll();

        List<Trip> tripList = Main.tdm.getTripList();

        if(tripList != null) {
            for (Trip t : tripList) {
                listContainer.add(buildTripCard(t));
                // 카드 사이 간격 15픽셀
                listContainer.add(Box.createVerticalStrut(15));
            }
        }
        listContainer.revalidate();
        listContainer.repaint();
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
                // 사이드바 토글 로직
            }
        });

        JLabel title = new JLabel("여행가요");
        title.setForeground(TITLE_COLOR);
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 26));

        leftPanel.add(menuIcon);
        leftPanel.add(title);

        JButton addBtn = new JButton("여행추가");
        addBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        addBtn.setFocusPainted(false);
        addBtn.setBackground(BLUE_ACCENT);
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        addBtn.setPreferredSize(new Dimension(150, 40));

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(MainPane.this);
                AddTripDialog dialog = new AddTripDialog(parentFrame, CUSTOM_FONT_NAME, MainPane.this);
                dialog.setVisible(true);
            }
        });


        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(addBtn, BorderLayout.EAST);

        return panel;
    }

    private JPanel buildTripListArea() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel listTitle = new JLabel("✈ 나의 여행");
        listTitle.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 20));
        listTitle.setForeground(TITLE_COLOR);
        listTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        listContainer = new JPanel();
        listContainer.setOpaque(false);
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        if(Main.tdm.getLoginedUser()!=null){
            //loginedUser가 null일때 메인화면에 경고창 띄우는 기능 구현
            refreshTripListUI();
        }

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(listTitle);
        panel.add(Box.createVerticalStrut(20));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel buildTripCard(Trip t) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setPreferredSize(new Dimension(800, 100));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); //카드 높이 고정
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Main.tdm.setCurrentTrip(t);
                travelSearchPanel.setTravelSearchPaneTitle();
                travelSearchPanel.refreshPlanListUI();
                MainPane.this.card.show(root, "여행지 검색");
            }
        });

        Object[] uidata = t.getUidata();
        String tripTitle = (String)uidata[Trip.TRIP_TITLE];
        String tripImgPath = (String)uidata[Trip.TRIP_IMG];

        // 1. 데이터가 제대로 넘어오는지 확인
        System.out.println("---------------------------------");
        System.out.println("여행 제목: " + tripTitle);
        
        if(uidata.length > 1) {
            System.out.println("이미지 경로 확인: " + tripImgPath); // 경로가 null인지 여기서 확인 가능
        } else {
            System.out.println("경고: 이미지 정보가 없습니다!");
        }

        // 1. 좌측 이미지 영역
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(100, 80));
        imagePanel.setBackground(new Color(220, 220, 220));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(100, 80));
        // 경로랑 파일이 존재하면 이미지 띄우기
        if (tripImgPath != null && !tripImgPath.isEmpty()) {
            File imgFile = new File(tripImgPath);
            
            if (imgFile.exists()) {
                // 파일이 있으면 로드
                ImageIcon icon = new ImageIcon(tripImgPath);
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(100, 80, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
                System.out.println("-> 이미지 로드 성공!");
            } 
        } 

        imagePanel.add(imageLabel, BorderLayout.CENTER);
        card.add(imagePanel, BorderLayout.WEST);

        // 2. 중앙 텍스트 영역
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel((String)uidata[Trip.TRIP_TITLE]);
        nameLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 16));
        nameLabel.setForeground(TITLE_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String toLocationLabel=(t.getPlanList().isEmpty())?"미정":t.getLocationDetail();
        JLabel locationLabel = new JLabel("📍 " + toLocationLabel);
        locationLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
        locationLabel.setForeground(new Color(90, 90, 110));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String toDateLabel=(t.getPlanList().isEmpty())?"미정":t.getDateDuration();
        JLabel dateLabel = new JLabel(toDateLabel);
        dateLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
        dateLabel.setForeground(new Color(90, 90, 110));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(locationLabel);
        textPanel.add(dateLabel);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private ImageIcon loadImage(String imagePath, int width, int height) {
        return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
    }
}