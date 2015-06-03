package project.tank;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Game_Run
{
    private Clip menu_music;
    private Clip game_music;
    private Clip pause_music;
    private Clip game_over_music;
    private Clip game_won_music;

    private String map_selected_path;

    private int game_background_time;
    private int map_texture_selected;
    private int turn_time;
    private int difficulty_level;

    private int cpu_players_count;
    private int user_players_count;

    Game_Run() throws LineUnavailableException, UnsupportedAudioFileException, IOException
    {
        menu_music = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/menuMusic.wav")).getFormat()));
        menu_music.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/menuMusic.wav")));

        game_music = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/gameMusic.wav")).getFormat()));
        game_music.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/gameMusic.wav")));

        pause_music = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/pauseMusic.wav")).getFormat()));
        pause_music.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/pauseMusic.wav")));

        game_over_music = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/gameOverMusic.wav")).getFormat()));
        game_over_music.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/gameOverMusic.wav")));

        game_won_music = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, AudioSystem.getAudioInputStream(new File("GameResources/Sounds/gameWonMusic.wav")).getFormat()));
        game_won_music.open(AudioSystem.getAudioInputStream(new File("GameResources/Sounds/gameWonMusic.wav")));
    }

    public void run_menu() throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException
    {
        Runtime game_runtime = Runtime.getRuntime();

        Menu menu = new Menu();
        menu_music.stop();
        menu_music.setFramePosition(0);
        menu_music.start();
        menu_music.loop(Clip.LOOP_CONTINUOUSLY);

        while(true)
        {
            if(menu.getGame_start() == 1)
            {
                menu.setVisible(false);
                break;
            }
            menu.repaint();
            Thread.sleep(50);
        }

        menu_music.stop();

        game_background_time = menu.map.getGame_background_time();
        map_texture_selected = menu.map.getMap_texture_selected();
        map_selected_path = menu.map.getMap_selected_path();
        turn_time = menu.getTurn_time();

        cpu_players_count = menu.getCpu_players();
        user_players_count = menu.getUser_players();

        difficulty_level = menu.getDifficulty_level();

        menu.dispose();
        game_runtime.gc();
        game_runtime.runFinalization();
    }

    public boolean run_game() throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException
    {
        Runtime game_runtime = Runtime.getRuntime();

        long frame_delay = 10;
        long time_0;
        long time_spend;

        Game_Surface game = new Game_Surface(game_background_time, map_texture_selected, map_selected_path, turn_time, cpu_players_count, user_players_count, difficulty_level);

        game_music.stop();
        game_music.setFramePosition(0);
        game_music.start();
        game_music.loop(Clip.LOOP_CONTINUOUSLY);

        while(true)
        {
            game.game_map_surface.update_parameters();

            time_0 = System.currentTimeMillis();

            if(game.game_map_surface.getTurn_has_recently_changed())
            {
                game.game_map_surface.setTurn_has_recently_changed(false);
                game.force_slider.setValue(game.game_map_surface.tanks.get(game.game_map_surface.getPlayer_turn()).getFire_power());
                game.degree_slider.setValue(game.game_map_surface.tanks.get(game.game_map_surface.getPlayer_turn()).getPipe_angle());
                game.update_combobox();
            }

            if(game.game_map_surface.getGame_state() == 2 || game.pause_menu_panel.getPause_state() == 2)
            {
                game.setVisible(false);
                break;
            }

            if(game.game_map_surface.getGame_state() == 1 && !pause_music.isRunning())
            {
                game.game_map_surface.startPause();
                game.pause_menu_panel.requestFocus();

                game_music.stop();
                pause_music.stop();
                pause_music.setFramePosition(0);
                pause_music.start();

                game.pause_menu_panel.setVisible(true);
                game.game_panel.setVisible(false);
                game.pack();
            }

            if(game.pause_menu_panel.getPause_state() == 1)
            {

                game.game_map_surface.endPause();
                pause_music.stop();
                game_music.start();

                game.pause_menu_panel.setPause_state(0);
                game.game_map_surface.setGame_state(0);
                game.pause_menu_panel.setVisible(false);
                game.game_panel.setVisible(true);
                game.game_map_surface.requestFocus();
                game.pack();
            }
            if(game.game_map_surface.tanks.size() <= 1)
            {
                game.game_map_surface.setGame_state(3);
                break;
            }

            game.update_buttons();
            game.update_panels();
            game.repaint();

            time_spend = System.currentTimeMillis() - time_0;
            if(frame_delay - time_spend > 0)
                Thread.sleep(frame_delay - time_spend);
        }

        game.game_map_surface.stopSounds();
        game_music.stop();
        pause_music.stop();

        if(game.pause_menu_panel.getPause_state() == 2)
        {
            game.dispose();
            return false;
        }

        if(game.game_map_surface.getGame_state() == 3 && (game.game_map_surface.tanks.size() == 0 || game.game_map_surface.tanks.get(0).getAI()))
            if (!run_game_over(game))
                return false;

        if(game.game_map_surface.getGame_state() == 3 && !game.game_map_surface.tanks.get(0).getAI())
            if (!run_game_won(game))
                return false;

        game.dispose();
        game_runtime.gc();
        game_runtime.runFinalization();

        return true;
    }

    public boolean run_game_over(Game_Surface game) throws InterruptedException
    {
        game.game_over_panel.setVisible(true);
        game.game_won_panel.setVisible(false);
        game.pause_menu_panel.setVisible(false);
        game.game_panel.setVisible(false);
        game.game_over_panel.requestFocus();
        game.pack();

        game_music.stop();
        game_over_music.setFramePosition(0);
        game_over_music.start();
        game_over_music.loop(Clip.LOOP_CONTINUOUSLY);

        while(true)
        {
            if(game.game_over_panel.getGame_over_state() != 0)
                break;
            Thread.sleep(50);
        }

        game_over_music.stop();

        if(game.game_over_panel.getGame_over_state() == 1)
        {
            game.game_over_panel.setVisible(false);
            game.setVisible(false);
        }

        if(game.game_over_panel.getGame_over_state() == 2)
        {
            game.game_over_panel.setVisible(false);
            game.setVisible(false);
            game.dispose();
            return false;
        }

        return true;
    }

    public boolean run_game_won(Game_Surface game) throws InterruptedException
    {
        game.game_won_panel.setWinner(game.game_map_surface.tanks.get(0).getName() + " !");

        game.game_won_panel.setVisible(true);
        game.game_over_panel.setVisible(false);
        game.pause_menu_panel.setVisible(false);
        game.game_panel.setVisible(false);
        game.game_won_panel.requestFocus();
        game.pack();

        game_won_music.stop();
        game_won_music.setFramePosition(0);
        game_won_music.start();
        game_won_music.loop(Clip.LOOP_CONTINUOUSLY);

        while(true)
        {
            if(game.game_won_panel.getGame_won_state() != 0)
                break;
            Thread.sleep(50);
        }

        game_won_music.stop();

        if(game.game_won_panel.getGame_won_state() == 1)
        {
            game.game_won_panel.setVisible(false);
            game.setVisible(false);
        }

        if(game.game_won_panel.getGame_won_state() == 2)
        {
            game.game_won_panel.setVisible(false);
            game.setVisible(false);
            game.dispose();
            return false;
        }

        return true;
    }
}