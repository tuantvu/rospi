/**
 * 
 */
package OLED.Menu;

/**
 * @author Tuan
 *
 */
public abstract class MenuItem {
	protected String name;
	protected MenuGroupItem parent;
	
	public MenuItem(String name, MenuGroupItem parent) {
		this.name = name;
		this.parent = parent;
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
}
