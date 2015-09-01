/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
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

package com.mytv.adapter;

import java.io.InputStream;
import java.util.List;
import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Chanel;
import com.mytv.model.LichChieu;
import com.mytv.slidingmenu.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableAdapter extends BaseExpandableListAdapter {

	private List<LichChieu> chanelList;
	private Context ctx;
	private static ImageLoader imageLoader; 
	
	public ExpandableAdapter(List<LichChieu> lich, Context ctx,ImageLoader imageLoader) {
		this.chanelList = lich;
		this.ctx = ctx;
		ExpandableAdapter.setImageLoader(imageLoader);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return chanelList.get(groupPosition).getChanelList().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return chanelList.get(groupPosition).getChanelList().get(childPosition).hashCode();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_layout, parent, false);
		}
		
		TextView itemName = (TextView) v.findViewById(R.id.itemName);
		ImageView MenuItemImage=(ImageView)v.findViewById(R.id.menu_item_img);
		if(chanelList.get(groupPosition).getName().equals("TV Shows")){
			getImageLoader().setIcon_temp(2);
			getImageLoader().setStub();
		}else if(chanelList.get(groupPosition).getName().equals("Film")){
			getImageLoader().setIcon_temp(1);
			getImageLoader().setStub();
		}else if(chanelList.get(groupPosition).getName().equals("Sport")){
			getImageLoader().setIcon_temp(4);
			getImageLoader().setStub();
		}else if(chanelList.get(groupPosition).getName().equals("LichChieu")){
			getImageLoader().setIcon_temp(3);
			getImageLoader().setStub();
		}
		
		Chanel det = chanelList.get(groupPosition).getChanelList().get(childPosition);
		if(det.getLogoUrl()!=null){
			getImageLoader().DisplayImage(det.getLogoUrl().toString(),MenuItemImage);
		}else{
			MenuItemImage.setImageBitmap(null);
		}
		if(det.getName().indexOf("-")>0){
			String result[] = det.getName().split("-");
		  itemName.setText(result[0].toString());
		}else{
			itemName.setText(det.getName());
		}
		return v;
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size = chanelList.get(groupPosition).getChanelList().size();
		return size;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return chanelList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
	  return chanelList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return chanelList.get(groupPosition).hashCode();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.group_layout, parent, false);
		}
		
		
		
		TextView groupName = (TextView) v.findViewById(R.id.groupName);
		ImageView exicon=(ImageView)v.findViewById(R.id.icon_ex);
		ImageView exnoicon=(ImageView)v.findViewById(R.id.icon_no_ex);
		LichChieu cat = chanelList.get(groupPosition);
		groupName.setText(cat.getName());
		if(isExpanded){
			exnoicon.setVisibility(View.VISIBLE);
			exicon.setVisibility(View.GONE);
		}else if(!isExpanded){
			exnoicon.setVisibility(View.GONE);
			exicon.setVisibility(View.VISIBLE);
		}
		return v;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	public static void setImageLoader(ImageLoader imageLoader) {
		ExpandableAdapter.imageLoader = imageLoader;
	}
	class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    @Override
	    protected void onPreExecute() {
	        // TODO Auto-generated method stub
	        super.onPreExecute();
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	          InputStream in = new java.net.URL(urldisplay).openStream();
	          mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    @Override 
	    protected void onPostExecute(Bitmap result) {
	        super.onPostExecute(result);
	        bmImage.setImageBitmap(result);
	    }
	  }
	}

