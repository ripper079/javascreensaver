package danieloikarainen.se.screensaver.dialogs;

/*
 	Saves all information/setting about drawing
 */

public class CConfigSettings 
{
	
	private int mSizeRectangle;
	
	private DrawingOption mDrawOption;
	private ColorMode mColorMode;
	

	public CConfigSettings(int pSizeRectangle, DrawingOption pDrawOption, ColorMode pColorMode)
	{
		mSizeRectangle = pSizeRectangle;
		
		mDrawOption = pDrawOption;
		mColorMode = pColorMode;
	}
	
	public int getSizeRectangle() 
	{
		return mSizeRectangle;
	}
	
	public DrawingOption getDrawOptions() 
	{
		return mDrawOption;
	}
	
	public ColorMode getColorMode() 
	{
		return mColorMode;
	}


	
	//Overriding each enum value is necessary for "User friendly" text in comboBox
	public enum DrawingOption
	{		
		WHOLE_SCREEN
		{
			@Override
			public String toString()	{ return "Whole Screen"; }
		}, 
		ONE_ROW
		{
			@Override
			public String toString()	{ return "One Row"; }
		}, 
		MANY_ROWS
		{
			@Override
			public String toString() 	{ return "Many Rows"; }
		}, 
		ONE_BLOCK
		{
			@Override
			public String toString()	{ return "One Block"; }
		}, 
		MANY_BLOCK
		{
			@Override
			public String toString()	{ return "Many Blocks"; }
		};
	}
	
	public enum ColorMode
	{
		SOLID_COLOR
		{
			@Override
			public String toString()	{ return "Solid Color"; }
		}, 
		GRADIENT_PAINT
		{
			@Override
			public String toString()	{ return "Gradient Paint"; }
		};
	}
	
	public static void main(String[] args) 
	{
	}		
}




