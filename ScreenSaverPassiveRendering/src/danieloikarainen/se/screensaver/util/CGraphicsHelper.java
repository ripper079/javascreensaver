package danieloikarainen.se.screensaver.util;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.util.ArrayList;

/*
 Utility class for appropriate aspect ratio for individual screen
  */

public class CGraphicsHelper 
{
	
	public static void main(String[] args)
	{		
		printDividableListFromSteamSurvey();
	}
	
	private static ArrayList<Integer> generateDividableList(int pScreenWidth, int pScreenHeight) 
	{
		ArrayList<Integer> commonDivadibleFactors= new ArrayList<>(30);

		for (int i = 1; i <= pScreenHeight; i++) 
		{
			//Both dividable 
			if ((pScreenWidth % i == 0) && (pScreenHeight % i == 0))
				commonDivadibleFactors.add(i);
		}
		
		return commonDivadibleFactors;
	}
	
	//Geek information
	@SuppressWarnings("unused")
	private static void printDividableListFromSteamSurvey() 
	{
		class ScreenDimensions
		{
			public ScreenDimensions(int pX, int pY) 
			{
				x = pX;
				y = pY;
			}
			public int x;
			public int y;
		}
		//Or Point(x,y))		
		
		ScreenDimensions[] myPair = 
		{
			new ScreenDimensions(1024, 768),	
			
			new ScreenDimensions(1280, 800),								
			new ScreenDimensions(1280, 1024),	
			new ScreenDimensions(1280, 720),	
			
			new ScreenDimensions(1360, 768),
			
			new ScreenDimensions(1366, 768),
			
			new ScreenDimensions(1440, 900),
			
			new ScreenDimensions(1600, 900),
			new ScreenDimensions(1600, 1200),
			
			new ScreenDimensions(1680, 1050),	
			
			new ScreenDimensions(1920, 1080),		
			new ScreenDimensions(1920, 1200),	
			
			new ScreenDimensions(2560, 1080),
			new ScreenDimensions(2560, 1440),
			
			new ScreenDimensions(3440, 1440),
			
			new ScreenDimensions(3840, 1080),
			new ScreenDimensions(3840, 1200),
			new ScreenDimensions(3840, 1600),
			new ScreenDimensions(3840, 2160),
			
			new ScreenDimensions(5120, 1440),
			new ScreenDimensions(5120, 2160),
			new ScreenDimensions(5120, 2880),
			
			new ScreenDimensions(7680, 4320)			
		};		
		
		
				
		System.out.println("Screen resolution\t" + "Aspect Ratio\t" + "Total Factor\t\t" + "Valid Divide Factors");
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println();
		for (int i = 0; i < myPair.length;i++)
		{
			ArrayList<Integer> myListRatios = generateDividableList(myPair[i].x, myPair[i].y);
			System.out.println(myPair[i].x + " x " + myPair[i].y + "\t\t" + CScreenInfo.getApectRatio(myPair[i].x, myPair[i].y) + "\t\t" + myListRatios.size() + "\t\t\t" + myListRatios);
			//System.out.print("DivideFactor:\t\t\t\t\t");
			/*
			System.out.print("\t\t\t");
			for(Integer item: myListRatios)
				System.out.print(item + ", ");
			System.out.println();
			*/
			//System.out.println("Total factors:\t\t\t\t\t" + myListRatios.size());			
		 }
	}

	/* Generates a list of legal scaling number for a display */
	public static ArrayList<Integer> generateLegalScalingFactors(int pScreenWidth, int pScreenHeight, int pCountElementAxis, int pSizeElementInXAxis, int pSizeElementInYAxis)
	{
		ArrayList<Integer> validScalingFactors= new ArrayList<>(30);
		
		for (int i = 1; i <=(pCountElementAxis+1)/2; i++)
		{
			//Both dividable 
			if ((pScreenWidth % (i*pSizeElementInXAxis) == 0) && (pScreenHeight % (i*pSizeElementInYAxis) == 0))
				validScalingFactors.add(i);
		}
				
		return validScalingFactors;
	}
		
	public static void printOptimizedLRatios() 
	{
		//Determine how many monitors
		GraphicsDevice[] allScreens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		System.out.println("Numbers of screens are: " + allScreens.length);
		System.out.println();
		for (int i = 0; i < allScreens.length; i++) 
		{
			System.out.println(getOptimizedApectRatio(allScreens[i]));
		}
	}
	
	//Return common know aspect ratios e.i 16:10 that in reality has an aspect ratio 8:5
	public static String getOptimizedApectRatio() 
	{
		Dimension myScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = myScreenSize.width;
		int screenHeight = myScreenSize.height;
	
		int factor = CScreenInfo.greatestCommonFactor(screenWidth, screenHeight);
		int WidthRatio = screenWidth / factor;
		int HeightRatio = screenHeight / factor;	
		
		int optimizedScalingFactor = calculateLogicalUppScaleFactor(WidthRatio, HeightRatio);
		
		HeightRatio *= optimizedScalingFactor;
		WidthRatio *= optimizedScalingFactor;
		
		
		return WidthRatio + ":" + HeightRatio;
	}
	
	public static String getOptimizedApectRatio(GraphicsDevice pScreenDevice) 
	{
		DisplayMode myDisplay = pScreenDevice.getDisplayMode();
		int screenWidth = myDisplay.getWidth();
		int screenHeight = myDisplay.getHeight();
	
		int factor = CScreenInfo.greatestCommonFactor(screenWidth, screenHeight);
		int WidthRatio = screenWidth / factor;
		int HeightRatio = screenHeight / factor;

		
		int optimizedScalingFactor = calculateLogicalUppScaleFactor(WidthRatio, HeightRatio);
		
		HeightRatio *= optimizedScalingFactor;
		WidthRatio *= optimizedScalingFactor;
		
		
		return WidthRatio + ":" + HeightRatio;
	}
	
	//As previous explanation does a screen with true aspect ratio 8:5(16:10 common know), this return 16
	public static int getOptimizedWidthRatio()
	{
		Dimension myScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = myScreenSize.width;
		int screenHeight = myScreenSize.height;
	
		int factor = CScreenInfo.greatestCommonFactor(screenWidth, screenHeight);
		int widthRatio = screenWidth / factor;
		int heightRatio = screenHeight / factor;			
					
		int optimizedScalingFactor = calculateLogicalUppScaleFactor(widthRatio, heightRatio);			
		widthRatio *= optimizedScalingFactor; 
		
		return widthRatio;
	}
	
	public static int getOptimizedWidthRatio(GraphicsDevice pScreenDevice)
	{
		DisplayMode myDisplay = pScreenDevice.getDisplayMode();
		int screenWidth = myDisplay.getWidth();
		int screenHeight = myDisplay.getHeight();
		
		int factor = CScreenInfo.greatestCommonFactor(screenWidth, screenHeight);
		int widthRatio = screenWidth / factor;
		int heightRatio = screenHeight / factor;
				
		int optimizedScalingFactor = calculateLogicalUppScaleFactor(widthRatio, heightRatio);			
		widthRatio *= optimizedScalingFactor;
		
		return widthRatio;
	}
	
	//As previous explanation does a screen with true aspect ratio 8:5(16:10 common know), this return 10
	public static int getOptimizedHeightRatio() 
	{
		Dimension myScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = myScreenSize.width;
		int screenHeight = myScreenSize.height;
	
		int factor = CScreenInfo.greatestCommonFactor(screenWidth, screenHeight);
		int widthRatio = screenWidth / factor;
		int heightRatio = screenHeight / factor;
								
		int optimizedScalingFactor = calculateLogicalUppScaleFactor(widthRatio, heightRatio);
		heightRatio *= optimizedScalingFactor;
		
		return heightRatio;
	}
	
	public static int getOptimizedHeightRatio(GraphicsDevice pScreenDevice) 
	{
		DisplayMode myDisplay = pScreenDevice.getDisplayMode();
		int screenWidth = myDisplay.getWidth();
		int screenHeight = myDisplay.getHeight();
	
		int factor = CScreenInfo.greatestCommonFactor(screenWidth, screenHeight);
		int widthRatio = screenWidth / factor;
		int heightRatio = screenHeight / factor;
				
		int optimizedScalingFactor = calculateLogicalUppScaleFactor(widthRatio, heightRatio);
		heightRatio *= optimizedScalingFactor;
		
		return heightRatio;
	}
	
	/*
		Converts to logical aspect ratios for uniform dimension across different screens
  		5:4 -> 15:12		Factor 3
  		4:3 -> 16:12		Factor 4   
  		3:2 -> 15:10		Factor 5		
  		8:5 -> 16:10		Factor 2
  		5:3 -> 15:9			Factor 5
  		16:9 -> 16:9		Factor 1
  		17:9 -> 17:9		Factor 1
  		21:9 -> 21:9		Factor 1
	*/
	private static int calculateLogicalUppScaleFactor(int pWidthRatio, int pHeightRatio) 
	{
		int optimizedScalingFactor = -1;
	
		if (pWidthRatio == 5 && pHeightRatio == 4) 
			optimizedScalingFactor = 3;
		else if (pWidthRatio == 4 && pHeightRatio == 3) 
			optimizedScalingFactor = 4;
		else if (pWidthRatio == 3 && pHeightRatio == 2) 
			optimizedScalingFactor = 5;
		else if (pWidthRatio == 8 && pHeightRatio == 5) 
			optimizedScalingFactor = 2;
		else if (pWidthRatio == 5 && pHeightRatio == 3) 
			optimizedScalingFactor = 5;
		else if (pWidthRatio == 16 && pHeightRatio == 9) 
			optimizedScalingFactor = 1;
		else if (pWidthRatio == 17 && pHeightRatio == 9) 
			optimizedScalingFactor = 1;
		else if (pWidthRatio == 21 && pHeightRatio == 9) 
			optimizedScalingFactor = 1;
		//Other obsecure
		else
			optimizedScalingFactor = 1;
		
		return optimizedScalingFactor;
	}
}
