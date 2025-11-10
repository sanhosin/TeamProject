package main.java.imageSetting;

import javax.swing.*;
import java.awt.Image;
import java.awt.Graphics;

public class ImagePanel extends JPanel{
    private Image bg;

    public ImagePanel(String bgPath){
        bg=new ImageIcon(bgPath).getImage();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g){
        g.drawImage(bg, 0, 0, getWidth(),getHeight(),this);
        super.paintComponent(g);
    }
}
