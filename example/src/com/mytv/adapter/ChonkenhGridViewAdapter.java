package com.mytv.adapter;

import java.util.List;

import com.mytv.database.DatabaseHandler;
import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Schedules;
import com.mytv.slidingmenu.FovariteListFragment;
import com.mytv.slidingmenu.R;
import com.mytv.utils.Global;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class ChonkenhGridViewAdapter extends BaseAdapter {
	private Activity mContext;
	private List<Schedules> chonkenhList;
	//private DisplayImageOptions options;
	public int check = 0;
	public ImageLoader imageLoader;
	

	public ChonkenhGridViewAdapter(Activity c, List<Schedules> list,ImageLoader imageLoader) {
		
		mContext = c;
		this.chonkenhList = list;
	    this.imageLoader=imageLoader;
		Global.database = new DatabaseHandler(this.mContext);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chonkenhList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.gridview_chonkenh_item,
					null);
			holder.imgChonKenh = (ImageView) convertView.findViewById(R.id.img);
			holder.textChonKenh = (TextView) convertView
					.findViewById(R.id.img_name);
			holder.imgFavorite = (ImageView) convertView
					.findViewById(R.id.favorite_img);
			holder.imgFavoriteNormal = (ImageView) convertView
					.findViewById(R.id.favorite_img_normal);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		imageLoader.DisplayImage(chonkenhList.get(position).getLogoUrl().toString(),holder.imgChonKenh);
		
		holder.textChonKenh.setText(chonkenhList.get(position).getName());
		if (CheckFavorite(chonkenhList.get(position), Global.favoriteList) == 1) {
			holder.imgFavorite.setVisibility(View.VISIBLE);
			holder.imgFavoriteNormal.setVisibility(View.GONE);
		} else {
			holder.imgFavorite.setVisibility(View.GONE);
			holder.imgFavoriteNormal.setVisibility(View.VISIBLE);
		}
		check = 0;
		holder.imgFavorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				holder.imgFavorite.setVisibility(View.GONE);
				holder.imgFavoriteNormal.setVisibility(View.VISIBLE);
				Global.database.deleteToDo(chonkenhList.get(position));
				FovariteListFragment.setFavoriteArray(Global.database.getAllChanel());
				Global.favoriteList=Global.database.getAllChanel();
				FovariteListFragment.favoriteAdapter = new FavoriteListAdapter(mContext,
						FovariteListFragment.getFavoriteArray(),imageLoader);
				FovariteListFragment.favoriteList.setAdapter(FovariteListFragment.favoriteAdapter);
			}
		});
		holder.imgFavoriteNormal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Global.database.getChanel(chonkenhList.get(position).getName()) == null) {
					Global.database.createAccount(chonkenhList.get(position));
					holder.imgFavorite.setVisibility(View.VISIBLE);
					holder.imgFavoriteNormal.setVisibility(View.GONE);
					FovariteListFragment.setFavoriteArray(Global.database.getAllChanel());
					Global.favoriteList=Global.database.getAllChanel();
					FovariteListFragment.favoriteAdapter = new FavoriteListAdapter(mContext,
							FovariteListFragment.getFavoriteArray(),imageLoader);
					FovariteListFragment.favoriteList.setAdapter(FovariteListFragment.favoriteAdapter);
				}
			}
		});

		return convertView;
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

	static class ViewHolder {

		ImageView imgChonKenh, imgFavorite, imgFavoriteNormal;
		TextView textChonKenh;

	}
}
