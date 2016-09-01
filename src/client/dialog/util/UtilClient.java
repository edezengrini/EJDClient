package client.dialog.util;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class UtilClient {
	
	private static final Dimension screenSize;
	private static final Dimension screenSizeHalf;
	
	private static Clipboard clipboard = null; 
	
	public static final Cursor CURSOR_DEFAULT = new Cursor(Cursor.DEFAULT_CURSOR);
	public static final Cursor CURSOR_WAIT = new Cursor(Cursor.WAIT_CURSOR);
	
	static{
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.height = screenSize.height - 30;
		
		screenSizeHalf = new Dimension(screenSize.width /2, screenSize.height / 2);	
		
	}
	
	public static Dimension getScreenSize(){
		return screenSize;
	}
	public static Dimension getScreenSizeHalf(){
		return screenSizeHalf;
	}
	
	public static void sendToClipboard(String text){
	  if(clipboard == null){
	    clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  }
    clipboard.setContents(new StringSelection(text), null);
	}
	
}
