/*
 * Copyright 2013 David Schreiber
 *           2013 John Paul Nalog
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mytv.adapter;

import java.util.List;

import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Chanel;
import com.mytv.model.Film;
import com.mytv.model.Schedules;
import com.mytv.slidingmenu.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;

@SuppressLint("InflateParams")
public class FancyCoverFlowSampleAdapter extends FancyCoverFlowAdapter {
	private LayoutInflater inflater;
	public Activity activity;
	public int width;
	public int height;
	public Chanel liveChanel;
	public ImageLoader imageLoader;
    
    public FancyCoverFlowSampleAdapter(LayoutInflater inflater,
			Activity activity, int width, int height, Chanel liveChanel,
			ImageLoader imageLoader) {
		super();
		this.inflater = inflater;
		this.activity = activity;
		this.width = width;
		this.height = height;
		this.liveChanel = liveChanel;
		this.imageLoader = imageLoader;
		
	}

	@Override
    public int getCount() {
        return liveChanel.getList().size();
    }

    @Override
    public Schedules getItem(int i) {
        return liveChanel.getList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressWarnings("deprecation")
	@Override
    public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
    	reuseableView = inflater.inflate(R.layout.livetv_apear_item, null);
		Gallery.LayoutParams layoutParams = new Gallery.LayoutParams(
				(width/3),LayoutParams.FILL_PARENT);
		reuseableView.setLayoutParams(layoutParams);
		
    	
		TextView nameUserAppeal = (TextView) reuseableView.findViewById(R.id.img_name);
		ImageView imgUserAppeal = (ImageView) reuseableView.findViewById(R.id.img);
        
		nameUserAppeal.setText(this.liveChanel.getList().get(i).getName());
		imageLoader.DisplayImage(this.liveChanel.getList().get(i).getLogoUrl(), imgUserAppeal);
        return reuseableView;
    }
}
