package com.mytv.slidingmenu;

import com.mytv.adapter.ExpandableAdapter;
import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.LichChieu;
import com.mytv.utils.Global;
import android.app.ProgressDialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView;

public class NavigationFragment extends Fragment {
	protected ExpandableListView menuList;
	protected ExpandableAdapter exAdpt;
	protected ProgressDialog prgDialog;
	public static ImageLoader imageLoader;
	public String res;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frament_navigation_bar,
				container, false);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		menuList = (ExpandableListView) getActivity().findViewById(R.id.myex);
		imageLoader = new ImageLoader(getActivity().getApplicationContext());
		
		exAdpt = new ExpandableAdapter(Global.lich, getActivity()
				.getApplicationContext(),imageLoader);
		
		menuList.setAdapter(exAdpt);

		menuList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (Global.lich.get(groupPosition).getName()
						.equals("LichChieu")) {
					replaceMainFragment(new LichchieuFragment(Global.lich
							.get(groupPosition).getChanelList()
							.get(childPosition)));
				} else if (Global.lich.get(groupPosition).getName()
						.equals("Film")) {
					replaceMainFragment(new FilmFragment(
							Long.toString(Global.lich.get(groupPosition)
									.getChanelList().get(childPosition).getId()),getActivity(),null));
				}else if(Global.lich.get(groupPosition).getName()
						.equals("Sport")){
					replaceMainFragment(new FilmFragment(
							Long.toString(Global.lich.get(groupPosition)
									.getChanelList().get(childPosition).getId()),getActivity(),null));
				}else if(Global.lich.get(groupPosition).getName()
						.equals("TV Show")){
					replaceMainFragment(new FilmFragment(
							Long.toString(Global.lich.get(groupPosition)
									.getChanelList().get(childPosition).getId()),getActivity(),null));
				}else if(Global.lich.get(groupPosition).getName()
						.equals("Live Tivi")){
					LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
					fca.setVisiblePopup(1);
					replaceMainFragment(new LiveTVFragment(Global.lich.get(groupPosition)
							.getChanelList().get(childPosition),null));
				}
				return false;
			}
		});
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		imageLoader.clearCache();
	}

	private void replaceMainFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof LeftAndRightActivity) {
			LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
			// fca.getSlidingMenu().showContent();
			fca.replaceMainFragment(fragment,0);

		}
	}
}
