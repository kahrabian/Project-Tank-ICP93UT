package project.tank;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MovingBackground
{
    private BufferedImage background;

    private double move_speed;

    private long time_0;
    private long time_delay;

    private int x_first;
    private int x_second;

    private int screen_width;
    private int screen_height;

    public MovingBackground(BufferedImage background, double move_speed, int screen_width, int screen_height)
    {
        this.background = background;
        this.move_speed = move_speed;

        this.screen_width = screen_width;
        this.screen_height = screen_height;

        time_0 = System.currentTimeMillis();
        this.time_delay = (long) 20;

        x_first = 0;
        if (move_speed > 0)
            x_second = -(screen_width);
        if (move_speed < 0)
            x_second = screen_width;
    }

    private void update_position()
    {
        if ((System.currentTimeMillis() - time_0) > time_delay)
        {
            time_0 = System.currentTimeMillis();
            if(move_speed > 0)
            {
                x_first = (int)(x_first + move_speed + screen_width) % (2 * screen_width) - screen_width;
                x_second = (int)(x_second + move_speed + screen_width) % (2 * screen_width) - screen_width;
            }
            if(move_speed < 0)
            {
                x_first = (int)(x_first + move_speed - screen_width) % (2 * screen_width) + screen_width;
                x_second = (int)(x_second + move_speed - screen_width) % (2 * screen_width) + screen_width;
            }
        }
    }

    public void draw(Graphics2D drawer)
    {
        update_position();
        drawer.drawImage(background, x_first, 0, screen_width, screen_height, null);
        drawer.drawImage(background, x_second, 0, screen_width, screen_height, null);
    }

    public void addTime(long pause_time)
    {
        time_0 += pause_time;
    }

    public void setMove_speed(double move_speed)
    {
        this.move_speed = move_speed;
    }
}