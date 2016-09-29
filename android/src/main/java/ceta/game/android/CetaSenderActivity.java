package ceta.game.android;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import ceta.game.CetaSender;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

public class CetaSenderActivity extends AndroidApplication {
	
	private CetaSender cs;
	@Override
	public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
			initialize(createCetaSender(), config);
			
	}
	
	private CetaSender createCetaSender(){
//		WifiManager wm = (WifiManager)getSystemService(WIFI_SERVICE);
//		String myIP = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
		//return new CetaSender(myIP, 12345);
		return new CetaSender("192.168.1.43", 12345); //this is the IP of the target device
	}
}
