/**
 * 
 */
package OLED.Menu;

import OLED.SSD1306_I2C_Display;

/**
 * @author Tuan
 *
 */
public class LogMenuItem extends MenuItem {
	SSD1306_I2C_Display display;

	public LogMenuItem(SSD1306_I2C_Display display, MenuGroupItem parent) {
		super("Logs", parent);
		this.display = display;
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#doMenuAction()
	 */
	@Override
	public MenuItem doMenuAction() {
		display.displayString("Logging");
		return this;
	}

}
