package com.mytv.slidingmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mytv.adapter.CustomListAdapter;
import com.mytv.adapter.ImagePagerAdapter;
import com.mytv.api.TVController;
import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Film;
import com.mytv.slidingmenu.SplashActivity.VolleyCallback;
import com.mytv.utils.Global;
import com.nirhart.parallaxscroll.views.ParallaxListView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class FilmFragment extends Fragment {
	private String idFilm;
	public TextView sizeText;
	public ProgressDialog prgDialog;
	public int width;
	public int height;
	public static String tag_json_obj = "jobj_req";

	private ProgressDialog showDialog;
	public Activity context;

	public ParallaxListView listView;
	public CustomListAdapter adapter;
	public ImagePagerAdapter imgAdap;

	public static ImageLoader imageLoader;

	public String android_id;

	List<Film> videoList = new ArrayList<Film>();

	public FilmFragment(String idFilm, Activity context, List<Film> videoList) {
		this.setIdFilm(idFilm);
		this.context = context;
		this.videoList = videoList;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.film_frament, null);

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView = (ParallaxListView) getActivity()
				.findViewById(R.id.list_view);

		imageLoader = new ImageLoader(getActivity().getApplicationContext());
		if (videoList == null) {
			showDialog = ProgressDialog.show(context, "MyTV", "Loading...",
					true, false);
			getListVideo(
					new VolleyCallback() {

						@Override
						public void onSuccess(String result) {

						}

						@Override
						public void onLoadVideo(String result) {
							Log.i("Result_Film", result);
							Global.filmList = Global.tvapisender.getMenuVideo(result);
							Log.i("SizeFilmList",
									Integer.toString(Global.filmList.size()));
							DisplayMetrics displayMetric = new DisplayMetrics();
							getActivity().getWindowManager()
									.getDefaultDisplay()
									.getMetrics(displayMetric);
							width = displayMetric.widthPixels;
							height = displayMetric.heightPixels;

							adapter = new CustomListAdapter(
									LayoutInflater.from(getActivity()
											.getApplicationContext()),
									getActivity(), width, height,
									Global.filmList/*, imageLoader*/);

							AutoScrollViewPager v = (AutoScrollViewPager) getActivity()
									.getLayoutInflater().inflate(
											R.layout.imageheader, null);

							imgAdap = new ImagePagerAdapter(getActivity(),
									getActivity().getApplicationContext(),
									Global.filmList);
							v.setAdapter(imgAdap);

							AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
									width, (height / 3));

							v.setLayoutParams(layoutParams);

							v.setInterval(4000);
							v.startAutoScroll();
							v.setCurrentItem(Integer.MAX_VALUE / 2
									- Integer.MAX_VALUE / 2
									% Global.filmList.size());
							listView.addParallaxedHeaderView(v);
							listView.setAdapter(adapter);
							listView.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
											.getSystemService(
													getActivity().INPUT_METHOD_SERVICE);
									inputMethodManager.hideSoftInputFromWindow(
											getActivity().getCurrentFocus()
													.getWindowToken(), 0);
									Log.i("position", Integer.toString(arg2));
									replaceMainFragment(new FilmEpisodeFragment(
											arg3, getIdFilm(), Global.filmList
													.get(arg2 - 1).getEpi()));

								}
							});
							listView.setOnScrollListener(new OnScrollListener() {

								@Override
								public void onScrollStateChanged(
										AbsListView view, int scrollState) {
									// TODO Auto-generated method stub
									int threshold = 1;
									int count = listView.getCount();

									if (scrollState == SCROLL_STATE_IDLE) {
										if (listView.getLastVisiblePosition() >= count
												- threshold) {
											showDialog = ProgressDialog.show(
													context, "MyTV",
													"Loading...", true, false);
											getListVideo(new VolleyCallback() {

												@Override
												public void onSuccess(
														String result) {
													// TODO Auto-generated
													// method stub

												}

												@Override
												public void onLoadVideo(
														String result) {
													// TODO Auto-generated
													// method stub
													if(result!=null){
													 videoList = Global.tvapisender
															.getMenuVideo(result);
													 Global.filmList
															.addAll(videoList);
													 adapter.notifyDataSetChanged();
													 imgAdap.notifyDataSetChanged();
													}
												}

												@Override
												public void onLoadMenuShow(
														String result) {
													// TODO Auto-generated
													// method stub

												}

												@Override
												public void onLoadLiveTV(
														String result) {
													// TODO Auto-generated
													// method stub

												}
											}, getIdFilm(), showDialog, Integer
													.toString(Global.filmList
															.size()), Integer
													.toString(10));
										}
									}
								}

								@Override
								public void onScroll(AbsListView view,
										int firstVisibleItem,
										int visibleItemCount, int totalItemCount) {
								}
							});
						}

						@Override
						public void onLoadMenuShow(String result) {

						}

						@Override
						public void onLoadLiveTV(String result) {
							// TODO Auto-generated method stub

						}
					}, getIdFilm(), showDialog, Integer.toString(0),
					Integer.toString(20));

		} else if (videoList != null) {
			Global.filmList = videoList;
			Log.i("SizeFilmList", Integer.toString(Global.filmList.size()));
			DisplayMetrics displayMetric = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay()
					.getMetrics(displayMetric);
			width = displayMetric.widthPixels;
			height = displayMetric.heightPixels;

			adapter = new CustomListAdapter(LayoutInflater.from(getActivity()
					.getApplicationContext()), getActivity(), width, height,
					Global.filmList/*, imageLoader*/);

			AutoScrollViewPager v = (AutoScrollViewPager) getActivity()
					.getLayoutInflater().inflate(R.layout.imageheader, null);

			imgAdap = new ImagePagerAdapter(getActivity(), getActivity()
					.getApplicationContext(), Global.filmList);
			v.setAdapter(imgAdap);

			AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
					width, (height / 3));

			v.setLayoutParams(layoutParams);

			v.setInterval(4000);
			v.startAutoScroll();
			v.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
					% Global.filmList.size());
			listView.addParallaxedHeaderView(v);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
							.getSystemService(
									getActivity().INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(getActivity()
							.getCurrentFocus().getWindowToken(), 0);
					Log.i("position", Integer.toString(arg2));
					replaceMainFragment(new FilmEpisodeFragment(arg3,
							getIdFilm(), Global.filmList.get(arg2 - 1).getEpi()));

				}
			});
			listView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					// TODO Auto-generated method stub
					int threshold = 1;
					int count = listView.getCount();

					if (scrollState == SCROLL_STATE_IDLE) {
						if (listView.getLastVisiblePosition() >= count
								- threshold) {
							showDialog = ProgressDialog.show(context, "MyTV",
									"Loading...", true, false);
							getListVideo(new VolleyCallback() {

								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadVideo(String result) {
									// TODO Auto-generated method stub
									if (result != null) {
										videoList = Global.tvapisender
												.getMenuVideo(result);
										Global.filmList.addAll(videoList);
										adapter.notifyDataSetChanged();
										imgAdap.notifyDataSetChanged();
									}
								}

								@Override
								public void onLoadMenuShow(String result) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadLiveTV(String result) {
									// TODO Auto-generated method stub

								}
							}, getIdFilm(), showDialog, Integer
									.toString(Global.filmList.size()), Integer
									.toString(10));
						}
					}
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
				}
			});
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(imageLoader!=null){
		  imageLoader.clearCache();
		}
	}

	public String getIdFilm() {
		return idFilm;
	}

	public void setIdFilm(String idFilm) {
		this.idFilm = idFilm;
	}

	public void getListVideo(final VolleyCallback callback, String filmId,
			final ProgressDialog prgDialog, String page_start, String page_end) {
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				Global.getURL_FILM(Global.url.URL, filmId,
						Global.end.ENDPOINT_VIDEO_API,
						Global.end.ENDPOINT_VIDEO_FEATURES,
						Global.param.PARAM_VIDEO, Global.param.PARAM_PAGE,
						page_start, Global.param.PARAM_PAGE_SIZE, page_end),
				null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject response) {
						callback.onLoadVideo(response.toString());
						if(response.toString()!=null){
						 Handler handler = new Handler();
						 handler.postDelayed(new Runnable() {
							public void run() {
								prgDialog.dismiss();
							}
						  }, 10);
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d("Error", "Error: " + error.getMessage());
						prgDialog.dismiss();
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/json");
				return headers;
			}

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				return params;
			}
		};
		TVController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
	}

	private void replaceMainFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof LeftAndRightActivity) {
			LeftAndRightActivity fca = (LeftAndRightActivity) getActivity();
			fca.menuButton.setImageResource(R.drawable.icon_back);
			fca.replaceMainFragment(fragment,
					Integer.parseInt(this.getIdFilm()));
		}
	}
}
