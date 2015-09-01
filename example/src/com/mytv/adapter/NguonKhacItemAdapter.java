package com.mytv.adapter;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mytv.model.Link;
import com.mytv.slidingmenu.R;

public class NguonKhacItemAdapter extends ArrayAdapter<Link> {
	Activity context = null;
	public List<Link> myArray = new ArrayList<Link>();

	public NguonKhacItemAdapter(Activity context, List<Link> arr) {
		super(context, R.layout.list_nguon_khac_item, arr);
		this.context = context;
		this.myArray = arr;
	}

	@SuppressLint("ViewHolder")
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		convertView = inflater.inflate(R.layout.list_nguon_khac_item, null);
		TextView nguonName = (TextView) convertView.findViewById(R.id.item_nguon_khac);
		nguonName.setText("Nguồn : "+position);
		return convertView;
	}

	static class ViewHolder {
		TextView nguonName;
	}

}
