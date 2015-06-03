package project.tank;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Map_Surface extends JPanel implements ActionListener
{
    private final int game_screen_width = 960;
    private final int game_screen_height = 640;

    private int map_screen_width;

    private int map_screen_start;

    private final int map_table_width = 192;
    private final int map_table_height = 128;

    private final int block_width = 5;
    private final int block_height = 5;

    private final int coin_width = 13;
    private final int coin_height = 13;

    private final int tank_width = 40;
    private final int tank_height = 20;

    private final int rip_width = 30;
    private final int rip_height = 30;

    private final int pipe_width = 30;
    private final int pipe_height = 4;

    private final int bullet_width = 10;
    private final int bullet_height = 4;

    private final int map_images_count = 11;
    private final int bullet_images_count = 3;

    private final double physics_scale = 3.5;

    public final int bullet_type_2_price = 100;
    public final int bullet_type_3_price = 300;

    private final int coin_price = 200;

    private final int tank_explosion_frame_dimension = 100;

    private final long tank_explosion_frame_delay = 150;

    private final int explosion_frame_width = 134;
    private final int explosion_frame_height = 134;

    private final int explosion_frame_count = 12;

    private int map_texture_selected;

    private String map_selected_path;

    private Random rnd;

    private String[][] map_table;

    private String[][] map_images_paths;
    private String[] bullet_images_paths;
    private BufferedImage[] map_images;
    private BufferedImage[] bullet_images;
    private BufferedImage map_images_tank;
    private BufferedImage map_images_rip;
    private BufferedImage map_images_pipe;
    private BufferedImage game_background_night;
    private BufferedImage game_background_day;
    private BufferedImage game_background_cloudy;
    private MovingBackground[] movingbackground_clouds;
    private BufferedImage fire_power_texture;
    private BufferedImage explosion_image;
    private BufferedImage coin_animation;

    private Font time_font;
    private Font other_font;

    private String time_text;
    private String turn_text;
    private String wind_text;
    private String health_text;
    private String money_text;
    private String fuel_text;

    private long time_0;
    private long time;

    private int turn_time;
    private int player_turn;
    private int wind_speed;

    public ArrayList<Block> blocks;
    public ArrayList<Coin> coins;
    public ArrayList<Tank> tanks;
    public ArrayList<Bullet> bullets;
    public ArrayList<Animation> animations;
    public ArrayList<Rip> rips;

    private int game_background_time;

    private boolean turn_has_recently_changed;

    private int players_count;
    private int cpu_players_count;
    private int user_players_count;
    private int difficulty_level;

    private TexturePaint fire_power_texture_paint;

    private Clip bullet_fired_sound;
    private Clip bullet_impact_sound;
    private Clip tank_explosion_sound;
    private Clip coin_obtained_sound;
    private Clip coin_spend_sound;
    private Clip click_sound;
    private Clip tank_collision_sound;

    private int game_state;

    private long pause_time_start;

    private Point mouse_start_position;

    private int[] skyline_Y;

    private Ai tank_AI;

    public Map_Surface(int game_background_time, int map_texture_selected, String map_selected_path, int turn_time, int cpu_players_count, int user_players_count, int difficulty_level) throws IOException, UnsupportedAudioFileException, LineUnavailableException
    {
        this.game_background_time = game_background_time;
        this.map_texture_selected = map_texture_selected;
        this.map_selected_path = map_selected_path;
        this.turn_time = turn_time;
        this.cpu_players_count = cpu_players_count;
        this.user_players_count = user_players_count;
        players_count = user_players_count + cpu_players_count;
        this.difficulty_level = difficulty_level;

        setPreferredSize(new Dimension(game_screen_width, game_screen_height));

        rnd = new Random();

        map_table = new String[map_table_height][map_table_width];

        construct_map_images();

        construct_bullet_images();

        construct_fonts();

        construct_texts();

        construct_times();

        construct_sounds();

        construct_objects();

        construct_others();

        readMap();

        if(tanks.get(player_turn).getAI())
            tank_AI.generate_movement(tanks, coins, game_screen_width, tank_width, coin_width, player_turn);
    }

    private void construct_map_images() throws IOException
    {
        construct_map_images_paths();

        map_images = new BufferedImage[map_images_count];
        for (int i = 0 ; i < map_images_count ; i++)
            map_images[i] = ImageIO.read(new File(map_images_paths[map_texture_selected][i]));

        map_images_tank = ImageIO.read(new File("GameResources/tank.png"));
        map_images_pipe = ImageIO.read(new File("GameResources/pipe.png"));
        map_images_rip = ImageIO.read(new File("GameResources/rip.png"));

        game_background_night = ImageIO.read(new File("GameResources/BackGrounds/static_night.jpg"));
        game_background_day = ImageIO.read(new File("GameResources/BackGrounds/static_day.png"));
        game_background_cloudy = ImageIO.read(new File("GameResources/BackGrounds/moving_cloudy_layer_1.png"));

        BufferedImage[] game_background_cloudy_moving = new BufferedImage[2];
        game_background_cloudy_moving[0] = ImageIO.read(new File("GameResources/BackGrounds/moving_cloudy_layer_2.png"));
        game_background_cloudy_moving[1] = ImageIO.read(new File("GameResources/BackGrounds/moving_cloudy_layer_3.png"));

        movingbackground_clouds = new MovingBackground[2];
        movingbackground_clouds[0] = new MovingBackground(game_background_cloudy_moving[0], 2, game_screen_width, game_screen_height);
        movingbackground_clouds[1] = new MovingBackground(game_background_cloudy_moving[1], -2, game_screen_width, game_screen_height);

        fire_power_texture = ImageIO.read(new File("GameResources/fire_power_texture_1.jpg"));

        explosion_image = ImageIO.read(new File("GameResources/explosion.png"));
        coin_animation = ImageIO.read(new File("GameResources/coin_animation.png"));
    }

    private void construct_map_images_paths()
    {
        int map_texture_count = 4;
        map_images_paths = new String[map_texture_count][map_images_count];
        map_images_paths[0][0] = "GameResources/Map_Textures/surface_1_0.png";
        map_images_paths[0][1] = "GameResources/Map_Textures/surface_1_1.png";
        map_images_paths[0][2] = "GameResources/Map_Textures/surface_1_2.png";
        map_images_paths[0][3] = "GameResources/Map_Textures/surface_1_3.png";
        map_images_paths[0][4] = "GameResources/Map_Textures/surface_1_4.png";
        map_images_paths[0][5] = "GameResources/Map_Textures/surface_1_0_1.png";
        map_images_paths[0][6] = "GameResources/Map_Textures/surface_1_0_2.png";
        map_images_paths[0][7] = "GameResources/Map_Textures/surface_1_0_3.png";
        map_images_paths[0][8] = "GameResources/Map_Textures/surface_1_0_4.png";
        map_images_paths[0][9] = "GameResources/Map_Textures/surface_1_0_5.png";
        map_images_paths[0][10] = "GameResources/Map_Textures/surface_1_0_6.png";

        map_images_paths[1][0] = "GameResources/Map_Textures/surface_2_0.png";
        map_images_paths[1][1] = "GameResources/Map_Textures/surface_2_1.png";
        map_images_paths[1][2] = "GameResources/Map_Textures/surface_2_2.png";
        map_images_paths[1][3] = "GameResources/Map_Textures/surface_2_3.png";
        map_images_paths[1][4] = "GameResources/Map_Textures/surface_2_4.png";
        map_images_paths[1][5] = "GameResources/Map_Textures/surface_2_0_1.png";
        map_images_paths[1][6] = "GameResources/Map_Textures/surface_2_0_2.png";
        map_images_paths[1][7] = "GameResources/Map_Textures/surface_2_0_3.png";
        map_images_paths[1][8] = "GameResources/Map_Textures/surface_2_0_4.png";
        map_images_paths[1][9] = "GameResources/Map_Textures/surface_2_0_5.png";
        map_images_paths[1][10] = "GameResources/Map_Textures/surface_2_0_6.png";

        map_images_paths[2][0] = "GameResources/Map_Textures/surface_3_0.png";
        map_images_paths[2][1] = "GameResources/Map_Textures/surface_3_1.png";
        map_images_paths[2][2] = "GameResources/Map_Textures/surface_3_2.png";
        map_images_paths[2][3] = "GameResources/Map_Textures/surface_3_3.png";
        map_images_paths[2][4] = "GameResources/Map_Textures/surface_3_4.png";
        map_images_paths[2][5] = "GameResources/Map_Textures/surface_3_0_1.png";
        map_images_paths[2][6] = "GameResources/Map_Textures/surface_3_0_2.png";
        map_images_paths[2][7] = "GameResources/Map_Textures/surface_3_0_3.png";
        map_images_paths[2][8] = "GameResources/Map_Textures/surface_3_0_4.png";
        map_images_paths[2][9] = "GameResources/Map_Textures/surface_3_0_5.png";
        map_images_paths[2][10] = "GameResources/Map_Textures/surface_3_0_6.png";

        map_images_paths[3][0] = "GameResources/Map_Textures/surface_4_0.png";
        map_images_paths[3][1] = "GameResources/Map_Textures/surface_4_1.png";
        map_images_paths[3][2] = "GameResources/Map_Textures/surface_4_2.png";
        map_images_paths[3][3] = "GameResources/Map_Textures/surface_4_3.png";
        map_images_paths[3][4] = "GameResources/Map_Textures/surface_4_4.png";
        map_images_paths[3][5] = "GameResources/Map_Textures/surface_4_0_1.png";
        map_images_paths[3][6] = "GameResources/Map_Textures/surface_4_0_2.png";
        map_images_paths[3][7] = "GameResources/Map_Textures/surface_4_0_3.png";
        map_images_paths[3][8] = "GameResources/Map_Textures/surface_4_0_4.png";
        map_images_paths[3][9] = "GameResources/Map_Textures/surface_4_0_5.png";
        map_images_paths[3][10] = "GameResources/Map_Textures/surface_4_0_6.png";
    }

    private void construct_bullet_images() throws IOException
    {
        construct_bullet_images_paths();

        bullet_images = new BufferedImage[bullet_images_count];
        for(int i = 0 ; i < bullet_images_count ; i++)
            bullet_images[i] = ImageIO.read(new File(bullet_images_paths[i]));
    }

    private void construct_bullet_images_paths()
    {
        bullet_images_paths = new String[bullet_images_count];
        bullet_images_paths[0] = "GameResources/bullet_0.png";
        bullet_images_paths[1] = "GameResources/bullet_1.png";
        bullet_images_paths[2] = "GameResources/bullet_2.png";
    }

    private void construct_fonts()
    {
        int time_font_size = 40;
        time_font = new Font("Jokerman", Font.ITALIC, time_font_size);
        int text_font_size = 17;
        other_font = new Font("Forte", Font.PLAIN, text_font_size);
    }

    private void construct_texts()
    {
        time_text = "";
        turn_text = "";
        wind_text = "";
        money_text = "";
        fuel_text = "";
        health_text = "";
    }

    private void construct_times()
    {
        time_0 = System.currentTimeMillis();
        time = System.currentTimeMillis();
    }

    private void construct_sounds() throws IOException, UnsupportedAudioFileException, LineUnavailableException
    {
        bullet_fired_sound = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/bulletFired.wav")).getFormat()));
        bullet_fired_sound.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/bulletFired.wav")));

        bullet_impact_sound = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/bulletImpact.wav")).getFormat()));
        bullet_impact_sound.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/bulletImpact.wav")));

        tank_explosion_sound = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/tankExplosion.wav")).getFormat()));
        tank_explosion_sound.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/tankExplosion.wav")));

        coin_obtained_sound = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/coinObtain.wav")).getFormat()));
        coin_obtained_sound.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/coinObtain.wav")));

        coin_spend_sound = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/coinSpend.wav")).getFormat()));
        coin_spend_sound.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/coinSpend.wav")));

        click_sound = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/clickSound.wav")).getFormat()));
        click_sound.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/clickSound.wav")));

        tank_collision_sound = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/tankCollisionSound.wav")).getFormat()));
        tank_collision_sound.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/tankCollisionSound.wav")));
    }

    private void construct_objects()
    {
        blocks = new ArrayList<Block>(0);
        coins = new ArrayList<Coin>(0);
        tanks = new ArrayList<Tank>(0);
        bullets = new ArrayList<Bullet>(0);
        animations = new ArrayList<Animation>(0);
        rips = new ArrayList<Rip>(0);
    }

    private void construct_others() throws IOException
    {
        setLayout(null);

        game_state = 0;

        pause_time_start = 0;

        player_turn = 0;

        setWind_speed();

        turn_has_recently_changed = true;

        fire_power_texture_paint = new TexturePaint(fire_power_texture, new Rectangle(310, 310));

        CustomShapedButton restart_button = new CustomShapedButton(ImageIO.read(new File("GameResources/restart.png")));
        restart_button.setBounds(game_screen_width - 30 - 5, game_screen_height - 30 - 5, 30, 30);
        restart_button.setActionCommand("restart");
        restart_button.addActionListener(this);
        add(restart_button);

        CustomShapedButton pause_button = new CustomShapedButton(ImageIO.read(new File("GameResources/pause.png")));
        pause_button.setBounds(5, game_screen_height - 30 - 5, 30, 30);
        pause_button.setActionCommand("pause");
        pause_button.addActionListener(this);
        add(pause_button);

        mouse_start_position = new Point();

        skyline_Y = new int[game_screen_width];

        tank_AI = new Ai();
    }

    public void readMap() throws FileNotFoundException
    {
        // READING MAP FROM FILE
        Scanner scan = new Scanner(new File(map_selected_path));

        int initial_width = scan.nextInt();
        map_screen_width = initial_width * block_width;

        int initial_height = scan.nextInt();

        map_screen_start = (map_table_width - initial_width) / 2 * block_width;

        String tmp = scan.nextLine();

        for(int i = 0 ; i < (map_table_height - initial_height) ; i++)
            for(int j = 0 ; j < map_table_width ; j++)
                map_table[i][j] = "#";

        for(int i = (map_table_height - initial_height) ; i < map_table_height ; i++)
            for(int j = 0 ; j < (map_table_width - initial_width) / 2 ; j++)
                map_table[i][j] = "#";

        for(int i = (map_table_height - initial_height) ; i < map_table_height ; i++)
        {
            tmp = scan.nextLine();
            for (int j = (map_table_width - initial_width) / 2 ; j < initial_width + (map_table_width - initial_width) / 2  ; j++)
                map_table[i][j] = Character.toString(tmp.charAt(j - ((map_table_width - initial_width) / 2)));
        }

        for(int i = (map_table_height - initial_height) ; i < map_table_height ; i++)
            for(int j = initial_width + (map_table_width - initial_width) / 2 ; j < map_table_width ; j++)
                map_table[i][j] = "#";

        locate_coins();

        for(int i = (map_table_height - initial_height) ; i < map_table_height ; i++)
            for (int j = (map_table_width - initial_width) / 2 ; j < initial_width + (map_table_width - initial_width) / 2  ; j++)
                if(tmp.charAt(j) == 't' || tmp.charAt(j) == 'T' || tmp.charAt(j) == 'c' || tmp.charAt(j) == 'C')
                    map_table[i][j] = "#";

        skyline_builder();
        locate_tanks();

        for (int i = 0 ; i < map_table_height ; i++)
            for (int j = 0 ; j < map_table_width ; j++)
            {
                if (map_table[i][j].equals("0"))
                {
                    if(i != 0 && (map_table[i - 1][j].equals("#") || map_table[i - 1][j].equals("3") || map_table[i - 1][j].equals("4")))
                        blocks.add(new Block(j * block_width, i * block_height, 7));
                    else if(i != 0 && map_table[i - 1][j].equals("1"))
                        blocks.add(new Block(j * block_width, i * block_height, 5));
                    else if(i != 0 && map_table[i - 1][j].equals("2"))
                        blocks.add(new Block(j * block_width, i * block_height, 6));
                    else if(i != map_table_height - 1 && (map_table[i + 1][j].equals("#") || map_table[i + 1][j].equals("1") || map_table[i + 1][j].equals("2")))
                        blocks.add(new Block(j * block_width, i * block_height, 10));
                    else if(i != map_table_height - 1 && map_table[i + 1][j].equals("3"))
                        blocks.add(new Block(j * block_width, i * block_height, 9));
                    else if(i != map_table_height - 1 && map_table[i + 1][j].equals("4"))
                        blocks.add(new Block(j * block_width, i * block_height, 8));
                    else blocks.add(new Block(j * block_width, i * block_height, 0));
                }

                if (map_table[i][j].equals("1"))
                    blocks.add(new Block(j * block_width, i * block_height, 1));

                if (map_table[i][j].equals("2"))
                    blocks.add(new Block(j * block_width, i * block_height, 2));

                if (map_table[i][j].equals("3"))
                    blocks.add(new Block(j * block_width, i * block_height, 3));

                if (map_table[i][j].equals("4"))
                    blocks.add(new Block(j * block_width, i * block_height, 4));
            }


    }

    private void locate_coins()
    {
        // locating coins
        for (int i = 0 ; i < map_table_height ; i++)
            for (int j = 0 ; j < map_table_width ; j++)
                if (map_table[i][j].equals("c") || map_table[i][j].equals("C"))
                {
                    coins.add(new Coin(j * block_width - (coin_width / 2) + (block_width / 2), i * block_height - coin_height + block_height));
                    int coin_animation_frame_width = 44;
                    int coin_animation_frame_height = 40;
                    int coin_animation_frame_count = 10;
                    long coin_animation_frame_delay = 100;
                    animations.add(new Animation(j * block_width + (block_width / 2), i * block_height - coin_height + block_height + (coin_height / 2),
                            coin_animation, coin_animation_frame_width, coin_animation_frame_height, coin_animation_frame_count,
                            coin_animation_frame_delay, coin_width, true));
                    map_table[i][j] = "#";
                }
        // end of locating coins
    }

    private void locate_tanks()
    {
        char[] tank_order = new char[players_count];

        Vector<Integer> positions = new Vector<Integer>();

        for(int i = 0 ; i < players_count ; i++)
            positions.addElement(i);

        Random position_generator = new Random(System.currentTimeMillis());

        for(int i = 0 ; i < cpu_players_count ; i++)
        {
            int generated_position = position_generator.nextInt(positions.size());
            tank_order[positions.elementAt(generated_position)] = 'A';
            positions.remove(generated_position);
        }

        for(int i = 0 ; i < user_players_count ; i++)
        {
            int generated_position = position_generator.nextInt(positions.size());
            tank_order[positions.elementAt(generated_position)] = 'U';
            positions.remove(generated_position);
        }

        for(int l = 0 ; l < players_count ; l++)
        {
            boolean correct_X = false;

            int X = 0;

            while(!correct_X)
            {
                X = (l * (map_screen_width / players_count)) + (tank_width / 2 + 1) + position_generator.nextInt((map_screen_width / players_count) - tank_width - 2) + map_screen_start;
                correct_X = true;
                for(int i = 0 ; i < (tank_width / 2) ; i++)
                {
                    if(skyline_Y[X + i] - skyline_Y[X + i + 1] >= block_height)
                        correct_X = false;
                }

                for(int i = -(tank_width / 2) ; i < 0 ; i++)
                {
                    if(skyline_Y[X + i] - skyline_Y[X + i - 1] >= block_height)
                        correct_X = false;
                }
            }

            boolean found = false;

            int Y = skyline_Y[X];

            if(tank_order[l] == 'U')
                tanks.add(new Tank(0, 0, 0, "Player " + Integer.toString(tanks.size() + 1), false));
            else
                tanks.add(new Tank(0, 0, 0, "Player " + Integer.toString(tanks.size() + 1) + " (CPU)", true));

            for(int i = 1 ; i < (tank_width / 2) ; i++)
            {
                for(int j = -45 ; j <= 45 ; j++)
                {
                    Area tank_area = new Area(new Rectangle(X - (tank_width / 2), Y - i - tank_height, tank_width, tank_height));

                    AffineTransform transform_tank = new AffineTransform();
                    transform_tank.rotate(Math.toRadians(j), X, Y - i);

                    GeneralPath tank_path = new GeneralPath();
                    tank_path.append(tank_area.getPathIterator(transform_tank), true);

                    Area tank = new Area(tank_path);
                    boolean correct = true;

                    for(int k = -(tank_width / 2) ; k < (tank_width / 2) ; k++)
                        if(tank.contains(new Point(X + k, skyline_Y[X + k])))
                            correct = false;

                    if(correct)
                    {
                        tanks.get(tanks.size() - 1).position.setX(X - (tank_width / 2));
                        tanks.get(tanks.size() - 1).position.setY(Y - i - tank_height);
                        tanks.get(tanks.size() - 1).setTank_slope(j);
                        found = true;
                        break;
                    }
                }
                if(found)
                    break;
            }
        }
    }

    public void skyline_builder()
    {
        for(int i = 0 ; i < map_table_width ; i++)
        {
            for(int k = 0 ; k < block_width ; k++)
                skyline_Y[i * block_width + k] = game_screen_height;

            for(int j = 0 ; j < map_table_height ; j++)
            {
                if(map_table[j][i].equals("0") || map_table[j][i].equals("3") || map_table[j][i].equals("4"))
                {
                    for(int k = 0 ; k < block_width ; k++)
                        skyline_Y[i * block_width + k] = j * block_height;
                    break;
                }
                if(map_table[j][i].equals("1"))
                {
                    for(int k = 0 ; k < block_width ; k++)
                        skyline_Y[i * block_width + k] = j * block_height + k + 1;
                    break;
                }
                if(map_table[j][i].equals("2"))
                {
                    for(int k = 0 ; k < block_width ; k++)
                        skyline_Y[i * block_width + k] = (j + 1) * block_height - k;
                    break;
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw_Game(g);
    }

    private void draw_Game(Graphics g)
    {
        Graphics2D map_drawer = (Graphics2D) g;

        draw_map_background(map_drawer);

        draw_bullets(map_drawer);

        draw_rips(map_drawer);

        draw_tanks(map_drawer);

        draw_animation(map_drawer);

        draw_map(map_drawer);

        draw_texts(map_drawer);
    }

    private void draw_map(Graphics2D map_drawer)
    {
        // MAP BLOCKS
        for (Block block : blocks)
            map_drawer.drawImage(map_images[block.getType()], (int) block.position.getX(), (int) block.position.getY(), block_width, block_height, null);
        // END OF MAP BLOCKS
    }

    private void draw_map_background(Graphics2D map_drawer)
    {
        // MAP BACKGROUND
        if (game_background_time == 1)
            map_drawer.drawImage(game_background_day, 0, 0, game_screen_width, game_screen_height, null);
        if (game_background_time == 2)
            map_drawer.drawImage(game_background_night, 0, 0, game_screen_width, game_screen_height, null);
        if (game_background_time == 3)
        {
            map_drawer.drawImage(game_background_cloudy, 0, 0, game_screen_width, game_screen_height, null);
            movingbackground_clouds[0].draw(map_drawer);
            movingbackground_clouds[1].draw(map_drawer);
        }
        // END OF MAP BACKGROUND
    }

    private void draw_tanks(Graphics2D map_drawer)
    {
        // TANKS
        for(int i = 0 ; i < tanks.size() ; i++)
        {

            AffineTransform original_Transform = map_drawer.getTransform();
            AffineTransform transformer = new AffineTransform();

            transformer.translate((int)tanks.get(i).position.getX() + (tank_width / 2), (int)tanks.get(i).position.getY() + tank_height);
            transformer.rotate(Math.toRadians(tanks.get(i).getTank_slope()));

            transformer.translate(-(int)(tank_width * 0.05), -(int)(tank_height * 0.8 + pipe_height / 2));
            transformer.rotate(-Math.toRadians(tanks.get(i).getPipe_angle()));

            map_drawer.setTransform(transformer);

            map_drawer.drawImage(map_images_pipe, 0, -(pipe_height / 2), pipe_width, pipe_height, null);
            if (i == getPlayer_turn())
            {
                map_drawer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                map_drawer.setPaint(fire_power_texture_paint);
                draw_fire_power(map_drawer);
            }
            map_drawer.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            transformer.rotate(Math.toRadians(tanks.get(i).getPipe_angle()));
            transformer.translate((int)(tank_width * 0.05), (int)(tank_height * 0.8 + pipe_height / 2));

            map_drawer.setTransform(transformer);

            map_drawer.drawImage(map_images_tank, -(tank_width / 2), -tank_height, tank_width, tank_height, null);

            map_drawer.setTransform(original_Transform);
        }
        // END OF TANKS
    }

    private void draw_rips(Graphics2D map_drawer)
    {
        for (Rip rip : rips)
        {
            AffineTransform original_Transform = map_drawer.getTransform();
            AffineTransform transformer = new AffineTransform();

            transformer.translate((int) rip.position.getX() + (rip_width / 2), (int) rip.position.getY() + rip_height);
            transformer.rotate(Math.toRadians(rip.getRip_slope()));

            map_drawer.setTransform(transformer);

            map_drawer.drawImage(map_images_rip, -(rip_width / 2), -rip_height, rip_width, rip_height, null);

            map_drawer.setTransform(original_Transform);
        }
    }

    private void draw_fire_power(Graphics2D map_drawer)
    {
        int fire_power_angle = 18;
        int fire_power_width = 500;
        int fire_power_height = 200;
        map_drawer.fillArc(pipe_width - tanks.get(getPlayer_turn()).getFire_power() * fire_power_width / 200 + 5, -tanks.get(getPlayer_turn()).getFire_power() * fire_power_height / 200, tanks.get(getPlayer_turn()).getFire_power() * fire_power_width / 100, tanks.get(getPlayer_turn()).getFire_power() * fire_power_height / 100, -(fire_power_angle / 2), fire_power_angle);
    }

    private void draw_bullets(Graphics2D map_drawer)
    {
        // BULLETS
        for (Bullet bullet : bullets)
        {
            AffineTransform original_Transform = map_drawer.getTransform();
            AffineTransform transformer = new AffineTransform();

            transformer.translate((int) bullet.position.getX() + (bullet_width / 2),
                    (int) bullet.position.getY() + (bullet_height / 2));
            transformer.rotate(Math.toRadians(bullet.getAngle()));

            map_drawer.setTransform(transformer);

            map_drawer.drawImage(bullet_images[bullets.get(0).getType() - 1], 0, -(bullet_height / 2), bullet_width, bullet_height, null);
            bullet.updatePosition();

            map_drawer.setTransform(original_Transform);
        }
        // END OF BULLETS
    }

    private void draw_texts(Graphics2D map_drawer)
    {
        //TEXTS
        map_drawer.setFont(other_font);

        map_drawer.setColor(Color.YELLOW);
        map_drawer.drawString(turn_text, 5, 3 + (int)map_drawer.getFontMetrics().getStringBounds(turn_text, map_drawer).getHeight());
        map_drawer.drawString(wind_text, game_screen_width - (int)map_drawer.getFontMetrics().getStringBounds(wind_text, map_drawer).getWidth() - 5, 3 + (int)map_drawer.getFontMetrics().getStringBounds(wind_text, map_drawer).getHeight());

        map_drawer.setColor(Color.RED);
        for (int i = 0 ; i < tanks.size() ; i++)
        {
            health_text = tanks.get(i).getName() + " Health: " + Integer.toString(tanks.get(i).getHealth());
            map_drawer.drawString(health_text, 5, 6 + (i + 2) * (int)map_drawer.getFontMetrics().getStringBounds(health_text, map_drawer).getHeight());
        }

        map_drawer.setColor(Color.GREEN);

        money_text = " Money: " + Integer.toString(tanks.get(player_turn).getMoney());
        map_drawer.drawString(money_text, game_screen_width - (int)map_drawer.getFontMetrics().getStringBounds(money_text, map_drawer).getWidth() - 5, 6 + 2 * (int)map_drawer.getFontMetrics().getStringBounds(money_text, map_drawer).getHeight());

        fuel_text = " Fuel: " + Integer.toString((tanks.get(player_turn).getFuel() + 3) / 4);
        map_drawer.drawString(fuel_text, game_screen_width - (int)map_drawer.getFontMetrics().getStringBounds(fuel_text, map_drawer).getWidth() - 5, 6 + 3 * (int)map_drawer.getFontMetrics().getStringBounds(money_text, map_drawer).getHeight());

        map_drawer.setColor(Color.WHITE);
        map_drawer.setFont(time_font);
        map_drawer.drawString(time_text, (game_screen_width / 2) - ((int)map_drawer.getFontMetrics().getStringBounds(time_text, map_drawer).getWidth() / 2), (int)map_drawer.getFontMetrics().getStringBounds(time_text, map_drawer).getHeight());
        //END OF TEXTS
    }

    private void draw_animation(Graphics2D map_drawer)
    {
        for (int i = 0 ; i < animations.size() ; i++)
        {
            animations.get(i).draw(map_drawer);
            if (!animations.get(i).active)
                animations.remove(i);
        }
    }

    public void update_parameters()
    {
        if (game_state != 1)
        {
            update_sounds();

            if (bullets.size() == 0)
                updateTime();
            else
                check_collision();
            updateTime_text();
            updateTurn_text();
            updateWind_text();

            if(time >= turn_time * 1000 + 900)
                changePlayer_turn();

            move_tank_fall();

            if(tanks.get(player_turn).getAI())
                do_AI_command();
        }
    }

    private void update_sounds()
    {
        if(!bullet_impact_sound.isRunning())
            bullet_impact_sound.stop();

        if(!tank_explosion_sound.isRunning())
            tank_explosion_sound.stop();

        if(!bullet_fired_sound.isRunning())
            bullet_fired_sound.stop();

        if(!coin_spend_sound.isRunning())
            coin_spend_sound.stop();

        if(!coin_obtained_sound.isRunning())
            coin_obtained_sound.stop();

        if(!click_sound.isRunning())
            click_sound.stop();
    }

    private void check_collision()
    {
        for (int i = 0 ; i < bullets.size() ; i++)
        {
            Area bullet_area = new Area(new Rectangle((int)bullets.get(i).position.getX(),
                    (int)bullets.get(i).position.getY(),
                    bullet_width, bullet_height));

            AffineTransform transform_bullet = new AffineTransform();
            transform_bullet.rotate(Math.toRadians(bullets.get(i).getAngle()),
                    (int)bullets.get(i).position.getX() + (bullet_width / 2),
                    (int)bullets.get(i).position.getY() + (bullet_height / 2));


            GeneralPath bullet_path = new GeneralPath();
            bullet_path.append(bullet_area.getPathIterator(transform_bullet), true);

            Area bullet = new Area(bullet_path);

            if((bullets.get(i).position.getX() < 0 && bullets.get(i).acceleration.getX() < 0)
                    || (bullets.get(i).position.getX() > game_screen_width && bullets.get(i).acceleration.getX() > 0)
                    || bullets.get(i).position.getY() > game_screen_height
                    || (bullets.get(i).position.getX() > game_screen_width && bullets.get(i).acceleration.getX() == 0 && bullets.get(i).velocity.getX() > 0)
                    || (bullets.get(i).position.getX() > game_screen_width && bullets.get(i).acceleration.getX() == 0 && bullets.get(i).velocity.getX() < 0))
            {
                bullets.clear();
                changePlayer_turn();
            }

            int explosion_frame_dimension = 50;
            long explosion_frame_delay = 100;
            for (int j = 0; j < tanks.size(); j++)
            {
                Tank tank1 = tanks.get(j);
                Area tank_area = new Area(new Rectangle((int) tank1.position.getX(), (int) tank1.position.getY(), tank_width, tank_height));

                AffineTransform transform_tank = new AffineTransform();
                transform_tank.rotate(Math.toRadians(tank1.getTank_slope()), (int) tank1.position.getX() + (tank_width / 2), (int) tank1.position.getY() + tank_height);

                GeneralPath tank_path = new GeneralPath();
                tank_path.append(tank_area.getPathIterator(transform_tank), true);

                Area tank = new Area(tank_path);
                tank.intersect(bullet);

                if (!tank.isEmpty() && (i < bullets.size())) {
                    animations.add(new Animation(bullets.get(0).position.getX() + (bullet_width * Math.cos(Math.toRadians(bullets.get(i).getAngle()))) + ((bullet_height / 2) * Math.cos(Math.toRadians(bullets.get(i).getAngle() - 90))), bullets.get(0).position.getY() + (bullet_width * Math.sin(Math.toRadians(bullets.get(i).getAngle()))) + ((bullet_height / 2) * Math.sin(Math.toRadians(bullets.get(i).getAngle() - 90))), explosion_image, explosion_frame_width, explosion_frame_height, explosion_frame_count, explosion_frame_delay, explosion_frame_dimension, false));

                    check_blast_radius((int) (bullets.get(0).position.getX() + (bullet_width * Math.cos(Math.toRadians(bullets.get(i).getAngle()))) + ((bullet_height / 2) * Math.cos(Math.toRadians(bullets.get(i).getAngle() - 90)))), (int) (bullets.get(0).position.getY() + (bullet_width * Math.sin(Math.toRadians(bullets.get(i).getAngle()))) + ((bullet_height / 2) * Math.sin(Math.toRadians(bullets.get(i).getAngle() - 90)))), i);

                    bullets.clear();
                    changePlayer_turn();
                }
            }

            for (int j = 0; j < coins.size(); j++)
            {
                Coin coin1 = coins.get(j);
                Area coin_area = new Area(new Ellipse2D.Double((int) coin1.position.getX(), (int) coin1.position.getY(), coin_width, coin_height));

                GeneralPath coin_path = new GeneralPath();
                coin_path.append(coin_area.getPathIterator(new AffineTransform()), true);

                Area coin = new Area(coin_path);
                coin.intersect(bullet);

                if (!coin.isEmpty() && (i < bullets.size())) {
                    animations.add(new Animation(coin1.position.getX() + (coin_width / 2), coin1.position.getY() + (coin_height / 2), explosion_image, explosion_frame_width, explosion_frame_height, explosion_frame_count, explosion_frame_delay, explosion_frame_dimension, false));

                    check_blast_radius((int) (bullets.get(0).position.getX() + (bullet_width * Math.cos(Math.toRadians(bullets.get(i).getAngle()))) + ((bullet_height / 2) * Math.cos(Math.toRadians(bullets.get(i).getAngle() - 90)))), (int) (bullets.get(0).position.getY() + (bullet_width * Math.sin(Math.toRadians(bullets.get(i).getAngle()))) + ((bullet_height / 2) * Math.sin(Math.toRadians(bullets.get(i).getAngle() - 90)))), i);

                    bullets.clear();
                    changePlayer_turn();
                }
            }

            for (int j = 0; j < blocks.size(); j++) {
                Block block1 = blocks.get(j);
                Area block_area = new Area(new Rectangle((int) block1.position.getX(), (int) block1.position.getY(), block_width, block_height));

                GeneralPath block_path = new GeneralPath();
                block_path.append(block_area.getPathIterator(new AffineTransform()), true);

                Area block = new Area(block_path);
                block.intersect(bullet);

                if (!block.isEmpty() && (i < bullets.size())) {
                    animations.add(new Animation(block1.position.getX() + (block_width / 2), block1.position.getY() - (block_height / 2), explosion_image, explosion_frame_width, explosion_frame_height, explosion_frame_count, explosion_frame_delay, explosion_frame_dimension, false));

                    check_blast_radius((int) (bullets.get(0).position.getX() + (bullet_width * Math.cos(Math.toRadians(bullets.get(i).getAngle()))) + ((bullet_height / 2) * Math.cos(Math.toRadians(bullets.get(i).getAngle() - 90)))), (int) (bullets.get(0).position.getY() + (bullet_width * Math.sin(Math.toRadians(bullets.get(i).getAngle()))) + ((bullet_height / 2) * Math.sin(Math.toRadians(bullets.get(i).getAngle() - 90)))), i);

                    bullets.clear();
                    changePlayer_turn();
                }
            }
        }
    }

    private void check_blast_radius(int x, int y, int i)
    {
        bullet_impact_sound.stop();
        bullet_impact_sound.setFramePosition(0);
        bullet_impact_sound.start();

        Ellipse2D.Double blast_radius_area = new Ellipse2D.Double();
        if(bullets.get(i).getType() == 1)
            blast_radius_area = new Ellipse2D.Double(x, y, 3 * block_width, 3 * block_height);
        else if(bullets.get(i).getType() == 2)
        {
            if(bullets.get(i).velocity.getX() <= 0)
                blast_radius_area = new Ellipse2D.Double(x - block_width, y, 4 * block_width, 4 * block_height);

            if(bullets.get(i).velocity.getX() > 0)
                blast_radius_area = new Ellipse2D.Double(x + block_width, y, 4 * block_width, 4 * block_height);
        }
        else if(bullets.get(i).getType() == 3)
            blast_radius_area = new Ellipse2D.Double(x, y, 5 * block_width, 5 * block_height);

        GeneralPath blast_radius_path = new GeneralPath();
        blast_radius_path.append(blast_radius_area.getPathIterator(new AffineTransform()), true);

        Area blast_radius = new Area(blast_radius_path);

        for (int j = 0 ; j < tanks.size() ; j++)
        {
            Area tank_area = new Area(new Rectangle((int)tanks.get(j).position.getX(),
                    (int)tanks.get(j).position.getY(),
                    tank_width, tank_height));

            AffineTransform transform_tank = new AffineTransform();
            transform_tank.rotate(Math.toRadians(tanks.get(j).getTank_slope()),
                    (int)tanks.get(j).position.getX() + (tank_width / 2),
                    (int)tanks.get(j).position.getY() + tank_height);

            GeneralPath tank_path = new GeneralPath();
            tank_path.append(tank_area.getPathIterator(transform_tank), true);

            Area tank = new Area(tank_path);
            tank.intersect(blast_radius);

            if (!tank.isEmpty())
            {
                int bullet_damage_type_1 = 200;
                if(bullets.get(i).getType() == 1)
                    tanks.get(j).setHealth(tanks.get(j).getHealth() - bullet_damage_type_1);

                int bullet_damage_type_2 = 400;
                if(bullets.get(i).getType() == 2)
                    tanks.get(j).setHealth(tanks.get(j).getHealth() - bullet_damage_type_2);

                int bullet_damage_type_3 = 800;
                if(bullets.get(i).getType() == 3)
                    tanks.get(j).setHealth(tanks.get(j).getHealth() - bullet_damage_type_3);

                if (tanks.get(j).getHealth() <= 0)
                {
                    rips.add(new Rip(tanks.get(j).position.getX() + (tank_width / 2) - (rip_width / 2), tanks.get(j).position.getY() + tank_height - rip_height, tanks.get(j).getTank_slope()));
                    animations.add(new Animation(tanks.get(j).position.getX() + (tank_width / 2), tanks.get(j).position.getY() + (tank_height / 2), explosion_image, explosion_frame_width, explosion_frame_height, explosion_frame_count, tank_explosion_frame_delay, tank_explosion_frame_dimension, false));
                    tanks.remove(j);
                    players_count--;
                    tank_explosion_sound.stop();
                    tank_explosion_sound.setFramePosition(0);
                    tank_explosion_sound.start();
                }
            }
        }

        for (int j = 0 ; j < coins.size() ; j++)
        {
            Area coin_area = new Area(new Ellipse2D.Double((int)coins.get(j).position.getX(), (int)coins.get(j).position.getY(), coin_width, coin_height));

            GeneralPath coin_path = new GeneralPath();
            coin_path.append(coin_area.getPathIterator(new AffineTransform()), true);

            Area coin = new Area(coin_path);
            coin.intersect(blast_radius);

            if (!coin.isEmpty())
            {
                tanks.get(getPlayer_turn()).setMoney(tanks.get(getPlayer_turn()).getMoney() + coin_price);
                coins.remove(j);
                animations.remove(j);
                coin_obtained_sound.stop();
                coin_obtained_sound.setFramePosition(0);
                coin_obtained_sound.start();
            }
        }
    }

    private boolean check_tank_collision(Area moving_tank_area)
    {
        for (int j = 0 ; j < tanks.size() ; j++)
        {
            if(j == player_turn)
                continue;

            Area tank_area = new Area(new Rectangle((int)tanks.get(j).position.getX(),
                    (int)tanks.get(j).position.getY(),
                    tank_width, tank_height));

            AffineTransform transform_tank = new AffineTransform();
            transform_tank.rotate(Math.toRadians(tanks.get(j).getTank_slope()), (int)tanks.get(j).position.getX() + (tank_width / 2), (int)tanks.get(j).position.getY() + tank_height);

            GeneralPath tank_path = new GeneralPath();
            tank_path.append(tank_area.getPathIterator(transform_tank), true);

            Area tank = new Area(tank_path);
            tank.intersect(moving_tank_area);

            if (!tank.isEmpty())
            {
                if(!tank_collision_sound.isRunning())
                {
                    tank_collision_sound.stop();
                    tank_collision_sound.setFramePosition(0);
                    tank_collision_sound.start();
                }
                return false;
            }
        }
        return true;
    }

    private void check_coin_collision(Area moving_tank_area)
    {
        for (int j = 0 ; j < coins.size() ; j++)
        {
            Area coin_area = new Area(new Ellipse2D.Double((int)coins.get(j).position.getX(), (int)coins.get(j).position.getY(), coin_width, coin_height));

            GeneralPath coin_path = new GeneralPath();
            coin_path.append(coin_area.getPathIterator(new AffineTransform()), true);

            Area coin = new Area(coin_path);
            coin.intersect(moving_tank_area);

            if (!coin.isEmpty())
            {
                tanks.get(getPlayer_turn()).setMoney(tanks.get(getPlayer_turn()).getMoney() + coin_price);
                coins.remove(j);
                animations.remove(j);
                coin_obtained_sound.stop();
                coin_obtained_sound.setFramePosition(0);
                coin_obtained_sound.start();
                if(tanks.get(player_turn).getAI())
                    tank_AI.generate_movement(tanks, coins, game_screen_width, tank_width, coin_width, player_turn);
            }
        }
    }

    public void move_tank_right()
    {
        Tank tank = tanks.get(player_turn);

        if(tank.getTank_slope() == 90 || tank.getTank_slope() == -90)
            return;

        boolean found = false;

        int X = (int)tank.position.getX() + (tank_width / 2) + 1;

        if (X >= game_screen_width - (tank_width / 2) || tank.getFuel() == 0 || (skyline_Y[X + (tank_width / 2) - 1] - skyline_Y[X + (tank_width / 2)]) >= block_height)
            return;

        int Y = skyline_Y[X];

        if(skyline_Y[X] - skyline_Y[X - 1] > 15)
        {
            tank.setTank_slope(90);
            return;
        }

        for(int i = 1 ; i <= (tank_width / 2) ; i++)
        {
            for(int j = -45 ; j <= 45 ; j++)
            {
                Area tank_area = new Area(new Rectangle(X - (tank_width / 2), Y - tank_height - i, tank_width, tank_height));

                AffineTransform transform_tank = new AffineTransform();
                transform_tank.rotate(Math.toRadians(j), X, Y - i);

                GeneralPath tank_path = new GeneralPath();
                tank_path.append(tank_area.getPathIterator(transform_tank), true);

                tank_area = new Area(tank_path);
                boolean correct = true;

                for(int k = -(tank_width / 2) ; k < (tank_width / 2) ; k++)
                    if(tank_area.contains(new Point(X + k, skyline_Y[X + k])))
                        correct = false;

                if(correct)
                {
                    if(!check_tank_collision(tank_area))
                    {
                        found = true;
                        if(tank.getAI())
                            tank_AI.terminate_move();
                        break;
                    }
                    check_coin_collision(tank_area);
                    tank.position.setX(X - (tank_width / 2));
                    tank.position.setY(Y - i - tank_height);
                    tank.setTank_slope(j);
                    tank.setFuel(tank.getFuel() - 1);
                    found = true;
                    break;
                }
            }
            if(found)
                break;
        }
    }

    public void move_tank_left()
    {
        Tank tank = tanks.get(player_turn);

        if(tank.getTank_slope() == 90 || tank.getTank_slope() == -90)
            return;

        boolean found = false;

        int X = (int)tank.position.getX() + (tank_width / 2) - 1;

        if (X < (tank_width / 2) || tank.getFuel() == 0 || (skyline_Y[X - (tank_width / 2) + 1] - skyline_Y[X - (tank_width / 2)]) >= block_height)
            return;

        int Y = skyline_Y[X];

        if(skyline_Y[X] - skyline_Y[X + 1] > 15)
        {
            tank.setTank_slope(-90);
            return;
        }

        for(int i = 1 ; i <= (tank_width / 2) ; i++)
        {
            for(int j = 45 ; j >= -45 ; j--)
            {
                Area tank_area = new Area(new Rectangle(X - (tank_width / 2), Y - tank_height - i, tank_width, tank_height));

                AffineTransform transform_tank = new AffineTransform();
                transform_tank.rotate(Math.toRadians(j), X, Y - i);

                GeneralPath tank_path = new GeneralPath();
                tank_path.append(tank_area.getPathIterator(transform_tank), true);

                tank_area = new Area(tank_path);
                boolean correct = true;

                for(int k = -(tank_width / 2) ; k < (tank_width / 2) ; k++)
                    if(tank_area.contains(new Point(X + k, skyline_Y[X + k])))
                        correct = false;

                if(correct)
                {
                    if(!check_tank_collision(tank_area))
                    {
                        found = true;
                        break;
                    }
                    check_coin_collision(tank_area);
                    tank.position.setX(X - (tank_width / 2));
                    tank.position.setY(Y - i - tank_height);
                    tank.setTank_slope(j);
                    tank.setFuel(tank.getFuel() - 1);
                    found = true;
                    break;
                }
            }
            if(found)
                break;
        }
    }

    public void move_tank_fall()
    {
        for(int i = 0 ; i < tanks.size() ; i++)
        {
            if(tanks.get(i).getTank_slope() == 90 || tanks.get(i).getTank_slope() == -90)
            {
                tanks.get(i).position.setY(tanks.get(i).position.getY() + 2);

                Area tank_area = new Area(new Rectangle((int)tanks.get(i).position.getX(), (int)tanks.get(i).position.getY(), tank_width, tank_height));

                AffineTransform transform_tank = new AffineTransform();
                transform_tank.rotate(Math.toRadians(tanks.get(i).getTank_slope()), (int)tanks.get(i).position.getX() + (tank_width / 2), (int)tanks.get(i).position.getY() + tank_height);

                GeneralPath tank_path = new GeneralPath();
                tank_path.append(tank_area.getPathIterator(transform_tank), true);

                tank_area = new Area(tank_path);

                for(int k = -(tank_height / 2) ; k <= (tank_height / 2) ; k++)
                {
                    if(tank_area.contains(new Point((int)tanks.get(i).position.getX() + k, skyline_Y[(int)tanks.get(i).position.getX() + k])))
                    {
                        tanks.get(i).setHealth(0);

                        rips.add(new Rip(tanks.get(i).position.getX() + (tank_width / 2) - (rip_width / 2), tanks.get(i).position.getY() + tank_height - rip_height, tanks.get(i).getTank_slope()));
                        animations.add(new Animation(tanks.get(i).position.getX() + (tank_width / 2), tanks.get(i).position.getY() + (tank_height / 2), explosion_image, explosion_frame_width, explosion_frame_height, explosion_frame_count, tank_explosion_frame_delay, tank_explosion_frame_dimension, false));
                        tanks.remove(i);
                        players_count--;
                        tank_explosion_sound.stop();
                        tank_explosion_sound.setFramePosition(0);
                        tank_explosion_sound.start();
                        i--;
                        break;
                    }
                    if(tanks.get(i).position.getY() + tank_width > game_screen_height)
                    {
                        tanks.get(i).setHealth(0);

                        animations.add(new Animation(tanks.get(i).position.getX() + (tank_width / 2), tanks.get(i).position.getY() + (tank_height / 2), explosion_image, explosion_frame_width, explosion_frame_height, explosion_frame_count, tank_explosion_frame_delay, tank_explosion_frame_dimension, false));
                        tanks.remove(i);
                        players_count--;
                        tank_explosion_sound.stop();
                        tank_explosion_sound.setFramePosition(0);
                        tank_explosion_sound.start();
                        i--;
                        break;
                    }
                }
            }
        }
    }

    private void do_AI_command()
    {
        char move_command = tank_AI.getMove_command();
        char shoot_command = tank_AI.getShoot_command();

        if(move_command == 'L')
            move_tank_left();

        if(move_command == 'R')
            move_tank_right();

        if(move_command == 'S')
            tank_AI.generate_shoot(tanks, coins, player_turn, wind_speed, physics_scale, pipe_width, pipe_height, tank_width, tank_height, coin_width, coin_height, bullet_type_2_price, bullet_type_3_price, difficulty_level);

        if(shoot_command == 'L')
            if(tanks.get(player_turn).getFire_power() != 0)
                tanks.get(player_turn).setFire_power(tanks.get(player_turn).getFire_power() - 1);

        if(shoot_command == 'R')
            if(tanks.get(player_turn).getFire_power() != 100)
                tanks.get(player_turn).setFire_power(tanks.get(player_turn).getFire_power() + 1);

        if(shoot_command == 'U')
            if(tanks.get(player_turn).getPipe_angle() != 180)
                tanks.get(player_turn).setPipe_angle(tanks.get(player_turn).getPipe_angle() + 1);

        if(shoot_command == 'D')
            if(tanks.get(player_turn).getPipe_angle() != 0)
                tanks.get(player_turn).setPipe_angle(tanks.get(player_turn).getPipe_angle() - 1);

        if(shoot_command == 'B')
            buy_bullet(2);

        if(shoot_command == 'N')
            buy_bullet(3);

        if(shoot_command == '1')
            fire_Bullet(1);

        if(shoot_command == '2')
            fire_Bullet(2);

        if(shoot_command == '3')
            fire_Bullet(3);
    }

    private void updateTime()
    {
        time = System.currentTimeMillis() - time_0;
    }

    private void updateTime_text()
    {
        time_text = Long.toString(turn_time - ((time / 1000) % 60));
    }

    private void updateTurn_text()
    {
        turn_text = "Turn: Player " + Long.toString(player_turn + 1);
    }

    private void updateWind_text()
    {
        wind_text = Integer.toString(wind_speed) + " KM/H";
    }

    private void changePlayer_turn()
    {
        turn_has_recently_changed = true;
        player_turn = (player_turn + 1) % players_count;
        setWind_speed();
        resetTime_0();
        if(tanks.get(player_turn).getAI())
            tank_AI.generate_movement(tanks, coins, game_screen_width, tank_width, coin_width, player_turn);
    }

    private void setWind_speed()
    {
        int wind_range = 100;
        wind_speed = rnd.nextInt(wind_range * 2 + 1) - (wind_range);
        if(wind_speed >= 0)
        {
            movingbackground_clouds[0].setMove_speed(-(wind_speed + 9) / 10);
            movingbackground_clouds[1].setMove_speed((wind_speed + 9) / 10);
        }
        if(wind_speed < 0)
        {
            movingbackground_clouds[0].setMove_speed(-(wind_speed - 9) / 10);
            movingbackground_clouds[1].setMove_speed((wind_speed - 9) / 10);
        }
    }

    private void resetTime_0()
    {
        time_0 = System.currentTimeMillis();
    }

    public int getPlayer_turn()
    {
        return player_turn;
    }

    public void setTurn_has_recently_changed(boolean turn_has_recently_changed)
    {
        this.turn_has_recently_changed = turn_has_recently_changed;
    }

    public boolean getTurn_has_recently_changed()
    {
        return turn_has_recently_changed;
    }

    public void fire_Bullet(int type)
    {
        if (type != 1)
            tanks.get(getPlayer_turn()).bullets_count[type - 2]--;

        if (bullets.size() == 0)
        {
            double x1 = tanks.get(getPlayer_turn()).position.getX() + (tank_width * 0.45);
            double x = tanks.get(getPlayer_turn()).position.getX() + (tank_width / 2);

            double y1 = tanks.get(getPlayer_turn()).position.getY() + (tank_height * 0.2) - (pipe_height / 2);
            double y = tanks.get(getPlayer_turn()).position.getY() + tank_height;

            double theta = Math.toRadians(-tanks.get(getPlayer_turn()).getTank_slope());

            double newCenterX = Math.cos(theta) * (x1 - x) + Math.sin(theta) * (y1 - y) + x;

            double newCenterY = -Math.sin(theta) * (x1 - x) + Math.cos(theta) * (y1 - y) + y;

            double X = newCenterX + (pipe_width * Math.cos(Math.toRadians(tanks.get(getPlayer_turn()).getPipe_angle() - tanks.get(getPlayer_turn()).getTank_slope())));
            double Y = newCenterY - (pipe_width * Math.sin(Math.toRadians(tanks.get(getPlayer_turn()).getPipe_angle() - tanks.get(getPlayer_turn()).getTank_slope())));

            bullets.add(new Bullet(X, Y, tanks.get(getPlayer_turn()).getFire_power() * Math.cos(Math.toRadians(tanks.get(getPlayer_turn()).getPipe_angle() - tanks.get(getPlayer_turn()).getTank_slope())), -tanks.get(getPlayer_turn()).getFire_power() * Math.sin(Math.toRadians(tanks.get(getPlayer_turn()).getPipe_angle() - tanks.get(getPlayer_turn()).getTank_slope())), wind_speed, type, physics_scale));

            int fire_frame_count = 12;
            int fire_frame_width = 134;
            int fire_frame_height = 134;
            int fire_frame_dimension = 20;
            long fire_frame_delay = 50;
            animations.add(new Animation(X, Y, explosion_image, fire_frame_width, fire_frame_height, fire_frame_count, fire_frame_delay, fire_frame_dimension, false));

            bullet_fired_sound.stop();
            bullet_fired_sound.setFramePosition(0);
            bullet_fired_sound.start();
        }
    }

    public void buy_bullet(int type)
    {
        if (type == 2 && tanks.get(getPlayer_turn()).getMoney() >= bullet_type_2_price)
        {
            tanks.get(getPlayer_turn()).bullets_count[type - 2]++;
            tanks.get(getPlayer_turn()).setMoney(tanks.get(getPlayer_turn()).getMoney() - bullet_type_2_price);

            coin_spend_sound.stop();
            coin_spend_sound.setFramePosition(0);
            coin_spend_sound.start();
        }
        if (type == 3 && tanks.get(getPlayer_turn()).getMoney() >= bullet_type_3_price)
        {
            tanks.get(getPlayer_turn()).bullets_count[type - 2]++;
            tanks.get(getPlayer_turn()).setMoney(tanks.get(getPlayer_turn()).getMoney() - bullet_type_3_price);

            coin_spend_sound.stop();
            coin_spend_sound.setFramePosition(0);
            coin_spend_sound.start();
        }
    }

    public int getGame_state()
    {
        return game_state;
    }

    public void setGame_state(int game_state)
    {
        this.game_state = game_state;
    }

    public void setMouse_start_position(Point mouse_start_position)
    {
        this.mouse_start_position = mouse_start_position;
    }

    public Point getMouse_start_position()
    {
        return mouse_start_position;
    }

    public void startPause()
    {
        pause_time_start = System.currentTimeMillis();
        stopSounds();
    }

    public void endPause()
    {
        time_0 += System.currentTimeMillis() - pause_time_start;

        for (Bullet bullet : bullets)
            bullet.addTime(System.currentTimeMillis() - pause_time_start);

        for (Animation animation : animations)
            animation.addTime(System.currentTimeMillis() - pause_time_start);

        int cloudy_moving_layers_count = 3;
        for(int i = 0 ; i < (cloudy_moving_layers_count - 1) ; i++)
            movingbackground_clouds[i].addTime(System.currentTimeMillis() - pause_time_start);

        resumeSounds();
    }

    public void stopSounds()
    {
        bullet_fired_sound.stop();
        bullet_impact_sound.stop();
        tank_explosion_sound.stop();
        coin_obtained_sound.stop();
        coin_spend_sound.stop();
        click_sound.stop();
        tank_collision_sound.stop();
    }

    public void resumeSounds()
    {
        if(bullet_fired_sound.getLongFramePosition() != 0)
            bullet_fired_sound.start();

        if(bullet_impact_sound.getLongFramePosition() != 0)
            bullet_impact_sound.start();

        if(tank_explosion_sound.getLongFramePosition() != 0)
            tank_explosion_sound.start();

        if(coin_obtained_sound.getLongFramePosition() != 0)
            coin_obtained_sound.start();

        if(coin_spend_sound.getLongFramePosition() != 0)
            coin_spend_sound.start();

        if(click_sound.getLongFramePosition() != 0)
            click_sound.start();

        if(tank_collision_sound.getLongFramePosition() != 0)
            tank_collision_sound.start();
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        CustomShapedButton source = (CustomShapedButton) event.getSource();

        if(event.getActionCommand().equals("pause") && source.getIs_click_correct())
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            game_state = 1;
            source.setIs_click_correct(false);
        }

        if(event.getActionCommand().equals("restart") && source.getIs_click_correct())
        {
            click_sound.stop();
            click_sound.setFramePosition(0);
            click_sound.start();
            game_state = 2;
            source.setIs_click_correct(false);
        }
    }
}