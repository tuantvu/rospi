/**
 * 
 */
package OLED.Menu;

import OLED.SSD1306_I2C_Display;

/**
 * @author Gigi
 *
 */
public class MainMenu extends MenuGroupItem {
	public MainMenu(SSD1306_I2C_Display display) {
		super("Main Menu", display);
		menuItems.add(new ModeMenu(display, this));
		menuItems.add(new LogMenuItem(display, this));
		menuItems.add(new PowerMenu(display, this));
	}
}
