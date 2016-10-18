import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.wiringpi.I2C;

import OLED.SSD1306_Constants;
import OLED.SSD1306_I2C_Display;

/**
 * 
 */

/**
 * @author Tuan Vu
 *
 */
public class HelloWorldOLEDExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        I2CBus i2c;
        SSD1306_I2C_Display display;
		try {
			i2c = I2CFactory.getInstance(I2C.CHANNEL_1);
			display = new SSD1306_I2C_Display(SSD1306_Constants.LCD_WIDTH_128, SSD1306_Constants.LCD_HEIGHT_64,
					gpio, i2c, 0x3c);
			display.begin();
			display.displayString("Hello World");
			
		} catch (UnsupportedBusNumberException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
