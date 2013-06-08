package com.noisepages.nettoyeur.bitcrusher;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;

public class MainActivity extends Activity implements OnCheckedChangeListener, OnSeekBarChangeListener {

	private OpenSlBitCrusher bitCrusher;
	private SeekBar crushBar;
	private Switch playSwitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		crushBar = (SeekBar) findViewById(R.id.crushBar);
		crushBar.setOnSeekBarChangeListener(this);
		playSwitch = (Switch) findViewById(R.id.playSwitch);
		playSwitch.setOnCheckedChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int sr = Integer.parseInt(am.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE));
		int bs = Integer.parseInt(am.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER));
		try {
			bitCrusher = new OpenSlBitCrusher(sr, bs);
			setCrush(crushBar.getProgress());
			if (playSwitch.isChecked()) {
				bitCrusher.start();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		bitCrusher.close();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			try {
				bitCrusher.start();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			bitCrusher.stop();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		setCrush(progress);
	}

	private void setCrush(int depth) {
		bitCrusher.crush(depth * 16 / 101);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// Do nothing.
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// Do nothing.
	}
}
