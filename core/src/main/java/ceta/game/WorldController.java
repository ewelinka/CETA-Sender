package ceta.game;

import ceta.game.actors.ActionSubmitTrigger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import java.util.Arrays;

/**
 * Created by ewe on 9/23/16.
 */
public class WorldController extends InputAdapter implements Disposable {
    private static final String TAG = WorldController.class.getName();

    public Level level;
    public Game game;
    private Stage stage;
    private VirtualBlocksManager virtualBlocksManager;
    
    private ActionSubmitTrigger actionSubmitTriggerButton;

    public short [] detected_numbers;
    public short [] previous_detected;
    public short currentDiff;


    public WorldController (Game game, Stage stage) {
        this.stage = stage;
        virtualBlocksManager = new VirtualBlocksManager(stage, ((CetaSender)game).getMessageSender());
        init(game);
        localInit();
    }
    
    private void localInit () {
        level = new Level(stage);

        //smarichal add the button to trigger the action submit countdown.
        this.actionSubmitTriggerButton = new ActionSubmitTrigger(5, false);
        stage.addActor(this.actionSubmitTriggerButton);
        
        virtualBlocksManager.init();
        

    }

    public void init (Game game) {

        this.game = game;
        initValues();

    }

    private void initValues(){
        detected_numbers = new short [5];
        previous_detected = new short [5];


        for(short i = 0; i<5;i++){
            detected_numbers[i] = 0;
            previous_detected[i] = 0;

        }

    }

    public void findDifferences(){
        // @SEBA aca se puede mandar eventos
        // ojo!!! lo que se corresponde a la pieza 1 esta en la posicion 0 !!

        for(short i = 0; i<5;i++){

            currentDiff = (short)(previous_detected[i] - detected_numbers[i]);
            if ( currentDiff < 0){
                Gdx.app.debug(TAG,"we should add "+currentDiff+" to:"+i+" position");

            }
            else if (currentDiff > 0){
                Gdx.app.debug(TAG,"we should remove "+currentDiff+" to:"+i+" position");

            }

        }

    }


    public void update (float deltaTime) {

        level.update(deltaTime);
        virtualBlocksManager.updateDetected();
        previous_detected = Arrays.copyOf(detected_numbers, detected_numbers.length);
        detected_numbers = virtualBlocksManager.getDetectedBlocks();
        findDifferences();


    }

    @Override
    public boolean keyUp (int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            Gdx.app.exit();
        }
        return false;
    }



    @Override
    public void dispose() {

    }
}
