/**
 * 
 */
package ros;

import java.io.IOException;
import java.util.ArrayList;

import org.ros.internal.message.Message;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import OLED.SSD1306_I2C_Display;
import OLED.ScrollableLog;
import OLED.Menu.MainMenu;
import OLED.Menu.MenuItem;
import OLED.Menu.MenuItem.MenuItemCallback;

/**
 * @author Tuan
 *
 */
public class DisplayNode extends AbstractNodeMain implements MenuItemCallback {
	
	private SSD1306_I2C_Display display;
	private MenuItem currentMenuItem;
	private ScrollableLog logs;
	private Publisher<std_msgs.String> blueToothConnectPublisher;
	
	/* (non-Javadoc)
	 * @see org.ros.node.NodeMain#getDefaultNodeName()
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("DisplayNode");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		logs = new ScrollableLog(100);
		try {
			display = new SSD1306_I2C_Display();
			display.begin();
			currentMenuItem = new MainMenu(display, this);
			currentMenuItem.doMenuAction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Set up pub/sub
		subscribeToBlueToothConnectTopic(connectedNode);
		subscribeToDisplayTopic(connectedNode);
		subscribeToDisplayOverrideTopic(connectedNode);
	    subscribeToPowerTopic(connectedNode);
	}

	/**
	 * @param connectedNode
	 */
	private void subscribeToPowerTopic(ConnectedNode connectedNode) {
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

	/**
	 * @param connectedNode
	 */
	private void subscribeToDisplayOverrideTopic(ConnectedNode connectedNode) {
		//Override Topic Subscriber
		Subscriber<std_msgs.String> overrideSubscriber = connectedNode.newSubscriber(
				ROSConstants.DISPLAY_OVERRIDE_TOPIC, std_msgs.String._TYPE);
	    overrideSubscriber.addMessageListener(new MessageListener<std_msgs.String>() {
	      @Override
	      public void onNewMessage(std_msgs.String message) {
	        display.displayString("", message.getData());
	      }
	    });
	}

	/**
	 * @param connectedNode
	 */
	private void subscribeToDisplayTopic(ConnectedNode connectedNode) {
		//Display Topic Subscriber
		Subscriber<std_msgs.String> displaySubscriber = connectedNode.newSubscriber(
				ROSConstants.DISPLAY_TOPIC, std_msgs.String._TYPE);
	    displaySubscriber.addMessageListener(new MessageListener<std_msgs.String>() {
	      @Override
	      public void onNewMessage(std_msgs.String message) {
	    	  logs.add(message.getData());
	      }
	    });
	}
	
	/**
	 * @param connectedNode
	 */
	private void subscribeToBlueToothConnectTopic(ConnectedNode connectedNode) {
		//Bluetooth Connect Topic Subscriber
		blueToothConnectPublisher = connectedNode.newPublisher(ROSConstants.BLUETOOTH_CONNECT_TOPIC, std_msgs.String._TYPE);
		
		//Bluetooth Connect Topic Subscriber
		Subscriber<std_msgs.String> overrideSubscriber = connectedNode.newSubscriber(
				ROSConstants.BLUETOOTH_CONNECT_TOPIC, std_msgs.String._TYPE);
	    overrideSubscriber.addMessageListener(new MessageListener<std_msgs.String>() {
	      @Override
	      public void onNewMessage(std_msgs.String message) {
	    	  String messageString = message.getData();
	    	  logs.add(messageString);
	    	  currentMenuItem.onMessage(messageString);
	      }
	    });
	}

	@Override
	public void connectBluetooth() {
		publishString(blueToothConnectPublisher, ROSConstants.BLUETOOTH_CONNECT_START);
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem.MenuItemCallback#disconnectBluetooth()
	 */
	@Override
	public void disconnectBluetooth() {
		publishString(blueToothConnectPublisher, ROSConstants.BLUETOOTH_CONNECT_STOP);
	}
	
	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem.MenuItemCallback#getLog()
	 */
	@Override
	public ScrollableLog getLog() {
		return logs;
	}

	private void publishString(Publisher<std_msgs.String> publisher, String message) {
		ROSUtils.publishString(publisher, message);
		logs.add(message);
	}
	
	
}
