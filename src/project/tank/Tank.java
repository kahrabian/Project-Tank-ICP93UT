package project.tank;

public class Tank
{
    public Vector_2D position;

    private int health;
    private int money;
    private int fuel;

    public int[] bullets_count;

    private int tank_slope;
    private int pipe_angle;

    private int fire_power;

    private String name;

    private boolean AI;

    public Tank(double x, double y, int tank_slope, String name, boolean AI)
    {
        position = new Vector_2D(x, y);

        health = 1000;
        money = 200;

        bullets_count = new int[] {0, 0};

        pipe_angle = 30;
        this.tank_slope = tank_slope;

        fire_power = 50;

        this.name = name;

        this.AI = AI;

        fuel = 400;
    }

    public int getHealth()
    {
        return health;
    }

    public int getMoney()
    {
        return money;
    }

    public int getPipe_angle()
    {
        return pipe_angle;
    }

    public int getTank_slope()
    {
        return tank_slope;
    }

    public int getFire_power()
    {
        return fire_power;
    }

    public String getName()
    {
        return name;
    }

    public boolean getAI()
    {
        return AI;
    }

    public int getFuel()
    {
        return fuel;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }

    public void setMoney(int money)
    {
        this.money = money;
    }

    public void setPipe_angle(int pipe_angle)
    {
        this.pipe_angle = pipe_angle;
    }

    public void setTank_slope(int tank_slope)
    {
        this.tank_slope = tank_slope;
    }

    public void setFire_power(int fire_power)
    {
        this.fire_power = fire_power;
    }

    public void setFuel(int fuel)
    {
        this.fuel = fuel;
    }
}