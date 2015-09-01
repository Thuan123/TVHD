package com.mytv.adapter;

import java.util.List;

import com.mytv.model.Film;
import com.mytv.slidingmenu.R;
import com.mytv.utils.Global;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

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
public class CustomListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	public Activity activity;
	public int width;
	public int height;
	List<Film> filmList;
	public ImageLoader imageLoader;
	private DisplayImageOptions options;

	public CustomListAdapter(LayoutInflater inflater, Activity activity,
			int width, int height, List<Film> filmList/*,ImageLoader imageLoader*/) {
		this.inflater = inflater;
		this.activity = activity;
		this.width = width;
		this.height = height;
		this.filmList = filmList;
		//this.imageLoader=imageLoader;
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.image_loading)
				.showImageOnLoading(R.drawable.image_loading)
				.showImageOnFail(R.drawable.image_loading)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				activity).defaultDisplayImageOptions(options)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(100 * 1024 * 1024).build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public int getCount() {
		return Global.filmList.size();
	}

	@Override
	public Film getItem(int position) {
		return Global.filmList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.tv_show_list_item, null);
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				width, (height / 5));
		convertView.setLayoutParams(layoutParams);
		ImageView iconImage = (ImageView) convertView
				.findViewById(R.id.img_show_item);
		TextView imageDes = (TextView) convertView.findViewById(R.id.film_des);
		TextView imageText = (TextView) convertView
				.findViewById(R.id.film_name);
		imageText.setText(filmList.get(position).getName());

		String desc = filmList.get(position).getFilmDesc();
		desc = Global.tachtu(desc);
		imageDes.setText(desc);
		imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(filmList.get(position).getLogoUrl().toString(),iconImage);
		return convertView;
	}
}
