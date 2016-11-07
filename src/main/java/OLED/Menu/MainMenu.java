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
public class MainMenu extends MenuGroupItem {
	public MainMenu(SSD1306_I2C_Display display, MenuItemCallback callback) {
		super("Main Menu", display, callback);
		menuItems.add(new ModeMenu(display, this, callback));
		menuItems.add(new LogMenuItem(display, this, callback));
		menuItems.add(new PowerMenu(display, this, callback));
	}
}
