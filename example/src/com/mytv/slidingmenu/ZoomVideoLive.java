/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mytv.slidingmenu;

import com.mytv.utils.Global;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

public class ZoomVideoLive extends Activity implements
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
		OnVideoSizeChangedListener, SurfaceHolder.Callback {

	private static final String TAG = "MediaPlayerDemo";
	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private String path;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private boolean isPaused = false;
	
	private Bundle extras;
	
	public long length;
	
	private LinearLayout pause,showLive;
	private ImageView pauseBtn, replayBtn,zoomBtn,favoriteImgPress,favoriteImgNormal;
	private TextView songExit;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.zoom_video_live);
		extras = getIntent().getExtras();
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		pauseBtn = (ImageView) findViewById(R.id.pausebtn);
		songExit=(TextView)findViewById(R.id.songexit);
		replayBtn = (ImageView) findViewById(R.id.replaybtn);
		
		favoriteImgPress = (ImageView) findViewById(R.id.enjoybtn_press);
		favoriteImgNormal = (ImageView) findViewById(R.id.enjoybtn_normal);

		pause = (LinearLayout) findViewById(R.id.pauseLayout);
		showLive = (LinearLayout) findViewById(R.id.show_live);
		mPreview = (SurfaceView) findViewById(R.id.surface);
		
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888);
		songExit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//mMediaPlayer.release();
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
						ZoomVideoLive.this,
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

		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
		}
	}

	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
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

	public void onPrepared(MediaPlayer mediaplayer) {
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			//startVideoPlayback();
		}
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
		Log.d(TAG, "surfaceChanged called");

	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		Log.d(TAG, "surfaceDestroyed called");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated called");
		if (!isPaused) {
		playVideo(5);
		}else{
			try {
				mMediaPlayer.setDisplay(holder);
				mMediaPlayer.seekTo(length);
				mMediaPlayer.start();
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
		mMediaPlayer.stop();
		mMediaPlayer.release();
		//doCleanUp();
		//releaseMediaPlayer();
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
		mMediaPlayer.start();
	}
	public void show(View v) {

		/*if (Global.database.getChanel(getDangchieuChanel().getName()) != null) {
			favoriteImgPress.setVisibility(View.VISIBLE);
			favoriteImgNormal.setVisibility(View.GONE);
		} else {
			favoriteImgPress.setVisibility(View.GONE);
			favoriteImgNormal.setVisibility(View.VISIBLE);
		}*/

		pause.setVisibility(View.VISIBLE);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				//if (pauseItem == 0) {
					pause.setVisibility(View.GONE);
				//}
			}
		}, 4000);
	}

}
