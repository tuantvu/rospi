/**
 * 
 */
package OLED.Menu;

import OLED.SSD1306_I2C_Display;

/**
 * @author Gigi
 *
 */
public class ModeMenu extends MenuGroupItem {
	public ModeMenu(SSD1306_I2C_Display display, MenuGroupItem parent) {
		super("Mode", display);
		menuItems.add(new WanderMenuItem(display, this));
		menuItems.add(new RemoteControlMenuItem(display, this));
		menuItems.add(new ExitMenuItem(parent));
	}
}
