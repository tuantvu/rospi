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
	private Publisher<std_msgs.String> blueToothConnectPublisher;
	private boolean bluetoothStart = false;

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
		
		subscribeToBlueToothConnectTopic(connectedNode);
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
	
	/**
	 * @param connectedNode
	 */
	private void subscribeToBlueToothConnectTopic(ConnectedNode connectedNode) {
		//Bluetooth Connect Topic Subscriber
		blueToothConnectPublisher = connectedNode.newPublisher(
				ROSConstants.BLUETOOTH_CONNECT_TOPIC, std_msgs.String._TYPE);
		
		//Bluetooth Connect Topic Subscriber
		Subscriber<std_msgs.String> overrideSubscriber = connectedNode.newSubscriber(
				ROSConstants.BLUETOOTH_CONNECT_TOPIC, std_msgs.String._TYPE);
	    overrideSubscriber.addMessageListener(new MessageListener<std_msgs.String>() {
	      @Override
	      public void onNewMessage(std_msgs.String message) {
	    	  String messageString = message.getData();
	    	  if (ROSConstants.BLUETOOTH_CONNECT_START.equals(messageString)) {
	    		  bluetoothStart = true;
	    		  new SerialConnectThread().start();
	    	  }
	    	  else if (ROSConstants.BLUETOOTH_CONNECT_STOP.equals(messageString)) {
	    		  bluetoothStart = false;
	    		  boolean disconnectSuccessful = bluetooth.stop();
	    		  if (disconnectSuccessful) {
	    			  ROSUtils.publishString(blueToothConnectPublisher, ROSConstants.BLUETOOTH_CONNECT_DISCONNECTED);
	    		  }
	    		  else {
	    			  ROSUtils.publishString(blueToothConnectPublisher, ROSConstants.BLUETOOTH_CONNECT_DISCONNECT_FAIL);
	    		  }
	    	  }
	      }
	    });
	}
	
	class SerialConnectThread extends Thread {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			boolean isConnected = bluetooth.start();
			while (bluetoothStart && !isConnected) {
				ROSUtils.publishString(blueToothConnectPublisher, ROSConstants.BLUETOOTH_CONNECT_WAITING);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				isConnected = bluetooth.start();
			}
			if (isConnected) {
				ROSUtils.publishString(blueToothConnectPublisher, ROSConstants.BLUETOOTH_CONNECT_CONNECTED);
			}
			else {
				ROSUtils.publishString(blueToothConnectPublisher, ROSConstants.BLUETOOTH_CONNECT_CONNECT_FAIL);
			}
			
		}
		
	}

}
