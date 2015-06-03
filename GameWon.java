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

public class GameWon extends JPanel implements ActionListener, KeyListener
{
    private final int game_screen_width = 960;
    private final int game_screen_height = 640;

    private BufferedImage background;

    private CustomShapedButton continue_button;
    private CustomShapedButton restart_button;

    private Font game_won_font;

    private String game_won_text;

    private int game_won_state;

    private Clip click_sound;

    private String winner;

    public GameWon() throws IOException, LineUnavailableException, UnsupportedAudioFileException
    {
        setLayout(null);
        setPreferredSize(new Dimension(game_screen_width, game_screen_height));
        addKeyListener(this);
        setFocusable(true);

        construct_components();

        setButtons();

        click_sound = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/clickSound.wav")).getFormat()));
        click_sound.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/clickSound.wav")));

        winner = "";
    }

    private void construct_components() throws IOException
    {
        continue_button = new CustomShapedButton(ImageIO.read(new File("GameResources/GameWon/continue.png")));
        restart_button = new CustomShapedButton(ImageIO.read(new File("GameResources/GameWon/restart.png")));

        game_won_font = new Font("Jokerman", Font.ITALIC, 40);
        background = ImageIO.read(new File("GameResources/GameWon/background.jpg"));
        game_won_text = "Congratulation ";
        game_won_state = 0;
    }

    private void setButtons()
    {
        continue_button.setBounds(game_screen_width - 60 - 5, game_screen_height - 60 - 5, 60, 60);
        continue_button.setActionCommand("continue");
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
        drawer.setFont(game_won_font);
        drawer.drawString(game_won_text + winner, (game_screen_width / 2) - ((int)drawer.getFontMetrics().getStringBounds(game_won_text + winner, drawer).getWidth() / 2), (int)drawer.getFontMetrics().getStringBounds(game_won_text + winner, drawer).getHeight());
    }

    public int getGame_won_state()
    {
        return game_won_state;
    }

    public void setWinner(String winner)
    {
        this.winner = winner;
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
            game_won_state = 1;
            source.setIs_click_correct(false);
        }

        if(event.getActionCommand().equals("continue") && source.getIs_click_correct())
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            game_won_state = 2;
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
            game_won_state = 2;
        }

        if (event.getKeyCode() == KeyEvent.VK_F5)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            game_won_state = 1;
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {}
}