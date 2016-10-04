package ceta.game;

import java.util.Collection;
import java.util.LinkedList;

import ceta.game.actors.ActionSubmitTrigger;
import ceta.game.interfaces.ActionSubmitCallbackInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by ewe on 9/23/16.
 */
public class SenderScreen implements Screen {
    private static final String TAG = SenderScreen.class.getName();
    protected Game game;

    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private OrthographicCamera camera;
    private Stage stage;

    private Skin skin;
    private boolean paused;
	private TextButton btnTrigger;
    private ActionSubmitTrigger actionSubmit;
    
    

    public SenderScreen (Game game) {
        this.game = game;
        this.actionSubmit = new ActionSubmitTrigger(5000, false);
    }


    @Override
    public void show() {
        // TODO load preferences
        stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH , Constants.VIEWPORT_HEIGHT));
        worldController = new WorldController(game, stage);
        // Todo here we should make camera stuff and fitviewport
        worldRenderer = new WorldRenderer(worldController,stage);
        stage.addActor(buildActionSubmitButton());
        // android back key
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(getInputProcessor());
    }

    @Override
    public void render(float delta) {
        // Do not update game world when paused.
        if (!paused) {
            worldController.update(delta);

        }
        // Render game world to screen
        worldRenderer.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        Assets.instance.init(new AssetManager());
        paused = false;
    }

    @Override
    public void hide() {
        worldController.dispose();
        worldRenderer.dispose();
        stage.dispose();
        Gdx.input.setCatchBackKey(false);

    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }

    public InputProcessor getInputProcessor() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(worldController);
        return multiplexer;
    }
    
    
    private Table buildActionSubmitButton () {
        /// ------------------ start -- just to create a simple button!! what a caos!!
        skin = new Skin();
        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(140, 70, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        // Store the default libgdx font under the name "default".
        BitmapFont bfont=new BitmapFont();
        bfont.getData().scale(2);
        // bfont.scale(1);
        skin.add("default",bfont);
        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
        /// ------------------ end --
        btnTrigger=new TextButton("ActSub",textButtonStyle);

        Table tbl = new Table();
        //tbl.left().bottom();
        tbl.add(btnTrigger).align(Align.top);
        tbl.setPosition(-120 , 50);
        btnTrigger.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	if(!actionSubmit.isTriggered()){
            		btnTrigger.setText("Cancel");
            		actionSubmit.trigger(new ActionSubmitCallbackInterface() {
						@Override
						public void onActionSubmitFinished() {
							Gdx.app.log(TAG, "***** en el callback ****");
							btnTrigger.setText("ActSub");
						}
					});
            		sendOSCStartActionSubmitCountdown();
            		
            	}else{
            		actionSubmit.cancel();
            		sendOSCCancelActionSubmitCountdown();
            	}
            }
        });
                
        return tbl;
    }
    
    
    private void sendOSCStartActionSubmitCountdown() {
    	Collection<Object> elementsToSend = new LinkedList<Object>();
    	elementsToSend.add("startCountdown");
    	((CetaSender)game).getMessageSender().sendMessage(elementsToSend, "/wizardOfOz");
	}
    
    private void sendOSCCancelActionSubmitCountdown() {
    	Collection<Object> elementsToSend = new LinkedList<Object>();
    	elementsToSend.add("cancelCountdown");
    	((CetaSender)game).getMessageSender().sendMessage(elementsToSend, "/wizardOfOz");
	}
    
}
