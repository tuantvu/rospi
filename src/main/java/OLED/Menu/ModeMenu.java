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
public class ModeMenu extends MenuGroupItem {
	public ModeMenu(SSD1306_I2C_Display display, MenuGroupItem parent, MenuItemCallback callback) {
		super("Mode", display, callback);
		menuItems.add(new WanderMenuItem(display, this, callback));
		menuItems.add(new RemoteControlMenuItem(display, this, callback));
		menuItems.add(new ExitMenuItem(parent));
	}
}
