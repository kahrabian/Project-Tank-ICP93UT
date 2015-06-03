package project.tank;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PauseMenu extends JPanel implements ActionListener, KeyListener
{
    private final int game_screen_width = 960;
    private final int game_screen_height = 640;

    private BufferedImage background;

    private CustomShapedButton exit_button;
    private CustomShapedButton resume_button;

    private Font pause_font;

    private String pause_text;

    private int pause_state;

    private Clip click_sound;

    public PauseMenu() throws IOException, LineUnavailableException, UnsupportedAudioFileException
    {
        setLayout(null);
        setPreferredSize(new Dimension(game_screen_width, game_screen_height));
        addKeyListener(this);
        setFocusable(true);

        construct_components();

        setButtons();

        click_sound = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/clickSound.wav")).getFormat()));
        click_sound.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/clickSound.wav")));
    }

    private void construct_components() throws IOException
    {
        exit_button = new CustomShapedButton(ImageIO.read(new File("GameResources/Pause/exit.png")));
        resume_button = new CustomShapedButton(ImageIO.read(new File("GameResources/Pause/resume.png")));

        pause_font = new Font("Jokerman", Font.ITALIC, 40);
        background = ImageIO.read(new File("GameResources/Pause/background.jpg"));
        pause_text = "Game Paused!";
        pause_state = 0;
    }

    private void setButtons()
    {
        exit_button.setBounds(5, game_screen_height - 60 - 5, 60, 60);
        exit_button.setActionCommand("exit");
        exit_button.addActionListener(this);
        exit_button.addKeyListener(this);
        exit_button.setFocusable(false);
        add(exit_button);

        resume_button.setBounds(game_screen_width - 60 - 5, game_screen_height - 60 - 5, 60, 60);
        resume_button.setActionCommand("resume");
        resume_button.addActionListener(this);
        resume_button.addKeyListener(this);
        resume_button.setFocusable(false);
        add(resume_button);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g)
    {
        Graphics2D drawer = (Graphics2D) g;

        drawer.setColor(Color.WHITE);
        drawer.drawImage(background, 0, 0, game_screen_width, game_screen_height, null);
        drawer.setFont(pause_font);
        drawer.drawString(pause_text, (game_screen_width / 2) - ((int)drawer.getFontMetrics().getStringBounds(pause_text, drawer).getWidth() / 2), (int)drawer.getFontMetrics().getStringBounds(pause_text, drawer).getHeight());
    }

    public void setPause_state(int pause_state)
    {
        this.pause_state = pause_state;
    }

    public int getPause_state()
    {
        return pause_state;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        CustomShapedButton source = (CustomShapedButton) event.getSource();
        if(event.getActionCommand().equals("resume") && source.getIs_click_correct())
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            pause_state = 1;
            source.setIs_click_correct(false);
        }

        if(event.getActionCommand().equals("exit") && source.getIs_click_correct())
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            pause_state = 2;
            source.setIs_click_correct(false);
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {}

    @Override
    public void keyReleased(KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.VK_ENTER)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            pause_state = 2;
        }
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            pause_state = 1;
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {}
}