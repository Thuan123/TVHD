package com.mytv.slidingmenu;

import java.util.ArrayList;
import java.util.List;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import com.mytv.adapter.FilmEpisodeAdapter;
import com.mytv.adapter.ImageEpisodePageAdapter;
import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Episode;
import com.mytv.utils.Global;
import com.nirhart.parallaxscroll.views.ParallaxListView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class FilmEpisodeFragment extends Fragment {
	private long position;
	private String idFilm;
	private List<Episode> Epi, viewpagerEpi;
	public int width;
	public int height;
     
	public ParallaxListView listView;
	public FilmEpisodeAdapter adapter;
	
	private static ImageLoader imageLoader;

	public FilmEpisodeFragment(long position, String idFilm, List<Episode> Epi) {
		super();
		this.setPosition(position);
		this.setIdFilm(idFilm);
		this.setEpi(Epi);
	}
    
	
	public FilmEpisodeFragment() {
		super();
	}


	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.film_frament, null);
	}

	@SuppressLint("InflateParams")
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		imageLoader = new ImageLoader(getActivity().getApplicationContext());
		
		listView = (ParallaxListView) getActivity()
				.findViewById(R.id.list_view);
		viewpagerEpi = new ArrayList<Episode>();
		DisplayMetrics displayMetric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetric);
		width = displayMetric.widthPixels;
		height = displayMetric.heightPixels;
		for (int i = 0; i < getEpi().size(); i++) {
			if (getEpi().get(i).getLogoUrl() != null) {
				viewpagerEpi.add(getEpi().get(i));
				break;
			}
		}
		adapter = new FilmEpisodeAdapter(LayoutInflater.from(getActivity()
				.getApplicationContext()), getActivity(), width, height,
				getEpi(),imageLoader);
		AutoScrollViewPager v = (AutoScrollViewPager) getActivity()
				.getLayoutInflater().inflate(R.layout.imageheader, null);

		v.setAdapter(new ImageEpisodePageAdapter(getActivity(), getActivity()
				.getApplicationContext(), /*this.getEpi()*/viewpagerEpi));

		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				width, (height / 3));

		v.setLayoutParams(layoutParams);

		v.setInterval(4000);
		// v.startAutoScroll();

		v.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
				% Global.filmList.size());
		listView.addParallaxedHeaderView(v);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
			    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);	
				String URL_FILM = "";
				Log.i("URL_FILM", Epi.get(arg2 - 1).getVideoList().get(0)
						.getVideoUrl());
				for (int i = 0; i < Epi.get(arg2 - 1).getVideoList().size(); i++) {
					Log.i("url", Epi.get(arg2 - 1).getVideoList().get(i)
							.getVideoUrl());
					if (Epi.get(arg2 - 1).getVideoList().get(i).getVideoUrl() != null) {
						URL_FILM = Epi.get(arg2 - 1).getVideoList().get(i)
								.getVideoUrl();
						break;
					}
				}
				
				//Intent i = new Intent(getActivity(),com.mytv.slidingmenu.PlayVideoActivity.class);
				Intent i = new Intent(getActivity(),com.mytv.slidingmenu.XemPhimActivity.class);
				i.putExtra("url", URL_FILM);
				startActivity(i);
				
			}
		});
	}
	
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	imageLoader.clearCache();
    }

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public String getIdFilm() {
		return idFilm;
	}

	public void setIdFilm(String idFilm) {
		this.idFilm = idFilm;
	}

	@SuppressWarnings("unused")
	private void replaceMainFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof LeftAndRightActivity) {
			LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
			fca.menuButton.setImageResource(R.drawable.icon_menu);
			fca.replaceMainFragment(fragment, 0);
		}
	}
	private void replaceVideoFragment(Fragment f){
		if (getActivity() == null)
			return;
		if (getActivity() instanceof LeftAndRightActivity) {
			LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
			fca.replaceVideoFragment(f);
		}
	}

	public List<Episode> getEpi() {
		return Epi;
	}

	public void setEpi(List<Episode> epi) {
		Epi = epi;
	}
}
