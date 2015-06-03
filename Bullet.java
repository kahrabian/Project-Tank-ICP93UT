package project.tank;

public class Bullet
{
    public Vector_2D position_0;
    public Vector_2D position;
    private Vector_2D velocity_0;
    public Vector_2D velocity;
    public Vector_2D acceleration;

    private int type;

    private int weight;

    private double time_0;
    private double time;

    public Bullet(double x, double y, double v0x, double v0y, double wind_force, int type, double scale)
    {
        this.type = type;

        if(type == 1)
            weight = 1;
        if(type == 2)
            weight = 10;
        if(type == 3)
            weight = 100;

        position_0 = new Vector_2D(x, y);
        position = new Vector_2D(x, y);
        velocity_0 = new Vector_2D(scale * v0x, scale * v0y);
        velocity = new Vector_2D(scale * v0x, scale * v0y);
        acceleration = new Vector_2D(scale * wind_force / weight, 2 * scale * 10);

        time_0 = (double)System.currentTimeMillis();
        time = 0;
    }

    public void updatePosition()
    {
        time = (System.currentTimeMillis() - time_0) / 1000;

        velocity.setX(velocity_0.getX() + acceleration.getX() * time);
        velocity.setY(velocity_0.getY() + acceleration.getY() * time);

        position.setX(position_0.getX() + acceleration.getX() * (time * time) / 2 + velocity_0.getX() * time);
        position.setY(position_0.getY() + acceleration.getY() * (time * time) / 2 + velocity_0.getY() * time);
    }

    public double getAngle()
    {
        if (velocity.getY() == 0 && velocity.getX() < 0)
            return 180;

        if (velocity.getY() == 0 && velocity.getX() > 0)
            return 0;

        if (velocity.getX() == 0 && velocity.getY() < 0)
            return 90;

        if (velocity.getX() == 0 && velocity.getY() > 0)
            return 270;

        if (velocity.getX() == 0 && velocity.getY() == 0)
            return 0;

        if (velocity.getX() < 0)
            return Math.toDegrees(Math.atan(velocity.getY() / velocity.getX())) + 180;
        else
            return Math.toDegrees(Math.atan(velocity.getY() / velocity.getX()));
    }

    public int getType()
    {
        return type;
    }

    public void addTime(double pause_time)
    {
        time_0 += pause_time;
    }
}