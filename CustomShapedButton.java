package project.tank;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CustomShapedButton extends JButton implements MouseListener
{
    private BufferedImage button_image;
    private boolean is_click_correct;

    public CustomShapedButton(BufferedImage button_image)
    {
        super(new ImageIcon(button_image));

        is_click_correct = false;
        this.button_image = button_image;

        addMouseListener(this);
        setBorder(null);
        setContentAreaFilled(false);
    }

    public boolean getIs_click_correct()
    {
        return is_click_correct;
    }

    public void setIs_click_correct(boolean is_click_correct)
    {
        this.is_click_correct = is_click_correct;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(((button_image.getRGB(e.getX(), e.getY()) >>24) & 0x000000FF) != 0)
        {
            is_click_correct = true;
            doClick();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}