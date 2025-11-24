package main.java.GUImain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class AddTripDialog extends JDialog {

    private final String CUSTOM_FONT_NAME = "맑은 고딕";
    private final Color MAIN_BLUE = new Color(90, 80, 255);
    private final Color LIGHT_GRAY = new Color(220, 220, 220);
    private final Color TITLE_COLOR = new Color(40, 60, 90);

    private JTextField titleField;
    private MainPane mainPane;

    // 이미지 경로 및 라벨
    private String selectedImagePath = null;
    private JLabel imagePreviewLabel;

    public AddTripDialog(Frame owner, String fontName, MainPane mainPane) {
        super(owner, true);
        this.mainPane = mainPane;

        setTitle("새 여행 추가");
        setSize(400, 480);
        setResizable(false);
        setLocationRelativeTo(owner);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(LIGHT_GRAY, 1));

        setContentPane(buildPanel());
    }

    private JPanel buildPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(buildHeader(), BorderLayout.NORTH);
        mainPanel.add(buildContent(), BorderLayout.CENTER);
        mainPanel.add(buildButtonPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("새 여행 추가");
        title.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 20));
        title.setForeground(TITLE_COLOR);

        JLabel closeBtn = new JLabel("❌");
        closeBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 18));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        panel.add(title, BorderLayout.WEST);
        panel.add(closeBtn, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. 여행 제목 입력
        JLabel titleLabel = new JLabel("여행 제목");
        titleLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        titleLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleField = new JTextField("여행 제목을 입력하세요");
        titleField.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 14));
        titleField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LIGHT_GRAY, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 2. 사진 추가 (업로드 영역)
        JLabel photoLabel = new JLabel("사진 추가");
        photoLabel.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        photoLabel.setBorder(new EmptyBorder(15, 0, 5, 0));
        photoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel imageUploadPanel = buildImageUploadArea();
        imageUploadPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        imageUploadPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        contentPanel.add(titleLabel);
        contentPanel.add(titleField);
        contentPanel.add(photoLabel);
        contentPanel.add(imageUploadPanel);
        contentPanel.add(Box.createVerticalGlue());

        return contentPanel;
    }

    private JPanel buildImageUploadArea() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createDashedBorder(LIGHT_GRAY, 2, 4, 2, true));
        panel.setPreferredSize(new Dimension(340, 150));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 5, 0);

        JLabel icon = new JLabel("⬆");
        icon.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 30));
        icon.setForeground(LIGHT_GRAY);
        panel.add(icon, gbc);

        gbc.gridy = 1;
        JLabel text = new JLabel("클릭하여 이미지 업로드");
        text.setFont(new Font(CUSTOM_FONT_NAME, Font.PLAIN, 12));
        text.setForeground(LIGHT_GRAY);
        panel.add(text, gbc);

        // 클릭시 컴퓨터에서 이미지 찾아서 업로드 
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                // 업로드 누르면 바로 사진을 찾기 쉽도록 변경
                String userHome = System.getProperty("user.home");
                File Img_File = new File(userHome, "OneDrive/사진");
                fileChooser.setCurrentDirectory(Img_File);
        
                fileChooser.setFileFilter(new FileNameExtensionFilter("이미지 파일", "jpg", "png", "jpeg", "gif"));

                if (fileChooser.showOpenDialog(AddTripDialog.this) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    selectedImagePath = selectedFile.getAbsolutePath();

                    // 화면 갱신 로직을 여기에 직접 작성
                    panel.removeAll(); 
                    
                    ImageIcon icon = new ImageIcon(selectedImagePath);
                    Image img = icon.getImage().getScaledInstance(340, 150, Image.SCALE_SMOOTH);
                    JLabel imgLabel = new JLabel(new ImageIcon(img));
                    
                    panel.add(imgLabel);
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });

        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));
        panel.setBackground(Color.WHITE);

        JButton cancelBtn = new JButton("취소");
        cancelBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(70, 35));
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setForeground(TITLE_COLOR);
        cancelBtn.setBorder(BorderFactory.createLineBorder(LIGHT_GRAY));
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> dispose());

        JButton addBtn = new JButton("추가");
        addBtn.setFont(new Font(CUSTOM_FONT_NAME, Font.BOLD, 16));
        addBtn.setPreferredSize(new Dimension(70, 35));
        addBtn.setBackground(MAIN_BLUE);
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText().trim();
                
                if (title.isEmpty() || title.equals("여행 제목을 입력하세요")) {
                    JOptionPane.showMessageDialog(AddTripDialog.this, "여행 제목을 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Object[] uidata = { title, selectedImagePath };

               // 삭제 :  String imagePath=null; //이미지 경로 입력되도록 수정
                Main.tdm.addTrip(uidata);
                mainPane.refreshTripListUI();
                dispose();
            }
        });

        panel.add(cancelBtn);
        panel.add(addBtn);
        return panel;
    }
}