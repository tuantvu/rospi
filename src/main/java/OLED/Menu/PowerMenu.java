/**
 * 
 */
package OLED.Menu;

import OLED.SSD1306_I2C_Display;
import OLED.Menu.MenuItem.MenuItemCallback;

/**
 * @author Gigi
 *
 */
public class PowerMenu extends MenuGroupItem {
	public PowerMenu(SSD1306_I2C_Display display, MenuGroupItem parent, MenuItemCallback callback) {
		super("Power", display, callback);
		menuItems.add(new ShutDownMenuItem(display, this, "Reset", "reboot"));
		menuItems.add(new ShutDownMenuItem(display, this, "Shut Down", "halt"));
		menuItems.add(new ExitMenuItem(parent));
	}
}
