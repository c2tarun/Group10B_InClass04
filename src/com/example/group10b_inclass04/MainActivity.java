package com.example.group10b_inclass04;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
	TextView resultView;

	ProgressDialog asyncProgress;
	Button asyncButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		complexityValue = (TextView) findViewById(R.id.complexityValue);
		resultView = (TextView) findViewById(R.id.result);

		complexityBar = (SeekBar) findViewById(R.id.complexityBar);
		complexityBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						progress = (progress == 0) ? 1 : progress;
						complexityValue.setText(progress + "");
					}
				});
		asyncButton = (Button) findViewById(R.id.asyncButton);
		asyncButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int complexity = complexityBar.getProgress();
				complexity = (complexity == 0) ? 1 : complexity;
				new DoWork().execute(complexity);
			}
		});

	}

	class DoWork extends AsyncTask<Integer, Integer, Double> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			asyncProgress = new ProgressDialog(MainActivity.this);
			asyncProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			asyncProgress.setMax(100);
			asyncProgress.setCancelable(false);
			asyncProgress.show();
		}

		@Override
		protected Double doInBackground(Integer... params) {
			int counter = params[0];
			Log.d("demo", "Counter: " + counter);

			double number = 0.0d;

			for (int i = 0; i < counter; i++) {
				number += HeavyWork.getNumber();
				publishProgress(calculateProgressValue(i + 1, counter));
			}

			number = number / counter;
			return number;
		}

		public int calculateProgressValue(int i, int counter) {
			Double percent = (double) ((i * 100) / counter);
			return percent.intValue();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			Log.d("demo", "Progress Value: " + values[0]);
			asyncProgress.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Double result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			asyncProgress.dismiss();
			resultView.setText(result + "");
		}

	}

}
