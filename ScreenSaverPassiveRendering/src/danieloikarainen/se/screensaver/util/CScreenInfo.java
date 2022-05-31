package danieloikarainen.se.screensaver.util;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.util.ArrayList;

/*
 Util class for getting information about screen display
 e.i Common Aspect Ratio= 5:4, 4:3, 3:2, 8:5 (aka 16:10), 5:3, 16:9, 17:9
 
 * */

public class CScreenInfo 
{
	public static void main(String[] args) 
	{		
		printMultiMonitorInfo();	
	}
			
	/* Numbers of screens on user computer system */
	public static int getTotalScreenCount() 
	{
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length;
	}
	
	//Geek info
	public static void printMultiMonitorInfo()
	{
		//Determine how many monitors
				GraphicsDevice[] allScreens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
				System.out.println("Numbers of screens are: " + allScreens.length);
				System.out.println();
				for (int i = 0; i < allScreens.length; i++) 
				{
					DisplayMode myDisplay = allScreens[i].getDisplayMode();			
					System.out.println("Monitor: " + allScreens[i].toString());
					System.out.print("Resolution: " + myDisplay.getWidth() + " x " + myDisplay.getHeight() + " \t");
					System.out.print("Bit depth: " + myDisplay.getBitDepth() + " \t");
					System.out.print("Refresh rate(current): " + myDisplay.getRefreshRate() + "Hz \t");
					System.out.print("Aspect Ratio: " + CScreenInfo.getApectRatio(allScreens[i]) + " \t");
					System.out.print("Full Screen exclusive mode supported: " + allScreens[i].isFullScreenSupported() + " ");
					System.out.println();
				}
	}
	
	public static int getWidthRatio()
	{
		Dimension myScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = myScreenSize.width;
		int screenHeight = myScreenSize.height;
	
		int factor = greatestCommonFactor(screenWidth, screenHeight);
		int widthRatio = screenWidth / factor;
		int heightRatio = screenHeight / factor;
		
		/* Special Case 8:5 is equal to 16:10 */
		//if (widthRatio == 8 && heightRatio == 5)
		//	widthRatio *= 2; 
		return widthRatio;
	}
	
	public static int getWidthRatio(GraphicsDevice pScreenDevice)
	{
		DisplayMode myDisplay = pScreenDevice.getDisplayMode();
		int screenWidth = myDisplay.getWidth();
		int screenHeight = myDisplay.getHeight();
		
		int factor = greatestCommonFactor(screenWidth, screenHeight);
		int widthRatio = screenWidth / factor;
		int heightRatio = screenHeight / factor;
		
		/* Special Case 8:5 is equal to 16:10 */
		//if (widthRatio == 8 && heightRatio == 5)
		//	widthRatio *= 2; 
		return widthRatio;
	}
	
	public static int getHeightRatio() 
	{
		Dimension myScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = myScreenSize.width;
		int screenHeight = myScreenSize.height;
	
		int factor = greatestCommonFactor(screenWidth, screenHeight);
		int widthRatio = screenWidth / factor;
		int heightRatio = screenHeight / factor;
		/* Special Case 8:5 is equal to 16:10 */
		//if (widthRatio == 8 && heightRatio == 5) 
		//{
		//	heightRatio *= 2;
		//}
			 
		return heightRatio;
	}
	
	public static int getHeightRatio(GraphicsDevice pScreenDevice) 
	{
		DisplayMode myDisplay = pScreenDevice.getDisplayMode();
		int screenWidth = myDisplay.getWidth();
		int screenHeight = myDisplay.getHeight();
	
		int factor = greatestCommonFactor(screenWidth, screenHeight);
		int widthRatio = screenWidth / factor;
		int heightRatio = screenHeight / factor;
		/* Special Case 8:5 is equal to 16:10 */
		//if (widthRatio == 8 && heightRatio == 5)
		//{
		//	heightRatio *= 2;
		//} 
		return heightRatio;
	}
	
	public static String getApectRatio() 
	{
		Dimension myScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = myScreenSize.width;
		int screenHeight = myScreenSize.height;
	
		int factor = greatestCommonFactor(screenWidth, screenHeight);
		int WidthRatio = screenWidth / factor;
		int HeightRatio = screenHeight / factor;	
		/* Special Case 8:5 is equal to 16:10 */
		//if (WidthRatio == 8 && HeightRatio == 5) 
		//{
		//	WidthRatio *= 2;
		//	HeightRatio *= 2;
		//}
		return WidthRatio + ":" + HeightRatio;
	}
	
	public static String getApectRatio(GraphicsDevice pScreenDevice) 
	{
		DisplayMode myDisplay = pScreenDevice.getDisplayMode();
		int screenWidth = myDisplay.getWidth();
		int screenHeight = myDisplay.getHeight();
	
		int factor = greatestCommonFactor(screenWidth, screenHeight);
		int WidthRatio = screenWidth / factor;
		int HeightRatio = screenHeight / factor;
		/* Special Case 8:5 is equal to 16:10 */
		//if (WidthRatio == 8 && HeightRatio == 5) 
		//{
		//	WidthRatio *= 2;
		//	HeightRatio *= 2;
		//}
		return WidthRatio + ":" + HeightRatio;
	}
	
	public static String getApectRatio(int pScreenWidth, int pScreenHeight) 
	{
		int factor = greatestCommonFactor(pScreenWidth, pScreenHeight);
		int WidthRatio = pScreenWidth / factor;
		int HeightRatio = pScreenHeight / factor;
		/* Special Case 8:5 is equal to 16:10 */
		//if (WidthRatio == 8 && HeightRatio == 5) 
		//{
		//	WidthRatio *= 2;
		//	HeightRatio *= 2;
		//}
		return WidthRatio + ":" + HeightRatio;
	}
	
	public static String getScreenResolution() 
	{
		return getScreenWidth() + " x " + getScreenHeight();
	}
	
	public static String getScreenResolution(GraphicsDevice pScreenDevice) 
	{
		return getScreenWidth(pScreenDevice) + " x " + getScreenHeight(pScreenDevice);
	}
	
	public static int getScreenWidth()
	{
		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}
	
	public static int getScreenWidth(GraphicsDevice pScreenDevice)
	{
		return pScreenDevice.getDisplayMode().getWidth();
	}
	
	public static int getScreenHeight()
	{
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}
	
	public static int getScreenHeight(GraphicsDevice pScreenDevice)
	{
		return pScreenDevice.getDisplayMode().getHeight();
	}
	
	//CGraphicsHelper need this function
	//Aka Högsta gemensamma nämnare
	public static int greatestCommonFactor(int pWidth, int pHeight) 
	{
	    return (pHeight == 0) ? pWidth : greatestCommonFactor(pHeight, pWidth % pHeight);
	}
	
}
