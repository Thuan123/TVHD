package com.mytv.adapter;

import java.util.List;

import com.mytv.model.Film;
import com.mytv.slidingmenu.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImagePagerAdapter extends PagerAdapter {

	Context mContext;
	LayoutInflater mLayoutInflater;
	//List<Integer> imageIdList;
	List<Film> filmList;
	Activity ac;
	private DisplayImageOptions options;
	
	//public static ImageLoader imageLoader;

	public ImagePagerAdapter(Activity ac, Context context,
			/*List<Integer> imageIdList,*/List<Film> filmList) {
		mContext = context;
		this.ac = ac;
		//this.imageIdList = imageIdList;
		this.filmList=filmList;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//imageLoader = new ImageLoader(ac.getApplicationContext());
		//imageLoader.setIcon_temp(5);
		//imageLoader.setStub();
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.image_loading)
		.showImageOnFail(R.drawable.image_loading)
		.resetViewBeforeLoading(true)
		.cacheOnDisk(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.defaultDisplayImageOptions(options)
				.memoryCache(new WeakMemoryCache())
				.discCacheSize(100 * 1024 * 1024).build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public int getCount() {
		return filmList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View itemView = mLayoutInflater.inflate(R.layout.img, container, false);
		TextView filmName = (TextView) itemView.findViewById(R.id.des);
		ImageView imageView = (ImageView) itemView.findViewById(R.id.film_img);
		filmName.setText(filmList.get(position).getName());
		if(filmList.get(position).getLogoUrl()!=null){
		//imageLoader.DisplayImage(filmList.get(position).getLogoUrl(),imageView);
			ImageLoader.getInstance().displayImage(filmList.get(position).getLogoUrl().toString(), imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				}
			});
		}else{
		  imageView.setBackgroundResource(R.drawable.image_loading);
		}
		container.addView(itemView);
		return itemView;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((RelativeLayout) object);
	}
	
}
