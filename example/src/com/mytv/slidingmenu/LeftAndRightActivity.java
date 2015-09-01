package com.mytv.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mytv.database.DatabaseHandler;
import com.mytv.model.Chanel;
import com.mytv.model.LichChieu;
import com.mytv.model.Schedules;
import com.mytv.slidingmenu.R;
import com.mytv.utils.Global;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class LeftAndRightActivity extends BaseActivity implements
		OnClickListener {
	public ImageButton menuButton, favoriteButton;
	public LinearLayout menuSecon, videoFrame,topBar;
	public TextView popupTextView;
	public DrawerLayout drawerLayout;
	public static DrawerLayout videoLayout;
	public ProgressDialog prgDialog;
	public List<Schedules> searchList = new ArrayList<Schedules>();
	public EditText searchView;
	private int count_menu = 0;
	private int visiblePopup = 0;
	private int t = 0;
	public Schedules currentChanel;
	public int check = 0;
	android.app.Fragment currentFragment;

	public LichChieu getLichC() {
		return lichC;
	}

	public void setLichC(LichChieu lichC) {
		this.lichC = lichC;
	}

	public Chanel getLichChanel() {
		return lichChanel;
	}

	public void setLichChanel(Chanel lichChanel) {
		this.lichChanel = lichChanel;
	}

	private LichChieu lichC;
	private Chanel lichChanel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		setContentView(R.layout.content_frame);
		Global.database = new DatabaseHandler(this);
		Global.favoriteList=Global.database.getAllChanel();

		
		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.menu_frame_two,
						new FovariteListFragment(Global.favoriteList)).commit();

		if (!Global.lich.isEmpty()) {
			for (int i = 0; i < Global.lich.size(); i++) {
				if (Global.lich.get(i).getName().equals("Live Tivi")) {
					lichC = Global.lich.get(i);
					break;
				}
			}

			for (int j = 0; j < lichC.getChanelList().size(); j++) {
				if (lichC.getChanelList().get(j).getName().equals("HTV")) {
					lichChanel = lichC.getChanelList().get(j);
					break;
				}

			}
		}
		inCreate();

		setVisiblePopup(1);
		replaceMainFragment(new LiveTVFragment(lichChanel, null), 0);
        
		if (!Global.listTV.isEmpty()) {
			for (int i = 0; i < Global.listTV.size(); i++) {
				for (int j = 0; j < Global.listTV.get(i).getList().size(); j++) {
					if (CheckFavorite(Global.listTV.get(i).getList().get(j),
							searchList) == 0) {
						searchList.add(Global.listTV.get(i).getList().get(j));
						check=0;
					}else{
						check=0;
					}
				}
			}
		
		searchView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							currentChanel=getCurrentChanel(searchView.getText().toString());
							if(currentChanel!=null){
						    	for (int i = 0; i < Global.lich.size(); i++) {
									if (Global.lich.get(i).getName().equals("Live Tivi")) {
										for (int j = 0; j < Global.lich.get(i).getChanelList()
												.size(); j++) {
											if (Global.lich
													.get(i)
													.getChanelList()
													.get(j)
													.getName()
													.equals(currentChanel.getChanelType())) {
												
												replaceEpisodeFragment(new LiveTVFragment(
														Global.lich.get(i).getChanelList()
																.get(j),currentChanel.getName()));
												
												break;
											}
										}
									}
								}			
						    	
						    }
							InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
						    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
						
							return true;
						}

						return false;
					}
				});
		}
	}
	public Schedules getCurrentChanel(String name){
		Schedules currentChanel=null;
		for(int i=0;i<searchList.size();i++){
			if(searchList.get(i).getName().equals(name)){
				currentChanel=searchList.get(i);
				break;
			}
		}
		return currentChanel;
	}

	public void inCreate() {
		topBar=(LinearLayout)findViewById(R.id.topbar);
		videoFrame = (LinearLayout) findViewById(R.id.video_frame);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		videoLayout = (DrawerLayout) findViewById(R.id.videodrawer);
		favoriteButton = (ImageButton) findViewById(R.id.menu_star);
		menuButton = (ImageButton) findViewById(R.id.menu_button);
		searchView = (EditText) findViewById(R.id.menu_search);
		menuButton.setOnClickListener(this);
		favoriteButton.setOnClickListener(this);
		popupTextView = (TextView) findViewById(R.id.nguonkhac);

	}

	public int CheckFavorite(Schedules sche, List<Schedules> favoriteList) {
		for (int i = 0; i < favoriteList.size(); i++) {
			if ((favoriteList.get(i).getName().equals(sche.getName()))) {
				check = 1;
				break;
			}
		}
		if (check == 1) {
			return check;
		} else if (check == 0) {
			return check;
		}
		return check;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_button: {
			if (this.count_menu != 0) {
				menuButton.setImageResource(R.drawable.icon_menu);
				replaceMainFragment(
						new FilmFragment(
								Integer.toString(this.getCount_menu()), this,Global.filmList),
						0);
				this.count_menu = 0;
			} else {
				getSlidingMenu().showMenu();
			}
			break;
		}
		case R.id.menu_star: {
			getSlidingMenu().showSecondaryMenu();
			break;
		}
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void replaceMainFragment(Fragment fragment, int i) {
		this.count_menu = i;
		Log.i("giatri", Integer.toString(this.count_menu));
		if (getVisiblePopup() == 1) {
			popupTextView.setVisibility(View.VISIBLE);
			searchView.setVisibility(View.GONE);
		} else {
			popupTextView.setVisibility(View.GONE);
			searchView.setVisibility(View.VISIBLE);
		}
		setVisiblePopup(0);
		if (this.count_menu != 0) {
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			getSlidingMenu().showContent();
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction tx = fm.beginTransaction();
			tx.replace(R.id.main_container, fragment);
			tx.commit();

		} else if (this.count_menu == 0) {
			getSlidingMenu()
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			getSlidingMenu().showContent();

			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction tx = fm.beginTransaction();
			tx.replace(R.id.main_container, fragment);
			tx.commit();

		}
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public int getCount_menu() {
		return count_menu;
	}

	public void setCount_menu(int count_menu) {
		this.count_menu = count_menu;
	}

	public int getVisiblePopup() {
		return visiblePopup;
	}

	public void setVisiblePopup(int visiblePopup) {
		this.visiblePopup = visiblePopup;
	}

	public void replaceVideoFragment(Fragment fragment) {
		
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		getSlidingMenu().showContent();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		tx.replace(R.id.video_container, fragment);
		tx.commit();
		videoFrame.setVisibility(View.GONE);
		videoLayout.setVisibility(View.VISIBLE);
	}

	public void replaceEpisodeFragment(Fragment fragment) {
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		getSlidingMenu().showContent();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		tx.replace(R.id.main_container, fragment);
		tx.commit();
		videoFrame.setVisibility(View.VISIBLE);
		videoLayout.setVisibility(View.GONE);
	}

	public void replaceBackFragment() {
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		getSlidingMenu().showContent();
		videoFrame.setVisibility(View.VISIBLE);
		videoLayout.setVisibility(View.GONE);
	}

	public void hideSoftKeyboard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
