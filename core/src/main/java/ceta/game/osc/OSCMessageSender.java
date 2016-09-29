package ceta.game.osc;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;
import com.illposed.osc.utility.OSCJavaToByteArrayConverter;

public class OSCMessageSender {

	private OSCPortOut portOut;
	private String address = "/cetaTUIEvent";
	
	public OSCMessageSender(String ip, int port){
		try {
			this.portOut = new OSCPortOut(InetAddress.getByName(ip), port);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Collection<Object> elementsToSend, String address){
		if (portOut != null) {
	         

	          /* The version of JavaOSC from the Maven Repository is slightly different from the one
	           * from the download link on the main website at the time of writing this tutorial.
	           *
	           * The Maven Repository version (used here), takes a Collection, which is why we need
	           * Arrays.asList(thingsToSend).
	           *
	           * If you're using the downloadable version for some reason, you should switch the
	           * commented and uncommented lines for message below
	           */
	          //OSCMessage message = new OSCMessage(address, Arrays.asList(elementsToSend));
	           OSCMessage message = new OSCMessage(address, elementsToSend);
	           
	          try {
	            // Send the messages
	            portOut.send(message);

	          } catch (Exception e) {
	            // Error handling for some error
	        	  e.printStackTrace();
	          }
	        }
	}
	
}
