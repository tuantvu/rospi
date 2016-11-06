/**
 * 
 */
package OLED.Menu;

import OLED.SSD1306_I2C_Display;

/**
 * @author Tuan
 *
 */
public class RemoteControlMenuItem extends MenuItem {
	SSD1306_I2C_Display display;

	public RemoteControlMenuItem(SSD1306_I2C_Display display, MenuGroupItem parent) {
		super("Remote Control", parent);
		this.display = display;
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#doMenuAction()
	 */
	@Override
	public MenuItem doMenuAction() {
		display.displayString("Remote Controlling");
		return this;
	}

}
