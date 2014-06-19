package com.example.tipapp2;
//this project need a auto gen project call appcompat_v7
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
//import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
//import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
//import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
//http://courses.codepath.com/courses/intro_to_android/week/1#!assignment
public class MainActivity extends Activity
{
	//keep track of current tip percentage, default is 10
	public double defaultPercentage;
	public double currentPercentage;
	
	//TextView to show tip
	public TextView tvTipResult;
	public double tipResult;
	
	//EditText enter bill amount
	public EditText etBillAmount;
	public double billAmount;

	//EditText enter tip percentage
	public EditText etCustomTipPercentage;
	public double customTipPercent;

	//EditText enter how many people split
	public EditText etSplit;
	public double numOfPeopleSplit;

	//try to make EditText to change SeekBar too, but not work
	//logic tells me 2 call back method effect each other might be infinite loop
	public SeekBar sbCustomTipPercentage;
	
	//format variable tipResult
	DecimalFormat dollarCentFormat;
	
	//this RelativeLayout just use to change background from color green #00FF00 to an image
	public RelativeLayout rlTipApp;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		listeners();

		//this listener need the hit "enter" after change the value to fire up the event
		//SetOnEditorActionListener need to press enter to take action
		//      etBillAmount.setOnEditorActionListener(new OnEditorActionListener()
		//      {
		//         @Override
		//         public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		//         {
		//            if (event != null || actionId == EditorInfo.IME_ACTION_DONE)
		//            {
		//               tvTestListener.setText(etBillAmount.getText().toString());
		//               return true;
		//            }
		//            else
		//               return false;
		//         }
		//      });
	}

	//initialize must variables
	public void init()
	{
		defaultPercentage = 10;
		currentPercentage = defaultPercentage;
		
		tvTipResult = (TextView) findViewById(R.id.tvTipResult);
		tipResult = 0;
		
		etBillAmount = (EditText) findViewById(R.id.etBillAmount);
		billAmount = 0;
		
		etCustomTipPercentage = (EditText) findViewById(R.id.etCustomTipPercentage);
		customTipPercent = defaultPercentage;
		
		etSplit = (EditText) findViewById(R.id.etSplit);
		numOfPeopleSplit = 1;
		
		sbCustomTipPercentage = (SeekBar) findViewById(R.id.sbCustomTipPercentage);
		//set value, can set in XML android:progress="0"
		sbCustomTipPercentage.setProgress(0);
		//set max value, can set in XML android:max="10000"
		//set 10000 ticks and divide by 100 later in order to format 100.00 dollar and cent
		sbCustomTipPercentage.setMax(10000);
		
		/*
		formatting pattern
		0 	A digit - always displayed, even if number has less digits (then 0 is displayed)
		# 	A digit, leading zeroes are omitted.
		. 	Marks decimal separator
		, 	Marks grouping separator (e.g. thousand separator)
		E 	Marks separation of mantissa and exponent for exponential formats.
		; 	Separates formats
		- 	Marks the negative number prefix
		% 	Multiplies by 100 and shows number as percentage
		? 	Multiplies by 1000 and shows number as per mille
		¤ 	Currency sign - replaced by the currency sign for the Locale. Also makes formatting use the monetary decimal separator instead of normal decimal separator. ¤¤ makes formatting use international monetary symbols.
		X 	Marks a character to be used in number prefix or suffix
		' 	Marks a quote around special characters in prefix or suffix of formatted number.
		 */
		//argument in the DecimalFormat constructor is String pattern
		//dollarCentFormat = new DecimalFormat("$0.00");
		dollarCentFormat = new DecimalFormat("0.00");
		
		rlTipApp = (RelativeLayout) findViewById(R.id.rlTipApp);
		//change background from green to red or to image
		//rlTipApp.setBackgroundColor(Color.parseColor("#FF0000"));
		rlTipApp.setBackgroundResource(R.drawable.despicable_me_minions);
	}

	//listener for EditText and SeekBar whenever they change value, make change to tip result
	//https://github.com/thecodepath/android_guides/wiki/Basic-Event-Listeners
	public void listeners()
	{
		//listen to when user input bill amount, change tvTipResult
		//addTextChangedListener: Fires each time the text in the field is being changed
		etBillAmount.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				// Fires right as the text is being changed (even supplies the range of text)
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				// Fires right before text is changing
			}
			@Override
			public void afterTextChanged(Editable s)
			{
				// Fires right after the text has changed
				//if have bill, calculate tip, else 0 tip
				if(!isEmptyString(etBillAmount))
				{
					billAmount = Double.valueOf(etBillAmount.getText().toString());
					tipResult = billAmount * currentPercentage / 100 / numOfPeopleSplit;
					tvTipResult.setText(dollarCentFormat.format(tipResult));
				}
				else
				{
					tvTipResult.setText(dollarCentFormat.format(0));
				}
			}
		});

		//listen to when user input custom tip percentage, change tvTipResult
		etCustomTipPercentage.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}
			@Override
			public void afterTextChanged(Editable s)
			{
				//if has customTipPercent calculate tip, else calculate with defaultPercentage
				if(!isEmptyString(etCustomTipPercentage))
				{
					customTipPercent = Double.valueOf(etCustomTipPercentage.getText().toString());
					currentPercentage = customTipPercent;
					tipResult = billAmount * currentPercentage / 100 / numOfPeopleSplit;
					tvTipResult.setText(dollarCentFormat.format(tipResult));
				}
				else
				{
					currentPercentage = defaultPercentage;
					tipResult = billAmount * currentPercentage / 100 / numOfPeopleSplit;
					tvTipResult.setText(dollarCentFormat.format(tipResult));
				}
			}
		});

		//listen to when user input how many way to split, change tvTipResult
		etSplit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}
			@Override
			public void afterTextChanged(Editable s)
			{
				//if has split calculate tip, else set split to 1 [1 person pay bill] and calculate
				if(!isEmptyString(etSplit))
				{
					numOfPeopleSplit = Double.valueOf(etSplit.getText().toString());
					tipResult = billAmount * currentPercentage / 100 / numOfPeopleSplit;
					tvTipResult.setText(dollarCentFormat.format(tipResult));
				}
				else
				{
					numOfPeopleSplit = 1;
					tipResult = billAmount * currentPercentage / 100 / numOfPeopleSplit;
					tvTipResult.setText(dollarCentFormat.format(tipResult));
				}
			}
		});

		//listen to change in SeekBar, make change to etCustomTipPercentage, 
		//let etCustomTipPercentage's listener to calculate the tip
		sbCustomTipPercentage.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				//progress / 100 is use to show 2 digit after decimal point, 
				//in combination of MAX is 10000, 
				//so the whole SeekBar has 10000 tick format in 100.00 like dollar and cent
				etCustomTipPercentage.setText(Double.toString((double)progress / 100));
			}
		});
	}

	//check if empty with its length
	public boolean isEmptyString(EditText et)
	{
		return (et.length() == 0);
	}

	//change tvTipResult, call when 10, 15, 20 percent buttons clicked
	public void calculateTip()
	{
		if(!isEmptyString(etBillAmount))
		{
			tipResult = billAmount * currentPercentage / 100 / numOfPeopleSplit;
			tvTipResult.setText(dollarCentFormat.format(tipResult));
		}
		else
		{
			tvTipResult.setText(dollarCentFormat.format(0));
		}
	}

	//listen to 10 percent buttons clicked
	public void onClick10pc(View v)
	{
		currentPercentage = 10;
		etCustomTipPercentage.setText("10.00");
		calculateTip();
	}

	//listen to 15 percent buttons clicked
	public void onClick15pc(View v)
	{
		currentPercentage = 15;
		etCustomTipPercentage.setText("15.00");
		calculateTip();
	}

	//listen to 20 percent buttons clicked
	public void onClick20pc(View v)
	{
		currentPercentage = 20;
		etCustomTipPercentage.setText("20.00");
		calculateTip();
	}

	//save, write to file, can use commons-io-2.4.jar library like ToDoApp easier
	//http://www.java2s.com/Code/Android/Hardware/ExternalStorageDirectory.htm
	//http://www.tutorialspoint.com/java/io/inputstreamreader_read_char.htm
	public void onClickSaveTip(View v)
	{
		try
		{
			//open existing file or create new if not exist
			FileOutputStream fOut = openFileOutput("textfile.txt", MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			//write the string to the file
			osw.write(etCustomTipPercentage.getText().toString());
			osw.flush(); 
			osw.close();

			//display file saved message
			Toast.makeText(getBaseContext(), "File saved successfully!", Toast.LENGTH_SHORT).show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	//load, get access to a file, can use commons-io-2.4.jar library like ToDoApp easier
	//http://www.java2s.com/Code/Android/Hardware/ExternalStorageDirectory.htm
	//http://www.tutorialspoint.com/java/io/inputstreamreader_read_char.htm
	public void onClickLoadTip(View v)
	{
		try
		{
			FileInputStream fIn = openFileInput("textfile.txt");
			InputStreamReader isr = new InputStreamReader(fIn);
			
			char[] inputBuffer = new char[100];
			String s = "";

			int charRead;
			//read until end of file
			while ((charRead = isr.read(inputBuffer)) > 0)
			{
				//convert the chars to a String
				String readString = String.copyValueOf(inputBuffer, 0, charRead);
				s += readString;

				inputBuffer = new char[100];
			}
			//set the EditText to the text that has been read
			etCustomTipPercentage.setText(s);

			Toast.makeText(getBaseContext(), "File loaded successfully!", Toast.LENGTH_SHORT).show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}