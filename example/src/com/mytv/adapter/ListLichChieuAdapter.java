package com.mytv.adapter;

import java.util.List;

import com.mytv.model.Schedules;
import com.mytv.slidingmenu.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListLichChieuAdapter extends ArrayAdapter<Schedules> {
	Activity context = null;
	public List<Schedules> myArray = null;

	public ListLichChieuAdapter(Activity context, List<Schedules> arr) {
		super(context, R.layout.list_lich_chieu_item, arr);
		this.context = context;
		this.myArray = arr;
	}

	@SuppressLint("ViewHolder")
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = context.getLayoutInflater();
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_lich_chieu_item, null);
			holder.txtTime = (TextView) convertView
					.findViewById(R.id.time_kenh);
			holder.txtNews = (TextView) convertView.findViewById(R.id.sk);
			Schedules emp = myArray.get(position);
			holder.txtTime.setText(emp.getTime() + " : ");
			holder.txtNews.setText(emp.getTitles());
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	static class ViewHolder {
		TextView txtTime;
		TextView txtNews;
	}

}
