/**
 * 
 */
package OLED.Menu;

import java.util.Arrays;
import java.util.List;

import OLED.SSD1306_I2C_Display;
import OLED.ScrollableLog;
import ros.ROSConstants;

/**
 * @author Tuan
 *
 */
public class RemoteControlMenuItem extends MenuItem {
	private ScrollableLog logs;
	private SSD1306_I2C_Display display;
	private List<String> validMessages = Arrays.asList(new String[]{
			ROSConstants.BLUETOOTH_CONNECT_CONNECTED, 
			ROSConstants.BLUETOOTH_CONNECT_CONNECT_FAIL, 
			ROSConstants.BLUETOOTH_CONNECT_START, 
			ROSConstants.BLUETOOTH_CONNECT_WAITING});

	public RemoteControlMenuItem(SSD1306_I2C_Display display, MenuGroupItem parent, MenuItemCallback callback) {
		super("Remote Control", parent, callback);
		this.display = display;
		this.logs = new ScrollableLog();
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#doMenuAction()
	 */
	@Override
	public MenuItem doMenuAction() {
		//Reset stuff
		logs = new ScrollableLog();
		callback.connectBluetooth();
		return this;
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#doShortPress()
	 */
	@Override
	public void doShortPress() {
		logs.scroll();
		display.displayString(logs.get());
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#doLongPress()
	 */
	@Override
	public MenuItem doLongPress() {
		//Stop the bluetooth connection before exiting
		callback.disconnectBluetooth();
		return super.doLongPress();
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#onMessage(java.lang.String)
	 */
	@Override
	public void onMessage(String message) {
		//Display latest status of blue tooth
		if (validMessages.contains(message)) {
			logs.add(message);
			display.displayString(logs.get());
		}
	}
	
	
}
