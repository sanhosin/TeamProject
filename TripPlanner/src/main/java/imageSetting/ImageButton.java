package main.java.imageSetting;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class ImageButton extends JButton {
    private Image imgNormal;
    private Image imgHover;
    private Image imgPressed;
    private Image currentImage;

    public ImageButton(String normal,String hover,String pressed){
        imgNormal=new ImageIcon(normal).getImage();
        imgHover=new ImageIcon(hover).getImage();
        imgPressed=new ImageIcon(pressed).getImage();
        currentImage=imgNormal;

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){ currentImage=imgHover; repaint();}
            @Override
            public void mouseExited(MouseEvent e){ currentImage=imgNormal; repaint();}
            @Override
            public void mousePressed(MouseEvent e){ currentImage=imgPressed; repaint();}
            @Override
            public void mouseReleased(MouseEvent e){ currentImage=imgHover; repaint();}
        });
    }

    @Override
    protected void paintComponent(Graphics g){
        g.drawImage(currentImage, 0, 0, getWidth(),getHeight(),this);
        super.paintComponent(g);
    }
}
