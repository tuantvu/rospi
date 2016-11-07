/**
 * 
 */
package OLED.Menu;

import OLED.Menu.MenuItem.MenuItemCallback;

/**
 * @author Tuan
 *
 */
public class ExitMenuItem extends MenuItem {

	/**
	 * @param name
	 * @param menuGroup
	 */
	public ExitMenuItem(MenuGroupItem parent) {
		super("Exit", parent);
	}


	/* (non-Javadoc)
	 * @see OLED.Menu.MenuItem#doMenuAction()
	 */
	@Override
	public MenuItem doMenuAction() {
		return doLongPress();
	}

}
