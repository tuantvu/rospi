/**
 * 
 */
package component;

import java.io.IOException;
import java.util.ArrayList;

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
		return translateToCM(input.getValue());
	}
	
	/**
	 * 
	 * @param value
	 */
	private double translateToCM(double value) {
		//Find the two pairs that are lower and higher than the value
		int higher = -1, lower = -1;
		double result = 0;
		
		//Extreme cases larger or smaller than our range
		if (value >= TRANSLATION.get(0).reading) {
			result = TRANSLATION.get(0).cm;
		}
		else if (value <= TRANSLATION.get(TRANSLATION.size() - 1).reading) {
			result = TRANSLATION.get(TRANSLATION.size() - 1).cm;
		}
		else {
			//Within range. Loop through
			for (int i = 0; i < TRANSLATION.size(); i++) {
				if (value > TRANSLATION.get(i).reading) {
					lower = i;
				}
				else if (value <= TRANSLATION.get(i).reading) {
					higher = i;
				}
				
				//When lower is not -1, then we're done
				if (lower != -1 )
					break;
			}
			
			//Average the difference between the two pairs and calculate what the final distance is
			double readingHigher = TRANSLATION.get(higher).reading;
			double readingLower = TRANSLATION.get(lower).reading;
			double cmHigher = TRANSLATION.get(higher).cm;
			double cmLower = TRANSLATION.get(lower).cm;
			double increment = ( cmLower - cmHigher ) / ( readingHigher - readingLower );
			result = value * increment;
		}
		
		return result;
	}
	
	
	static class Pair {
		int reading;
		int cm;
		/**
		 * @param reading
		 * @param cm
		 */
		public Pair(int reading, int cm) {
			super();
			this.reading = reading;
			this.cm = cm;
		}
	}
	
	private final static ArrayList<Pair> TRANSLATION = new ArrayList<>();
	static {
		TRANSLATION.add(new Pair(16000, 6));
		TRANSLATION.add(new Pair(14700, 7));
		TRANSLATION.add(new Pair(13300, 8));
		TRANSLATION.add(new Pair(12200, 9));
		TRANSLATION.add(new Pair(11150, 10));
		TRANSLATION.add(new Pair(10380, 11));
		TRANSLATION.add(new Pair(9530, 12));
		TRANSLATION.add(new Pair(8950, 13));
		TRANSLATION.add(new Pair(8560, 14));
		TRANSLATION.add(new Pair(8050, 15));
		TRANSLATION.add(new Pair(7660, 16));
		TRANSLATION.add(new Pair(7260, 17));
		TRANSLATION.add(new Pair(6980, 18));
		TRANSLATION.add(new Pair(6710, 19));
		TRANSLATION.add(new Pair(6550, 20));
		TRANSLATION.add(new Pair(5600, 25));
		TRANSLATION.add(new Pair(4870, 30));
		TRANSLATION.add(new Pair(3820, 40));
		TRANSLATION.add(new Pair(3200, 50));
		TRANSLATION.add(new Pair(2740, 60));
		TRANSLATION.add(new Pair(2450, 70));
		TRANSLATION.add(new Pair(2220, 80));
		TRANSLATION.add(new Pair(2050, 90));
		TRANSLATION.add(new Pair(1890, 100));
		TRANSLATION.add(new Pair(1780, 110));
		TRANSLATION.add(new Pair(1680, 120));
		TRANSLATION.add(new Pair(1570, 130));
		TRANSLATION.add(new Pair(1460, 140));
		TRANSLATION.add(new Pair(1400, 150));
	}
}
