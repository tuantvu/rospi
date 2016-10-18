/**
 * 
 */
package ros;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import component.MotorController;


/**
 * @author Tuan
 *
 */
public class MotorNode extends AbstractNodeMain {
	private MotorController motor;

	/* (non-Javadoc)
	 * @see org.ros.node.NodeMain#getDefaultNodeName()
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("MotorNode");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		//Motor
		motor = new MotorController();
		
		//Subscriber
	    Subscriber<geometry_msgs.Point> subscriber = connectedNode.newSubscriber(ROSConstants.MOTOR_TOPIC, geometry_msgs.Point._TYPE);
	    subscriber.addMessageListener(new MessageListener<geometry_msgs.Point>() {
	      @Override
	      public void onNewMessage(geometry_msgs.Point message) {
	    	  int left = (int)message.getX();
	    	  int right = (int)message.getY();
	    	  
	    	  motor.moveWheels(left, right);
	      }
	    });
	}
	
}
