package project.tank;

public class Rip
{
    public Vector_2D position;
    private int rip_slope;

    public Rip(double x, double y, int rip_slope)
    {
        position = new Vector_2D(x, y);
        this.rip_slope = rip_slope;
    }

    public int getRip_slope()
    {
        return rip_slope;
    }
}