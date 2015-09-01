package com.mytv.slidingmenu;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.mytv.slidingmenu.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {
	protected Fragment mFrag;
    //public List<ChanelCategory> chanelList;
	//protected List<Category> catList;
    //protected ExpandableListView menuList;
    //protected ExpandableAdapter exAdpt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();		
            mFrag = new NavigationFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (Fragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}
		//menuList=(ExpandableListView)findViewById(R.id.myex);
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		//initData();
		//exAdpt = new ExpandableAdapter(catList,this);
	}
	public void onClickMenu(View v) {
	/*switch (v.getId()) {
		case R.id.list_btn:{
		
			//playTxt=(TextView)findViewById(R.id.menu_list);
			//playTxt.setText("Dao Hong Thuan");
			break;
		}
	 }*/
	}
	/*public void initData(){
		chanelList=new ArrayList<ChanelCategory>();
		ChanelCategory chanelTV=createChanelCategory();
		chanelTV.setListItemTV(createItemTVs(4));
	}
	public ChanelCategory createChanelCategory(){
		return new ChanelCategory(0,"Lich Chieu");
	}
	public List<ItemTV> createItemTVs(int num){
		List<ItemTV> result=new ArrayList<ItemTV>();
		for(int i=0;i<num;i++){
			ItemTV item=new ItemTV(i,"","VTV"+i);
		}
	   	return result;
	}*/
	/* private void initData() {
	    	catList = new ArrayList<Category>();
	    	
	    	Category cat1 = createCategory("Games", "Game for console", 1);
	    	cat1.setItemList(createItems("Game Item", "This is the game n.", 5));

	    	Category cat2 = createCategory("Mobile Phone", "All the mobile phone", 2);
	    	cat2.setItemList(createItems("Phone Item", "This is the phone n.", 5));

	    	catList.add(cat1);
	    	catList.add(cat2);
	    }
	    
	    private Category createCategory(String name, String descr, long id) {
	    	return new Category(id, name, descr);
	    }
	    
	    
	    private List<ItemDetail> createItems(String name, String descr, int num) {
	    	List<ItemDetail> result = new ArrayList<ItemDetail>();
	    	
	    	for (int i=0; i < num; i++) {
	    		ItemDetail item = new ItemDetail(i, 0, "item" + i, "Descr" + i);
	    		result.add(item);
	    	}
	    	
	    	return result;
	    }*/
}
