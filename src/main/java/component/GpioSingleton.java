/**
 * 
 */
package component;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

/**
 * @author Tuan
 *
 */
public class GpioSingleton {
	private GpioController gpio;
	private GpioSingleton(){
		gpio = GpioFactory.getInstance();
	}
	
	private static class LazyHolder {
        private static final GpioSingleton INSTANCE = new GpioSingleton();
    }

    public static GpioSingleton getInstance() {
        return LazyHolder.INSTANCE;
    }
    
    public GpioController get() {
    	return gpio;
    }
}
