package project.tank;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Map_Demo extends JPanel
{
    private final int game_screen_width = 576;
    private final int game_screen_height = 384;

    private final int map_table_width = 192;
    private final int map_table_height = 128;

    private final int block_width = 3;
    private final int block_height = 3;

    private final int map_images_count = 11;

    private final int map_texture_count = 4;
    private int map_texture_selected;

    private final int map_count = 4;
    private int map_selected;

    private String[][] map_table;

    private String[][] map_images_paths;
    private String[] map_paths;
    private BufferedImage[][] map_images;
    private BufferedImage game_background_night;
    private BufferedImage game_background_day;
    private BufferedImage game_background_cloudy;
    private MovingBackground[] movingbackground_clouds;

    public ArrayList<ArrayList<Block>> maps;

    private int game_background_time;

    public Map_Demo() throws IOException
    {
        setPreferredSize(new Dimension(game_screen_width, game_screen_height));

        map_table = new String[map_table_height][map_table_width];

        construct_map_images();

        construct_map_paths();

        construct_others();

        readMap();
    }

    private void construct_map_images() throws IOException
    {
        construct_map_images_paths();

        map_images = new BufferedImage[map_texture_count][map_images_count];
        for (int i = 0 ; i < map_texture_count ; i++)
            for (int j = 0 ; j < map_images_count ; j++)
                map_images[i][j] = ImageIO.read(new File(map_images_paths[i][j]));

        game_background_night = ImageIO.read(new File("GameResources/BackGrounds/static_night.jpg"));
        game_background_day = ImageIO.read(new File("GameResources/BackGrounds/static_day.png"));
        game_background_cloudy = ImageIO.read(new File("GameResources/BackGrounds/moving_cloudy_layer_1.png"));

        BufferedImage[] game_background_cloudy_moving = new BufferedImage[2];
        game_background_cloudy_moving[0] = ImageIO.read(new File("GameResources/BackGrounds/moving_cloudy_layer_2.png"));
        game_background_cloudy_moving[1] = ImageIO.read(new File("GameResources/BackGrounds/moving_cloudy_layer_3.png"));

        movingbackground_clouds = new MovingBackground[2];
        movingbackground_clouds[0] = new MovingBackground(game_background_cloudy_moving[0], 2, game_screen_width, game_screen_height);
        movingbackground_clouds[1] = new MovingBackground(game_background_cloudy_moving[1], -2, game_screen_width, game_screen_height);
    }

    private void construct_map_paths()
    {
        map_paths = new String[map_count];
        map_paths[0] = "GameResources/Maps/map_1.txt";
        map_paths[1] = "GameResources/Maps/map_2.txt";
        map_paths[2] = "GameResources/Maps/map_3.txt";
        map_paths[3] = "GameResources/Maps/map_4.txt";
    }

    private void construct_map_images_paths()
    {
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

    private void construct_others()
    {
        map_texture_selected = 0;

        map_selected = 0;

        maps = new ArrayList<ArrayList<Block>>(map_count);

        for (int i = 0 ; i < map_count ; i++)
            maps.add(new ArrayList<Block>(0));

        game_background_time = 3;
    }

    public void readMap() throws FileNotFoundException
    {
        // READING MAP FROM FILE
        String tmp;
        for(int k = 0 ; k < map_count ; k++)
        {
            Scanner scan = new Scanner(new File(map_paths[k]));

            int initial_width = scan.nextInt();
            int initial_height = scan.nextInt();

            scan.nextLine();

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
                {
                    if(tmp.charAt(j) == 't' || tmp.charAt(j) == 'T' || tmp.charAt(j) == 'c' || tmp.charAt(j) == 'C')
                        map_table[i][j] = "#";
                    else
                        map_table[i][j] = Character.toString(tmp.charAt(j - ((map_table_width - initial_width) / 2)));
                }
            }

            for(int i = (map_table_height - initial_height) ; i < map_table_height ; i++)
                for(int j = initial_width + (map_table_width - initial_width) / 2 ; j < map_table_width ; j++)
                    map_table[i][j] = "#";

            for (int i = 0 ; i < map_table_height ; i++)
                for (int j = 0 ; j < map_table_width ; j++)
                {
                    if (map_table[i][j].equals("0"))
                    {
                        if(i != 0 && (map_table[i - 1][j].equals("#") || map_table[i - 1][j].equals("3") || map_table[i - 1][j].equals("4")))
                            maps.get(k).add(new Block(j * block_width, i * block_height, 7));
                        else if(i != 0 && map_table[i - 1][j].equals("1"))
                            maps.get(k).add(new Block(j * block_width, i * block_height, 5));
                        else if(i != 0 && map_table[i - 1][j].equals("2"))
                            maps.get(k).add(new Block(j * block_width, i * block_height, 6));
                        else if(i != map_table_height - 1 && (map_table[i + 1][j].equals("#") || map_table[i + 1][j].equals("1") || map_table[i + 1][j].equals("2")))
                            maps.get(k).add(new Block(j * block_width, i * block_height, 10));
                        else if(i != map_table_height - 1 && map_table[i + 1][j].equals("3"))
                            maps.get(k).add(new Block(j * block_width, i * block_height, 9));
                        else if(i != map_table_height - 1 && map_table[i + 1][j].equals("4"))
                            maps.get(k).add(new Block(j * block_width, i * block_height, 8));
                        else maps.get(k).add(new Block(j * block_width, i * block_height, 0));
                    }

                    if (map_table[i][j].equals("1"))
                        maps.get(k).add(new Block(j * block_width, i * block_height, 1));

                    if (map_table[i][j].equals("2"))
                        maps.get(k).add(new Block(j * block_width, i * block_height, 2));

                    if (map_table[i][j].equals("3"))
                        maps.get(k).add(new Block(j * block_width, i * block_height, 3));

                    if (map_table[i][j].equals("4"))
                        maps.get(k).add(new Block(j * block_width, i * block_height, 4));
                }
        }
        // END OF READING MAP FROM FILE
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

        draw_map(map_drawer);
    }

    private void draw_map(Graphics2D map_drawer)
    {
        draw_map_background(map_drawer);

        // MAP BLOCKS
        for (int i = 0 ; i < maps.get(map_selected).size() ; i++)
        {
            map_drawer.drawImage(map_images[map_texture_selected][maps.get(map_selected).get(i).getType()], (int)maps.get(map_selected).get(i).position.getX(), (int)maps.get(map_selected).get(i).position.getY(), block_width, block_height, null);
        }
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

    public void setGame_background_time(int game_background_time)
    {
        this.game_background_time = game_background_time;
    }

    public int getGame_background_time()
    {
        return game_background_time;
    }

    public int getMap_texture_count()
    {
        return map_texture_count;
    }

    public void setMap_texture_selected(int map_texture_selected)
    {
        this.map_texture_selected = map_texture_selected;
    }

    public int getMap_texture_selected()
    {
        return map_texture_selected;
    }

    public int getMap_count()
    {
        return map_count;
    }

    public void setMap_selected(int map_selected)
    {
        this.map_selected = map_selected;
    }

    public int getMap_selected()
    {
        return map_selected;
    }

    public String getMap_selected_path()
    {
        return map_paths[map_selected];
    }
}