/**
 * 
 */
package component;

import java.io.IOException;

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

/**
 * @author Tuan
 *
 */
public class IRSensor {
	private GpioController gpio;
	private GpioPinAnalogInput input;

	public IRSensor() {
		gpio = GpioSingleton.getInstance().get();
		try {
			input = gpio.provisionAnalogInputPin(new ADS1115GpioProvider(1, 0x48), ADS1115Pin.INPUT_A0, "IR_INPUT");
			input.setShutdownOptions(true);
		} catch (UnsupportedBusNumberException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double getReading() {
		return input.getValue();
	}
	
}
