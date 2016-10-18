/**
 * 
 */
package ros;

import java.io.IOException;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import OLED.SSD1306_I2C_Display;

/**
 * @author Tuan
 *
 */
public class DisplayNode extends AbstractNodeMain {
	
	private SSD1306_I2C_Display display;

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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedBusNumberException e) {
			e.printStackTrace();
		}
		
		Subscriber<std_msgs.String> subscriber = connectedNode.newSubscriber(ROSConstants.DISPLAY_TOPIC, std_msgs.String._TYPE);
	    subscriber.addMessageListener(new MessageListener<std_msgs.String>() {
	      @Override
	      public void onNewMessage(std_msgs.String message) {
	        display.displayString("", message.getData());
	      }
	    });
	}

	
}
