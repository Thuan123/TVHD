package com.mytv.slidingmenu;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaMetadataRetriever;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

public class PlayVideoActivity extends Activity implements
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
		OnVideoSizeChangedListener, SurfaceHolder.Callback {

	private LinearLayout pause;
	private TextView duration;
	private ImageView pauseBtn, replayBtn;

	private static final String TAG = "MediaPlayerDemo";
	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private Bundle extras;
	private String path;
	public long length;
	public String time;
	public int pauseItem = 0;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private SeekBar seekbar;
	public MediaMetadataRetriever retriever;
	public int pause_count = 0;
	public TextView editText;

	private long current = 0;
	private boolean running = true, isPaused = false;
	private int durations = 0;    
	
	private double timeElapsed = 0, finalTime = 0,timeDuration=0;;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.playvideo);
		pause = (LinearLayout) findViewById(R.id.pauseLayout);
		pauseBtn = (ImageView) findViewById(R.id.pausebtn);
		replayBtn = (ImageView) findViewById(R.id.replaybtn);
		
		seekbar = (SeekBar) findViewById(R.id.seekBar);
		seekbar.setMax((int) finalTime);
		seekbar.setClickable(false);
		duration = (TextView) findViewById(R.id.songDuration);
		editText = (TextView) findViewById(R.id.enjoytext);
		extras = getIntent().getExtras();
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mPreview = (SurfaceView) findViewById(R.id.surface);
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888);
		pauseBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pauseItem = 1;
				pause.setVisibility(View.VISIBLE);
				pauseBtn.setVisibility(View.GONE);
				replayBtn.setVisibility(View.VISIBLE);
				mMediaPlayer.pause();
				length = mMediaPlayer.getCurrentPosition();
			}
		});
		replayBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pauseItem = 0;
				pauseBtn.setVisibility(View.VISIBLE);
				replayBtn.setVisibility(View.GONE);
				mMediaPlayer.seekTo(length);
				mMediaPlayer.start();
				seekbar.postDelayed(updateSeekBarTime, 100);
				pause.setVisibility(View.GONE);
			}
		});
		editText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
	}

	private void playVideo(Integer Media) {
		doCleanUp();
		try {

			path = extras.getString("url");
			if (path == "") {
				Toast.makeText(
						PlayVideoActivity.this,
						"Please edit MediaPlayerDemo_Video Activity,"
								+ " and set the path variable to your media file URL.",
						Toast.LENGTH_LONG).show();
				return;
			}
			mMediaPlayer = new MediaPlayer(this);
			mMediaPlayer.setDataSource(path);
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepareAsync();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			timeDuration = mMediaPlayer.getDuration();

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
		}
	}

	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
		double ratio = percent / 100.0;
		int bufferingLevel = (int) (mMediaPlayer.getDuration() * ratio);
		seekbar.setSecondaryProgress(bufferingLevel);
	}

	public void onCompletion(MediaPlayer arg0) {
		Log.d(TAG, "onCompletion called");
	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged called");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height
					+ ")");
			return;
		}
		mIsVideoSizeKnown = true;
		DisplayMetrics displayMetric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetric);
		mVideoWidth = displayMetric.widthPixels;
		mVideoHeight = displayMetric.heightPixels;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mediaplayer) {
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			durations = (int) mMediaPlayer.getDuration();
			seekbar.setMax(durations);
			seekbar.postDelayed(updateSeekBarTime, 100);
			
		}

	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
		Log.d(TAG, "surfaceChanged called");
		Log.i("Change", "change");
	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		Log.d(TAG, "surfaceDestroyed called");
		Log.i("Destroy", "Destroy");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated called");
		Log.i("Created Video", "create");
		if (!isPaused) {
			playVideo(5);
		} else {
			try {
				pauseBtn.setVisibility(View.VISIBLE);
				replayBtn.setVisibility(View.GONE);
				mMediaPlayer.setDisplay(holder);
				mMediaPlayer.seekTo(length);
				mMediaPlayer.start();
				seekbar.postDelayed(updateSeekBarTime, 100);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.pause();
			isPaused = true;
		}
		length = mMediaPlayer.getCurrentPosition();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		seekbar.removeCallbacks(updateSeekBarTime);
	}

	private void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	private void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
		holder.setFixedSize(mVideoWidth, mVideoHeight);
		try {
			mMediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMediaPlayer.start();
		timeElapsed = mMediaPlayer.getCurrentPosition();
		seekbar.setProgress((int) timeElapsed);
		seekbar.postDelayed(updateSeekBarTime, 100);
		
		pause.setVisibility(View.GONE);
		pause_count = 1;
		
	}
	//handler to change seekBarTime
		private Runnable updateSeekBarTime = new Runnable() {
			@TargetApi(Build.VERSION_CODES.GINGERBREAD)
			public void run() {
				//get current position
				timeElapsed = mMediaPlayer.getCurrentPosition();
				//set seekbar progress
				seekbar.setProgress((int) timeElapsed);
				//set time remaing
				double timeRemaining = finalTime - timeElapsed;
				
				duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) -timeRemaining), -(TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)))));
				
				//repeat yourself that again in 100 miliseconds
				if(mMediaPlayer.getCurrentPosition()<mMediaPlayer.getDuration()){
				   seekbar.postDelayed(this, 100);
				}else{
					seekbar.setSecondaryProgress(0);
					seekbar.setProgress(0);
					pauseBtn.setVisibility(View.GONE);
					replayBtn.setVisibility(View.VISIBLE);
					mMediaPlayer.pause();
				    length=0;
				    pauseItem =1;
				    seekbar.removeCallbacks(updateSeekBarTime);
				}
			}
		};
		
	public void show(View v) {
		if (pause_count == 1) {
			pause.setVisibility(View.VISIBLE);
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					if (pauseItem == 0) {
						pause.setVisibility(View.GONE);
					}
				}
			}, 5000);
		}
	}
}
