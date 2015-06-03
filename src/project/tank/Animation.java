package project.tank;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Animation
{

    public Vector_2D position;

    private BufferedImage animation_image;

    private int frame_width;
    private int frame_height;

    private int frames_count;

    private long frames_delay;

    private int current_frame;

    public boolean active;

    private boolean loop;

    private long time_0;

    private int draw_dimension;

    Animation(double x, double y, BufferedImage animation_image, int frame_width, int frame_height, int frames_count, long frames_delay, int draw_dimension, boolean loop)
    {
        position = new Vector_2D(x, y);

        this.animation_image = animation_image;
        this.frame_width = frame_width;
        this.frame_height = frame_height;
        this.frames_count = frames_count;
        this.frames_delay = frames_delay;
        this.draw_dimension = draw_dimension;

        time_0 = System.currentTimeMillis();

        current_frame = 0;

        active = true;

        this.loop = loop;
    }

    public void draw(Graphics2D drawer)
    {
        current_frame = (int)(((System.currentTimeMillis() - time_0) / frames_delay) % frames_count);
        if (current_frame == frames_count - 1 && !loop)
            active = false;

        drawer.drawImage(animation_image, (int)(position.getX() - (draw_dimension / 2)), (int)(position.getY() - (draw_dimension / 2)),
                (int)(position.getX() + (draw_dimension / 2)), (int)(position.getY() + (draw_dimension / 2)),
                current_frame * frame_width, 0, (current_frame + 1) * frame_width, frame_height, null);
    }

    public void addTime(long pause_time)
    {
        time_0 += pause_time;
    }
}