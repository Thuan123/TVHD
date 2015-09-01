package com.mytv.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mytv.adapter.ListLichChieuAdapter.ViewHolder;
import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Link;
import com.mytv.model.Schedules;
import com.mytv.slidingmenu.FovariteListFragment;
import com.mytv.slidingmenu.R;
import com.mytv.utils.Global;

public class FavoriteListAdapter extends ArrayAdapter<Schedules> {
	Activity context = null;
	public List<Schedules> myArray = new ArrayList<Schedules>();
	public ImageView deletePressChanel, deleteChanel;
	public ImageView nguonIcon;
	public LayoutInflater inflater;
	private ImageLoader imageLoader;
	public TextView nguonName;

	public FavoriteListAdapter(Activity context, List<Schedules> arr,
			ImageLoader imageLoader) {
		super(context, R.layout.favorite_list_item, arr);
		this.context = context;
		this.myArray = arr;
		this.imageLoader = imageLoader;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@SuppressLint("ViewHolder")
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.favorite_list_item, parent,
					false);
			holder.nguonIcon = (ImageView) convertView
					.findViewById(R.id.them_chanel);
			holder.nguonName = (TextView) convertView
					.findViewById(R.id.text_themkenh);
			holder.deleteChanel = (ImageView) convertView
					.findViewById(R.id.delete_normal);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		getImageLoader().DisplayImage(this.myArray.get(position).getLogoUrl(),
				holder.nguonIcon);
		holder.nguonName.setText(this.myArray.get(position).getName());
		holder.deleteChanel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Global.database.deleteToDo(Global.favoriteList.get(position));

				FovariteListFragment.setFavoriteArray(Global.database
						.getAllChanel());
				Global.favoriteList = Global.database.getAllChanel();
				FovariteListFragment.favoriteAdapter = new FavoriteListAdapter(
						context, Global.favoriteList, imageLoader);
				FovariteListFragment.favoriteList
						.setAdapter(FovariteListFragment.favoriteAdapter);
			}
		});
		/*
		 * holder.deletePressChanel.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * holder.deleteChanel.setVisibility(View.VISIBLE);
		 * holder.deletePressChanel.setVisibility(View.GONE);
		 * 
		 * FovariteListFragment.getFavoriteArray().get(position) .setiDelete(0);
		 * Global.favoriteList.get(position).setiDelete(0);
		 * FovariteListFragment.favoriteAdapter.notifyDataSetChanged();
		 * 
		 * } });
		 */

		return convertView;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public void setImageLoader(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}

	static class ViewHolder {
		ImageView nguonIcon, deleteChanel/* , deletePressChanel */;
		TextView nguonName;
	}
}
