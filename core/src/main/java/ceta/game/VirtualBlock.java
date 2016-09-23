package ceta.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static java.lang.Math.abs;

/**
 * Created by ewe on 9/23/16.
 */
public class VirtualBlock extends Actor {
    public static final String TAG = VirtualBlock.class.getName();
    private short blockValue;
    public Vector2 home;
    private boolean wasDetected;
    public float[] vertices;
    //private float previousRotation =0;
    private float baseRotation =0;
    private float temporalRotation =0;


    private boolean wasMoved;
    private VirtualBlocksManager virtualBlocksManager;

    public Rectangle bounds;
    public TextureRegion regTex;



    public VirtualBlock(short val, VirtualBlocksManager vbm){
        blockValue = val;
        this.virtualBlocksManager = vbm;
        bounds = new Rectangle();
        init();
    }

    public void init(){
        regTex = Assets.instance.box.box;
        //horizontal
        //this.setSize(Constants.BASE*abs(blockValue),Constants.BASE);
        //vertical
        this.setSize(Constants.BASE, Constants.BASE*abs(blockValue));
        this.setOrigin(this.getWidth()/2, this.getHeight()/ 2);
        //Gdx.app.log(TAG,this.getWidth()+" "+this.getHeight());
        this.setScale(1,1);
        this.setRotation(0);
        // Bounding box for collision detection!!
        bounds.set(0, 0, this.getWidth(), this.getHeight());
        this.setBounds(this.getX(),this.getY(),this.getWidth(),this.getHeight());
        this.setDebug(true);

        home = new Vector2();
        wasDetected = false;
        wasMoved = false;
        vertices = new float[]{
                0,0,
                bounds.width,0,
                bounds.width,bounds.height,
                0,bounds.height
        };

        setMyColor();



        this.setTouchable(Touchable.enabled);
        // to move around
        this.addListener(new ActorGestureListener() {

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                //TODO we should check if the piece is in stage limits or the controller should do this?

                float sin = (float)Math.sin(Math.toRadians(getRotation()));
                float cos = (float)Math.cos(Math.toRadians(getRotation()));

                float rotatedX = (deltaX) * cos - (deltaY) * sin;
                float rotatedY = (deltaX) * sin + (deltaY) * cos;
                setPosition(getX()+rotatedX,getY()+rotatedY);
                //the code below was working without rotation
                //setPosition(getX()+deltaX,getY()+deltaY);
            }
            //
            @Override
            public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)  {
                //Actor actor = event.getListenerActor();

                Vector2 a = initialPointer2.sub(initialPointer1);
                Vector2 b = pointer2.sub(pointer1);
                a = a.nor();
                b = b.nor();
                float deltaRot = (float)(Math.atan2(b.y,b.x) - Math.atan2(a.y,a.x));
                float deltaRotDeg = (float)(((deltaRot*180)/Math.PI + 360) % 360);
                temporalRotation = deltaRotDeg;

                if(deltaRotDeg>0){
                    setRotation((baseRotation + deltaRotDeg)%360);
                    //Gdx.app.log(TAG,"--- "+deltaRot+" "+getRotation());
                }
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button){
                baseRotation = getRotation();
                Gdx.app.debug(TAG, "isTouched!!!");
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                baseRotation = (baseRotation + temporalRotation)%360;
                wasMoved = true;
                Gdx.app.debug(TAG, "wasMoved!!! " + blockValue);
            }

        });
//        this.addListener(new ClickListener() {
//             @Override
//             public void touchDragged(InputEvent event, float x, float y, int pointer){
//                 //Gdx.app.log(TAG,"x: "+x+" y: "+y);
//                 setPosition(getX()+x - getWidth()/2, getY()+y - getHeight()/2);
//                 setColor(1,0,0,1);
//             }
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
//                setColor(1,1,0,1);
//                return true;
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
//                setColor(0,0,1,1);
//            }
//         });


        // this later on won't be necessary because the detection will be based on the position not on click
//        this.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//               // Gdx.app.log(TAG,event+" "+x+" "+y+" "+blockValue);
//                //virtualBlocksManager.logDetected(blockValue);
//                if(blockValue > 0)
//                    virtualBlocksManager.blockAdded(blockValue);
//                else
//                    virtualBlocksManager.blockRemoved(blockValue);
//
//            }
//
//
//        });

    }

    private void setMyColor(){
        // amarillo, rojo, verde, naranja y celeste

        switch (blockValue){
            case 1:
                setColor(Color.LIME);
                break;
            case 2:
                setColor(Color.RED);
                break;
            case 3:
                setColor(Color.GREEN);
                break;
            case 4:
                setColor(Color.ORANGE);
                break;
            case 5:
                setColor(Color.CYAN);
                break;
        }
    }



    public boolean getWasDetected(){
        return wasDetected;

    }

    public float[] getVertices(){
        //Gdx.app.log(TAG," we have vertices!!! ");
        return vertices;
    }

    public boolean getWasMoved(){
        return wasMoved;
    }

    public void resetWasMoved(){
        wasMoved = false;
        Gdx.app.debug(TAG, "was moved set to FALSEEE");
    }

    public void setWasDetected(boolean was){
        //Gdx.app.debug(TAG," was detected! "+blockValue);
        wasDetected = was;
    }

    public void goHome(){

        addAction(Actions.moveTo(home.x,home.y,1f));
    }

    public void goHomeAndRemove(){
//        addAction(sequence(Actions.moveTo(home.x,home.y,0.5f),run(new Runnable() {
//                                                                      public void run() {
//                                                                          remove();
//                                                                      }
//                                                                  })));
        addAction(sequence(parallel(Actions.moveTo(home.x,home.y,1f),Actions.rotateTo(0,1f)),run(new Runnable() {
            public void run() {
                remove();
            }
        })));
    }

    public void rotate90(){
        if(blockValue>1)
            addAction(Actions.rotateTo(90,0.5f));
    }

    public short getBlockValue(){
        return blockValue;
    }

    public void setHome(float x, float y){
        home.x = x;
        home.y = y;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //batch.setProjectionMatrix(camera.combined);
        // batch.draw(regTex,this.getX(),this.getY());
        batch.setColor(this.getColor());
        batch.draw(regTex.getTexture(), this.getX(), this.getY(),
                this.getOriginX(), this.getOriginY(),
                this.getWidth() ,this.getHeight(),
                this.getScaleX(), this.getScaleY(),
                this.getRotation(),
                regTex.getRegionX(), regTex.getRegionY(),
                regTex.getRegionWidth(), regTex.getRegionHeight(), false,false);
        batch.setColor(1,1,1,1);
    }

//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {}

}
