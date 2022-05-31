package danieloikarainen.se.screensaver.dialogs;

import danieloikarainen.se.screensaver.ScreenSaverRectangle;
import danieloikarainen.se.screensaver.constants.CConstants;
import danieloikarainen.se.screensaver.util.CGraphicsHelper;
import danieloikarainen.se.screensaver.util.CScreenInfo;

import javax.swing.JDialog;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;


/**
 *
 * @author Daniel Oikarainen
 * @version 1.0
 */
public class CSettingsDialog extends JDialog
{

	//Simulate screen numbers
	//static final int COUNT_SCREEN = 1;
	
	public static void main(String[] args)
    {	
		/*
		CSettingsDialog settingDialog = new CSettingsDialog("Configure Screen Saver", COUNT_SCREEN);
		settingDialog.setVisible(true);
		settingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		*/
		
		//All gui code here
		EventQueue.invokeLater
		(() -> 
				{					
					//New
					CEnhancedSettingsDialog newSettingDialog = new CEnhancedSettingsDialog("Configure Screen Saver");
					newSettingDialog.setVisible(true);
					newSettingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					
					
					//Old
					//CSettingsDialog settingDialog = new CSettingsDialog("Configure Screen Saver", CConstants.COUNT_SCREEN);
					//settingDialog.setVisible(true);
					//settingDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				}
		);
    }
	
	private void initDialog() 
	{
		int heightScreen = Toolkit.getDefaultToolkit().getScreenSize().height;
		int widthScreen = Toolkit.getDefaultToolkit().getScreenSize().width;
		
		int widthDialogOneScreen = Toolkit.getDefaultToolkit().getScreenSize().width / 6;
		int heightDialogOneScreen = Toolkit.getDefaultToolkit().getScreenSize().height / 6;

		int totalWidthDialog = widthDialogOneScreen;
		int totalHeightDialog = heightDialogOneScreen;
		
		if (CConstants.COUNT_SCREEN == 2)
			totalWidthDialog *= 2;
		else if (CConstants.COUNT_SCREEN == 3)
			totalWidthDialog *= 3;
		else if (CConstants.COUNT_SCREEN == 4) 
		{
			totalWidthDialog *= 2;
			totalHeightDialog *= 2;
		}
		else if (CConstants.COUNT_SCREEN == 5 || CConstants.COUNT_SCREEN == 6) 
		{
			totalWidthDialog *= 3;
			totalHeightDialog *= 2;
		}
		else		
		{
			totalWidthDialog *= 1;
			totalHeightDialog *= 1;
		}
		

		setSize(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height / 6);
		setSize(totalWidthDialog, totalHeightDialog);
				
		//Center 		
		setLocation(new Point(
											(widthScreen-getWidth())/2 ,					//Upper left X cord 
											(heightScreen-getHeight()) / 2					//Upper left Y cord
							)
		);
	}
	
    public CSettingsDialog(String pTitle, int pCountScreens)
    {
    	initDialog();
    	setTitle(pTitle);      	    	
    	
    	Container contentPane = getContentPane();
    
        createALLSettingPanel();
        createButtonPanel();
                            	
        //Finally add panels to contentPane
        contentPane.add(mPanelContainerALLSetting, BorderLayout.CENTER);
        contentPane.add(mButtonPanel, BorderLayout.PAGE_END);
                
        setResizable(false);        
    }
    
    private SettingPanel createOptionPanelForOneScreen(final int pIndexScreen)
    {
    	SettingPanel panelOptions = new SettingPanel();     	    	    	    	    	
    	    	    	
    	//An array of legal scalingfactor for each frame
		ArrayList<Integer> myListOfFactors = CGraphicsHelper.generateLegalScalingFactors(
				CScreenInfo.getScreenWidth(ScreenSaverRectangle.allScreenGraphicDevices[pIndexScreen]), 															//pScreenWidtht
				CScreenInfo.getScreenHeight(ScreenSaverRectangle.allScreenGraphicDevices[pIndexScreen]), 															//pScreenHeight
				CScreenInfo.getScreenWidth(ScreenSaverRectangle.allScreenGraphicDevices[pIndexScreen]) / CScreenInfo.getWidthRatio(ScreenSaverRectangle.allScreenGraphicDevices[pIndexScreen]), 	//pCountElementAxis
				CScreenInfo.getWidthRatio(ScreenSaverRectangle.allScreenGraphicDevices[pIndexScreen]), 																//pSizeElementInXAxis
				CScreenInfo.getHeightRatio(ScreenSaverRectangle.allScreenGraphicDevices[pIndexScreen]));															//pSizeElementInYAxis
		    	
		 //Count rectangle Section - This will vary across different screen(s)
		Integer[] countRectangles = new Integer[myListOfFactors.size()];    	
		//Reverse the list - Better for displaying to the user
		Collections.reverse(myListOfFactors);
		
		for (int i = 0; i < myListOfFactors.size(); i++)
    	{
    		int extractedValue = myListOfFactors.get(i);
    		countRectangles[i] = extractedValue;
    	}
    	    	
    	//Add Combo Box Size
    	JComboBox<Integer> mListSizeRectComboBox = new JComboBox<>(countRectangles);
        panelOptions.add(new JLabel("Size Rectangle(s)"));
        panelOptions.add(mListSizeRectComboBox);
        panelOptions.addSizeRectComboBox(mListSizeRectComboBox);
        
        
      //Adding Color Mode label with checkbox
    	JComboBox<CConfigSettings.ColorMode> colorModesComboBox = new JComboBox<>();
    	colorModesComboBox.setModel(new DefaultComboBoxModel<>(CConfigSettings.ColorMode.values()));
    	panelOptions.add(new JLabel("Color Mode"));
    	panelOptions.add(colorModesComboBox);
    	panelOptions.addColorModeComboBox(colorModesComboBox);
                        
        JComboBox<CConfigSettings.DrawingOption> mListDrawingComboBox = new JComboBox<>();
        mListDrawingComboBox.setModel(new DefaultComboBoxModel<>(CConfigSettings.DrawingOption.values()));
        panelOptions.add(new JLabel("Drawing method"));
        panelOptions.add(mListDrawingComboBox);
        panelOptions.addDrawingComboBox(mListDrawingComboBox);                
        
        //Visual identification
        panelOptions.setBorder(BorderFactory.createLineBorder(Color.red, 2));
                
        mOptionPanel[pIndexScreen] = panelOptions;
        
        return panelOptions;    	    	
    }
        
    private void createALLSettingPanel() 
    {    	
    	//Create instances for all screens
    	mPanelEachScreen = new JPanel[CConstants.COUNT_SCREEN];
    	
    	//Create option panel equal to screen numbers
    	mOptionPanel = new SettingPanel[CConstants.COUNT_SCREEN];
    	
    	//Populate all panels
    	for (int i = 0; i < CConstants.COUNT_SCREEN; i++) 
    	{
    		String screenName = ScreenSaverRectangle.allScreenGraphicDevices[i].toString();    		
    		mPanelEachScreen[i] = new JPanel(new BorderLayout());
    		mPanelEachScreen[i].add(new JLabel(screenName, SwingConstants.CENTER), BorderLayout.PAGE_START);
    		mPanelEachScreen[i].add(createOptionPanelForOneScreen(i), BorderLayout.CENTER);
    	}
    	    	    	    	    	    	
    	//The gridlayout depends how many screen(s) there is
    	//Better fail fast;
    	int countRows = -1;
    	int countCols = -1;
    	if (CConstants.COUNT_SCREEN == 1) 
    	{
    		countRows = 1;
    		countCols = 1;
    	}
    	else if (CConstants.COUNT_SCREEN == 2) 
    	{
    		countRows = 1;
    		countCols = 2;
    	}
    	else if (CConstants.COUNT_SCREEN == 3) 
    	{
    		countRows = 1;
    		countCols = 3;
    	}
    	else if (CConstants.COUNT_SCREEN == 4) 
    	{
    		countRows = 2;
    		countCols = 2;
    	}
    	else if (CConstants.COUNT_SCREEN == 5) 
    	{
    		countRows = 2;
    		countCols = 3;
    	}
    	else if (CConstants.COUNT_SCREEN == 6) 
    	{
    		countRows = 2;
    		countCols = 3;
    	}

    	//Panel that hold the ALL setting
    	mPanelContainerALLSetting = new JPanel(new GridLayout(countRows, countCols, 15, 40));
    	    	    	    	
    	//Add all panels
    	for (int i = 0; i < CConstants.COUNT_SCREEN; i++)
    		mPanelContainerALLSetting.add(mPanelEachScreen[i]);
    	
    	mPanelContainerALLSetting.setBorder(BorderFactory.createLineBorder(Color.cyan, 4));    	           
    }
    
    public CConfigSettings[] getSelectedOptions() 
    {
    	CConfigSettings[] allSettings = new CConfigSettings[CConstants.COUNT_SCREEN];
    	
    	//Loop through each screen
    	for (int i = 0; i < CConstants.COUNT_SCREEN; i++)
    	{    		
    		//Integer refreshrate = (Integer)mOptionPanel[i].getRefreshRates().getSelectedItem();
    		//String drawingMethodChoose = (String)mOptionPanel[i].getDrawing().getSelectedItem();
    		
    		//Get all setting from gui components
    		CConfigSettings.ColorMode colorMode = (CConfigSettings.ColorMode)mOptionPanel[i].getColorMode().getSelectedItem();
    		Integer sizeOfRectangles = (Integer)mOptionPanel[i].getSizeRect().getSelectedItem();	    		
    		CConfigSettings.DrawingOption drawingOption = (CConfigSettings.DrawingOption)mOptionPanel[i].getDrawingOption().getSelectedItem();
    		
    		
    		//Construct setting object
    		allSettings[i] = new CConfigSettings(sizeOfRectangles, drawingOption, colorMode);    		
    	}
    	
    	return allSettings;
    }
    
    public JButton getOkButton() 
    {
    	return startScreenSaver;
    }
    
    private void createButtonPanel() 
    {
    	//Panel hold the button
    	mButtonPanel = new JPanel();
        /*JButton*/ startScreenSaver = new JButton("Start Screen Saver");        
        JButton closeButton = new JButton("Exit");
        
        JButton invisibleEasterEggViskanAb = new JButton(" ");
        
        invisibleEasterEggViskanAb.setOpaque(false);
        invisibleEasterEggViskanAb.setContentAreaFilled(false);
        invisibleEasterEggViskanAb.setBorderPainted(false);
        invisibleEasterEggViskanAb.addActionListener(event -> System.out.println("Cliked on easter egg..."));

        mButtonPanel.add(startScreenSaver);
        mButtonPanel.add(closeButton);   
        
        mButtonPanel.add(invisibleEasterEggViskanAb);
    	
        //New game close button
        closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	CConfigSettings[] getSettings = getSelectedOptions();
            	
            	for (int i = 0; i < getSettings.length; i++) 
            	{
            		System.out.println("Screen:" + (i+1));
            		System.out.println("Rectangle size: " + getSettings[i].getSizeRectangle());
            		System.out.println("ColorMode: " + getSettings[i].getColorMode());
            		System.out.println("DrawOptions: " + getSettings[i].getDrawOptions());
            		System.out.println();
            		System.out.println();
            	}
            	
                System.exit(1);
                
            }
        }
        );
    }
                
    //Container for all panel(s)
    private JPanel mPanelContainerALLSetting;
    //Container for one screen setting + label + info
    private JPanel[] mPanelEachScreen;
                        
    //Panel for the buttons
    private JPanel mButtonPanel;
    
    //The optionpanel
    private SettingPanel[] mOptionPanel;
    
    private JButton startScreenSaver;
    
    
    //private int mScreenCount;
    
    public static final int WIDTH = 250;
    public static final int HEIGHT = 150;
}


//The panel that contains all options for one screen - monitor
class SettingPanel extends JPanel
{
	public static final int mCountRows = 3;
	//public static final int mCountRows = 2;
	public static final int mCountCols = 2;
	
	public SettingPanel() 
	{
		super(new GridLayout(mCountRows, mCountCols));	
	}
	
	/*
	public JComboBox<Integer> getRefreshRates()
	{
		return mComboBoxRefreshRates;
	}
	*/
	
	/*
	public void addRefreshComboBox(JComboBox<Integer> pComboBox) 
	{
		mComboBoxRefreshRates = pComboBox;
		add(pComboBox);
	}
	*/
	
	public JComboBox<Integer> getSizeRect()
	{
		return mComboBoxSizeRect;
	}
	
	public void addSizeRectComboBox(JComboBox<Integer> pComboBox) 
	{
		mComboBoxSizeRect = pComboBox;
		add(pComboBox);
	}
	
	public JComboBox<CConfigSettings.DrawingOption> getDrawingOption()
	{
		return mComboBoxDrawingOption;
	}
	
	public void addDrawingComboBox(JComboBox<CConfigSettings.DrawingOption> pComboBox) 
	{
		mComboBoxDrawingOption = pComboBox;
		add(pComboBox);
	}
	
	public JComboBox<CConfigSettings.ColorMode> getColorMode()
	{
		return mComboBoxColorMode;
	}
	
	public void addColorModeComboBox(JComboBox<CConfigSettings.ColorMode> pComboBox) 
	{
		mComboBoxColorMode = pComboBox;
		add(pComboBox);
	}
	
	//This should be removed
	//private JComboBox<Integer> mComboBoxRefreshRates;
	
	private JComboBox<Integer> mComboBoxSizeRect;
	private JComboBox<CConfigSettings.DrawingOption> mComboBoxDrawingOption;	
	private JComboBox<CConfigSettings.ColorMode> mComboBoxColorMode;
}


class CEnhancedSettingsDialog extends JDialog 
{	
	//Only one needed
	public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	private static final int SIMULATE_COUNT_MONITORS = 6;
	private ArrayList<CEnhancedPanel> mSettingPanels = new ArrayList<>(SIMULATE_COUNT_MONITORS);
	
	private JButton mStartScreenSaverButton;
	private JButton mExitButton;
	
	private static final Color DEFAULT_TITLE_COLOR = new Color(200, 200, 200);
	
	public CEnhancedSettingsDialog(String pTitle) 
	{
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setTitle(pTitle);
		setResizable(false);
		
		//Container Panel(s) for 
		JPanel allConfigSettingPanel = new JPanel();
		allConfigSettingPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		allConfigSettingPanel.setLayout(new FlowLayout());				
				
		//Populate array with panels and add it to allConfigSetting Panel
		for (int i = 0; i < SIMULATE_COUNT_MONITORS; i++) 
		{
			String monitorName = "Screen " + i;
			CEnhancedPanel oneSettingPanel = new CEnhancedPanel(DEFAULT_TITLE_COLOR, monitorName);
			//Add the panel to the array
			mSettingPanels.add(oneSettingPanel);
			//Add the panel to container-panel (allConfigSettingPanel)
			allConfigSettingPanel.add(oneSettingPanel);
		}
				
		//Panel for buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		//The buttons
		mStartScreenSaverButton = new JButton("Start Screen Saver");
		mExitButton = new JButton("Exit");
		buttonPanel.add(mStartScreenSaverButton);
		buttonPanel.add(mExitButton);
		
		//Add actionListener to buttons
		mStartScreenSaverButton.addActionListener(e -> clickStartScreenSaverbutton(e));
		mExitButton.addActionListener(e -> clickExitButton(e));
		
		//Add everything to contentPane
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(allConfigSettingPanel, BorderLayout.CENTER);
		contentPane.add(buttonPanel, BorderLayout.PAGE_END);
		
		pack();
		
		int widthFrame =  this.getWidth();
		int heightFrame =  this.getHeight();
		
		//Center 		
		setLocation(new Point(
								(SCREEN_WIDTH-widthFrame) / 2 ,					//Upper left X cord 
								(SCREEN_HEIGHT-heightFrame) / 4					//Upper left Y cord
							)
		);
		
	}
	
	public static void clickExitButton(ActionEvent pEvent) 
	{
		System.out.println("Exit button clicked!!!!");
	}
	
	public static void clickStartScreenSaverbutton(ActionEvent pEvent) 
	{
		System.out.println("Start screeen saver button clicked!!!!");
	}
}

class CEnhancedPanel extends JPanel 
{	
	private final static int PADDING = 5;
	
	public final int DEFAULT_WIDTH_OFFSET = 20;
	public final int DEFAULT_WIDTH = (CEnhancedSettingsDialog.SCREEN_WIDTH / CConstants.MAXIMUM_SCREEN_COUNT) - DEFAULT_WIDTH_OFFSET;	 
	//NOT static - May be many panels
	public final int DEFAULT_HEIGHT = 100;
	
	//pTitle should be the name of the monitor : default is Monitor 1, Monitor 2 ....
	//pTitle should be a prime candidate for jni call that actually fetches name from monitor
	public CEnhancedPanel(Color pColorBackgroundForTitle, String pTitle) 
	{
		setBackground(pColorBackgroundForTitle);
		setLayout(new BorderLayout());
		
		//Add title to panel
		JLabel titleLabel = new JLabel(pTitle, SwingConstants.CENTER);
		//Padding around
		titleLabel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		add(titleLabel, BorderLayout.PAGE_START);	
		
		
		//Labels on the left side
		JLabel labelSizeRectangle = new JLabel("Size of Rectangles");
		JLabel labelColorMode = new JLabel("ColorMode");
		JLabel labelDrawingMethod = new JLabel("Drawing method");
		
		//Panel that holds on the left side labels
		JPanel labelRectangleLeftPanel = new JPanel(new GridLayout(3, 1));
		labelRectangleLeftPanel.add(labelSizeRectangle);
		labelRectangleLeftPanel.add(labelColorMode);
		labelRectangleLeftPanel.add(labelDrawingMethod);
		
		//Comboboxes on the right side
		String[] sizeBlock = {"S", "M", "L", "XL" };
    	JComboBox<String> listAgeComboBox = new JComboBox<>(sizeBlock);
		String[] colorModesBlock = {"Solid Color", "Gradient Paint"};
		JComboBox<String> listNamesComboBox = new JComboBox<>(colorModesBlock);
		String[] genders = {"Female", "Male", "Male", "Female"};
		JComboBox<String> listGendersComboBox = new JComboBox<>(genders);
    	
		//Panel that holds on the right side comboxes
		JPanel comboBoxesRectangleRightPanel = new JPanel(new GridLayout(3, 1));
		//Add comboboxes to panel
		comboBoxesRectangleRightPanel.add(listAgeComboBox);
		comboBoxesRectangleRightPanel.add(listNamesComboBox);
		comboBoxesRectangleRightPanel.add(listGendersComboBox);		
		
		//Panel that hold all the setting info
		JPanel settingPanel = new JPanel(new GridLayout(1, 2));
		settingPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		settingPanel.add(labelRectangleLeftPanel);
		settingPanel.add(comboBoxesRectangleRightPanel);
		add(settingPanel, BorderLayout.CENTER);
	}
	
	public Dimension getPreferredSize() 
	{
		System.out.println("Default width is: " + DEFAULT_WIDTH);
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
}
