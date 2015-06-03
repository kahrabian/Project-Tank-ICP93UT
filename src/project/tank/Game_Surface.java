package project.tank;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Game_Surface extends JFrame implements ChangeListener, ActionListener, KeyListener
{

    private DefaultComboBoxModel<String> bullet_list_available;

    private JLabel force_label;
    private JLabel degree_label;

    public JSlider force_slider;
    public JSlider degree_slider;

    private JComboBox<String> bullet_list;

    private JButton fire_button;
    private JButton buy_type2_button;
    private JButton buy_type3_button;

    public Map_Surface game_map_surface;

    private JPanel player_panel;
    private JPanel player1_panel;
    private JPanel player2_panel;

    private JPanel force_panel;
    private JPanel degree_panel;
    private JPanel fire_panel;
    private JPanel buy_panel;
    public JPanel game_panel;

    public PauseMenu pause_menu_panel;

    public GameOver game_over_panel;
    public GameWon game_won_panel;

    private Clip click_sound;

    public Game_Surface(int game_background_time ,int map_texture_selected, String map_selected_path, int turn_time, int cpu_players_count, int user_players_count, int difficulty_level) throws IOException, UnsupportedAudioFileException, LineUnavailableException
    {
        super("Tank");

        construct_components();

        int windows_width = 960;
        int windows_height = 730;
        setSize(windows_width, windows_height);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        game_map_surface = new Map_Surface(game_background_time, map_texture_selected, map_selected_path, turn_time, cpu_players_count, user_players_count, difficulty_level);
        game_map_surface.addKeyListener(this);
        game_map_surface.setFocusable(true);

        game_map_surface.addMouseListener(new MouseAdapter() {
                                              @Override
                                              public void mousePressed(MouseEvent event)
                                              {
                                                  if(!game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getAI())
                                                      game_map_surface.setMouse_start_position(event.getPoint());
                                              }
                                          }
        );
        game_map_surface.addMouseMotionListener(new MouseMotionAdapter() {
                                                    @Override
                                                    public void mouseDragged(MouseEvent event)
                                                    {
                                                        if(!game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getAI())
                                                        {
                                                            int delta_x = event.getX() - game_map_surface.getMouse_start_position().x;
                                                            int delta_y = -(event.getY() - game_map_surface.getMouse_start_position().y);
                                                            if(delta_x >= 0)
                                                            {
                                                                if(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getFire_power() + (delta_x / 2) > 100)
                                                                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setFire_power(100);
                                                                else
                                                                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setFire_power(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getFire_power() + (delta_x / 2));
                                                            }
                                                            else
                                                            {
                                                                if(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getFire_power() + (delta_x / 2) < 0)
                                                                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setFire_power(0);
                                                                else
                                                                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setFire_power(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getFire_power() + (delta_x / 2));
                                                            }

                                                            if(delta_y >= 0)
                                                            {
                                                                if(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getPipe_angle() + delta_y > 180)
                                                                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setPipe_angle(180);
                                                                else
                                                                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setPipe_angle(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getPipe_angle() + delta_y);
                                                            }
                                                            else
                                                            {
                                                                if(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getPipe_angle() + delta_y < 0)
                                                                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setPipe_angle(0);
                                                                else
                                                                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setPipe_angle(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getPipe_angle() + delta_y);
                                                            }

                                                            game_map_surface.setMouse_start_position(event.getPoint());

                                                            update_sliders();
                                                        }
                                                    }
                                                }
        );

        set_buttons();
        set_sliders();
        set_lables();
        set_list();

        build_player_panel();

        build_game_panel();

        click_sound = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/clickSound.wav")).getFormat()));
        click_sound.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/clickSound.wav")));

        setUndecorated(true);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        addKeyListener(this);

        pack();
    }

    private void construct_components() throws IOException, LineUnavailableException, UnsupportedAudioFileException
    {
        bullet_list_available = new DefaultComboBoxModel<String>();

        force_label = new JLabel("  Force: ");
        force_label.addKeyListener(this);
        force_label.setFocusable(false);

        degree_label = new JLabel("Degree:");
        degree_label.addKeyListener(this);
        degree_label.setFocusable(false);

        force_slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        force_slider.addKeyListener(this);
        force_slider.setFocusable(false);

        degree_slider = new JSlider(JSlider.HORIZONTAL, 0, 180, 30);
        degree_slider.addKeyListener(this);
        degree_slider.setFocusable(false);

        bullet_list = new JComboBox<String>();
        bullet_list.addKeyListener(this);
        bullet_list.setFocusable(false);

        fire_button = new JButton("Fire!");
        fire_button.addKeyListener(this);
        fire_button.setFocusable(false);

        buy_type2_button = new JButton("Buy Type 2 Bullet");
        buy_type2_button.addKeyListener(this);
        buy_type2_button.setFocusable(false);

        buy_type3_button = new JButton("Buy Type 3 Bullet");
        buy_type3_button.addKeyListener(this);
        buy_type3_button.setFocusable(false);

        player_panel = new JPanel();
        player_panel.addKeyListener(this);
        player_panel.setFocusable(false);

        player1_panel = new JPanel();
        player1_panel.addKeyListener(this);
        player1_panel.setFocusable(false);

        player2_panel = new JPanel();
        player2_panel.addKeyListener(this);
        player2_panel.setFocusable(false);

        force_panel = new JPanel();
        force_panel.addKeyListener(this);
        force_panel.setFocusable(false);

        degree_panel = new JPanel();
        degree_panel.addKeyListener(this);
        degree_panel.setFocusable(false);

        fire_panel = new JPanel();
        fire_panel.addKeyListener(this);
        fire_panel.setFocusable(false);

        buy_panel = new JPanel();
        buy_panel.addKeyListener(this);
        buy_panel.setFocusable(false);

        game_panel = new JPanel();
        game_panel.addKeyListener(this);
        game_panel.setFocusable(false);

        pause_menu_panel = new PauseMenu();

        game_over_panel = new GameOver();
        game_won_panel = new GameWon();
    }

    private void set_buttons()
    {
        //setting the buttons
        fire_button.setPreferredSize(new Dimension(150, 25));
        fire_button.setActionCommand("fire");
        fire_button.addActionListener(this);

        buy_type2_button.setPreferredSize(new Dimension(150, 25));
        buy_type2_button.setActionCommand("buy_type_2");
        buy_type2_button.addActionListener(this);

        buy_type3_button.setPreferredSize(new Dimension(150, 25));
        buy_type3_button.setActionCommand("buy_type_3");
        buy_type3_button.addActionListener(this);
        //end of setting the buttons
    }

    private void set_sliders()
    {
        //setting the sliders
        force_slider.setMajorTickSpacing(10);
        force_slider.setMinorTickSpacing(1);
        force_slider.setPaintTicks(true);
        force_slider.setPaintLabels(true);
        force_slider.setSnapToTicks(true);
        force_slider.setPreferredSize(new Dimension(300,50));
        force_slider.setName("force");
        force_slider.addChangeListener(this);

        degree_slider.setMajorTickSpacing(30);
        degree_slider.setMinorTickSpacing(1);
        degree_slider.setPaintTicks(true);
        degree_slider.setPaintLabels(true);
        degree_slider.setSnapToTicks(true);
        degree_slider.setPreferredSize(new Dimension(300,50));
        degree_slider.setName("degree");
        degree_slider.addChangeListener(this);
        // end of setting the sliders
    }

    private void set_lables()
    {
        //setting lables allignment
        force_label.setHorizontalAlignment(JLabel.CENTER);
        force_label.setVerticalAlignment(JLabel.CENTER);

        degree_label.setHorizontalAlignment(JLabel.CENTER);
        degree_label.setVerticalAlignment(JLabel.CENTER);
        //end of setting allignments
    }

    private void set_list()
    {
        //setting the bullet list
        bullet_list.setPreferredSize(new Dimension(150, 25));
        //end of detting the bullet list
    }

    private void build_player_panel()
    {
        build_player_panel_part1();
        build_player_panel_part2();

        //constructing player panel
        GridLayout player_layout = new GridLayout(1,2);
        player_layout.setHgap(30);
        player_panel.setLayout(player_layout);
        player_panel.add(player1_panel);
        player_panel.add(player2_panel);
        //end of constructing player panel
    }

    private void build_player_panel_part1()
    {
        //constructing player panel PART 1
        player1_panel.setLayout(new BoxLayout(player1_panel, BoxLayout.Y_AXIS));
        build_force_panel();
        player1_panel.add(force_panel);
        build_fire_panel();
        player1_panel.add(fire_panel);
        //end of constructing player panel PART 1
    }

    private void build_player_panel_part2()
    {
        //constructing player panel PART 2
        player2_panel.setLayout(new BoxLayout(player2_panel, BoxLayout.Y_AXIS));
        build_degree_panel();
        player2_panel.add(degree_panel);
        build_buy_panel();
        player2_panel.add(buy_panel);
        //end of constructing player panel PART 2
    }

    private void build_force_panel()
    {
        //constructing force panel
        force_panel.setLayout(new FlowLayout());
        force_panel.add(force_label);
        force_panel.add(force_slider);
        //end of constructing force panel
    }

    private void build_degree_panel()
    {
        //constructing degree panel
        degree_panel.setLayout(new FlowLayout());
        degree_panel.add(degree_label);
        degree_panel.add(degree_slider);
        //end of constructing degree panel
    }

    private void build_fire_panel()
    {
        //constructing fire panel
        fire_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
        fire_panel.add(new JScrollPane(bullet_list));
        fire_panel.add(fire_button);
        //end of constructing fire panel
    }

    private void build_buy_panel()
    {
        //constructing buy panel
        buy_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buy_panel.add(buy_type2_button);
        buy_panel.add(buy_type3_button);
        //end of constructing buy1 panel
    }

    private void build_game_panel()
    {
        //constructing game_panel
        game_panel.setLayout(new BoxLayout(game_panel, BoxLayout.Y_AXIS));
        game_panel.add(game_map_surface);
        game_panel.add(player_panel);

        pause_menu_panel.setVisible(false);
        game_over_panel.setVisible(false);
        game_won_panel.setVisible(false);

        add(pause_menu_panel);
        add(game_over_panel);
        add(game_won_panel);
        add(game_panel);
        //end of constructing game_panel
    }

    public void update_combobox()
    {
        bullet_list_available.removeAllElements();

        bullet_list_available.addElement("Type 1");

        if (this.game_map_surface.tanks.get(this.game_map_surface.getPlayer_turn()).bullets_count[0] > 0)
            bullet_list_available.addElement("Type 2 (" + Integer.toString(this.game_map_surface.tanks.get(this.game_map_surface.getPlayer_turn()).bullets_count[0]) + " remaining)");

        if (this.game_map_surface.tanks.get(this.game_map_surface.getPlayer_turn()).bullets_count[1] > 0)
            bullet_list_available.addElement("Type 3 (" + Integer.toString(this.game_map_surface.tanks.get(this.game_map_surface.getPlayer_turn()).bullets_count[1]) + " remaining)");

        bullet_list.setModel(bullet_list_available);
    }

    public void update_panels()
    {
        if(!game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getAI())
        {
            player_panel.setVisible(true);
            pack();
        }
        else
        {
            player_panel.setVisible(false);
            pack();
        }
    }

    public void update_sliders()
    {
        force_slider.setValue(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getFire_power());
        degree_slider.setValue(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getPipe_angle());
    }

    public void update_buttons()
    {
        if(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getMoney() < game_map_surface.bullet_type_2_price)
            buy_type2_button.setEnabled(false);
        else
            buy_type2_button.setEnabled(true);

        if(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getMoney() < game_map_surface.bullet_type_3_price)
            buy_type3_button.setEnabled(false);
        else
            buy_type3_button.setEnabled(true);
    }

    @Override
    public void stateChanged(ChangeEvent event)
    {
        if(!game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getAI())
        {
            JSlider source = (JSlider) event.getSource();

            if (source.getName().equals("force"))
                game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setFire_power(source.getValue());

            if (source.getName().equals("degree"))
                game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setPipe_angle(source.getValue());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        if(!game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getAI())
        {
            if(event.getActionCommand().equals("fire"))
            {
                if(bullet_list.getSelectedItem().toString().startsWith("Type 1"))
                    game_map_surface.fire_Bullet(1);

                if(bullet_list.getSelectedItem().toString().startsWith("Type 2"))
                    game_map_surface.fire_Bullet(2);

                if(bullet_list.getSelectedItem().toString().startsWith("Type 3"))
                    game_map_surface.fire_Bullet(3);
            }

            if(event.getActionCommand().equals("buy_type_2"))
                game_map_surface.buy_bullet(2);

            if(event.getActionCommand().equals("buy_type_3"))
                game_map_surface.buy_bullet(3);

            update_combobox();
        }
    }

    @Override
    public void keyPressed(KeyEvent event)
    {
        if(!game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getAI())
        {
            if (event.getKeyCode() == KeyEvent.VK_LEFT)
            {
                if(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getPipe_angle() != 0)
                {
                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setPipe_angle(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getPipe_angle() - 1);
                    update_sliders();
                }
            }

            if (event.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                if(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getPipe_angle() != 180)
                {
                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setPipe_angle(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getPipe_angle() + 1);
                    update_sliders();
                }
            }

            if (event.getKeyCode() == KeyEvent.VK_UP)
            {
                if(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getFire_power() != 100)
                {
                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setFire_power(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getFire_power() + 1);
                    update_sliders();
                }
            }

            if (event.getKeyCode() == KeyEvent.VK_DOWN)
            {
                if(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getFire_power() != 0)
                {
                    game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).setFire_power(game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getFire_power() - 1);
                    update_sliders();
                }
            }

            if (event.getKeyCode() == KeyEvent.VK_A)
            {
                game_map_surface.move_tank_left();
            }

            if (event.getKeyCode() == KeyEvent.VK_D)
            {
                game_map_surface.move_tank_right();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent event)
    {
        if(!game_map_surface.tanks.get(game_map_surface.getPlayer_turn()).getAI())
        {
            if (event.getKeyCode() == KeyEvent.VK_ENTER)
            {
                if(bullet_list.getSelectedItem().toString().startsWith("Type 1"))
                    game_map_surface.fire_Bullet(1);

                if(bullet_list.getSelectedItem().toString().startsWith("Type 2"))
                    game_map_surface.fire_Bullet(2);

                if(bullet_list.getSelectedItem().toString().startsWith("Type 3"))
                    game_map_surface.fire_Bullet(3);
            }

            if (event.getKeyCode() == KeyEvent.VK_2)
            {
                game_map_surface.buy_bullet(2);
                update_combobox();
            }

            if (event.getKeyCode() == KeyEvent.VK_3)
            {
                game_map_surface.buy_bullet(3);
                update_combobox();
            }
        }

        if (event.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            game_map_surface.setGame_state(1);
        }

        if (event.getKeyCode() == KeyEvent.VK_F5)
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            game_map_surface.setGame_state(2);
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {}
}