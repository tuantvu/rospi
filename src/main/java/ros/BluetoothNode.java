/**
 * 
 */
package ros;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import component.BluetoothListener;

/**
 * @author Tuan
 *
 */
public class BluetoothNode extends AbstractNodeMain {
	private BluetoothListener bluetooth;
	private Publisher<std_msgs.String> publisher;

	/* (non-Javadoc)
	 * @see org.ros.node.NodeMain#getDefaultNodeName()
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("BluetoothNode");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		final Log log = connectedNode.getLog();
		
		//Set up pub
		publisher = connectedNode.newPublisher(ROSConstants.BLUETOOTH_TOPIC, std_msgs.String._TYPE);
		
		//Set up bluetooth
		bluetooth = new BluetoothListener();
		bluetooth.setCallback(new BluetoothListener.Callback() {
			
			@Override
			public void onData(String data) {
//				log.info("bt: " + data);
				std_msgs.String str = publisher.newMessage();
				str.setData(data);
				publisher.publish(str);
			}
		});
		
		//Set up sub
//		Subscriber<std_msgs.Bool> subscriber = connectedNode.newSubscriber(ROSConstants.POWER_BUTTON_TOPIC, std_msgs.Bool._TYPE);
//	    subscriber.addMessageListener(new MessageListener<std_msgs.Bool>() {
//	      @Override
//	      public void onNewMessage(std_msgs.Bool bool) {
//	        if (bool.getData()) {
//	        	boolean isSuccessful = bluetooth.start();
//	        	if (!isSuccessful) {
//	        		std_msgs.String str = publisher.newMessage();
//					str.setData("Bluetooth Error");
//					publisher.publish(str);
//	        	}
//	        }
//	        else {
//	        	bluetooth.stop();
//	        }
//	      }
//	    });
	}

	@Override
	public void onError(Node node, Throwable throwable) {
		throwable.printStackTrace();
		super.onError(node, throwable);
	}

	@Override
	public void onShutdown(Node node) {
		if (bluetooth != null) {
			bluetooth.stop();
		}
	}
	
	

}
