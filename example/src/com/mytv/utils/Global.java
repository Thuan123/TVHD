package com.mytv.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.format.Time;
import android.widget.ProgressBar;

import com.mytv.api.Endpoint;
import com.mytv.api.Param;
import com.mytv.api.Url;
import com.mytv.api.net.Api;
import com.mytv.api.net.Checknetwork;
import com.mytv.api.net.TVApiSender;
import com.mytv.database.DatabaseHandler;
import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Chanel;
import com.mytv.model.Film;
import com.mytv.model.FilmDowload;
import com.mytv.model.LichChieu;
import com.mytv.model.Schedules;
import com.mytv.slidingmenu.LeftAndRightActivity;

import io.vov.vitamio.utils.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressLint("SimpleDateFormat")
public class Global {
	public static TVApiSender tvapi = new TVApiSender();
	public static List<Chanel> listKenh = new ArrayList<Chanel>();
	public static Url url;
	public static Endpoint end;
	public static Param param;
	public static String timer, day_time;
	public static Api apiGet;
	public static TVApiSender tvapisender;
	public static DatabaseHandler database;
	public static Checknetwork network;
	public static List<FilmDowload> listFilmDowload = new ArrayList<FilmDowload>();
	public static List<Chanel> chanelList = new ArrayList<Chanel>();
	public static List<Chanel> listTV = new ArrayList<Chanel>();
	public static List<LichChieu> lich = new ArrayList<LichChieu>();
	public static List<Film> filmList = new ArrayList<Film>();
	public static List<Schedules> favoriteList=new ArrayList<Schedules>();

	public static String getCurrentTime() {
		Calendar ci = Calendar.getInstance();
		int time = ci.get(Calendar.MONTH) + 1;
		int day = ci.get(Calendar.DAY_OF_MONTH);
		if (day < 10) {
			day_time = "0" + Integer.toString(day);
		} else {
			day_time = Integer.toString(day);
		}
		if (time < 10) {
			timer = "0" + Integer.toString(time);
		} else {
			timer = Integer.toString(time);
		}
		String DateTime = "" + ci.get(Calendar.YEAR) + "-" + timer + "-"
				+ day_time;
		Log.i("DATETIME", DateTime);
		return DateTime;
	}

	public static String getTimeHour() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

	public static int cutString(String str){
		String[] temp=str.split(":");
		return Integer.parseInt(temp[0]);
	}
	
	public static String getURL(String Url, String endPoint, String time,
			String Param) {
		String CONSTURL = Url + endPoint + time + Param;
		return CONSTURL;
	}

	public static String getURL_MENU(String url, String endPoint, String Param) {
		String CONSTURL = url + endPoint + Param;
		return CONSTURL;
	}

	public static String getURL_FILM(String url, String id, String endPoint1,
			String endPoint2, String Param, String params_page, String page,
			String params_page_size, String page_size) {
		String CONSTURL = url + endPoint1 + id + endPoint2 + Param
				+ params_page + page + params_page_size + page_size;
		return CONSTURL;
	}

	public static class VideoAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@SuppressWarnings({ "static-access", "static-access", "static-access",
				"static-access", "static-access" })
		@Override
		protected String doInBackground(String... urls) {

			return Global.apiGet.GET(urls[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			cancel(true);
		}
	}

	public static class LichChieuAsyncTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... urls) {

			return Global.apiGet.GET(urls[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

		}
	}

	public static class ChanelAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... urls) {

			return Global.apiGet.GET(urls[0]);
		}

		@Override
		protected void onPostExecute(String result) {
		}
	}

	public static String tachtu(String str) {
		int iTu = 0;
		int vt = 0;
		String chuoicon = "";
		for (int i = 0; i < str.length() - 1; i++) {
			if (((str.charAt(i) == '.') || (str.charAt(i) == ' ') || (str
					.charAt(i) == ',')) && str.charAt(i + 1) != '0') {
				iTu = iTu + 1;
				if (iTu == 20) {
					vt = i;
					break;
				}
			}
		}
		if (vt < 20) {
			vt = str.length() - 1;
		}

		for (int j = 0; j <= vt; j++) {
			chuoicon = chuoicon + str.charAt(j);
		}
		chuoicon = chuoicon + "...";
		return chuoicon;

	}

}
