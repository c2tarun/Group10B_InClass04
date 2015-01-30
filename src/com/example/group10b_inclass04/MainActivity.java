package com.example.group10b_inclass04;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	Handler handler;
	ExecutorService threadpool;

	ProgressDialog asyncProgress;
	ProgressDialog progressDialog;
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
						progress++;
						complexityValue.setText(progress + " " + getResources().getString(R.string.times));
					}
				});
		complexityValue.setText((complexityBar.getProgress() + 1) + " " + getResources().getString(R.string.times));
		asyncButton = (Button) findViewById(R.id.asyncButton);
		asyncButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int complexity = complexityBar.getProgress();
				complexity++;
				new DoWork().execute(complexity);
			}
		});

		threadpool = Executors.newFixedThreadPool(2);

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setTitle(R.string.progress_titles);

		findViewById(R.id.threadButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						progressDialog.setMax(complexityBar.getProgress() + 1);
						int complexity = complexityBar.getProgress();
						complexity++;
						threadpool.execute(new DoThreads(complexity));
					}
				});

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case DoThreads.STATUS_START:
					progressDialog.show();
					break;
				case DoThreads.STATUS_STEP:
					progressDialog.setProgress((Integer) msg.obj);
					break;
				case DoThreads.STATUS_DONE:
					Double value;
					value = msg.getData().getDouble(DoThreads.RESULT);
					resultView.setText(value + "");
					progressDialog.dismiss();
					break;

				default:
					break;
				}
				return false;
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
			asyncProgress.setMax(complexityBar.getProgress() + 1);
			asyncProgress.setCancelable(false);
			asyncProgress.setTitle(R.string.progress_titles);
			asyncProgress.show();
		}

		@Override
		protected Double doInBackground(Integer... params) {
			int counter = params[0];
			Log.d("demo", "Counter: " + counter);

			double number = 0.0d;

			for (int i = 0; i < counter; i++) {
				number += HeavyWork.getNumber();
				publishProgress(i + 1);
			}

			number = number / counter;
			return number;
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

	class DoThreads implements Runnable {
		int counter;
		Double number = 0.0d;
		Double result;
		public static final String RESULT = "result";
		static final int STATUS_START = 0;
		static final int STATUS_STEP = 1;
		static final int STATUS_DONE = 2;

		public DoThreads(int counter) {
			super();
			this.counter = counter;
		}

		@Override
		public void run() {
			Message msg = new Message();
			msg.what = STATUS_START;
			handler.sendMessage(msg);
			for (int i = 0; i < counter; i++) {
				number = number + HeavyWork.getNumber();
				msg = new Message();
				msg.what = STATUS_STEP;
				msg.obj = i + 1;
				handler.sendMessage(msg);
			}
			result = number / counter;
			msg = new Message();
			Bundle data = new Bundle();
			Log.d("demo", "Result calculated " + result);
			data.putDouble(RESULT, result);
			msg.setData(data);

			msg.what = STATUS_DONE;
			handler.sendMessage(msg);
		}

	}
}
