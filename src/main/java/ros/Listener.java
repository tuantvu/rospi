/**
 * 
 */
package ros;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;


/**
 * @author Tuan
 *
 */
public class Listener extends AbstractNodeMain {

	/* (non-Javadoc)
	 * @see org.ros.node.NodeMain#getDefaultNodeName()
	 */
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("Listener");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		final Log log = connectedNode.getLog();
	    Subscriber<std_msgs.String> subscriber = connectedNode.newSubscriber("chatter", std_msgs.String._TYPE);
	    subscriber.addMessageListener(new MessageListener<std_msgs.String>() {
	      @Override
	      public void onNewMessage(std_msgs.String message) {
	        log.info("I heard: \"" + message.getData() + "\"");
	      }
	    });
	}
	
	

}
