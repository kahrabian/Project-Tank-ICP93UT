package project.tank;

public class Block
{
    public Vector_2D position;

    private int type;

    public Block(double x, double y, int type)
    {
        position = new Vector_2D(x, y);
        this.type = type;
    }

    public int getType()
    {
        return type;
    }
}