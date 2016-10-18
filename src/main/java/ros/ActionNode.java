/**
 * 
 */
package ros;

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
public class ActionNode extends AbstractNodeMain {
	private BluetoothListener bluetooth;
	private Publisher<geometry_msgs.Point> publisher;

	/* (non-Javadoc)
	 * @see org.ros.node.NodeMain#getDefaultNodeName()
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("ActionNode");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		
		//Set up pub
		publisher = connectedNode.newPublisher(ROSConstants.MOTOR_TOPIC, geometry_msgs.Point._TYPE);
		
		//Set up sub
		Subscriber<std_msgs.String> subscriber = connectedNode.newSubscriber(ROSConstants.BLUETOOTH_TOPIC, std_msgs.String._TYPE);
	    subscriber.addMessageListener(new MessageListener<std_msgs.String>() {
	      @Override
	      public void onNewMessage(std_msgs.String message) {
	    	  handleBluetoothData(message.getData());
	      }
	    });
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
	
	private void handleBluetoothData(String data)
    {
         //Split the data
        String[] dataArray = data.split(",");
        //Get the last two data elements
        if (dataArray.length >= 2)
        {
            String data1 = dataArray[dataArray.length - 2];
            String data2 = dataArray[dataArray.length - 1];
            //System.out.println("Split speed: " + speed + ", turn: " + turn);
            
            boolean automaticMode = true;
            
            //Data with s & t is speed and turn, else left and right wheel
            if (data1.contains("s") && data2.contains("t"))
            {
                data1 = data1.replace("s","");
                data2 = data2.replace("t","");
                automaticMode = true;
            }
            else if ( data1.contains("l") && data2.contains("r"))
            {
                data1 = data1.replace("l","");
                data2 = data2.replace("r","");
                automaticMode = false;
            }
            
            try
            {
                int data1Value = Integer.parseInt(data1);
                int data2Value = Integer.parseInt(data2);
                
                if (automaticMode)
                {
                	System.err.println("automatic mode not supported");
                }
                else
                {
//                    System.out.println("Move wheels: " + data1Value + ", " + data2Value);
                    geometry_msgs.Point instr = publisher.newMessage();
                    instr.setX(data1Value);
                    instr.setY(data2Value);
                    publisher.publish(instr);
                }
            }
            catch (NumberFormatException e)
            {
                //Do nothing
                System.err.println("NumberFormatEx parsing: " + data1 + " or " + data2);
            }
            
        }
    }

}
