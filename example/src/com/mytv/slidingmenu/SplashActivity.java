package com.mytv.slidingmenu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mytv.api.TVController;
import com.mytv.model.Chanel;
import com.mytv.model.Film;
import com.mytv.model.LichChieu;
import com.mytv.utils.Global;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

public class SplashActivity extends Activity {

	ProgressBar progressBar;
	public static List<Chanel> chanelList;
	public static List<LichChieu> lichchieuList;
	public static List<Film> filmList;
	public static List<Film> list_Film;
	public static String res;
	public static String tag_json_obj = "jobj_req";
	public static JSONObject jsonObject;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		progressBar = (ProgressBar) findViewById(R.id.activity_splash_progress_bar);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
			}
		}, 1000);
		getMenuShowTV(new VolleyCallback() {

			@Override
			public void onSuccess(String result) {
			}

			@Override
			public void onLoadMenuShow(String result) {
				Global.lich = Global.tvapisender.getMenuShowTV(result);
				Log.i("log", Integer.toString(Global.listFilmDowload.size()));
				getLiveShow(new VolleyCallback() {

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadVideo(String result) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadMenuShow(String result) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadLiveTV(String result) {
						// TODO Auto-generated method stub
						Log.i("LiveTV", result);
						Global.listTV = Global.tvapisender.listLiveShow(result);
						// if(Global.listTV!=null){
						Global.lich.add(new LichChieu("Live Tivi",
								Global.listTV));
						// }
						try {
							getShedule(new VolleyCallback() {
								@Override
								public void onSuccess(String result) {
									Global.chanelList = Global.tvapisender
											.getShedule(result);
									if (Global.chanelList != null) {
										Global.lich
												.add(new LichChieu("LichChieu",
														Global.chanelList));
									}
									Handler handler = new Handler();
									handler.postDelayed(new Runnable() {
										public void run() {
											startApp();
										}
									}, 2000);
								}

								@Override
								public void onLoadMenuShow(String result) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadVideo(String result) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadLiveTV(String result) {
									// TODO Auto-generated method stub

								}

							});
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
			}

			@Override
			public void onLoadVideo(String result) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onLoadLiveTV(String result) {
				// TODO Auto-generated method stub

			}
		});

	}

	public interface VolleyCallback {
		void onSuccess(String result);

		void onLoadMenuShow(String result);

		void onLoadVideo(String result);

		void onLoadLiveTV(String result);
	}

	private void startApp() {
		Intent intent = new Intent(SplashActivity.this,
				LeftAndRightActivity.class);
		startActivity(intent);
		finish();
	}

	public void getMenuShowTV(final VolleyCallback callback) {
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				Global.getURL_MENU(Global.url.URL,
						Global.end.ENDPOINT_MENUSHOW,
						Global.param.PARAM_MENUSHOW), null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						callback.onLoadMenuShow(response.toString());
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d("Error", "Error: " + error.getMessage());
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
				params.put("name", "Androidhive");
				params.put("email", "abc@androidhive.info");
				params.put("pass", "password123");

				return params;
			}

		};
		TVController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

	}

	public void getShedule(final VolleyCallback callback) throws JSONException {
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				Global.getURL(Global.url.URL, Global.end.ENDPOINT_LICHCHIEU,
						Global.getCurrentTime(), Global.param.PARAM_LICHCHIEU),
				null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject response) {
						callback.onSuccess(response.toString());
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d("Error", "Error: " + error.getMessage());
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

	public static void getLiveShow(final VolleyCallback callback) {
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
				Global.url.URL_LIVESHOW, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject response) {
						callback.onLoadLiveTV(response.toString());
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d("Error", "Error: " + error.getMessage());
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
}