import org.ros.RosCore;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import ros.Listener;
import ros.Talker;

/**
 * 
 */

/**
 * @author Tuan
 *
 */
public class ROSTest2 {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		startChatter();
	}

	private static void startChatter() {
		NodeMainExecutor e = DefaultNodeMainExecutor.newDefault();
		
//		System.out.println("Starting talker node...");
//	    NodeConfiguration talkerConfig = NodeConfiguration.newPrivate();
//	    talkerConfig.setMasterUri(core.getUri());
//	    talkerConfig.setNodeName("Talker");
//	    NodeMain talker = new Talker();
//	    e.execute(talker, talkerConfig);
	    
	    System.out.println("Starting listener node...");
	    NodeConfiguration listenerConfig = NodeConfiguration.newPrivate();
	    listenerConfig.setMasterUri(NodeConfiguration.DEFAULT_MASTER_URI);
	    listenerConfig.setNodeName("Listener");
	    NodeMain listener = new Listener();
	    e.execute(listener, listenerConfig);
	}
	
	
	
	

}
