/**
 * 
 */
package OLED.Menu;

import java.util.ArrayList;

import OLED.SSD1306_I2C_Display;

/**
 * @author Tuan
 *
 */
public class MenuGroupItem extends MenuItem {
	private SSD1306_I2C_Display display;
	protected ArrayList<MenuItem> menuItems;
	private int currentPosition;
	
	public MenuGroupItem(String name, SSD1306_I2C_Display display) {
		super(name, null);
		this.display = display;
		this.menuItems = new ArrayList<>();
		this.currentPosition = 0;
	}

	@Override
	public MenuItem doMenuAction() {
		display.setMenu(menuItems.toArray(new MenuItem[menuItems.size()]));
		display.setMenuSelection(currentPosition);
		display.display();
		
		return null;
	}

	/* (non-Javadoc)
	 * @see OLED.MenuItem#doLongPress()
	 */
	@Override
	public MenuItem doLongPress() {
		MenuItem item = menuItems.get(currentPosition);
		MenuItem nextItem = item.doMenuAction();
		if (nextItem == null) {
			nextItem = item;
		}
		return nextItem;
	}
	
	/* (non-Javadoc)
	 * @see OLED.MenuItem#doShortPress()
	 */
	@Override
	public void doShortPress() {
		int nextPosition = currentPosition + 1;
		if (nextPosition >= menuItems.size()) {
			nextPosition = 0;
		}
		
		display.setMenuSelection(nextPosition);
		display.display();
		currentPosition = nextPosition;
	}

}
