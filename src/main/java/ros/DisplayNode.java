/**
 * 
 */
package ros;

import java.io.IOException;
import java.util.ArrayList;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import OLED.SSD1306_I2C_Display;
import OLED.Menu.MainMenu;
import OLED.Menu.MenuItem;

/**
 * @author Tuan
 *
 */
public class DisplayNode extends AbstractNodeMain {
	
	private SSD1306_I2C_Display display;
	private MenuItem currentMenuItem;
	private ArrayList<String> logs;
	
	/* (non-Javadoc)
	 * @see org.ros.node.NodeMain#getDefaultNodeName()
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("DisplayNode");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		try {
			display = new SSD1306_I2C_Display();
			display.begin();
			currentMenuItem = new MainMenu(display);
			currentMenuItem.doMenuAction();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedBusNumberException e) {
			e.printStackTrace();
		}
		
		logs = new ArrayList<>();
		
		//Display Topic Subscriber
		Subscriber<std_msgs.String> displaySubscriber = connectedNode.newSubscriber(
				ROSConstants.DISPLAY_TOPIC, std_msgs.String._TYPE);
	    displaySubscriber.addMessageListener(new MessageListener<std_msgs.String>() {
	      @Override
	      public void onNewMessage(std_msgs.String message) {
	    	  logs.add(message.getData());
	      }
	    });
		
		//Override Topic Subscriber
		Subscriber<std_msgs.String> overrideSubscriber = connectedNode.newSubscriber(
				ROSConstants.DISPLAY_OVERRIDE_TOPIC, std_msgs.String._TYPE);
	    overrideSubscriber.addMessageListener(new MessageListener<std_msgs.String>() {
	      @Override
	      public void onNewMessage(std_msgs.String message) {
	        display.displayString("", message.getData());
	      }
	    });
	    
	    //Power Subscriber
	    Subscriber<std_msgs.Bool> powerSubscriber = connectedNode.newSubscriber(
	    		ROSConstants.POWER_BUTTON_TOPIC, std_msgs.Bool._TYPE);
	    powerSubscriber.addMessageListener(new MessageListener<std_msgs.Bool>() {
	      @Override
	      public void onNewMessage(std_msgs.Bool bool) {
	        if (bool.getData()) {
	        	//Long Press
	        	MenuItem newItem = currentMenuItem.doLongPress();
	        	if (newItem != null) {
	        		currentMenuItem = newItem;
	        	}
	        }
	        else {
	        	//Short Press
	        	currentMenuItem.doShortPress();
	        }
	      }
	    });
	}
	
}
