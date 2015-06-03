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

public class GameOver extends JPanel implements ActionListener, KeyListener
{
    private final int game_screen_width = 960;
    private final int game_screen_height = 640;

    private BufferedImage background;

    private CustomShapedButton continue_button;
    private CustomShapedButton restart_button;

    private Font game_over_font;

    private String game_over_text;

    private int game_over_state;

    private Clip click_sound;

    public GameOver() throws IOException, LineUnavailableException, UnsupportedAudioFileException
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
        continue_button = new CustomShapedButton(ImageIO.read(new File("GameResources/GameOver/continue.png")));
        restart_button = new CustomShapedButton(ImageIO.read(new File("GameResources/GameOver/restart.png")));

        game_over_font = new Font("Jokerman", Font.ITALIC, 40);
        background = ImageIO.read(new File("GameResources/GameOver/background.jpg"));
        game_over_text = "Game Over!";
        game_over_state = 0;
    }

    private void setButtons()
    {
        continue_button.setBounds(game_screen_width - 60 - 5, game_screen_height - 60 - 5, 60, 60);
        continue_button.setActionCommand("exit");
        continue_button.addActionListener(this);
        continue_button.addKeyListener(this);
        continue_button.setFocusable(false);
        add(continue_button);

        restart_button.setBounds(5, game_screen_height - 60 - 5, 60, 60);
        restart_button.setActionCommand("restart");
        restart_button.addActionListener(this);
        restart_button.addKeyListener(this);
        restart_button.setFocusable(false);
        add(restart_button);
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
        drawer.setFont(game_over_font);
        drawer.drawString(game_over_text, (game_screen_width / 2) - ((int)drawer.getFontMetrics().getStringBounds(game_over_text, drawer).getWidth() / 2), (int)drawer.getFontMetrics().getStringBounds(game_over_text, drawer).getHeight());
    }

    public int getGame_over_state()
    {
        return game_over_state;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        CustomShapedButton source = (CustomShapedButton) event.getSource();
        if(event.getActionCommand().equals("restart") && source.getIs_click_correct())
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            game_over_state = 1;
            source.setIs_click_correct(false);
        }

        if(event.getActionCommand().equals("exit") && source.getIs_click_correct())
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            game_over_state = 2;
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
            game_over_state = 2;
        }

        if (event.getKeyCode() == KeyEvent.VK_F5)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            game_over_state = 1;
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {}
}