/**
 * 
 */
package component;

import java.io.IOException;
import java.util.HashMap;

import com.pi4j.component.sensor.SensorListener;
import com.pi4j.component.sensor.SensorState;
import com.pi4j.component.sensor.SensorStateChangeEvent;
import com.pi4j.component.sensor.impl.GpioSensorComponent;
import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

/**
 * @author Tuan
 *
 */
public class ProximitySensor {
	
	public final static String FRONT_RIGHT = "FRONT_RIGHT";
	public final static String FRONT_LEFT = "FRONT_LEFT";
	public final static String RIGHT = "RIGHT";;
	public final static String LEFT = "LEFT";
	public final static String BACK = "BACK";
	
	private final static Pin PIN_FRONT_RIGHT = RaspiPin.GPIO_02;
	private final static Pin PIN_FRONT_LEFT = RaspiPin.GPIO_00;
	private final static Pin PIN_RIGHT = RaspiPin.GPIO_12;
	private final static Pin PIN_LEFT = RaspiPin.GPIO_03;
	private final static Pin PIN_BACK = RaspiPin.GPIO_13;
	
	private String name;
	private GpioController gpio;
	private	GpioSensorComponent sensor;
	private static HashMap<String, Pin> map;
	
	static{
		map = new HashMap<>();
		map.put(FRONT_RIGHT, PIN_FRONT_RIGHT);
		map.put(FRONT_LEFT, PIN_FRONT_LEFT);
		map.put(RIGHT, PIN_RIGHT);
		map.put(LEFT, PIN_LEFT);
		map.put(BACK, PIN_BACK);
	}

	public ProximitySensor(String name, String pinName) {
		this.name = name;
		gpio = GpioSingleton.getInstance().get();
		GpioPinDigitalInput input = gpio.provisionDigitalInputPin(map.get(pinName));
		input.setShutdownOptions(true);
		
		sensor = new GpioSensorComponent(input);
	}
	
	public void setCallback(final Callback callback){
		sensor.addListener(new SensorListener() {
			
			@Override
			public void onStateChange(SensorStateChangeEvent event) {
				if (event.getNewState() == SensorState.OPEN) {
					callback.onCollided(name);
				}
				else {
					callback.onNotCollided(name);
				}
			}
		});
	}
	
	public boolean isCollided(){
		return sensor.isOpen();
	}
	
	public interface Callback {
		void onCollided(String name);
		void onNotCollided(String name);
	}

}
