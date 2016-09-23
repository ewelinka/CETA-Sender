package ceta.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by ewe on 9/23/16.
 */
public class WorldRenderer implements Disposable {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private WorldController worldController;
    private Stage stage;

    private short linesRange;

    public WorldRenderer (WorldController worldController, Stage stage) {
        this.stage = stage;
        this.worldController = worldController;
        init();
    }
    private void init () {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);

        shapeRenderer = new ShapeRenderer();
        linesRange = (short)(Constants.VIEWPORT_HEIGHT/Constants.BASE/2)*Constants.BASE;

        //normal
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.setToOrtho(false,Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
        camera.position.set(0,-260,0);
        // moveCamera(0, -512/2);
        camera.zoom = 0.7f;
        camera.update();
        stage.getViewport().setCamera(camera);


    }

    public void render () {
        renderBackground();
        renderWorld(batch);
        renderHelperNumberLines();
        renderHelperNumbers(batch);

        //renderHelperLines();
    }

    private void renderBackground(){
        // white
        Gdx.gl.glClearColor(1, 1, 1, 1);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // detection zone
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(-Constants.VIEWPORT_WIDTH/2, Constants.DETECTION_LIMIT,Constants.VIEWPORT_WIDTH, -Constants.DETECTION_LIMIT);
        shapeRenderer.end();
    }



    private void renderWorld (SpriteBatch batch) {
       // worldController.cameraHelper.applyTo(camera);



        batch.begin();
        worldController.level.render(batch);
        batch.end();
    }


    private void renderHelperNumberLines(){
        Gdx.gl.glLineWidth(1);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);

        for(int i = -200; i<=200;i+=Constants.BASE){
            shapeRenderer.line(i , 0, i,Constants.VIEWPORT_HEIGHT/2);
        }
        shapeRenderer.setColor(1, 0, 0, 1);
        //horizontal
        shapeRenderer.line(-Constants.VIEWPORT_WIDTH/2, 0, Constants.VIEWPORT_WIDTH/2,0);
        //vertical
        //shapeRenderer.line(0 , -Constants.VIEWPORT_HEIGHT/2, 0,Constants.VIEWPORT_HEIGHT/2);
        // and detection limit in blue
        shapeRenderer.setColor(0, 0, 1, 1);
        shapeRenderer.line(-Constants.VIEWPORT_WIDTH/2, Constants.DETECTION_LIMIT, Constants.VIEWPORT_WIDTH/2,Constants.DETECTION_LIMIT);

        shapeRenderer.end();
    }


    private void renderHelperLines(){
        Gdx.gl.glLineWidth(1);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);

        for(int i = -linesRange; i<=linesRange;i+=Constants.BASE){
            //shapeRenderer.line(i, 20,i,0);
            shapeRenderer.line(-Constants.VIEWPORT_WIDTH/2 , i, Constants.VIEWPORT_WIDTH/2,i);
            shapeRenderer.line(i , -Constants.VIEWPORT_HEIGHT/2, i,Constants.VIEWPORT_HEIGHT/2);
        }
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.line(-Constants.VIEWPORT_WIDTH/2, 0, Constants.VIEWPORT_WIDTH/2,0);
        shapeRenderer.line(0 , -Constants.VIEWPORT_HEIGHT/2, 0,Constants.VIEWPORT_HEIGHT/2);
        // and detection limit
        shapeRenderer.setColor(0, 0, 1, 1);
        shapeRenderer.line(-Constants.VIEWPORT_WIDTH/2, Constants.DETECTION_LIMIT, Constants.VIEWPORT_WIDTH/2,Constants.DETECTION_LIMIT);

        shapeRenderer.end();
    }
    private void renderHelperNumbers(SpriteBatch batch){
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        int counter  = 0;
        for(int i = -200; i<240;i+=Constants.BASE){
            String text = counter+"";
            GlyphLayout layout = new GlyphLayout(font, text);
            font.draw(batch, counter+"", i - layout.width/2, 0);
            counter+=1;
        }
        batch.end();

    }


    public void resize (int width, int height) {


        // called from TestScreen on resize
        stage.getViewport().update(width, height, true);
        camera.position.set(0,0,0);
    }

    @Override public void dispose () {
        batch.dispose();
    }
}
