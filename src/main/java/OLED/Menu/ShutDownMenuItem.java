/**
 * 
 */
package OLED.Menu;

import OLED.SSD1306_I2C_Display;
import component.GpioSingleton;

/**
 * @author Tuan
 *
 */
public class ShutDownMenuItem extends MenuItem {
	private static final int SHUTDOWN_COUNT = 5;
	private SSD1306_I2C_Display display;
	private boolean isShuttingDown = false;
	private String action;

	public ShutDownMenuItem(SSD1306_I2C_Display display, MenuGroupItem parent,
			String displayString, String action) {
		super(displayString, parent);
		this.action = action;
		this.display = display;
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#doMenuAction()
	 */
	@Override
	public MenuItem doMenuAction() {
		new ShutdownThread().start();
		return this;
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#doShortPress()
	 */
	@Override
	public void doShortPress() {
		isShuttingDown = false;
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#doLongPress()
	 */
	@Override
	public MenuItem doLongPress() {
		isShuttingDown = false;
		return super.doLongPress();
	}

	private class ShutdownThread extends Thread {

		@Override
		public void run() {
			try {
				isShuttingDown = true;
				for (int i = SHUTDOWN_COUNT; i > 0 && isShuttingDown; i--) {
					System.out.println(name + " in " + i);
					display.displayString(name + " in " + i);
					Thread.sleep(1000);
				}
				
				//Check if shutdown override has been done
				if (isShuttingDown){
					System.out.println(name);
					display.displayString(name);
					GpioSingleton.getInstance().get().shutdown();
					ProcessBuilder process = new ProcessBuilder(action);
					process.start();
				}
				else {
					System.out.println(name + " Aborted");
					display.displayString(name + " Aborted");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
