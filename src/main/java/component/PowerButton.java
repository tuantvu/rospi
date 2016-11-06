/**
 * 
 */
package component;

import java.io.IOException;

import com.pi4j.component.switches.SwitchListener;
import com.pi4j.component.switches.SwitchState;
import com.pi4j.component.switches.SwitchStateChangeEvent;
import com.pi4j.component.switches.impl.GpioMomentarySwitchComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


/**
 * @author Mini
 *
 */
public class PowerButton {
	
	//Switch Operations
	private final static int SHUTDOWN_TIME = 5;
	private final static long SHUTDOWN_MILLIS = 4000;
	private final static long LONG_PRESS_MILLIS = 500;
	private boolean isButtonPressed = false;
	private long buttonPressTimeStamp = 0;
	private boolean isShuttingDown = false;
	private long lastInstructionTimeStamp = 0;
		
	//Robot
	private GpioController gpio;
	private Callback callback;

	public PowerButton () {
		// create gpio controller
         gpio = GpioSingleton.getInstance().get();
		
		//Using Pi's internal pull up resistor instead of an external one. 
        //Dangerous if this pin is ever set as an output pin, but saves circuitry
        final GpioPinDigitalInput switchInputPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, "InputPin", PinPullResistance.PULL_UP);
        switchInputPin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioMomentarySwitchComponent shutDownSwitch = new GpioMomentarySwitchComponent(switchInputPin);
        
        shutDownSwitch.addListener(new SwitchListener() {
			@Override
			public void onStateChange(SwitchStateChangeEvent event) {
	//				System.out.println("Old: " + event.getOldState() + ", New: " + event.getNewState());
				
				//User pressed button and let go. Remove shutdown countdown and start or stop the robot
				//Pulling the pin up sets the unpressed state as ON, while pressing the button is OFF. Backwards I know
				if (SwitchState.OFF == event.getOldState() && SwitchState.ON == event.getNewState()) {
					handleButtonUnpress();
				}
				
				//User pressed button. Start countdown to shut down
				else if (SwitchState.ON == event.getOldState() && SwitchState.OFF == event.getNewState()) {
					handleButtonPress();
				}
			}
		});
	}
	

	/**
	 * When button is let go, issue instructions to the robot or abort a shutdown sequence
	 * @param bluetooth 
	 */
	private void handleButtonUnpress() {
		//Only turn on/off robot if the shut down sequence has not been started
		//Also can not issue instructions WAIT_MILLIS from each other
		if (!isShuttingDown) {
		
			long currentTS = System.currentTimeMillis();
			
			if (currentTS - buttonPressTimeStamp > LONG_PRESS_MILLIS){
				callback.onLongPress();
			}
			else {
				callback.onShortPress();
			}
			lastInstructionTimeStamp = System.currentTimeMillis();
		}
		
		//This will abort the shutdown sequence if it's started
		resetShutDownTimeStamp();
	}
	
	/**
	 * Set buttonPressed to true and capture the timestamp
	 */
	private void handleButtonPress(){
		buttonPressTimeStamp = System.currentTimeMillis();
		isButtonPressed = true;
		
		//New thread
		ShutdownThread shutdownThread = new ShutdownThread();
		shutdownThread.start();
	}
	
	/**
	 * Aborts the shut down sequence
	 */
	private void resetShutDownTimeStamp(){
		isButtonPressed = false;
		isShuttingDown = false;
	}
	
	/**
	 * Starts the shutdown sequence. After SHUTDOWN_TIME seconds, shuts down the
	 * gpio to disable all pins and shuts down the Pi. After green light stops blinking
	 * it will be safe to unplug the Pi. Shut down sequence can be overriden by letting
	 * go of the button
	 * @param gpio
	 */
	private void startShutDown(){
		isShuttingDown = true;
		ProcessBuilder process = new ProcessBuilder("halt");
		try {
			for (int i = SHUTDOWN_TIME; i > 0 && isShuttingDown; i--) {
				System.out.println("Shutting Down in " + i);
				callback.onShutdown("Shutting Down in " + i);
				Thread.sleep(1000);
			}
			
			//Check if shutdown override has been done
			if (isShuttingDown){
				callback.onShutdown("Shut Down");
				gpio.shutdown();
				process.start();
			}
			else {
				System.out.println("Shut Down Aborted");
				callback.onShutdown("Shut Down Aborted");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setCallback(Callback callback){
        this.callback = callback;
    }
	
	public interface Callback{
		void onShortPress();
		void onLongPress();
        void onShutdown(String shutdownString);
    }
	
	private class ShutdownThread extends Thread {

		@Override
		public void run() {
			//If buttonPressedTimeStamp is more than 4 seconds, start the shutdown timer
        	while (isButtonPressed)
			{
        		long current = System.currentTimeMillis();
        		//Checking isButtonPressed again due to quick button presses
        		if (isButtonPressed && 
        				(current - buttonPressTimeStamp) > SHUTDOWN_MILLIS) {
            		startShutDown();
        		}
        	}
		}
	}
}
