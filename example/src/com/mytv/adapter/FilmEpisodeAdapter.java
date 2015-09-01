package com.mytv.adapter;

import java.util.List;

import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Episode;
import com.mytv.model.Film;
import com.mytv.slidingmenu.R;
import com.mytv.utils.Global;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class FilmEpisodeAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	public Activity activity;
	public int width;
	public int height;
	List<Episode> epiList;
	private ImageLoader imageLoader;

	public FilmEpisodeAdapter(LayoutInflater inflater, Activity activity,
			int width, int height, List<Episode> epiList,ImageLoader imageLoader) {
		this.inflater = inflater;
		this.activity = activity;
		this.width = width;
		this.height = height;
		this.epiList = epiList;
		this.imageLoader=imageLoader;
		

	}

	@Override
	public int getCount() {
		return this.epiList.size();
	}

	@Override
	public Episode getItem(int position) {
		return this.epiList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.tv_show_list_item,null);
			AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
					width, (height / 5));
			convertView.setLayoutParams(layoutParams);
			ImageView iconImage = (ImageView) convertView
					.findViewById(R.id.img_show_item);
			TextView imageDes = (TextView) convertView
					.findViewById(R.id.film_des);
			TextView imageText = (TextView) convertView
					.findViewById(R.id.film_name);
			imageText.setText(epiList.get(position).getName());
			
			
			String desc=epiList.get(position).getEpisoDes();
			if(desc.indexOf(".")>0){
				desc=desc.substring(0,desc.indexOf("."));
				imageDes.setText(desc);
			}else{
				imageDes.setText(epiList.get(position).getEpisoDes());	
			}
			getImageLoader().DisplayImage(epiList.get(position).getLogoUrl().toString(),iconImage);

		return convertView;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public void setImageLoader(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}

	static class ViewHolder {
		ImageView iconImage, xemImage;
		TextView imageText;
		TextView imageDes;
	}
}
