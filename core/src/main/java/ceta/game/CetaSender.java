package ceta.game;

import ceta.game.osc.OSCMessageSender;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CetaSender extends Game {
	SpriteBatch batch;
	Texture img;

	OSCMessageSender messageSender;
	
	public CetaSender(String ip, int port){
		this.messageSender = new OSCMessageSender(ip, port);

	}
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		setScreen(new SenderScreen(this));

	}

	public OSCMessageSender getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(OSCMessageSender messageSender) {
		this.messageSender = messageSender;
	}

	
	
}
