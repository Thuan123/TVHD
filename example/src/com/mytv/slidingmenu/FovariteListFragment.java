package com.mytv.slidingmenu;

import java.util.List;

import com.mytv.adapter.FavoriteListAdapter;
import com.mytv.database.DatabaseHandler;
import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Schedules;
import com.mytv.slidingmenu.R;
import com.mytv.utils.Global;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FovariteListFragment extends Fragment {
	public static ListView favoriteList;
	public static LinearLayout themkenhLayout;
	public static ImageButton deleteButton;
	public static FavoriteListAdapter favoriteAdapter;
	private static List<Schedules> favoriteArray;
	public static ImageLoader imageLoader;
	
	public FovariteListFragment(List<Schedules> favoriteArray) {
		super();
		FovariteListFragment.favoriteArray = favoriteArray;
	}

	public FovariteListFragment() {
		setFavoriteArray(Global.favoriteList);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fovarite_navigation_bar, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		imageLoader = new ImageLoader(getActivity().getApplicationContext());
		Global.database = new DatabaseHandler(getActivity());
		favoriteList = (ListView) getActivity()
				.findViewById(R.id.favorite_list);
		deleteButton = (ImageButton) getActivity()
				.findViewById(R.id.deleteFbtn);
		themkenhLayout=(LinearLayout)getActivity().findViewById(R.id.themkenh_layout);

		if (!getFavoriteArray().isEmpty()) {
			
			favoriteAdapter = new FavoriteListAdapter(getActivity(),
					getFavoriteArray(),imageLoader);
			favoriteList.setAdapter(favoriteAdapter);
			
		}
		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for (int i = 0; i < getFavoriteArray().size(); i++) {
					//if (getFavoriteArray().get(i).getiDelete() == 1) {
						Global.database.deleteToDo(getFavoriteArray().get(i));
					//}
				}
				getFavoriteArray().removeAll(favoriteArray);
				setFavoriteArray(Global.database.getAllChanel());
				Global.favoriteList.removeAll(Global.favoriteList);
				Global.favoriteList = Global.database.getAllChanel();
				favoriteAdapter = new FavoriteListAdapter(getActivity(),
						getFavoriteArray(),imageLoader);
				favoriteList.setAdapter(favoriteAdapter);
			}
		});
		favoriteList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				for (int i = 0; i < Global.lich.size(); i++) {
					if (Global.lich.get(i).getName().equals("Live Tivi")) {
						for (int j = 0; j < Global.lich.get(i).getChanelList()
								.size(); j++) {
							if (Global.lich
									.get(i)
									.getChanelList()
									.get(j)
									.getName()
									.equals(Global.favoriteList.get(arg2)
											.getChanelType())) {
								
								LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
								fca.setVisiblePopup(1);
								
								replaceMainFragment(new LiveTVFragment(
										Global.lich.get(i).getChanelList()
												.get(j),Global.favoriteList.get(arg2).getName()));
								
								break;
							}
						}
					}
				}				
			}
		});
		
		themkenhLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				replaceVideoFragment(new ChonkenhFragment());
			}
		});

	}

	public static List<Schedules> getFavoriteArray() {
		return FovariteListFragment.favoriteArray;
	}

	public static void setFavoriteArray(List<Schedules> favoriteArray) {
		FovariteListFragment.favoriteArray = favoriteArray;
	}
	private void replaceVideoFragment(Fragment f){
		if (getActivity() == null)
			return;
		if (getActivity() instanceof LeftAndRightActivity) {
			LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
			fca.replaceVideoFragment(f);
		}
	}
	private void replaceMainFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof LeftAndRightActivity) {
			LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
			fca.menuButton.setImageResource(R.drawable.icon_menu);
			fca.replaceMainFragment(fragment, 0);

		}
	}
}
