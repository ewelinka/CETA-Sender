package ceta.game.actors;


public class ActionSubmitTrigger {

	
	private int countdown;
	private boolean triggered;

	
	public ActionSubmitTrigger(int countdown, boolean triggered) {
		super();
		this.countdown = countdown;
		this.triggered = triggered;
	}

	
	public void trigger(){
		//TODO Send OSC message
		final Thread t = new Thread(){
			@Override
		    public void run() {
				triggered=true;
				long initialMs = System.currentTimeMillis();
				long actualMs = initialMs;
				while( (actualMs-initialMs<countdown)&&triggered){
					try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					actualMs = System.currentTimeMillis();
				}
				triggered=false;
			}
		};
		t.start();
	}
	

	public int getCountdown() {
		return countdown;
	}


	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}


	public boolean isTriggered() {
		return triggered;
	}


	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
	}
	
	
}
