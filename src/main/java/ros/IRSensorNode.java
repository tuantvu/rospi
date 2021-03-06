/**
 * 
 */
package ros;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import component.IRSensor;

/**
 * @author Tuan
 *
 */
public class IRSensorNode extends AbstractNodeMain {
	private IRSensor sensor;

	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("IRSensorNode");
	}

	@Override
	public void onStart(ConnectedNode connectedNode) {
		sensor = new IRSensor();
		
		final Publisher<std_msgs.Float64> publisher = connectedNode.newPublisher(ROSConstants.IR_SENSOR_TOPIC, std_msgs.Float64._TYPE);
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			
			@Override
			protected void loop() throws InterruptedException {
				double readingTotal = 0.0;
				for (int i = 0; i < 10; i++) {
					double reading = sensor.getReading();
					readingTotal += reading;
					std_msgs.Float64 value = publisher.newMessage();
					value.setData(reading);
					publisher.publish(value);
//					System.out.println(reading);
					Thread.sleep(100);
				}
				System.out.println("Avg: " + (readingTotal / 10.0));
			}
		});
	}
	
	

}
