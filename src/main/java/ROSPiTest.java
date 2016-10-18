import org.ros.RosCore;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import ros.ActionNode;
import ros.BluetoothNode;
import ros.DisplayNode;
import ros.IRSensorNode;
import ros.MotorNode;
import ros.PowerNode;

/**
 * 
 */

/**
 * @author Tuan
 *
 */
public class ROSPiTest {
	private static RosCore core;
	private static NodeMainExecutor executor;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		core = RosCore.newPublic(13111);
		core.start();
		try {
			core.awaitStart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		executor = DefaultNodeMainExecutor.newDefault();
		run();
	}

	private static void run() {
		execute("Power", new PowerNode());
		execute("Display", new DisplayNode());
		execute("Bluetooth", new BluetoothNode());
		execute("Motor", new MotorNode());
		execute("Action", new ActionNode());
	}
	
	private static void execute(String name, NodeMain node) {
		System.out.println("Starting " + name + " node...");
	    NodeConfiguration config = NodeConfiguration.newPrivate();
	    config.setMasterUri(core.getUri());
	    config.setNodeName(name);
	    executor.execute(node, config);
	}
	
	

}
