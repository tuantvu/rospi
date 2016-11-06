/**
 * 
 */
package OLED.Menu;

import OLED.SSD1306_I2C_Display;

/**
 * @author Tuan
 *
 */
public class WanderMenuItem extends MenuItem {
	SSD1306_I2C_Display display;

	public WanderMenuItem(SSD1306_I2C_Display display, MenuGroupItem parent) {
		super("Wander", parent);
		this.display = display;
	}

	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#doMenuAction()
	 */
	@Override
	public MenuItem doMenuAction() {
		display.displayString("Wandering");
		return this;
	}

}
