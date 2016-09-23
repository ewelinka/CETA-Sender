package ceta.game.android;

import android.os.Bundle;
import ceta.game.CetaSender;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class CetaSenderActivity extends AndroidApplication {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
			initialize(new CetaSender(), config);
	}
}
