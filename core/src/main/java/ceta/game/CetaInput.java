package ceta.game;


import java.net.InetAddress;
import java.net.UnknownHostException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class CetaInput implements Screen {


	String message;
	SpriteBatch batch;
	BitmapFont font;
	Game game;
	Screen previousScreen;

	String ip;
	public CetaInput (Game game) {
		this.game = game;
		this.previousScreen = game.getScreen();
		this.ip = null;
	}



	
	Stage stage;
	Dialog endDialog;
	
	private void showErrorDialog(){
      
		//TODO show error 
      /*  Skin skin = new Skin();
        endDialog = new Dialog("Ip Format Error!", skin)
        {
            protected void result(Object object)
            {
                System.out.println("Option: " + object);
                Timer.schedule(new Task()
                {
                    @Override
                    public void run()
                    {
                        endDialog.show(stage);
                    }
                }, 1);
            };
        };

        endDialog.button("Ok", 1L);
        endDialog.button("Option 2", 2L);

        Timer.schedule(new Task()
        {

            @Override
            public void run()
            {
                endDialog.show(stage);
            }
        }, 1);
       */
		
	}
	@Override
	public void show() {
		message = "Touch screen for dialog";
		batch = new SpriteBatch();
		font = new BitmapFont();

		Gdx.input.getTextInput(new Input.TextInputListener() {
			@Override
			public void input (String text) {
				ip = text;
				try {
					InetAddress.getByName(ip);
					((CetaSender)game).updateSenderIp(ip);
				} catch (UnknownHostException e) {
					e.printStackTrace();
					showErrorDialog();
				}
				//game.setScreen(previousScreen);
			}

			@Override
			public void canceled () {
			}
		}, "IP", "", "IP");
	}

	@Override
	public void render(float v) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.draw(batch, message, 20, Gdx.graphics.getHeight()/2);
		batch.end();

	}

	@Override
	public void resize(int i, int i1) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}



	public String getIp() {
		return ip;
	}



	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
}
