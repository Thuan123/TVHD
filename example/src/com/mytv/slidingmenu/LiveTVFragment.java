package com.mytv.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mytv.adapter.DangchieuListAdapter;
import com.mytv.adapter.FancyCoverFlowSampleAdapter;
import com.mytv.adapter.FavoriteListAdapter;
import com.mytv.adapter.NguonKhacItemAdapter;
import com.mytv.database.DatabaseHandler;
import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Chanel;
import com.mytv.model.LichChieu;
import com.mytv.model.Link;
import com.mytv.model.Schedules;
import com.mytv.utils.Global;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LiveTVFragment extends Fragment implements Callback,
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
		OnVideoSizeChangedListener, OnErrorListener {

	private LinearLayout pause, showLive, videoLayout, mainLayout;
	private ImageView pauseBtn, replayBtn, zoomBtn;
	private TextView zoomsmallBtn;

	private static final String TAG = "MediaPlayerDemo";
	private int mVideoWidth;
	private int mVideoHeight;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private String path;
	private ListView listNguon;
	private List<Link> listNguonKhac = new ArrayList<Link>();
	public LichChieu dangChieu;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int pauseItem = 0;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	public long length;
	private Chanel liveChanel;
	private Schedules dangchieuChanel;
	private TextView nameUserAppeal;
	private ImageView imgUserAppeal, favoriteImgPress, favoriteImgNormal;
	private ListView dangchieuList;

	private LinearLayout horizontalLayout, liveLayout;
	private int temp = 0;
	private String currentChanel;
	public static ImageLoader imageLoader;
	private FancyCoverFlow fancyCoverFlow;
	private ProgressDialog progressDialog;

	public LiveTVFragment(Chanel chanel, String currentChanel) {
		super();
		this.setLiveChanel(chanel);
		this.setCurrentChanel(currentChanel);
		if (getCurrentChanel() == null) {

			for (int i = 0; i < getLiveChanel().getList().size(); i++) {
				for (int j = 0; j < getLiveChanel().getList().get(i)
						.getLinkList().size(); j++) {
					if (getLiveChanel().getList().get(i).getLinkList().get(j)
							.getUrl() != null) {

						setDangchieuChanel(getLiveChanel().getList().get(i));

						setPath(getLiveChanel().getList().get(i).getLinkList()
								.get(j).getUrl().toString());

						listNguonKhac.addAll(getLiveChanel().getList().get(i)
								.getLinkList());

						temp = 1;
						break;
					}
				}
				if (temp == 1) {

					break;
				}
			}
			temp = 0;
		} else if (getCurrentChanel() != null) {
			for (int i = 0; i < getLiveChanel().getList().size(); i++) {
				for (int j = 0; j < getLiveChanel().getList().get(i)
						.getLinkList().size(); j++) {
					if (getLiveChanel().getList().get(i).getName()
							.equals(getCurrentChanel())) {

						setDangchieuChanel(getLiveChanel().getList().get(i));

						setPath(getLiveChanel().getList().get(i).getLinkList()
								.get(j).getUrl().toString());
						listNguonKhac.addAll(getLiveChanel().getList().get(i)
								.getLinkList());

						temp = 1;
						break;
					}
				}
				if (temp == 1) {

					break;
				}
			}
			temp = 0;
		}

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.livetv_frame, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!LibsChecker.checkVitamioLibs(getActivity())) {
			return;
		}
        
		progressDialog = ProgressDialog.show(getActivity(), "",
				"Loading .....", true, false);
		progressDialog.getWindow().setGravity(Gravity.TOP);
		WindowManager.LayoutParams wmlp = progressDialog.getWindow().getAttributes();
		DisplayMetrics displayMetric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetric);
		int height = displayMetric.heightPixels;
		wmlp.y = height / 6;
		progressDialog.getWindow().setAttributes(wmlp);
		
		
		imageLoader = new ImageLoader(getActivity().getApplicationContext());
		Global.database = new DatabaseHandler(getActivity());
		LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();

		pauseBtn = (ImageView) getActivity().findViewById(R.id.pausebtn);
		replayBtn = (ImageView) getActivity().findViewById(R.id.replaybtn);
		zoomBtn = (ImageView) getActivity().findViewById(R.id.bigbtn);
		fancyCoverFlow = (FancyCoverFlow) getActivity().findViewById(
				R.id.fancyCoverFlow);
		fancyCoverFlow.setReflectionEnabled(true);
		fancyCoverFlow.setReflectionRatio(0.1f);
		fancyCoverFlow.setReflectionGap(0);

		dangchieuList = (ListView) getActivity().findViewById(
				R.id.list_dang_chieu);
		favoriteImgPress = (ImageView) getActivity().findViewById(
				R.id.enjoybtn_press);
		favoriteImgNormal = (ImageView) getActivity().findViewById(
				R.id.enjoybtn_normal);
		zoomsmallBtn = (TextView) getActivity().findViewById(R.id.smallbtn);

		mPreview = (SurfaceView) getActivity().findViewById(R.id.surface);
		pause = (LinearLayout) getActivity().findViewById(R.id.pauseLayout);
		showLive = (LinearLayout) getActivity().findViewById(R.id.show_live);

		videoLayout = (LinearLayout) getActivity()
				.findViewById(R.id.layoutview);
		mainLayout = (LinearLayout) getActivity().findViewById(R.id.liveframe);
		liveLayout = (LinearLayout) getActivity().findViewById(R.id.liveframe);

		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.RGBA_8888);
		loadAppeal();
		getListDangChieu();
		if (Global.database.getChanel(getDangchieuChanel().getName()) != null) {
			favoriteImgPress.setVisibility(View.VISIBLE);
			favoriteImgNormal.setVisibility(View.GONE);
		} else {
			favoriteImgPress.setVisibility(View.GONE);
			favoriteImgNormal.setVisibility(View.VISIBLE);
		}

		showLive.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				show();
			}
		});
		pauseBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mMediaPlayer.isPlaying() && mMediaPlayer != null) {
					pauseItem = 1;
					pause.setVisibility(View.VISIBLE);
					pauseBtn.setVisibility(View.GONE);
					replayBtn.setVisibility(View.VISIBLE);
					mMediaPlayer.pause();
					length = mMediaPlayer.getCurrentPosition();
				}
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
				pause.setVisibility(View.GONE);
			}
		});
		fca.popupTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(getActivity(),
						android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				dialog.setContentView(R.layout.dialog_nguonkhac);
				DisplayMetrics displayMetric = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay()
						.getMetrics(displayMetric);
				int width = (2 * displayMetric.widthPixels) / 3
						+ displayMetric.widthPixels / 6;
				int height = (2 * displayMetric.heightPixels) / 3;
				dialog.show();
				dialog.getWindow().setLayout(width, height);
				listNguon = (ListView) dialog.findViewById(R.id.list_nguon);
				NguonKhacItemAdapter itemAdapter = new NguonKhacItemAdapter(
						getActivity(), listNguonKhac);
				listNguon.setAdapter(itemAdapter);
				listNguon.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							int arg2, long arg3) {
						progressDialog = ProgressDialog.show(getActivity(), "",
								"Loading .....", true, false);
						progressDialog.getWindow().setGravity(Gravity.TOP);
						WindowManager.LayoutParams wmlp = progressDialog.getWindow().getAttributes();
						DisplayMetrics displayMetric = new DisplayMetrics();
						getActivity().getWindowManager().getDefaultDisplay()
								.getMetrics(displayMetric);
						int height = displayMetric.heightPixels;
						wmlp.y = height / 6;
						progressDialog.getWindow().setAttributes(wmlp);
						pauseBtn.setVisibility(View.VISIBLE);
						replayBtn.setVisibility(View.GONE);
						favoriteImgPress.setVisibility(View.GONE);
						favoriteImgNormal.setVisibility(View.VISIBLE);
						pause.setVisibility(View.GONE);
						setPath(listNguonKhac.get(arg2).getUrl());
						mMediaPlayer.stop();
						releaseMediaPlayer();
						playVideo();
						//DisplayMetrics displayMetric = new DisplayMetrics();
						getActivity().getWindowManager().getDefaultDisplay()
								.getMetrics(displayMetric);
						mVideoWidth = displayMetric.widthPixels;
						mVideoHeight = displayMetric.heightPixels / 3;
						startVideoPlayback();
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});

				liveLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (dialog.isShowing()) {
							dialog.cancel();
						}
					}
				});

			}
		});
		favoriteImgNormal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Global.database.getChanel(getDangchieuChanel().getName()) == null) {
					Global.favoriteList.add(getDangchieuChanel());
				}
				Global.database.createAccount(getDangchieuChanel());
				FovariteListFragment.setFavoriteArray(Global.favoriteList);
				FovariteListFragment.favoriteAdapter = new FavoriteListAdapter(
						getActivity(), FovariteListFragment.getFavoriteArray(),
						imageLoader);
				FovariteListFragment.favoriteList
						.setAdapter(FovariteListFragment.favoriteAdapter);
				favoriteImgPress.setVisibility(View.VISIBLE);
				favoriteImgNormal.setVisibility(View.GONE);
			}
		});
		zoomBtn.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {

				zoomBtn.setVisibility(View.GONE);
				zoomsmallBtn.setVisibility(View.VISIBLE);

				final LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
				fca.topBar.setVisibility(View.GONE);
				fca.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_NONE);
				fca.getSlidingMenu().showContent();
				getActivity().setRequestedOrientation(
						ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
				fca.drawerLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT));
				videoLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT));

			}
		});
		zoomsmallBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				zoomBtn.setVisibility(View.VISIBLE);
				zoomsmallBtn.setVisibility(View.GONE);

				LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
				fca.topBar.setVisibility(View.VISIBLE);
				fca.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
				fca.getSlidingMenu().showContent();
				fca.drawerLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT, 0, 1.8f));
				videoLayout.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT, 0, 1.3f));

				getActivity().setRequestedOrientation(
						ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
			}
		});

	}

	private void loadAppeal() {

		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		DisplayMetrics displayMetric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetric);
		int width = displayMetric.widthPixels;

		fancyCoverFlow.setAdapter(new FancyCoverFlowSampleAdapter(inflater,
				getActivity(), width, 0, this.liveChanel, imageLoader));
		fancyCoverFlow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				progressDialog = ProgressDialog.show(getActivity(), "",
						"Loading .....", true, false);
				progressDialog.getWindow().setGravity(Gravity.TOP);
				WindowManager.LayoutParams wmlp = progressDialog.getWindow().getAttributes();
				DisplayMetrics displayMetric = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay()
						.getMetrics(displayMetric);
				int height = displayMetric.heightPixels;
				wmlp.y = height / 6;
				progressDialog.getWindow().setAttributes(wmlp);
				
				pauseBtn.setVisibility(View.VISIBLE);
				replayBtn.setVisibility(View.GONE);
				favoriteImgPress.setVisibility(View.GONE);
				favoriteImgNormal.setVisibility(View.VISIBLE);
				pause.setVisibility(View.GONE);
				listNguonKhac = new ArrayList<Link>();
				listNguonKhac.addAll(getLiveChanel().getList().get(arg2)
						.getLinkList());
				Log.i("LiveType", getLiveChanel().getList().get((Integer) arg2)
						.getChanelType());
				setDangchieuChanel(getLiveChanel().getList().get(arg2));
				if (Global.database.getChanel(getDangchieuChanel().getName()) != null) {
					favoriteImgPress.setVisibility(View.VISIBLE);
					favoriteImgNormal.setVisibility(View.GONE);
				} else {
					favoriteImgPress.setVisibility(View.GONE);
					favoriteImgNormal.setVisibility(View.VISIBLE);
				}
				if (listNguonKhac.size() != 0) {
					for (int i = 0; i < listNguonKhac.size(); i++) {
						if (listNguonKhac.get(i).getUrl() != null) {
							setPath(listNguonKhac.get(i).getUrl());
							mMediaPlayer.stop();
							releaseMediaPlayer();
							playVideo();
							//DisplayMetrics displayMetric = new DisplayMetrics();
							getActivity().getWindowManager()
									.getDefaultDisplay()
									.getMetrics(displayMetric);
							mVideoWidth = displayMetric.widthPixels;
							mVideoHeight = displayMetric.heightPixels / 3;
							startVideoPlayback();
							break;
						}
					}
				}
			}

		});
	}

	private void getListDangChieu() {

		List<Schedules> chanelDangChieu = new ArrayList<Schedules>();
		for (int i = 0; i < Global.lich.size(); i++) {
			if (Global.lich.get(i).getName().equals("LichChieu")) {
				dangChieu = Global.lich.get(i);
				break;
			}
		}

		for (int j = 0; j < dangChieu.getChanelList().size(); j++) {

			for (int k = 0; k < dangChieu.getChanelList().get(j).getList()
					.size(); k++) {
				if (Global.cutString(dangChieu.getChanelList().get(j).getList()
						.get(k).getTime()) == Global.cutString(Global
						.getTimeHour())) {
					chanelDangChieu.add(new Schedules(dangChieu.getChanelList()
							.get(j).getName(), dangChieu.getChanelList().get(j)
							.getList().get(k).getTitles()));
				}
			}
		}
		dangchieuList.setAdapter(new DangchieuListAdapter(getActivity(),
				chanelDangChieu));
	}

	private void playVideo() {
		doCleanUp();
		try {
			if (path == "") {
				Toast.makeText(
						getActivity(),
						"Please edit MediaPlayerDemo_Video Activity,"
								+ " and set the path variable to your media file URL.",
						Toast.LENGTH_LONG).show();
				return;
			}
			mMediaPlayer = new MediaPlayer(getActivity());
			mMediaPlayer.setDataSource(path);
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepareAsync();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			mMediaPlayer.setOnErrorListener(this);
			getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

		} catch (Exception e) {
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		playVideo();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		releaseMediaPlayer();
		doCleanUp();
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// TODO Auto-generated method stub
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height
					+ ")");
			return;
		}
		mIsVideoSizeKnown = true;
		DisplayMetrics displayMetric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetric);
		mVideoWidth = displayMetric.widthPixels;
		mVideoHeight = displayMetric.heightPixels / 3;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			 if (progressDialog != null && progressDialog.isShowing()){
                 progressDialog.dismiss();
              }
			startVideoPlayback();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub

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
		// pause.setVisibility(View.GONE);
		holder.setFixedSize(mVideoWidth, mVideoHeight);
		mMediaPlayer.start();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				pause.setVisibility(View.GONE);
			}
		}, 800);

	}

	public void show() {

		if (Global.database.getChanel(getDangchieuChanel().getName()) != null) {
			favoriteImgPress.setVisibility(View.VISIBLE);
			favoriteImgNormal.setVisibility(View.GONE);
		} else {
			favoriteImgPress.setVisibility(View.GONE);
			favoriteImgNormal.setVisibility(View.VISIBLE);
		}

		pause.setVisibility(View.VISIBLE);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				if (pauseItem == 0) {
					pause.setVisibility(View.GONE);
				}
			}
		}, 4000);
	}

	public Chanel getLiveChanel() {
		return liveChanel;
	}

	public void setLiveChanel(Chanel liveChanel) {
		this.liveChanel = liveChanel;
	}

	public Schedules getDangchieuChanel() {
		return dangchieuChanel;
	}

	public void setDangchieuChanel(Schedules dangchieuChanel) {
		this.dangchieuChanel = dangchieuChanel;
	}

	public String getCurrentChanel() {
		return currentChanel;
	}

	public void setCurrentChanel(String currentChanel) {
		this.currentChanel = currentChanel;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (imageLoader != null) {
			imageLoader.clearCache();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		 if (progressDialog != null && progressDialog.isShowing()){
             progressDialog.dismiss();
          }
		Toast.makeText(getActivity(), "Error please choose other chanel",
				Toast.LENGTH_LONG).show();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				final Dialog dialog = new Dialog(getActivity(),
						android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				dialog.setContentView(R.layout.dialog_nguonkhac);
				DisplayMetrics displayMetric = new DisplayMetrics();
				getActivity().getWindowManager().getDefaultDisplay()
						.getMetrics(displayMetric);
				int width = (2 * displayMetric.widthPixels) / 3
						+ displayMetric.widthPixels / 6;
				int height = (2 * displayMetric.heightPixels) / 3;
				dialog.show();
				dialog.getWindow().setLayout(width, height);
				listNguon = (ListView) dialog.findViewById(R.id.list_nguon);
				NguonKhacItemAdapter itemAdapter = new NguonKhacItemAdapter(
						getActivity(), listNguonKhac);
				listNguon.setAdapter(itemAdapter);
				listNguon.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							int arg2, long arg3) {
						progressDialog = ProgressDialog.show(getActivity(), "",
								"Loading .....", true, false);
						progressDialog.getWindow().setGravity(Gravity.TOP);
						WindowManager.LayoutParams wmlp = progressDialog.getWindow().getAttributes();
						DisplayMetrics displayMetric = new DisplayMetrics();
						getActivity().getWindowManager().getDefaultDisplay()
								.getMetrics(displayMetric);
						int height = displayMetric.heightPixels;
						wmlp.y = height / 6;
						progressDialog.getWindow().setAttributes(wmlp);
						pauseBtn.setVisibility(View.VISIBLE);
						replayBtn.setVisibility(View.GONE);
						favoriteImgPress.setVisibility(View.GONE);
						favoriteImgNormal.setVisibility(View.VISIBLE);
						pause.setVisibility(View.GONE);
						setPath(listNguonKhac.get(arg2).getUrl());
						mMediaPlayer.stop();
						releaseMediaPlayer();
						playVideo();
						//DisplayMetrics displayMetric = new DisplayMetrics();
						getActivity().getWindowManager().getDefaultDisplay()
								.getMetrics(displayMetric);
						mVideoWidth = displayMetric.widthPixels;
						mVideoHeight = displayMetric.heightPixels / 3;
						startVideoPlayback();
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
					}
				});

				liveLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (dialog.isShowing()) {
							dialog.cancel();
						}
					}
				});
			}
		}, 800);

		return false;
	}

}
