package com.example.group10b_inclass04;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Group10B_InClass04
 *
 * Tarun Kumar Mall
 * Jeremy Cass
 * Pragya Rai
 *
 * MainActivity.java
 */
public class MainActivity extends Activity {
	
	SeekBar complexityBar;
	TextView complexityValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		complexityValue = (TextView) findViewById(R.id.complexityValue);
		
		complexityBar = (SeekBar) findViewById(R.id.complexityBar);
		complexityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				complexityValue.setText(progress + "");
			}
		});
	}
}
