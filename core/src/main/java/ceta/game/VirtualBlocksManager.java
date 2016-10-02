package ceta.game;

import java.util.ArrayList;
import java.util.Arrays;

import ceta.game.osc.OSCMessageSender;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by ewe on 9/23/16.
 */
public class VirtualBlocksManager {
    public static final String TAG = VirtualBlocksManager.class.getName();
    Stage stage;
    short margin;
    Polygon polygon;
    VirtualBlock vBlock;
    private ArrayList<VirtualBlock> virtualBlocksOnStage;
    private short [] detected_blocks = {0,0,0,0,0};


    private OSCMessageSender messageSender;
    
    public VirtualBlocksManager(Stage stage){
        this.stage = stage;
    }

    public VirtualBlocksManager(Stage stage, OSCMessageSender messageSender){
    	this.stage = stage;
    	this.messageSender = messageSender;
    }

    public void init(){

        margin = 20;
        virtualBlocksOnStage = new ArrayList<VirtualBlock>();
        // polygon and vBlock will be set for checks
        polygon = new Polygon();
        vBlock = new VirtualBlock((short)1,this);
        initBlocks();
    }

    private void initBlocks(){
        // we initialise first 5 blocks (numbres 1-5)
        for(short i=1;i<=5;i++){
            addVirtualBlock(i);

        }
    }
    public void addBlock(short val){
        // if we detected new block of value 4, we add up 1 in index 3(!)
        //Gdx.app.log(TAG,"detected: "+val+" at position "+(val-1));
        detected_blocks[val-1]+=1;
    }

    public void blockRemoved(short val){
        Gdx.app.log(TAG,"removed: "+val+" at position "+(Math.abs(val)-1));
        detected_blocks[Math.abs(val)-1]-=1;
    }



    public void updateDetected() {
        // reset the values
        //resetDetected();

        for (int i = 0; i < virtualBlocksOnStage.size(); i++) {

            vBlock = virtualBlocksOnStage.get(i);
            // set polygon for collision detection
            polygon.setVertices(vBlock.getVertices());
            polygon.setOrigin(vBlock.bounds.width/2, vBlock.bounds.height/2);
            polygon.setPosition(vBlock.getX(), vBlock.getY());
            polygon.setRotation(vBlock.getRotation());
            // check if we are above zero or below screen height
            // vBlock is already set
            checkMargins();

            // if the kid is touching/moving we ignore;
            // we just act when the piece is left free [we set wasMoved=true on release]
            if (vBlock.getWasMoved()){

                // if it was detected before and is in detection zone we do nothing
                // if it was detected before and is out of detection zone we make it disappear and reset
                if(vBlock.getWasDetected()){
                    if( polygon.getTransformedVertices()[1] < Constants.DETECTION_LIMIT){
                        // was detected but now gone
                        // TODO perhaps change alpha and die...
                        blockRemoved(vBlock.getBlockValue());
                        removeVirtualBlock(i);
                        sendRemoveBlockMessage(vBlock.getBlockValue());
                    }
                }else{
                    if( polygon.getTransformedVertices()[1] > Constants.DETECTION_LIMIT){
                        // new detected!
                        vBlock.setWasDetected(true);
                        addBlock(vBlock.getBlockValue());
                        // new virtual block in empty space
                        addVirtualBlock(vBlock.getBlockValue());
                        sendAddBlockMessage(vBlock, polygon);
                    }
                    else{
                        vBlock.goHome();
                    }
                }
                // we should check just one time so we set moved to false
                vBlock.resetWasMoved();
            }
        }

    }

  
	private void checkMargins(){
        //check up
        if (polygon.getTransformedVertices()[5] + margin > 0){
            //if (virtualBlocksOnStage.get(i).getY() + virtualBlocksOnStage.get(i).getHeight() + margin > 0){
            // we have to adjust Y
            vBlock.setY(vBlock.getY() - (polygon.getTransformedVertices()[5] + margin));
//            Gdx.app.log(TAG,"y: "+ vBlock.getY()+
//                    " height: "+vBlock.getHeight()+
//                    "x: "+ vBlock.getX());
//
//            Gdx.app.log(TAG,"POLYGON transformed: "+"\n"
//                    +" 0 "+polygon.getTransformedVertices()[0]+" "+polygon.getTransformedVertices()[1] +"\n"
//                    +" 1: "+polygon.getTransformedVertices()[2]+" "+polygon.getTransformedVertices()[3]+"\n"
//                    +" 2: "+polygon.getTransformedVertices()[4]+" "+polygon.getTransformedVertices()[5]+"\n"
//                    +" 3: "+polygon.getTransformedVertices()[6]+" "+polygon.getTransformedVertices()[7]
//
//            );
//            Gdx.app.log(TAG,"BOUNDS X: "+ vBlock.bounds.getX()+
//                    " y: "+vBlock.bounds.getY()+
//                    "height: "+ vBlock.bounds.getHeight());

        }
        //check down
        else if (polygon.getTransformedVertices()[1] < -Constants.VIEWPORT_HEIGHT/2 + margin){
            vBlock.setY(vBlock.getY() +
                    (-polygon.getTransformedVertices()[1]-Constants.VIEWPORT_HEIGHT/2 + margin)
            );
        }

        // check right
        if(polygon.getTransformedVertices()[2] + margin > Constants.VIEWPORT_WIDTH/2){
            vBlock.setX( vBlock.getX() -
                    (polygon.getTransformedVertices()[2]+margin - Constants.VIEWPORT_WIDTH/2)

            );
        }
        // check left
        else if (polygon.getTransformedVertices()[6]  < -Constants.VIEWPORT_WIDTH/2 + margin){
            vBlock.setX(  vBlock.getX() +
                    (-polygon.getTransformedVertices()[6] - Constants.VIEWPORT_WIDTH/2 + margin)
            );
        }

    }

    private void removeVirtualBlock(int which){
        // TODO go home and die
        // remove Actor
        virtualBlocksOnStage.get(which).goHomeAndRemove();
        //remove from virtual block from array
        virtualBlocksOnStage.remove(which);

    }

    private void addVirtualBlock(short val){
        // TODO create or take from pool!!
        VirtualBlock virtualBlock = new VirtualBlock(val,this);
        // this works for vertical blocks
        virtualBlock.setPosition(-260 + 2*Constants.BASE*val ,-Constants.BASE*12);

//            virtualBlock.setPosition(
//                    -virtualBlock.getWidth()/2,
//                    -linesRange - Constants.BASE*i - Constants.BASE/4*i
//            );
        stage.addActor(virtualBlock);
        // set home so that we can come back
        virtualBlock.setHome(virtualBlock.getX(),virtualBlock.getY());

        virtualBlocksOnStage.add(virtualBlock);


    }

    public short[] getDetectedBlocks() {
        return Arrays.copyOf(detected_blocks,detected_blocks.length);
    }
    
    private void sendRemoveBlockMessage(int blockId){
    	ArrayList<Object> collectionToSend = new ArrayList<Object>();
    	collectionToSend.add("removeBlock");
    	collectionToSend.add(blockId);  	

    	this.messageSender.sendMessage(collectionToSend, "/wizardOfOz");
    }
    
    private void sendAddBlockMessage(VirtualBlock block, Polygon polygon2) {
    	
    	ArrayList<Object> collectionToSend = new ArrayList<Object>();
    	collectionToSend.add("addBlock");
    	collectionToSend.add((int)block.getBlockValue());
    	collectionToSend.add(block.getX());
    	collectionToSend.add(block.getY());
    	collectionToSend.add(block.getRotation());
    	
//    	for(int i=0;i< polygon2.getVertices().length;i++){
//        	collectionToSend.add(polygon2.getVertices()[i]);
//    	}

    	this.messageSender.sendMessage(collectionToSend, "/wizardOfOz");
  	}

}
