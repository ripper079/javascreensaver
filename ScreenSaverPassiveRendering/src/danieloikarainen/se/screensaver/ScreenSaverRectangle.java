package danieloikarainen.se.screensaver;

import danieloikarainen.se.screensaver.dialogs.CConfigSettings;
import danieloikarainen.se.screensaver.dialogs.CSettingsDialog;
import danieloikarainen.se.screensaver.util.CRandomNumbers;
import danieloikarainen.se.screensaver.util.CScreenInfo;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;



/*
	Screen saver using passive rendering
	Support for multiple screens - Max 6
	Adjust according to aspect ratio of screen	
	5 different rendering patterns
 		
 */

public class ScreenSaverRectangle 
{	
	private static int countMouseMovement = 0;
	
	//Geek statistics
	private static final boolean showRenderTime = true;
	
	//Update interval 
	private static final int updateInteval = 1250;
	
	//Enable multi-threading
	final static boolean multhiThreadSolution = true;
			
	//All screen(s)
	public static GraphicsDevice[] allScreenGraphicDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
	
	//For render statistics
	public static ArrayList<Long> averageCounterRenderTime = new ArrayList<>(1_000_000);
	
	//Screen saver setting(s) for each individual screen	
	public static CConfigSettings[] allOptionSettings;
	
	public static void main(String[] args) 
	{		
		//All code here will be executed in the event dispatch thread later, later is the key word here
		EventQueue.invokeLater(() -> 
		{										
			buildAndRunScreenSaver();
			
		});		
	}
	
	private static void buildAndRunScreenSaver() 
	{		
		CSettingsDialog settingDialog = new CSettingsDialog("Configure Screen Saver", allScreenGraphicDevices.length);
		//Setup action listener for ok button
		settingDialog.getOkButton().addActionListener(event -> ScreenSaverRectangle.storeConfigAndDisposeFrame(event, settingDialog));
				
		settingDialog.setVisible(true);		
		settingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);		
	}
	
	//When user clicks 'Start Screen Saver' button
	public static void storeConfigAndDisposeFrame(ActionEvent pEvent, CSettingsDialog pDialog) 
	{				
		//Retrieve config settings from Dialog
		//Run screen saver with these settings
		allOptionSettings = pDialog.getSelectedOptions();
		pDialog.dispose();
		
		//Get a reference to all frames
		myRectangleFrame[] allFrames = createAndSetupFramesWithHandlers();
		enterFullScreenEclusiveMode(allFrames);
		setupTimerAndRun(allFrames);
	}
	
	private static myRectangleFrame[] createAndSetupFramesWithHandlers() 
	{
		//All screen(s) gets each a frame
		final myRectangleFrame[] allFrames = new myRectangleFrame[allScreenGraphicDevices.length];	
		
		//Create the frames
		for(int i = 0; i < allFrames.length; i++) 
		{
			//Get all valid scaling factors
			int scalingFactor = allOptionSettings[i].getSizeRectangle();
			
			//Create the frame
			allFrames[i] = new myRectangleFrame(allScreenGraphicDevices[i], allScreenGraphicDevices[i].getDefaultConfiguration(), scalingFactor);
		}
		
		//Key handlers
		for (int i = 0; i < allFrames.length; i++)
		{
			allFrames[i].addKeyListener( new KeyAdapter() 
			{
				public void keyPressed(KeyEvent pKeyEvent) 
				{
					//in real program any key should exit the program
					if (pKeyEvent.getKeyCode() == KeyEvent.VK_ESCAPE)
						System.exit(0);
				}
			});
		}
		
		//Mouse handlers
		//Screen saver exit when mouse moves
		//Be aware of
		//		1. Mouse is moved by ADT first time the frame is drawn
		//		2. Mouse is moved programmatically to make the mouse 'invisible' (out of view) of primary screen
		allFrames[0].addMouseMotionListener(new MouseAdapter()
		{
			public void mouseMoved(MouseEvent pMouseEvent) 
			{		
					allFrames[0].makeMouseInvisible(allScreenGraphicDevices[0]);												
					ScreenSaverRectangle.countMouseMovement++;
					if (ScreenSaverRectangle.countMouseMovement > 2)
						System.exit(0);
			}
		});
				
		return allFrames;
	}
	
	private static void enterFullScreenEclusiveMode(myRectangleFrame[] allFrames) 
	{
		for (int i = 0; i < allFrames.length; i++) 
		{
			allFrames[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			allFrames[i].setVisible(true);
		}						
		
		//Enable full screen exclusive mode
		for (int i = 0; i < allScreenGraphicDevices.length; i++) 
		{
			allScreenGraphicDevices[i].setFullScreenWindow(allFrames[i]);
			//allScreenGraphicDevices[i].setFullScreenWindow(null);
		}
	}
	
	
	private static void setupTimerAndRun(myRectangleFrame[] allFrames) 
	{						
		//Predefined time interval
		/*		
		javax.swing.Timer myTimer = new javax.swing.Timer(updateInteval, evt -> 
		{								
			String solutionString = (ScreenSaverRectangle.multhiThreadSolution) ? "MulthiThread" : "SingleThread";
			
			long startThreadTimer = System.currentTimeMillis();
			
			if (ScreenSaverRectangle.multhiThreadSolution)
				multiThreadRender(allFrames);
			else
				singleThreadRender(allFrames);
			long endThreadTimer = System.currentTimeMillis();
			averageCounterRenderTime.add(endThreadTimer-startThreadTimer);
			if (showRenderTime)
				System.out.println(solutionString + " solution. " + "Average Time to render: " + averageRenderTime(averageCounterRenderTime) + " miliseconds");
			System.out.println("Current thread: " + Thread.currentThread());
			
			for (int i = 0; i < allFrames.length; i++) 		
				allFrames[i].repaint();			
		});
		*/
		/*javax.swing.*/Timer myTimer = new /*javax.swing.*/Timer(updateInteval, evt -> ScreenSaverRectangle.evertyTick(evt, allFrames));
		myTimer.start();
	}
	
	public static void evertyTick(ActionEvent pEvent, myRectangleFrame[] pAllFrames) 
	{
		String solutionString = (ScreenSaverRectangle.multhiThreadSolution) ? "MulthiThread" : "SingleThread";
		
		long startThreadTimer = System.currentTimeMillis();
		
		if (ScreenSaverRectangle.multhiThreadSolution)
			multiThreadRender(pAllFrames);
		else
			singleThreadRender(pAllFrames);
		long endThreadTimer = System.currentTimeMillis();
		averageCounterRenderTime.add(endThreadTimer-startThreadTimer);
		if (showRenderTime)
			System.out.println(solutionString + " solution. " + "Average Time to render: " + averageRenderTime(averageCounterRenderTime) + " miliseconds");
		
		for (int i = 0; i < pAllFrames.length; i++) 		
			pAllFrames[i].repaint();	
	}
	
							
	
	/* Forces the uses to choose a scalar value - Scales according to asepct ratio of current screen
	   Low value = Many rectangles  (Lower performance penalty)
	   High value = Few rectangles	(Higher performance penalty)
	 *  */
	//Simpler gui for one setting on each screen used in beta version
	public static int chooseScalarRatio(GraphicsDevice pGraphicsScreen, ArrayList<Integer> pListOfFactors) 
	{
		//Reverse the list - More user friendly
		Collections.reverse(pListOfFactors);
		Object[] scalingValues = pListOfFactors.toArray();
		boolean properValue = false;
		int scalingFactor = -1;
		do 
		{
			Integer scalingInteger = (Integer)JOptionPane.showInputDialog(
					null,
					pGraphicsScreen.toString(),
					"Size of Rectangles",
					JOptionPane.PLAIN_MESSAGE,
					null,
					scalingValues,
					scalingValues[0]
					);
			if (scalingInteger != null) 
			{
				scalingFactor = scalingInteger;
				properValue = true;
			}			
		}
		while(properValue == false);
				
		return scalingFactor;
	}
	
	//Renders with one thread on multiple JFrames where each frame has its own specific settings
	public static void singleThreadRender(final myRectangleFrame[] pAllFrames) 
	{
			for (int i = 0; i < pAllFrames.length; i++) 		
				pAllFrames[i].render(allOptionSettings[i]);//Added for config settings			
	}
	
	//Renders with one thread per each JFrame with specific settings 
	public static void multiThreadRender(final myRectangleFrame[] pAllFrames) 
	{
		Thread[] threadPerScreen = new Thread[pAllFrames.length];

		for (int i = 0; i < pAllFrames.length; i++) 
		{
			final int lamdaFinali = i;			
			threadPerScreen[i] = new Thread(() -> pAllFrames[lamdaFinali].render(allOptionSettings[lamdaFinali]), "Thread-Screen[" + lamdaFinali + "]"); 
		}
		
		for (int i = 0; i < pAllFrames.length; i++)
			threadPerScreen[i].start();
			
		for (int i = 0; i < pAllFrames.length; i++) 
		{
			try 
			{
				threadPerScreen[i].join();
			} 
			catch (InterruptedException pExceptionObject) 
			{
				System.err.println("Interupted exception occured while joining thread: " + pExceptionObject.getMessage());
				pExceptionObject.printStackTrace();
			}
		}	
	}
			
	public static long averageRenderTime(ArrayList<Long> pAverageCounter) 
	{
		if (pAverageCounter.size() == 0)
			return 0;
		
		long countFrames = 0;
		for (int i = 0; i < pAverageCounter.size(); i++) 
		{
			countFrames += pAverageCounter.get(i);
		}
		
		return (countFrames /pAverageCounter.size());
	}
}

class myRectangleFrame extends JFrame
{
	public myRectangleFrame(GraphicsDevice pGraphicsDevice, GraphicsConfiguration pGraphicsConfiguration, int pScalarRatio) 
	{
		super(pGraphicsConfiguration);						
		this.setSize(CScreenInfo.getScreenWidth(pGraphicsDevice), CScreenInfo.getScreenHeight(pGraphicsDevice));
		this.setUndecorated(true);
		this.setResizable(false);				
		
		canvasToDraw = new DrawRectanglesComponent(pGraphicsDevice, pScalarRatio);
		getContentPane().add(canvasToDraw);							
	}
	
	//Setting mouse position outside view of user
	public void makeMouseInvisible(GraphicsDevice pGraphicsDevice) 
	{
		Robot robot;
		try 
		{
			robot = new Robot();
			robot.mouseMove(0, CScreenInfo.getScreenHeight(pGraphicsDevice));
		} 
		catch (AWTException pExceptionObject) 
		{
			System.err.println("Exception occured while moving mouse..." + pExceptionObject.getMessage());
			pExceptionObject.printStackTrace();
		}
	}
	
	//Separate rending code from painting code
	public void render(CConfigSettings pConfigSettings) 
	{
		canvasToDraw.renderToBufferImage(pConfigSettings);
	}
	
	private DrawRectanglesComponent canvasToDraw;
}

class DrawRectanglesComponent extends JComponent
{	
	//Render-target
	private BufferedImage myBufferedImage;
	
	//The screen
	private GraphicsDevice mGraphicsDevice;
	
	private int mWidth_rect;
	private int mHeight_rect;
	private int mScalar;	
	
	public DrawRectanglesComponent(GraphicsDevice pGraphicsDevice, int pScalarRatio) 
	{
		mGraphicsDevice = pGraphicsDevice;
		
		mScalar = pScalarRatio;		
		
		mWidth_rect = mScalar * CScreenInfo.getWidthRatio(mGraphicsDevice);
		mHeight_rect = mScalar * CScreenInfo.getHeightRatio(mGraphicsDevice);
		
		myBufferedImage = new BufferedImage(CScreenInfo.getScreenWidth(mGraphicsDevice), CScreenInfo.getScreenHeight(mGraphicsDevice), BufferedImage.TYPE_INT_RGB);
	}
		
	//Drawing or rendering options
	public void renderToBufferImage(CConfigSettings pConfigSettings) 
	{		
		clearBuffer();				
		
		//Render whole screen
		if (pConfigSettings.getDrawOptions() == CConfigSettings.DrawingOption.WHOLE_SCREEN)
		{
			if (pConfigSettings.getColorMode() == CConfigSettings.ColorMode.SOLID_COLOR)
				RenderWholeScreenToBufferSolidColor();
			else if (pConfigSettings.getColorMode() == CConfigSettings.ColorMode.GRADIENT_PAINT) 
				RenderWholeScreenToBufferGradientPaint();
		}
		//Render one block
		else if (pConfigSettings.getDrawOptions() == CConfigSettings.DrawingOption.ONE_BLOCK) 
		{
			if (pConfigSettings.getColorMode() == CConfigSettings.ColorMode.SOLID_COLOR)
				RenderPixelToBufferSolidColor();
			else if (pConfigSettings.getColorMode() == CConfigSettings.ColorMode.GRADIENT_PAINT)
				RenderPixelToBufferGradientPaint();
			
		}
		//Render many blocks
		else if (pConfigSettings.getDrawOptions() == CConfigSettings.DrawingOption.MANY_BLOCK) 
		{
			if (pConfigSettings.getColorMode() == CConfigSettings.ColorMode.SOLID_COLOR)
				RenderRandomPixelsToBufferSolidColor();
			else if (pConfigSettings.getColorMode() == CConfigSettings.ColorMode.GRADIENT_PAINT)
				RenderRandomPixelsToBufferGradientPaint();			
		}
		//Render one row
		else if (pConfigSettings.getDrawOptions() == CConfigSettings.DrawingOption.ONE_ROW) 
		{
			RenderOneRandomRowToBuffer(pConfigSettings);				
		}
		//Render many rows
		else if (pConfigSettings.getDrawOptions() == CConfigSettings.DrawingOption.MANY_ROWS) 
		{
			RenderRandomRowsToBuffer(pConfigSettings);				
		}
		
		//Default to whole screen with solid color - this code should be unreachable code
		else
			System.err.println("Unreachable code.Drawing setting wrong");													
	}
	
	private void clearBuffer() 
	{
		//Clear the buffer each update to black
		Graphics2D g2d = myBufferedImage.createGraphics();
		g2d.setBackground(Color.black);
		g2d.clearRect(0, 0, CScreenInfo.getScreenWidth(mGraphicsDevice), CScreenInfo.getScreenHeight(mGraphicsDevice));
	}
	
	//Invoked when repaint is called - Draw to image
	public void paintComponent(Graphics g) 
	{		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.drawImage(myBufferedImage,  0, 0, this);	
	}
		
	//Writes to entire buffer with Gradient Color
	private void RenderWholeScreenToBufferGradientPaint() 
	{		
		Graphics2D g2;
		g2 = (Graphics2D) myBufferedImage.getGraphics();
		
		g2.setStroke(new BasicStroke(2.0f));
		
		Color borderColor = shadesOfBlack();
		Rectangle2D rect;		
		for (int i = 0; i < countXAxisRectangles(); i++) 
		{
			for (int j = 0; j < countYAxisRectangles(); j++) 
			{
				rect = new Rectangle2D.Double(i*mWidth_rect, j*mHeight_rect, mWidth_rect , mHeight_rect);
				
				Color myColorStart = randomColor();
				Color myColorEnd = randomColor();				
				GradientPaint rectGradient = new GradientPaint
										(i*mWidth_rect, j*mHeight_rect, myColorStart, i*mWidth_rect+mWidth_rect, j*mHeight_rect, myColorEnd);
						
				g2.setPaint(rectGradient);
				g2.fill(rect);
				g2.setColor(borderColor);
				g2.draw(rect);				
			}
		}
	}
	
	//Writes to entire buffer with Gradient Color
	private void RenderWholeScreenToBufferSolidColor() 
	{		
		Graphics2D g2;
		g2 = (Graphics2D) myBufferedImage.getGraphics();
		
		g2.setStroke(new BasicStroke(2.0f));
		
		Color borderColor = shadesOfBlack();
		Rectangle2D rect;		
		for (int i = 0; i < countXAxisRectangles(); i++) 
		{
			for (int j = 0; j < countYAxisRectangles(); j++) 
			{
				rect = new Rectangle2D.Double(i*mWidth_rect, j*mHeight_rect, mWidth_rect , mHeight_rect);
				
				Color myColor = randomColor();
						
				g2.setColor(myColor);
				g2.fill(rect);
				g2.setColor(borderColor);
				g2.draw(rect);				
			}
		}
	}
	
	
	//Draw one random row to buffer
	private void RenderOneRandomRowToBuffer(CConfigSettings pConfigSettings) 
	{
		int randomizedRow = (int)(Math.random()*countYAxisRectangles());
		
		if (pConfigSettings.getColorMode() == CConfigSettings.ColorMode.SOLID_COLOR)
			RenderRowIndexToBufferSolidColor(randomizedRow);
		else if (pConfigSettings.getColorMode() == CConfigSettings.ColorMode.GRADIENT_PAINT)
			RenderRowIndexToBufferGradientPaint(randomizedRow);
	}
	
	//Draws a single row to buffer - using row index - Gradiant Color
	private void RenderRowIndexToBufferGradientPaint(int pRowIndex) 
	{		
		Graphics2D g2;
		g2 = (Graphics2D) myBufferedImage.getGraphics();
		
		g2.setStroke(new BasicStroke(2.0f));
		Color borderColor = shadesOfBlack();
		
		Rectangle2D rect;	
		for (int i = 0; i < countXAxisRectangles(); i++) 
		{
			rect = new Rectangle2D.Double(i*mWidth_rect, pRowIndex*mHeight_rect, mWidth_rect , mHeight_rect);
			
			Color myColorStart = randomColor();
			Color myColorEnd = randomColor();
			
			GradientPaint rectGradient = new GradientPaint
					(i*mWidth_rect, pRowIndex*mHeight_rect, myColorStart, i*mWidth_rect+mWidth_rect, pRowIndex*mHeight_rect, myColorEnd);
			g2.setPaint(rectGradient);
			g2.fill(rect);
			g2.setColor(borderColor);
			g2.draw(rect);
		}		
	}
	
	//Draws a single row to buffer - using row index - Solid Color
	private void RenderRowIndexToBufferSolidColor(int pRowIndex) 
	{		
		Graphics2D g2;
		g2 = (Graphics2D) myBufferedImage.getGraphics();
		
		g2.setStroke(new BasicStroke(2.0f));
		Color borderColor = shadesOfBlack();
		
		Rectangle2D rect;	
		for (int i = 0; i < countXAxisRectangles(); i++) 
		{
			rect = new Rectangle2D.Double(i*mWidth_rect, pRowIndex*mHeight_rect, mWidth_rect , mHeight_rect);
			
			Color myColor = randomColor();
			
			g2.setColor(myColor);
			g2.fill(rect);
			g2.setColor(borderColor);
			g2.draw(rect);
		}		
	}
	
	
	private void RenderRandomRowsToBuffer(CConfigSettings pConfigSettings) 
	{
		//Generate 1 to countYAxisRectangles()		
		int pCountRows = (int)(Math.random()*countYAxisRectangles()) + 1;		

		List<Integer> listIndexRows = CRandomNumbers.genRandomListNumbers(0,  countYAxisRectangles()-1, pCountRows);
		
		if (pConfigSettings.getColorMode() == CConfigSettings.ColorMode.SOLID_COLOR)
		{
			for (int rowIndexToDrawTo : listIndexRows)					
				RenderRowIndexToBufferSolidColor(rowIndexToDrawTo);
		}
		else if (pConfigSettings.getColorMode() == CConfigSettings.ColorMode.GRADIENT_PAINT)
		{
			for (int rowIndexToDrawTo : listIndexRows)					
				RenderRowIndexToBufferGradientPaint(rowIndexToDrawTo);
		}
	}
	
	
	//Drawn a random block - Gradiant color
	private void RenderPixelToBufferGradientPaint() 
	{
		//The coordinates are 0-based
		int randomXIndex = (int)(Math.random() * countXAxisRectangles());
		int randomYIndex = (int)(Math.random() * countYAxisRectangles());
		
		
		Graphics2D g2;
		g2 = (Graphics2D) myBufferedImage.getGraphics();
		
		g2.setStroke(new BasicStroke(2.0f));
		Color borderColor = shadesOfBlack();
		
		Rectangle2D rect  = new Rectangle2D.Double(randomXIndex*mWidth_rect, randomYIndex*mHeight_rect, mWidth_rect , mHeight_rect);			
		
		Color myColorStart = randomColor();
		Color myColorEnd = randomColor();
		
		GradientPaint rectGradient = new GradientPaint
				(randomXIndex*mWidth_rect, randomYIndex*mHeight_rect, myColorStart, randomXIndex*mWidth_rect+mWidth_rect, randomYIndex*mHeight_rect+mHeight_rect, myColorEnd);
		
		g2.setPaint(rectGradient);
		g2.fill(rect);
		g2.setColor(borderColor);
		g2.draw(rect);
	}
	
	//Drawn a random block - Solid color
	private void RenderPixelToBufferSolidColor() 
	{
		//The coordinates are 0-based
		int randomXIndex = (int)(Math.random() * countXAxisRectangles());
		int randomYIndex = (int)(Math.random() * countYAxisRectangles());
				
		Graphics2D g2;
		g2 = (Graphics2D) myBufferedImage.getGraphics();
		
		g2.setStroke(new BasicStroke(2.0f));
		Color borderColor = shadesOfBlack();
		
		Rectangle2D rect  = new Rectangle2D.Double(randomXIndex*mWidth_rect, randomYIndex*mHeight_rect, mWidth_rect , mHeight_rect);			
		
		Color myColor = randomColor();
				
		g2.setColor(myColor);
		g2.fill(rect);
		g2.setColor(borderColor);
		g2.draw(rect);
	}
	
	//Draw random numbers of blocks to screen - Gradient Color
	private void RenderRandomPixelsToBufferGradientPaint()
	{
		ArrayList<Point> allPossibleCordinates = new ArrayList<>(countXAxisRectangles()*countYAxisRectangles());
		//Populate the list
		for(int i = 0; i < countXAxisRectangles(); i++) 
		{
			for (int j = 0; j < countYAxisRectangles(); j++) 
			{
				allPossibleCordinates.add(new Point(i,j));
			}
		}
		
		//Generate random number rectangles to draw
		int randomCount = (int)(Math.random()*(countXAxisRectangles()*countYAxisRectangles()))+1;
		
		//The list with the rectangles(and coordinates) to draw from
		ArrayList<Point> RectanglesToDrawFromCord = new ArrayList<>(randomCount);
		
		Random randomGenerator = new Random();		
		//Transfer (or populate) from allPossibleCordinates to RectanglesToDrawFromCord
		for (int i = 0; i < randomCount; i++) 
		{
			int randomIndex = randomGenerator.nextInt(allPossibleCordinates.size());
			RectanglesToDrawFromCord.add(allPossibleCordinates.get(randomIndex));
			allPossibleCordinates.remove(randomIndex);
		}
		
		Graphics2D g2;
		g2 = (Graphics2D) myBufferedImage.getGraphics();
		
		g2.setStroke(new BasicStroke(2.0f));
		Color borderColor = shadesOfBlack();
		
		for (Point rectCordinate: allPossibleCordinates)
		{
			Rectangle2D rect  = new Rectangle2D.Double(rectCordinate.x*mWidth_rect, rectCordinate.y*mHeight_rect, mWidth_rect , mHeight_rect);			
			
			Color myColorStart = randomColor();
			Color myColorEnd = randomColor();
			
			GradientPaint rectGradient = new GradientPaint
					(rectCordinate.x*mWidth_rect, rectCordinate.y*mHeight_rect, myColorStart, rectCordinate.x*mWidth_rect+mWidth_rect, rectCordinate.y*mHeight_rect+mHeight_rect, myColorEnd);
			
			g2.setPaint(rectGradient);
			g2.fill(rect);
			g2.setColor(borderColor);
			g2.draw(rect);
		}				
	}
	
	//Draw random numbers of blocks to screen - Solid Color
	private void RenderRandomPixelsToBufferSolidColor()
	{
		ArrayList<Point> allPossibleCordinates = new ArrayList<>(countXAxisRectangles()*countYAxisRectangles());
		//Populate the list
		for(int i = 0; i < countXAxisRectangles(); i++) 
		{
			for (int j = 0; j < countYAxisRectangles(); j++) 
			{
				allPossibleCordinates.add(new Point(i,j));
			}
		}
		
		//Generate random number rectangles to draw
		int randomCount = (int)(Math.random()*(countXAxisRectangles()*countYAxisRectangles()))+1;
		
		//The list with the rectangles(and coordinates) to draw from
		ArrayList<Point> RectanglesToDrawFromCord = new ArrayList<>(randomCount);
		
		Random randomGenerator = new Random();		
		//Transfer (or populate) from allPossibleCordinates to RectanglesToDrawFromCord
		for (int i = 0; i < randomCount; i++) 
		{
			int randomIndex = randomGenerator.nextInt(allPossibleCordinates.size());
			RectanglesToDrawFromCord.add(allPossibleCordinates.get(randomIndex));
			allPossibleCordinates.remove(randomIndex);
		}
		
		Graphics2D g2;
		g2 = (Graphics2D) myBufferedImage.getGraphics();
		
		g2.setStroke(new BasicStroke(2.0f));
		Color borderColor = shadesOfBlack();
		
		for (Point rectCordinate: allPossibleCordinates)
		{
			Rectangle2D rect  = new Rectangle2D.Double(rectCordinate.x*mWidth_rect, rectCordinate.y*mHeight_rect, mWidth_rect , mHeight_rect);			
			
			Color myColor = randomColor();				
			
			g2.setColor(myColor);
			g2.fill(rect);
			g2.setColor(borderColor);
			g2.draw(rect);
		}				
	}
	
	//Count Rectangles in X-Axis based on scaling too
	private int countXAxisRectangles() 
	{
		return CScreenInfo.getScreenWidth(mGraphicsDevice) / mWidth_rect;
	}	
	
	//Count Rectangles in Y-Axis based on scaling too
	private int countYAxisRectangles() 
	{
		return CScreenInfo.getScreenHeight(mGraphicsDevice) / mHeight_rect;
	}
	
	//Generate random color - Math.random() Synchronized
	private /*static*/ Color randomColor()
	{
		/*
		int R = (int)(Math.random()*256);
		int G = (int)(Math.random()*256);
		int B= (int)(Math.random()*256);
		return new Color(R, G, B);
		*/
		Random randomColor = new Random();;				 		
		
		int R = randomColor.nextInt(256); 
		int G = randomColor.nextInt(256);
		int B= randomColor.nextInt(256);				
		
		return new Color(R, G, B); 
	}
		
	//Generate random color of predefined of black shade
	private /*static*/ Color shadesOfBlack()
	{
		/*
		Color[] shadesOfBlack = 
		{
				new Color(59, 60, 54),								//Black Olive
				new Color(53, 56, 57),								//Onyx
				new Color(65, 74, 76),								//Outer Space
				new Color(54, 69, 79),								//CharCoal
				new Color(52, 52, 52)								//Jet
		};
				
		int randomIndexColor = (int)(Math.random()*(shadesOfBlack.length));
		return shadesOfBlack[randomIndexColor];	
		*/
		//Random randomShadeColor = new Random();		
		Color[] shadesOfBlack = 
		{
				new Color(59, 60, 54),								//Black Olive
				new Color(53, 56, 57),								//Onyx
				new Color(65, 74, 76),								//Outer Space
				new Color(54, 69, 79),								//CharCoal
				new Color(52, 52, 52)								//Jet
		};
				
		//int randomIndexColor = randomShadeColor.nextInt(shadesOfBlack.length);
		int randomIndexColor = new Random().nextInt(shadesOfBlack.length);
		return shadesOfBlack[randomIndexColor];	
	}			
}
