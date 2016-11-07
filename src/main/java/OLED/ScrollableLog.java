/**
 * 
 */
package OLED;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

/**
 * @author Gigi
 *
 */
public class ScrollableLog {
	private ArrayList<String> logs;
	private int cursor;
	private int maxSize;
	private int screenSize;
	private final static int NO_SCROLL_CURSOR = -1;
	private final static int DEFAULT_SCREEN_SIZE = 4;
	private final static int DEFAULT_LOG_SIZE = 20;
	
	public ScrollableLog() {
		this(DEFAULT_LOG_SIZE, DEFAULT_SCREEN_SIZE);
	}
	
	public ScrollableLog(int size) {
		this(size, DEFAULT_SCREEN_SIZE);
	}
	
	public ScrollableLog(int size, int screen) {
		maxSize = size;
		logs = new ArrayList<>();
		screenSize = screen;
		cursor = NO_SCROLL_CURSOR;
	}
	
	/**
	 * Adds a log. Removes first log if over maxSize
	 * @param log
	 */
	public void add(String log) {
		//Compare the previous log and count them up.
		System.out.println("LOGS: " + logs);
		if (!logs.isEmpty()) {
			String prev = logs.get(logs.size() - 1);
			System.out.println("prev: " + prev);
			System.out.println("log: " + log);
			if (prev.contains(log)) {
				String stripped = prev.replace(log, "");
				System.out.println("stripped: " + stripped);
				stripped = stripped.replace("~x", "");
				System.out.println("stripped again: " + stripped);
				int currentCount = 1;
				if (NumberUtils.isNumber(stripped)) {
					currentCount = NumberUtils.toInt(stripped);
				}
				System.out.println("currentCount: " + currentCount);
				log += "~x" + (currentCount + 1);
				System.out.println("log: " + log);
				logs.remove(logs.size() - 1);
				System.out.println("LOGS: " + logs);
			}
		}
		logs.add(log);
		if (logs.size() > maxSize) {
			logs.remove(0);
		}
	}
	
	/**
	 * Returns the last screenSize # of lines
	 * @return
	 */
	public String[] latest() {
		List<String> temp = logs;
		if (logs.size() > screenSize) {
			temp = logs.subList(logs.size() - screenSize, logs.size());
		}
		return temp.toArray(new String[temp.size()]);
	}
	
	/**
	 * Returns the logs where the cursor is. Returns the latest if cursor
	 * has not been set or resets the cursor back to no scroll if cursor
	 * is near the end of the list of logs
	 * @return
	 */
	public String[] get() {
		//If not scrolling, get the latest
		//Check if cursor is within the screensize to the end. If so, switch it back to latest
		if (NO_SCROLL_CURSOR == cursor ||
				cursor >= logs.size() - screenSize) {
			cursor = NO_SCROLL_CURSOR;
			return latest();
		}
		else {
			List<String> temp = logs.subList(cursor, cursor + screenSize);
			return addPageNumber( temp.toArray(new String[temp.size()]) );
		}
	}
	
	/**
	 * Adds page number to end of the last log message
	 * @param logArray
	 * @return
	 */
	private String[] addPageNumber(String[] logArray) {
		if (NO_SCROLL_CURSOR != cursor ) {
			int numPages = ( logs.size() / screenSize ) + 1;
			int cursorPage = ( cursor / screenSize ) + 1;
			logArray[logArray.length - 1] = logArray[logArray.length - 1] + " #" + cursorPage + "/" + numPages;
		}
		return logArray;
	}
	
	/**
	 * Sets the cursor and scrolls screenSize # of lines. If at the end of the logs, scrolls back to the beginning
	 */
	public void scroll() {
		if (NO_SCROLL_CURSOR == cursor || 
				cursor >= logs.size() - screenSize) {
			cursor = 0;
		}
		else {
			cursor += screenSize;
		}
	}
	
	/**
	 * Sets the cursor and scrolls screenSize # of lines. If at the end of the logs, scrolls back to the beginning
	 */
	public void scrollTo(int cursorPos) {
		cursor = cursorPos;
		scroll();
	}
}
