/**
 * 
 */
package OLED.Menu;

import OLED.ScrollableLog;

/**
 * @author Tuan
 *
 */
public abstract class MenuItem {
	protected String name;
	protected MenuGroupItem parent;
	protected MenuItemCallback callback;
	
	public MenuItem(String name, MenuGroupItem parent) {
		this(name, parent, null);
	}
	
	public MenuItem(String name, MenuGroupItem parent, MenuItemCallback callback) {
		this.name = name;
		this.parent = parent;
		this.callback = callback;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract MenuItem doMenuAction();
	public void doShortPress(){}
	public MenuItem doLongPress(){
		//Default long press action is to go back to parent
		if (parent != null) {
			parent.doMenuAction();
		}
		return parent;
	}
	public void onMessage(String message){};
	
	public interface MenuItemCallback {
		void connectBluetooth();
		void disconnectBluetooth();
		ScrollableLog getLog();
	}
}
