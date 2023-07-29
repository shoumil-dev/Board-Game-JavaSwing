package game;

/**
 * background thread class for handling the main game loop without interrupting the UI
 */
public class PerformThread extends Thread{

    /**
     * Override method of run() in the Game class
     */
    @Override
    public void run(){
        Game game = Game.getInstance();  //main game loop runs in background thread
        game.run();
    }
}
