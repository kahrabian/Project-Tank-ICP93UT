package project.tank;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Menu extends JFrame implements ActionListener, ItemListener, KeyListener
{
    private JRadioButton cpu_mode;
    private JRadioButton player_mode;
    private ButtonGroup mode;

    private JRadioButton difficulty_easy;
    private JRadioButton difficulty_normal;
    private JRadioButton difficulty_hard;
    private ButtonGroup difficulty;

    private JRadioButton condition_cloudy;
    private JRadioButton condition_night;
    private JRadioButton condition_day;
    private ButtonGroup condition;

    private BasicArrowButton texture_next;
    private BasicArrowButton texture_previous;

    private BasicArrowButton map_next;
    private BasicArrowButton map_previous;

    private JLabel texture_text;
    private JLabel map_text;
    private JLabel mode_text;
    private JLabel difficulty_text;
    private JLabel condition_text;
    private JLabel turn_time_text;
    private JLabel player_text;
    private JLabel cpu_text;

    private JLabel game_logo;

    public Map_Demo map;

    private CustomShapedButton exit_button;
    private CustomShapedButton start_button;

    private JSpinner turn_time_spinner;
    private JFormattedTextField turn_time_spinner_editable;

    private JSpinner cpu_player_spinner;
    private JFormattedTextField cpu_player_spinner_editable;

    private JSpinner user_player_spinner;
    private JFormattedTextField user_player_spinner_editable;


    private JPanel mode_panel;
    private JPanel difficulty_panel;
    private JPanel condition_panel;
    private JPanel turn_time_panel;
    private JPanel user_player_panel;
    private JPanel cpu_player_panel;
    private JPanel exit_panel;
    private JPanel texture_panel;
    private JPanel map_panel;
    private JPanel edit_panel;
    private JPanel start_panel;

    private JPanel left_panel;
    private JPanel right_panel;

    private JPanel top_panel;
    private JPanel bottom_panel;

    private JPanel menu_panel;

    private int difficulty_level;
    private int game_start;
    private int game_mode;

    private Clip click_sound;


    public Menu() throws IOException, LineUnavailableException, UnsupportedAudioFileException
    {
        setFocusable(true);
        addKeyListener(this);

        difficulty_level = 1;
        game_start = 0;
        game_mode = 1;

        construct_components();

        turn_time_spinner_editable.setEditable(false);
        cpu_player_spinner_editable.setEditable(false);
        user_player_spinner_editable.setEditable(false);

        setButtons();

        setRadiobuttons();

        construct_menu_panel();

        add(menu_panel);

        click_sound = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/clickSound.wav")).getFormat()));
        click_sound.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/clickSound.wav")));

        setLayout(new FlowLayout());
        setUndecorated(true);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }

    private void construct_components() throws IOException
    {
        cpu_mode = new JRadioButton("Cpu vs. Player", new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_deselected.png"))), true);
        cpu_mode.setSelectedIcon(new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_selected.png"))));
        cpu_mode.setFocusable(false);
        cpu_mode.addKeyListener(this);

        player_mode = new JRadioButton("Player vs. Player", new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_deselected.png"))), false);
        player_mode.setSelectedIcon(new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_selected.png"))));
        player_mode.setFocusable(false);
        player_mode.addKeyListener(this);

        mode = new ButtonGroup();

        difficulty_easy = new JRadioButton("Easy", new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_deselected.png"))), true);
        difficulty_easy.setSelectedIcon(new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_selected.png"))));
        difficulty_easy.setFocusable(false);
        difficulty_easy.addKeyListener(this);

        difficulty_normal = new JRadioButton("Normal", new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_deselected.png"))), false);
        difficulty_normal.setSelectedIcon(new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_selected.png"))));
        difficulty_normal.setFocusable(false);
        difficulty_easy.addKeyListener(this);

        difficulty_hard = new JRadioButton("Hard", new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_deselected.png"))), false);
        difficulty_hard.setSelectedIcon(new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_selected.png"))));
        difficulty_hard.setFocusable(false);
        difficulty_hard.addKeyListener(this);

        difficulty = new ButtonGroup();

        condition_cloudy = new JRadioButton("Cloudy", new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_deselected.png"))), true);
        condition_cloudy.setSelectedIcon(new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_selected.png"))));
        condition_cloudy.setFocusable(false);
        condition_cloudy.addKeyListener(this);

        condition_night = new JRadioButton("Night", new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_deselected.png"))), false);
        condition_night.setSelectedIcon(new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_selected.png"))));
        condition_night.setFocusable(false);
        condition_night.addKeyListener(this);

        condition_day = new JRadioButton("Day", new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_deselected.png"))), false);
        condition_day.setSelectedIcon(new ImageIcon(ImageIO.read(new File("GameResources/radiobutton_selected.png"))));
        condition_day.setFocusable(false);
        condition_day.addKeyListener(this);

        condition = new ButtonGroup();

        texture_next = new BasicArrowButton(BasicArrowButton.EAST);
        texture_next.setFocusable(false);
        texture_next.addKeyListener(this);

        texture_previous = new BasicArrowButton(BasicArrowButton.WEST);
        texture_previous.setFocusable(false);
        texture_previous.addKeyListener(this);

        map_next = new BasicArrowButton(BasicArrowButton.EAST);
        map_next.setFocusable(false);
        map_next.addKeyListener(this);

        map_previous = new BasicArrowButton(BasicArrowButton.WEST);
        map_previous.setFocusable(false);
        map_previous.addKeyListener(this);

        texture_text = new JLabel("Change Map Texture");
        texture_text.setHorizontalAlignment(JLabel.CENTER);
        texture_text.setVerticalAlignment(JLabel.CENTER);
        texture_text.setFocusable(false);
        texture_text.addKeyListener(this);

        map_text = new JLabel("Change Map");
        map_text.setHorizontalAlignment(JLabel.CENTER);
        map_text.setVerticalAlignment(JLabel.CENTER);
        map_text.setFocusable(false);
        map_text.addKeyListener(this);

        mode_text = new JLabel("Game Mode:");
        mode_text.setHorizontalAlignment(JLabel.CENTER);
        mode_text.setVerticalAlignment(JLabel.CENTER);
        mode_text.setFocusable(false);
        mode_text.addKeyListener(this);

        difficulty_text = new JLabel("Game Difficulty:");
        difficulty_text.setHorizontalAlignment(JLabel.CENTER);
        difficulty_text.setVerticalAlignment(JLabel.CENTER);
        difficulty_text.setFocusable(false);
        difficulty_text.addKeyListener(this);

        condition_text = new JLabel("Game Condition:");
        condition_text.setHorizontalAlignment(JLabel.CENTER);
        condition_text.setVerticalAlignment(JLabel.CENTER);
        condition_text.setFocusable(false);
        condition_text.addKeyListener(this);

        turn_time_text = new JLabel("Turn Time:");
        turn_time_text.setHorizontalAlignment(JLabel.CENTER);
        turn_time_text.setVerticalAlignment(JLabel.CENTER);
        turn_time_text.setFocusable(false);
        turn_time_text.addKeyListener(this);

        player_text = new JLabel("User Players:");
        player_text.setHorizontalAlignment(JLabel.CENTER);
        player_text.setVerticalAlignment(JLabel.CENTER);
        player_text.setFocusable(false);
        player_text.addKeyListener(this);

        cpu_text = new JLabel("CPU Players:");
        cpu_text.setHorizontalAlignment(JLabel.CENTER);
        cpu_text.setVerticalAlignment(JLabel.CENTER);
        cpu_text.setFocusable(false);
        cpu_text.addKeyListener(this);

        game_logo = new JLabel(new ImageIcon(ImageIO.read(new File("GameResources/logo.png"))));
        game_logo.setHorizontalAlignment(JLabel.CENTER);
        game_logo.setVerticalAlignment(JLabel.CENTER);
        game_logo.setFocusable(false);
        game_logo.addKeyListener(this);

        map = new Map_Demo();
        map.setFocusable(false);
        map.addKeyListener(this);

        exit_button = new CustomShapedButton(ImageIO.read(new File("GameResources/exit.png")));
        exit_button.setFocusable(false);
        exit_button.addKeyListener(this);

        start_button = new CustomShapedButton(ImageIO.read(new File("GameResources/start.png")));
        start_button.setFocusable(false);
        start_button.addKeyListener(this);

        SpinnerModel turn_time_model = new SpinnerNumberModel(30, 30, 120, 5);
        turn_time_spinner = new JSpinner(turn_time_model);
        turn_time_spinner.setFocusable(false);
        turn_time_spinner.addKeyListener(this);
        turn_time_spinner.setBorder(null);
        turn_time_spinner_editable = ((JSpinner.DefaultEditor) turn_time_spinner.getEditor()).getTextField();

        SpinnerModel cpu_player_model = new SpinnerNumberModel(1, 1, 5, 1);
        cpu_player_spinner = new JSpinner(cpu_player_model);
        cpu_player_spinner.setFocusable(false);
        cpu_player_spinner.addKeyListener(this);
        cpu_player_spinner.setBorder(null);
        cpu_player_spinner_editable = ((JSpinner.DefaultEditor) cpu_player_spinner.getEditor()).getTextField();

        SpinnerModel user_player_model = new SpinnerNumberModel(1, 1, 5, 1);
        user_player_spinner = new JSpinner(user_player_model);
        user_player_spinner.setFocusable(false);
        user_player_spinner.addKeyListener(this);
        user_player_spinner.setBorder(null);
        user_player_spinner_editable = ((JSpinner.DefaultEditor) user_player_spinner.getEditor()).getTextField();

        mode_panel = new JPanel();
        mode_panel.setFocusable(false);
        mode_panel.addKeyListener(this);

        difficulty_panel = new JPanel();
        difficulty_panel.setFocusable(false);
        difficulty_panel.addKeyListener(this);

        condition_panel = new JPanel();
        condition_panel.setFocusable(false);
        condition_panel.addKeyListener(this);

        turn_time_panel = new JPanel();
        turn_time_panel.setFocusable(false);
        turn_time_panel.addKeyListener(this);

        user_player_panel = new JPanel();
        user_player_panel.setFocusable(false);
        user_player_panel.addKeyListener(this);

        cpu_player_panel = new JPanel();
        cpu_player_panel.setFocusable(false);
        cpu_player_panel.addKeyListener(this);

        exit_panel = new JPanel();
        exit_panel.setFocusable(false);
        exit_panel.addKeyListener(this);

        texture_panel = new JPanel();
        texture_panel.setFocusable(false);
        texture_panel.addKeyListener(this);

        JPanel map_demo_panel = new JPanel();
        map_demo_panel.setFocusable(false);
        map_demo_panel.addKeyListener(this);

        map_panel = new JPanel();
        map_panel.setFocusable(false);
        map_panel.addKeyListener(this);

        edit_panel = new JPanel();
        edit_panel.setFocusable(false);
        edit_panel.addKeyListener(this);

        start_panel = new JPanel();
        start_panel.setFocusable(false);
        start_panel.addKeyListener(this);

        left_panel = new JPanel();
        left_panel.setFocusable(false);
        left_panel.addKeyListener(this);

        right_panel = new JPanel();
        right_panel.setFocusable(false);
        right_panel.addKeyListener(this);

        top_panel = new JPanel();
        top_panel.setFocusable(false);
        top_panel.addKeyListener(this);

        bottom_panel = new JPanel();
        bottom_panel.setFocusable(false);
        bottom_panel.addKeyListener(this);

        menu_panel = new JPanel();
        menu_panel.setFocusable(false);
        menu_panel.addKeyListener(this);
    }

    private void setButtons()
    {
        texture_next.setActionCommand("texture_next");
        texture_next.addActionListener(this);

        texture_previous.setActionCommand("texture_previous");
        texture_previous.addActionListener(this);

        map_next.setActionCommand("map_next");
        map_next.addActionListener(this);

        map_previous.setActionCommand("map_previous");
        map_previous.addActionListener(this);

        exit_button.setActionCommand("exit");
        exit_button.addActionListener(this);

        start_button.setActionCommand("start");
        start_button.addActionListener(this);
    }

    private void setRadiobuttons()
    {
        cpu_mode.setName("cpu_mode");
        cpu_mode.addItemListener(this);

        player_mode.setName("player_mode");
        player_mode.addItemListener(this);

        difficulty_easy.setName("difficulty_easy");
        difficulty_easy.addItemListener(this);
        difficulty_normal.setName("difficulty_normal");
        difficulty_normal.addItemListener(this);
        difficulty_hard.setName("difficulty_hard");
        difficulty_hard.addItemListener(this);

        condition_cloudy.setName("condition_cloudy");
        condition_cloudy.addItemListener(this);
        condition_night.setName("condition_night");
        condition_night.addItemListener(this);
        condition_day.setName("condition_day");
        condition_day.addItemListener(this);
    }

    private void construct_menu_panel()
    {
        construct_top_panel();
        construct_bottom_panel();

        menu_panel.setLayout(new BoxLayout(menu_panel, BoxLayout.Y_AXIS));
        menu_panel.add(top_panel);
        menu_panel.add(bottom_panel);
    }

    private void construct_top_panel()
    {
        construct_left_panel();
        construct_right_panel();

        top_panel.setLayout(new BoxLayout(top_panel, BoxLayout.X_AXIS));
        top_panel.add(left_panel);
        top_panel.add(Box.createRigidArea(new Dimension(50,0)));
        top_panel.add(Box.createHorizontalGlue());
        top_panel.add(right_panel);
    }

    private void construct_left_panel()
    {
        construct_mode_panel();
        construct_condition_panel();
        construct_difficulty_panel();
        construct_turn_time_panel();
        construct_cpu_player_panel();
        construct_user_player_panel();

        left_panel.setLayout(new BoxLayout(left_panel, BoxLayout.Y_AXIS));
        left_panel.add(Box.createRigidArea(new Dimension(0,15)));
        left_panel.add(game_logo);
        left_panel.add(Box.createRigidArea(new Dimension(0,15)));
        left_panel.add(mode_panel);
        left_panel.add(Box.createRigidArea(new Dimension(0,5)));
        left_panel.add(condition_panel);
        left_panel.add(Box.createRigidArea(new Dimension(0,5)));
        left_panel.add(difficulty_panel);
        left_panel.add(Box.createRigidArea(new Dimension(0,5)));
        left_panel.add(cpu_player_panel);
        left_panel.add(Box.createRigidArea(new Dimension(0,5)));
        left_panel.add(user_player_panel);
        left_panel.add(Box.createRigidArea(new Dimension(0,5)));
        left_panel.add(turn_time_panel);
    }

    private void construct_mode_panel()
    {
        mode.add(cpu_mode);
        mode.add(player_mode);
        mode_panel.setLayout(new BoxLayout(mode_panel, BoxLayout.Y_AXIS));
        mode_panel.add(mode_text);
        mode_panel.add(cpu_mode);
        mode_panel.add(player_mode);
    }

    private void construct_user_player_panel()
    {
        user_player_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        user_player_panel.add(player_text);
        user_player_panel.add(user_player_spinner);
    }

    private void construct_difficulty_panel()
    {
        difficulty.add(difficulty_easy);
        difficulty.add(difficulty_normal);
        difficulty.add(difficulty_hard);
        difficulty_panel.setLayout(new BoxLayout(difficulty_panel, BoxLayout.Y_AXIS));
        difficulty_panel.add(difficulty_text);
        difficulty_panel.add(difficulty_easy);
        difficulty_panel.add(difficulty_normal);
        difficulty_panel.add(difficulty_hard);
    }

    private void construct_cpu_player_panel()
    {
        cpu_player_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        cpu_player_panel.add(cpu_text);
        cpu_player_panel.add(cpu_player_spinner);
    }

    private void construct_condition_panel()
    {
        condition.add(condition_cloudy);
        condition.add(condition_day);
        condition.add(condition_night);
        condition_panel.setLayout(new BoxLayout(condition_panel, BoxLayout.Y_AXIS));
        condition_panel.add(condition_text);
        condition_panel.add(condition_cloudy);
        condition_panel.add(condition_night);
        condition_panel.add(condition_day);
    }

    private void construct_turn_time_panel()
    {
        turn_time_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        turn_time_panel.add(turn_time_text);
        turn_time_panel.add(turn_time_spinner);
    }

    private void construct_right_panel()
    {
        construct_edit_panel();

        right_panel.setLayout(new BoxLayout(right_panel, BoxLayout.Y_AXIS));
        right_panel.add(map);
        right_panel.add(edit_panel);
    }

    private void construct_edit_panel()
    {
        construct_texture_panel();
        construct_map_panel();

        edit_panel.setLayout(new BoxLayout(edit_panel, BoxLayout.X_AXIS));
        edit_panel.add(texture_panel);
        edit_panel.add(map_panel);
    }

    private void construct_texture_panel()
    {
        texture_panel.setLayout(new FlowLayout());
        texture_panel.add(texture_previous);
        texture_panel.add(texture_text);
        texture_panel.add(texture_next);
    }

    private void construct_map_panel()
    {
        map_panel.setLayout(new FlowLayout());
        map_panel.add(map_previous);
        map_panel.add(map_text);
        map_panel.add(map_next);
    }

    private void construct_bottom_panel()
    {
        construct_start_panel();
        construct_exit_panel();

        bottom_panel.setLayout(new BoxLayout(bottom_panel, BoxLayout.X_AXIS));
        bottom_panel.add(start_panel);
        bottom_panel.add(exit_panel);
    }

    private void construct_start_panel()
    {
        start_panel.setLayout(new FlowLayout());
        start_panel.add(start_button);
    }

    private void construct_exit_panel()
    {
        exit_panel.setLayout(new FlowLayout());
        exit_panel.add(exit_button);
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        click_sound.stop();
        click_sound.setFramePosition(0);
        click_sound.start();

        if(event.getActionCommand().equals("start"))
        {
            CustomShapedButton source = (CustomShapedButton) event.getSource();
            if(source.getIs_click_correct())
            {
                if(game_mode == 2)
                {
                    if((Integer)user_player_spinner.getValue() >= 2)
                    {
                        game_start = 1;
                    }
                }

                if(game_mode == 1)
                {
                    if((Integer)user_player_spinner.getValue() + (Integer)cpu_player_spinner.getValue() >= 2)
                    {
                        game_start = 1;
                    }
                }
                source.setIs_click_correct(false);
            }
        }

        if(event.getActionCommand().equals("exit"))
        {
            CustomShapedButton source = (CustomShapedButton) event.getSource();
            if(source.getIs_click_correct())
            {
                System.exit(0);
                source.setIs_click_correct(false);
            }
        }

        if(event.getActionCommand().equals("texture_next"))
            map.setMap_texture_selected((map.getMap_texture_selected() + 1) % map.getMap_texture_count());

        if(event.getActionCommand().equals("texture_previous"))
            if(map.getMap_texture_selected() == 0)
                map.setMap_texture_selected(map.getMap_texture_count() - 1);
            else
                map.setMap_texture_selected((map.getMap_texture_selected() - 1) % map.getMap_texture_count());

        if(event.getActionCommand().equals("map_next"))
            map.setMap_selected((map.getMap_selected() + 1) % map.getMap_count());

        if(event.getActionCommand().equals("map_previous"))
            if(map.getMap_selected() == 0)
                map.setMap_selected(map.getMap_count() - 1);
            else
                map.setMap_selected((map.getMap_selected() - 1) % map.getMap_count());
    }

    @Override
    public void itemStateChanged(ItemEvent event)
    {
        JRadioButton changed_radiobutton = (JRadioButton)event.getSource();
        if (event.getStateChange() == ItemEvent.SELECTED && changed_radiobutton.getName().equals("condition_cloudy"))
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            map.setGame_background_time(3);
        }

        if (event.getStateChange() == ItemEvent.SELECTED && changed_radiobutton.getName().equals("condition_night"))
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            map.setGame_background_time(2);
        }

        if (event.getStateChange() == ItemEvent.SELECTED && changed_radiobutton.getName().equals("condition_day"))
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            map.setGame_background_time(1);
        }

        if (event.getStateChange() == ItemEvent.SELECTED && changed_radiobutton.getName().equals("cpu_mode"))
        {
            game_mode = 1;
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            difficulty_panel.setVisible(true);
            cpu_player_panel.setVisible(true);

            cpu_player_spinner.setValue(1);
        }

        if (event.getStateChange() == ItemEvent.SELECTED && changed_radiobutton.getName().equals("player_mode"))
        {
            game_mode = 2;
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            difficulty_panel.setVisible(false);
            cpu_player_panel.setVisible(false);

            cpu_player_spinner.setValue(0);
        }

        if (event.getStateChange() == ItemEvent.SELECTED && changed_radiobutton.getName().equals("difficulty_easy"))
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            difficulty_level = 1;
        }

        if (event.getStateChange() == ItemEvent.SELECTED && changed_radiobutton.getName().equals("difficulty_normal"))
        {
            click_sound.setFramePosition(0);
            click_sound.start();
            difficulty_level = 2;
        }

        if (event.getStateChange() == ItemEvent.SELECTED && changed_radiobutton.getName().equals("difficulty_hard"))
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            difficulty_level = 3;
        }
    }

    @Override
    public void keyPressed(KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.VK_UP)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            map.setMap_texture_selected((map.getMap_texture_selected() + 1) % map.getMap_texture_count());
        }
        if (event.getKeyCode() == KeyEvent.VK_DOWN)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            if(map.getMap_texture_selected() == 0)
                map.setMap_texture_selected(map.getMap_texture_count() - 1);
            else
                map.setMap_texture_selected((map.getMap_texture_selected() - 1) % map.getMap_texture_count());
        }
        if (event.getKeyCode() == KeyEvent.VK_LEFT)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            if(map.getMap_selected() == 0)
                map.setMap_selected(map.getMap_count() - 1);
            else
                map.setMap_selected((map.getMap_selected() - 1) % map.getMap_count());
        }
        if (event.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            map.setMap_selected((map.getMap_selected() + 1) % map.getMap_count());
        }

    }

    @Override
    public void keyReleased(KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.VK_ENTER)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            if(game_mode == 2)
            {
                if((Integer)user_player_spinner.getValue() >= 2)
                {
                    game_start = 1;
                }
            }

            if(game_mode == 1)
            {
                if((Integer)user_player_spinner.getValue() + (Integer)cpu_player_spinner.getValue() >= 2)
                {
                    game_start = 1;
                }
            }
        }
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {}

    public int getDifficulty_level()
    {
        return difficulty_level;
    }

    public int getGame_start()
    {
        return game_start;
    }

    public int getTurn_time()
    {
        return (Integer)turn_time_spinner.getValue();
    }

    public int getCpu_players()
    {
        return (Integer)cpu_player_spinner.getValue();
    }

    public int getUser_players()
    {
        return (Integer)user_player_spinner.getValue();
    }
}