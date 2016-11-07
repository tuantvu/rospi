/**
 * 
 */
package OLED.Menu;

import OLED.SSD1306_I2C_Display;
import OLED.ScrollableLog;

/**
 * @author Tuan
 *
 */
public class LogMenuItem extends MenuItem {
	private SSD1306_I2C_Display display;
	private ScrollableLog logs;

	public LogMenuItem(SSD1306_I2C_Display display, MenuGroupItem parent, MenuItemCallback callback) {
		super("Logs", parent, callback);
		this.display = display;
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#doMenuAction()
	 */
	@Override
	public MenuItem doMenuAction() {
		this.logs = callback.getLog();
		logs.scrollTo(0);
		display.displayString(logs.get());
		return this;
	}

	@Override
	public void doShortPress() {
		logs.scroll();
		display.displayString(logs.get());
	}
}
