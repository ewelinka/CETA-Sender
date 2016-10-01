package ceta.game;

import ceta.game.osc.OSCMessageSender;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CetaSender extends Game {
	SpriteBatch batch;
	Texture img;

    private static final String TAG = CetaSender.class.getName();

    
	OSCMessageSender messageSender;
	SenderScreen senderScreen;
	public CetaSender(String ip, int port){
		this.messageSender = new OSCMessageSender(ip, port);

	}
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		this.senderScreen = new SenderScreen(this);
		setScreen(this.senderScreen);

	}

	public OSCMessageSender getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(OSCMessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public void updateSenderIp(String ip) {
		this.messageSender.updateTargetIp(ip);
	}

	public void showInputIpAddress(){
		CetaInput cetaInput = new CetaInput(this);
		cetaInput.show();
	}
	
	
	public SenderScreen getSenderScreen(){
		return this.senderScreen;
	}
	
}
