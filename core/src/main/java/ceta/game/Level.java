package ceta.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by ewe on 9/23/16.
 */
public class Level {
    private Stage stage;

    public Level(Stage stage){
        this.stage = stage;
        init();
    };

    public void init() {}

    public void update(float deltaTime) {
        stage.act(deltaTime);

    }

    public void render(SpriteBatch batch) {
        // Sets the clear screen color to: Cornflower Blue
        // Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f,0xed / 255.0f, 0xff / 255.0f);

        stage.draw();
    }
}
