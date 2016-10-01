package ceta.game.android;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import ceta.game.CetaInput;
import ceta.game.CetaSender;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class CetaSenderActivity extends AndroidApplication {
	
    private static final String TAG = CetaSenderActivity.class.getName();

	private CetaSender cetaSender;
	MenuItem mSetIpItem;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		this.cetaSender=createCetaSender();
		initialize(this.cetaSender, config);
	}
	
	private CetaSender createCetaSender(){
		return new CetaSender("192.168.1.43", 12345); //this is the IP of the target device
	}
	
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mSetIpItem = menu.add("Set Target Ip");
        return true;
    }
	
	
	public boolean onOptionsItemSelected(MenuItem item){
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == mSetIpItem) {
        	this.cetaSender.showInputIpAddress();
        } 
        return true;
    }
	
	
	
	
	private void updateTargetIp(String ip){
		this.cetaSender.updateSenderIp(ip);
	}
}
