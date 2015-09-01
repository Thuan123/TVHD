package com.mytv.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import com.mytv.adapter.ChonkenhGridViewAdapter;
import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Schedules;
import com.mytv.utils.Global;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ChonkenhFragment extends Fragment{
	
	public GridView chonkenhGridView;
	public EditText editSearch;
	public Button SearchBtn,playBtn,backBtn;
	public List<Schedules> listSearch;
	public Schedules currentChanel;
	public ChonkenhGridViewAdapter chonkenhAdapter;
	public List<Schedules> chonkenhList = new ArrayList<Schedules>();
	public int check = 0;
	public ImageLoader imageLoader;
	
	
	public ChonkenhFragment() {
		super();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.chonkenh_layout, null);
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(imageLoader!=null){
		  imageLoader.clearCache();
		}
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SearchBtn=(Button)getActivity().findViewById(R.id.search_btn);
		editSearch=(EditText)getActivity().findViewById(R.id.search_text);
		chonkenhGridView = (GridView)getActivity().findViewById(R.id.kenh_id);
		playBtn=(Button)getActivity().findViewById(R.id.hoanthanh);
		backBtn=(Button)getActivity().findViewById(R.id.backbtn);
		imageLoader = new ImageLoader(getActivity().getApplicationContext());
		if (!Global.listTV.isEmpty()) {
			for (int i = 0; i < Global.listTV.size(); i++) {
				for (int j = 0; j < Global.listTV.get(i).getList().size(); j++) {
					if (CheckFavorite(Global.listTV.get(i).getList().get(j),
							chonkenhList) == 0) {
						chonkenhList.add(Global.listTV.get(i).getList().get(j));
						check=0;
					}else{
						check=0;
					}
				}
			}

			chonkenhAdapter = new ChonkenhGridViewAdapter(getActivity(), chonkenhList,imageLoader);
			chonkenhGridView.setAdapter(chonkenhAdapter);
			editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					     currentChanel=getCurrentChanel(editSearch.getText().toString());
				
						if(currentChanel==null){
							Toast.makeText(getActivity(), "No channels found", Toast.LENGTH_LONG).show();
						}else{
							listSearch=new ArrayList<Schedules>();
		                    listSearch.add(currentChanel);
		                    chonkenhAdapter = new ChonkenhGridViewAdapter(getActivity(),listSearch,imageLoader);
		        			chonkenhGridView.setAdapter(chonkenhAdapter);
						}
						InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
					    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);	
						return true;
					}

					return false;
				}
			 });
			/*SearchBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(editSearch.getText().toString()!=null){
					
					 currentChanel=getCurrentChanel(editSearch.getText().toString());
						
						if(currentChanel==null){
							InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
						    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
							Toast.makeText(getActivity(), "No channels found", Toast.LENGTH_LONG).show();
						}else{
							InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
						    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
							listSearch=new ArrayList<Schedules>();
		                    listSearch.add(currentChanel);
		                    chonkenhAdapter = new ChonkenhGridViewAdapter(getActivity(),listSearch,imageLoader);
		        			chonkenhGridView.setAdapter(chonkenhAdapter);
						}
				    }
				}
			});*/
			playBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
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
										
										replaceEpiFragment(new LiveTVFragment(
												Global.lich.get(i).getChanelList()
														.get(j),currentChanel.getName()));
										break;
									}
								}
							}
						}			
				    	
				    }
				}
			});
			backBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					replaceBackFragment();
				}
			});
			
		}
		
	}
	
	public Schedules getCurrentChanel(String name){
		Schedules currentChanel=null;
		for(int i=0;i<chonkenhList.size();i++){
			if(chonkenhList.get(i).getName().equals(name)){
				currentChanel=chonkenhList.get(i);
				break;
			}
		}
		return currentChanel;
		
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
	private void replaceEpiFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof LeftAndRightActivity) {
			LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
			fca.replaceEpisodeFragment(fragment);
		}
	}
	
	private void replaceBackFragment() {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof LeftAndRightActivity) {
			LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
			fca.replaceBackFragment();
		}
	}
	

}
