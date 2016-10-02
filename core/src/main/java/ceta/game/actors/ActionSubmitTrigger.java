package ceta.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class ActionSubmitTrigger extends Actor {

	
	private int countdown;
	private boolean triggered;
	private Skin skin;
	private TextButton btnTrigger;
	private Table table;
	
	public ActionSubmitTrigger(int countdown, boolean triggered) {
		super();
		this.countdown = countdown;
		this.triggered = triggered;
		this.table = this.buildButton();
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
	
	
	@Override
    public void draw(Batch batch, float parentAlpha) {
    	this.table.draw(batch, parentAlpha);      
    }
	
	
	
	private Table buildButton () {
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
        btnTrigger=new TextButton("JUGAR",textButtonStyle);

        Table tbl = new Table();
        //tbl.left().bottom();
        tbl.add(btnTrigger).align(Align.top);
        tbl.setPosition(200 , 20);
        btnTrigger.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                trigger();
            }
        });
                
        return tbl;
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
