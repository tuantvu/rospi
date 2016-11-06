/**
 * 
 */
package ros;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;

import component.PowerButton;

/**
 * @author Tuan
 *
 */
public class PowerNode extends AbstractNodeMain {
	private PowerButton power;
	private Publisher<std_msgs.Bool> powerPublisher;
	private Publisher<std_msgs.String> displayOverridePublisher;

	/* (non-Javadoc)
	 * @see org.ros.node.NodeMain#getDefaultNodeName()
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("PowerNode");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		//Set up pub
		powerPublisher = connectedNode.newPublisher(ROSConstants.POWER_BUTTON_TOPIC, std_msgs.Bool._TYPE);
		displayOverridePublisher = connectedNode.newPublisher(ROSConstants.DISPLAY_OVERRIDE_TOPIC, std_msgs.String._TYPE);
		
		power = new PowerButton();
		power.setCallback(new PowerButton.Callback() {
			
			@Override
			public void onShutdown(String shutdownString) {
				displayOverride(shutdownString);
			}
			
//			@Override
//			public void onPowerOn() {
//				std_msgs.Bool bool = powerPublisher.newMessage();
//				bool.setData(true);
//				powerPublisher.publish(bool);
//				
//				displayMessage("Turning Robot On");
//			}
//			
//			@Override
//			public void onPowerOff() {
//				std_msgs.Bool bool = powerPublisher.newMessage();
//				bool.setData(false);
//				powerPublisher.publish(bool);
//				
//				displayMessage("Turning Robot Off");
//			}

			@Override
			public void onShortPress() {
				std_msgs.Bool bool = powerPublisher.newMessage();
				bool.setData(false);
				powerPublisher.publish(bool);
			}

			@Override
			public void onLongPress() {
				std_msgs.Bool bool = powerPublisher.newMessage();
				bool.setData(true);
				powerPublisher.publish(bool);
			}
		});
	}

	@Override
	public void onShutdown(Node node) {
		super.onShutdown(node);
	}
	
	private void displayOverride(String message) {
		std_msgs.String data = displayOverridePublisher.newMessage();
		data.setData(message);
		displayOverridePublisher.publish(data);
	}

}
