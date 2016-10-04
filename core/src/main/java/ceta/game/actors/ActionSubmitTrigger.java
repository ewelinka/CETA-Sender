package ceta.game.actors;

import ceta.game.interfaces.ActionSubmitCallbackInterface;

import com.badlogic.gdx.Gdx;


public class ActionSubmitTrigger {

	private static String TAG = ActionSubmitTrigger.class.getName();
	private int countdown_ms;
	private boolean triggered;
	
	
	public ActionSubmitTrigger(int countdown_ms, boolean triggered) {
		super();
		this.countdown_ms = countdown_ms;
		this.triggered = triggered;
	}

	
	public void trigger(ActionSubmitCallbackInterface callback){
		//TODO Send OSC message
		
		final ActionSubmitCallbackInterface callbackInterface = callback;
		final Thread t = new Thread(){
			@Override
		    public void run() {
				Gdx.app.log(TAG, "------action submit start countdown_ms------");
				triggered=true;
				long initialMs = System.currentTimeMillis();
				long actualMs = initialMs;
				while( (actualMs-initialMs<countdown_ms)&&triggered){
					try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					actualMs = System.currentTimeMillis();
				}
				Gdx.app.log(TAG, "------action submit end countdown_ms------");
				triggered=false;
				callbackInterface.onActionSubmitFinished();
			}
		};
		t.start();
	}
	
	public void cancel(){
		//TODO send OSC message
		triggered=false;
	}

	public int getCountdown() {
		return countdown_ms;
	}


	public void setCountdown(int countdown) {
		this.countdown_ms = countdown;
	}


	public boolean isTriggered() {
		return triggered;
	}


	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
	}
	
	
}
