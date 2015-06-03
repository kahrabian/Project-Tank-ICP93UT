package project.tank;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class Game_main
{
    public static void main(String[] args)
    {
        int counter = 0;

        try
        {
            System.setErr(new PrintStream(new FileOutputStream("log.txt", true), true));
//            System.setOut(new PrintStream(new FileOutputStream("log.txt", true), true));

            Game_Run game = new Game_Run();

            Runtime game_runtime = Runtime.getRuntime();

            while (true)
            {
                game.run_menu();
                game_runtime.gc();
                game_runtime.runFinalization();

                while(true)
                    if(!game.run_game())
                        break;

                game_runtime.gc();
                game_runtime.runFinalization();
            }
        }
        catch(Exception ex)
        {
            counter++;
            System.err.println("Something Happened " + counter + ":");
            ex.printStackTrace();
            System.err.println();
        }
    }
}