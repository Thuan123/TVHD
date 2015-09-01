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

public class DangchieuListAdapter extends ArrayAdapter<Schedules> {
	Activity context = null;
	public List<Schedules> myArray = null;

	public DangchieuListAdapter(Activity context, List<Schedules> arr) {
		super(context, R.layout.list_dang_chieu_item, arr);
		this.context = context;
		this.myArray = arr;
	}

	@SuppressLint("ViewHolder")
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_dang_chieu_item, null);
			TextView txtTime = (TextView) convertView
					.findViewById(R.id.time_kenh);
			TextView txtNews = (TextView) convertView.findViewById(R.id.sk);
			Schedules emp = myArray.get(position);
			txtTime.setText(emp.getTime() + " : ");
			txtNews.setText(emp.getTitles());
		return convertView;
	}

	

}
