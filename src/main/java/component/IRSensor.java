/**
 * 
 */
package component;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.RaspiPin;

/**
 * @author Tuan
 *
 */
public class IRSensor {
	private GpioController gpio;
	private GpioPinAnalogInput input;

	public IRSensor() {
		gpio = GpioSingleton.getInstance().get();
		input = gpio.provisionAnalogInputPin(RaspiPin.GPIO_15, "IR_INPUT");
		input.setShutdownOptions(true);
	}

	public double getReading() {
		return input.getValue();
	}
	
}
