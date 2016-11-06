/**
 * 
 */
package ros;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import component.ProximitySensor;

/**
 * @author Tuan
 *
 */
public class ProximitySensorNode extends AbstractNodeMain {
	public final static String NAME_FRONT_RIGHT = "FrontRight";
	public final static String NAME_FRONT_LEFT = "FrontLeft";
	public final static String NAME_RIGHT = "Right";
	public final static String NAME_LEFT = "Left";
	public final static String NAME_BACK = "Back";
	
	private Publisher<std_msgs.String> proximityPublisher;
	private Publisher<std_msgs.String> displayPublisher;

	/* (non-Javadoc)
	 * @see org.ros.node.NodeMain#getDefaultNodeName()
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("ProximitySensorNode");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		//Set up pub
		proximityPublisher = connectedNode.newPublisher(ROSConstants.PROXIMITY_TOPIC, std_msgs.String._TYPE);
		displayPublisher = connectedNode.newPublisher(ROSConstants.DISPLAY_TOPIC, std_msgs.String._TYPE);
		
		createSensor(NAME_FRONT_RIGHT, ProximitySensor.FRONT_RIGHT);
		createSensor(NAME_FRONT_LEFT, ProximitySensor.FRONT_LEFT);
		createSensor(NAME_RIGHT, ProximitySensor.RIGHT);
		createSensor(NAME_LEFT, ProximitySensor.LEFT);
		createSensor(NAME_BACK, ProximitySensor.BACK);
	}
	
	private void createSensor(String name, String pinName) {
		ProximitySensor sensor = new ProximitySensor(name, pinName);
		sensor.setCallback(new ProximitySensor.Callback() {
			
			@Override
			public void onNotCollided(String name) {
				std_msgs.String data = proximityPublisher.newMessage();
				data.setData(name + "~false");
				proximityPublisher.publish(data);
				
				displayMessage(name + " clear");
			}
			
			@Override
			public void onCollided(String name) {
				std_msgs.String data = proximityPublisher.newMessage();
				data.setData(name + "~true");
				proximityPublisher.publish(data);
				
				displayMessage(name + " hit");
			}
		});
	}
	
	private void displayMessage(String message) {
		std_msgs.String data = displayPublisher.newMessage();
		data.setData(message);
		displayPublisher.publish(data);
		System.out.println(message);
	}

}
