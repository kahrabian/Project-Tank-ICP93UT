package project.tank;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

public class Ai
{
    private ArrayDeque<Character> movement;
    private ArrayDeque<Character> shoot;
    private ArrayList<Integer> shoot_power;
    private ArrayList<Integer> shoot_angle;
    private ArrayList<Integer> shoot_type;

    Ai()
    {
        movement = new ArrayDeque<Character>(0);
        shoot = new ArrayDeque<Character>(0);
        shoot_power = new ArrayList<Integer>(0);
        shoot_angle = new ArrayList<Integer>(0);
        shoot_type = new ArrayList<Integer>(0);
    }

    public void generate_movement(ArrayList<Tank> tanks, ArrayList<Coin> coins, final int game_screen_width, final int tank_width, final int coin_width, final int player_turn)
    {
        movement.clear();
        shoot.clear();

        boolean accessible;

        if(coins.size() > 0)
        {
            int min_move = game_screen_width;

            Coin target = coins.get(0);

            for (Coin coin : coins)
            {
                accessible = true;

                if (Math.abs((coin.position.getX() + coin_width / 2) - (tanks.get(player_turn).position.getX() + tank_width / 2)) < min_move)
                {
                    for (int j = 0; j < tanks.size(); j++)
                    {
                        if (j == player_turn)
                            continue;

                        if (((tanks.get(j).position.getX() + tank_width / 2) >= (coin.position.getX() + coin_width / 2) && (tanks.get(j).position.getX() + tank_width / 2) <= (tanks.get(player_turn).position.getX() + tank_width / 2)) || ((tanks.get(j).position.getX() + tank_width / 2) <= (coin.position.getX() + coin_width / 2) && (tanks.get(j).position.getX() + tank_width / 2) >= (tanks.get(player_turn).position.getX() + tank_width / 2)))
                            accessible = false;
                    }
                    if (accessible)
                    {
                        min_move = (int) Math.abs((coin.position.getX() + coin_width / 2) - (tanks.get(player_turn).position.getX() + tank_width / 2));
                        target = coin;
                    }
                }
            }

            int distance = (int)((target.position.getX() + coin_width / 2) - (tanks.get(player_turn).position.getX() + tank_width / 2));

            if(distance > 0)
            {
                accessible = true;

                for(int j = 0 ; j < tanks.size() ; j++)
                {
                    if(j == player_turn)
                        continue;

                    if(((tanks.get(j).position.getX() + tank_width / 2) >= (target.position.getX() + coin_width / 2) && (tanks.get(j).position.getX() + tank_width / 2) <= (tanks.get(player_turn).position.getX() + tank_width / 2)) || ((tanks.get(j).position.getX() + tank_width / 2) <= (target.position.getX() + coin_width / 2) && (tanks.get(j).position.getX() + tank_width / 2) >= (tanks.get(player_turn).position.getX() + tank_width / 2)))
                        accessible = false;
                }

                if(accessible && distance < tanks.get(player_turn).getFuel())
                    for(int i = 0 ; i < distance ; i++)
                        movement.add('R');
            }
            else
            {
                accessible = true;

                for(int j = 0 ; j < tanks.size() ; j++)
                {
                    if(j == player_turn)
                        continue;

                    if(((tanks.get(j).position.getX() + tank_width / 2) >= (target.position.getX() + coin_width / 2) && (tanks.get(j).position.getX() + tank_width / 2) <= (tanks.get(player_turn).position.getX() + tank_width / 2)) || ((tanks.get(j).position.getX() + tank_width / 2) <= (target.position.getX() + coin_width / 2) && (tanks.get(j).position.getX() + tank_width / 2) >= (tanks.get(player_turn).position.getX() + tank_width / 2)))
                        accessible = false;
                }

                if(accessible && -distance < tanks.get(player_turn).getFuel())
                    for(int i = 0 ; i < -distance ; i++)
                        movement.add('L');
            }
        }
        if(movement.size() == 0)
            movement.add('S');
    }

    @SuppressWarnings("ConstantConditions")
    public void generate_shoot(ArrayList<Tank> tanks, ArrayList<Coin> coins, int player_turn, double wind_force, double scale, int pipe_width, int pipe_height, int tank_width, int tank_height, int coin_width, int coin_height, int bullet_type_2_price, int bullet_type_3_price, int difficulty_level)
    {
        shoot.clear();
        shoot_power.clear();
        shoot_angle.clear();
        shoot_type.clear();

        double error = 1;

        if(difficulty_level == 1)
        {
            error = 0.9;
        }

        if(difficulty_level == 2)
        {
            error = 0.95;
        }

        if(difficulty_level == 3)
        {
            error = 1;
        }

        double x0;
        double y0;

        double v0x;
        double v0y;

        double a0x;
        double a0y;

        double delta_x;
        double delta_y;

        for(int i = 0 ; i < tanks.size() ; i++)
        {
            if(i == player_turn)
                continue;

            for(int j = 0 ; j <= 180 ; j++)
            {
                for(int k = 0 ; k <= 100 ; k++)
                {
                    for(int l = 1 ; l <= 3 ; l++)
                    {
                        v0x = k * Math.cos(Math.toRadians(j - tanks.get(player_turn).getTank_slope())) * scale;
                        v0y = -k * Math.sin(Math.toRadians(j - tanks.get(player_turn).getTank_slope())) * scale;

                        double x1 = tanks.get(player_turn).position.getX() + (tank_width * 0.45);
                        double x = tanks.get(player_turn).position.getX() + (tank_width / 2);

                        double y1 = tanks.get(player_turn).position.getY() + (tank_height * 0.2) - (pipe_height / 2);
                        double y = tanks.get(player_turn).position.getY() + tank_height;

                        double theta = Math.toRadians(-tanks.get(player_turn).getTank_slope());


                        double newCenterX = Math.cos(theta) * (x1 - x) + Math.sin(theta) * (y1 - y) + x;

                        double newCenterY = -Math.sin(theta) * (x1 - x) + Math.cos(theta) * (y1 - y) + y;

                        x0 = newCenterX + (pipe_width * Math.cos(Math.toRadians(j - tanks.get(player_turn).getTank_slope())));
                        y0 = newCenterY - (pipe_width * Math.sin(Math.toRadians(j - tanks.get(player_turn).getTank_slope())));

                        a0x = scale * wind_force / 1;

                        if(l == 2)
                            a0x = scale * wind_force / 10;

                        if(l == 3)
                            a0x = scale * wind_force / 100;

                        a0y = 2 * scale * 10;

                        for(int u = 0 ; u < tank_width ; u++)
                        {
                            delta_x = tanks.get(i).position.getX() + u - x0;
                            delta_y = tanks.get(i).position.getY() + (tank_height / 2) - y0; // adjust if necessary

                            double time = (- v0x + Math.sqrt((v0x * v0x) + (4 * a0x * delta_x))) / a0x;

//                                double answer = (2 * a0y / (a0x * a0x)) * (v0x * (v0x - Math.sqrt((v0x * v0x) + (2 * a0x * delta_x))) + (a0x * delta_x))
//                                        + (2 * v0y / a0x) * (-v0x + Math.sqrt((v0x * v0x) + (2 * a0x * delta_x))) - (2 * delta_y);

                            double answer = (a0y * time * time) / 2 + (v0y * time) - delta_y;
                            if(answer >= -0.01 && answer <= 0.01)
                            {
                                shoot_power.add((int)(j * error));
                                shoot_angle.add((int)(k * error));
                                shoot_type.add(l);
                            }
                        }
                    }
                }
            }
        }
        if(shoot_power.size() == 0)
        {
            for(int i = 0 ; i < coins.size() ; i++)
            {
                if(i == player_turn)
                    continue;

                for(int j = 0 ; j <= 180 ; j++)
                {
                    for(int k = 0 ; k <= 100 ; k++)
                    {
                        for(int l = 1 ; l <= 3 ; l++)
                        {
                            if(l == 2 && tanks.get(player_turn).bullets_count[0] == 0 || tanks.get(player_turn).getMoney() > bullet_type_2_price)
                                continue;

                            if(l == 3 && tanks.get(player_turn).bullets_count[1] == 0 || tanks.get(player_turn).getMoney() > bullet_type_3_price)
                                continue;

                            v0x = k * Math.cos(Math.toRadians(j - tanks.get(player_turn).getTank_slope())) * scale;
                            v0y = -k * Math.sin(Math.toRadians(j - tanks.get(player_turn).getTank_slope())) * scale;

                            double x1 = tanks.get(player_turn).position.getX() + (tank_width * 0.45);
                            double x = tanks.get(player_turn).position.getX() + (tank_width / 2);

                            double y1 = tanks.get(player_turn).position.getY() + (tank_height * 0.2) - (pipe_height / 2);
                            double y = tanks.get(player_turn).position.getY() + tank_height;

                            double theta = Math.toRadians(-tanks.get(player_turn).getTank_slope());


                            double newCenterX = Math.cos(theta) * (x1 - x) + Math.sin(theta) * (y1 - y) + x;

                            double newCenterY = -Math.sin(theta) * (x1 - x) + Math.cos(theta) * (y1 - y) + y;

                            x0 = newCenterX + (pipe_width * Math.cos(Math.toRadians(j - tanks.get(player_turn).getTank_slope())));
                            y0 = newCenterY - (pipe_width * Math.sin(Math.toRadians(j - tanks.get(player_turn).getTank_slope())));

                            a0x = scale * wind_force / 1;

                            if(l == 2)
                                a0x = scale * wind_force / 10;

                            if(l == 3)
                                a0x = scale * wind_force / 100;

                            a0y = 2 * scale * 10;

                            for(int u = 0 ; u < coin_width ; u += 2)
                            {
                                delta_x = coins.get(i).position.getX() + u - x0;
                                delta_y = coins.get(i).position.getY() + (coin_height / 2) - y0; // adjust if necessary

                                double time = (- v0x + Math.sqrt((v0x * v0x) + (4 * a0x * delta_x))) / a0x;

//                                double answer = (2 * a0y / (a0x * a0x)) * (v0x * (v0x - Math.sqrt((v0x * v0x) + (2 * a0x * delta_x))) + (a0x * delta_x))
//                                        + (2 * v0y / a0x) * (-v0x + Math.sqrt((v0x * v0x) + (2 * a0x * delta_x))) - (2 * delta_y);

                                double answer = (a0y * time * time) / 2 + (v0y * time) - delta_y;

                                if(answer >= -0.01 && answer <= 0.01)
                                {
                                    shoot_power.add((int)(j * error));
                                    shoot_angle.add((int)(k * error));
                                    shoot_type.add(l);
                                }
                            }
                        }
                    }
                }
            }
        }

        if(shoot_power.size() == 0)
            return;

        Random answer_picker = new Random();
        int picked_answer = answer_picker.nextInt(shoot_power.size());
//        System.out.println(shoot_power.size());
//        System.out.println(shoot_power.get(picked_answer));
//        System.out.println(tanks.get(player_turn).getFire_power());
//        System.out.println(shoot_angle.get(picked_answer));
//        System.out.println(tanks.get(player_turn).getPipe_angle());
//        System.out.println(shoot_type.get(picked_answer));

        if(shoot_power.get(picked_answer) >= tanks.get(player_turn).getFire_power())
        {
            for(int i = 0 ; i < shoot_power.get(picked_answer) - tanks.get(player_turn).getFire_power() ; i++)
            {
                shoot.add('R');
            }
        }
        else
        {
            for(int i = 0 ; i < tanks.get(player_turn).getFire_power() - shoot_power.get(picked_answer) ; i++)
            {
                System.out.println(tanks.get(player_turn).getFire_power());
                shoot.add('L');
            }
        }

        if(shoot_angle.get(picked_answer) >= tanks.get(player_turn).getPipe_angle())
        {
            for(int i = 0 ; i < shoot_angle.get(picked_answer) - tanks.get(player_turn).getPipe_angle() ; i++)
            {
                shoot.add('U');
            }
        }
        else
        {
            for(int i = 0 ; i < tanks.get(player_turn).getPipe_angle() - shoot_angle.get(picked_answer) ; i++)
            {
                shoot.add('D');
            }
        }

        if (shoot_type.get(picked_answer) == 1)
        {
            shoot.add('1');
        }

        if (shoot_type.get(picked_answer) == 2)
        {
            if(tanks.get(player_turn).bullets_count[0] > 0)
            {
                shoot.add('2');
            }
            else
            {
                shoot.add('B');
                shoot.add('2');
            }
        }
        if (shoot_type.get(picked_answer) == 3)
        {
            if(tanks.get(player_turn).bullets_count[1] > 0)
            {
                shoot.add('3');
            }
            else
            {
                shoot.add('N');
                shoot.add('3');
            }
        }
    }

    public char getMove_command()
    {
        if(movement.size() > 0)
            return movement.pop();
        return 'X';
    }

    public char getShoot_command()
    {
        if(shoot.size() > 0)
            return shoot.pop();
        return 'X';
    }

    public void terminate_move()
    {
        movement.clear();
        movement.add('S');
    }
}