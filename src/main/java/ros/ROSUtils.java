/**
 * 
 */
package ros;

import org.ros.node.topic.Publisher;

/**
 * @author Tuan
 *
 */
public class ROSUtils {
	public static void publishString(Publisher<std_msgs.String> publisher, String message) {
		std_msgs.String data = publisher.newMessage();
		data.setData(message);
		publisher.publish(data);
	}
}
