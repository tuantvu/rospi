import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import ros.ActionNode;
import ros.BluetoothNode;
import ros.IRSensorNode;
import ros.MotorNode;
import ros.ProximitySensorNode;

/**
 * 
 */

/**
 * @author Tuan
 *
 */
public class RobotMain {
	private static NodeMainExecutor executor;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		executor = DefaultNodeMainExecutor.newDefault();
		run();
	}

	private static void run() {
		execute("Bluetooth", new BluetoothNode());
		execute("Motor", new MotorNode());
		execute("Action", new ActionNode());
		//execute("IRSensor", new IRSensorNode());
		execute("Proximity", new ProximitySensorNode());
	}
	
	private static void execute(String name, NodeMain node) {
		System.out.println("Starting " + name + " node..." + NodeConfiguration.DEFAULT_MASTER_URI);
	    NodeConfiguration config = NodeConfiguration.newPrivate();
	    config.setMasterUri(NodeConfiguration.DEFAULT_MASTER_URI);
	    config.setNodeName(name);
	    executor.execute(node, config);
	}
	
	

}
