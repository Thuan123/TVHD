package com.mytv.api.net;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mytv.model.Chanel;
import com.mytv.model.Episode;
import com.mytv.model.Film;
import com.mytv.model.FilmDowload;
import com.mytv.model.LichChieu;
import com.mytv.model.Link;
import com.mytv.model.Schedules;
import com.mytv.model.Video;
import com.mytv.utils.Global;

public class TVApiSender {
	private static List<Chanel> chanelList;
	private static List<Chanel> listTV;
	private static List<LichChieu> lichchieuList;
	private static List<Film> filmList;

	public static List<Chanel> getShedule(String data) {
		setChanelList(new ArrayList<Chanel>());
		try {
			JSONObject res = new JSONObject(data);
			JSONArray contacts;
			try {
				contacts = res.getJSONArray("channels");
				for (int i = 0; i < contacts.length(); i++) {
					List<Schedules> list_item = new ArrayList<Schedules>();
					JSONObject chanel = contacts.getJSONObject(i);
					String id = chanel.getString("id");
					String name = chanel.getString("name");
					String logoUrl = chanel.getString("logoUrl");
					Log.i("name_Schedule",name);
					JSONObject schedules = chanel.getJSONObject("schedules");
					JSONArray sche_list = schedules.getJSONArray(Global
							.getCurrentTime());
					for (int j = 0; j < sche_list.length(); j++) {
						JSONObject sche = sche_list.getJSONObject(j);
						String time = sche.getString("time");
						String title = sche.getString("title");
						Schedules ScheTime = new Schedules(time, title);
						list_item.add(j, ScheTime);
					}
					Chanel C = new Chanel(Global.getCurrentTime(), name,
							Long.parseLong(id), logoUrl, list_item);
					getChanelList().add(i, C);
				}
				Log.i("listCSize", Integer.toString(getChanelList().size()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return getChanelList();

	}

	public static List<Film> getMenuVideo(String data) {
		setFilmList(new ArrayList<Film>());
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONArray videoList = jsonObject.getJSONArray("videoList");
			for (int i = 0; i < videoList.length(); i++) {
				List<Episode> epiList = new ArrayList<Episode>();
				JSONObject video = videoList.getJSONObject(i);
				String filmId = video.getString("id");
				String filmName = video.getString("name");
				String filmLogoUrl = video.getString("logoUrl");
				String filmDesc = video.getString("description");
				String filmDuration = video.getString("duration");
				JSONArray EpisoList = video.getJSONArray("episodeList");
				for (int j = 0; j < EpisoList.length(); j++) {
					List<Video> v = new ArrayList<Video>();
					JSONObject vjson = EpisoList.getJSONObject(j);
					String epiId = vjson.getString("id");
					String epiName = vjson.getString("name");
					String epiDesc = vjson.getString("description");
					String epiLogo = vjson.getString("logoUrl");
					JSONArray vidList = vjson.getJSONArray("linkList");
					for (int k = 0; k < vidList.length(); k++) {
						JSONObject videoJson = vidList.getJSONObject(k);
						String videoId = videoJson.getString("id");
						String videoRank = videoJson.getString("rank");
						String videoUrl = videoJson.getString("url");
						String videoCreated = videoJson
								.getString("createdTime");
						String videoUpdated = videoJson
								.getString("updatedTime");
						v.add(k,
								new Video(Long.parseLong(videoId), Integer
										.parseInt(videoRank), videoUrl,
										videoCreated, videoUpdated));
					}
					String epiDuration = vjson.getString("duration");
					String epiReleased = vjson.getString("released");
					epiList.add(j, new Episode(Long.parseLong(epiId), epiName,
							epiDesc, epiLogo, v, epiDuration, epiReleased));
				}
				String filmReleased = video.getString("released");
				getFilmList().add(
						i,
						new Film(Long.parseLong(filmId), filmName, filmLogoUrl,
								filmDesc, filmDuration, epiList, filmReleased));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getFilmList();
	}
	public static List<LichChieu> getMenuShowTV(String data) {
		setLichchieuList(new ArrayList<LichChieu>());
		try {
			JSONObject jsonObject = new JSONObject(data);
			JSONArray menuList = jsonObject.getJSONArray("menuList");
			for (int i = 0; i < menuList.length(); i++) {
				List<Chanel> MenuChanel = new ArrayList<Chanel>();
				JSONObject chanel = menuList.getJSONObject(i);
				String id = chanel.getString("id");
				Log.i("id", id);
				JSONArray cateList = chanel.getJSONArray("categoryList");
				for (int j = 0; j < cateList.length(); j++) {
					JSONObject sche = cateList.getJSONObject(j);
					String cateId = sche.getString("id");
					String nameShow = sche.getString("name");
					String descShow = sche.getString("description");
					String logoUrl = sche.getString("logoUrl");
					String type = sche.getString("type");
					Chanel A = new Chanel(null, descShow, nameShow,
							Long.parseLong(cateId), logoUrl, null, type);
					MenuChanel.add(j, A);
				}
				String nameLich = chanel.getString("name");
				Log.i("nameLich", nameLich);
				LichChieu lich = new LichChieu(nameLich, MenuChanel);
				getLichchieuList().add(i, lich);

			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return getLichchieuList();
	}
	public static List<Chanel> listLiveShow(String data){
		setListTV(new ArrayList<Chanel>());
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(data);
			JSONArray groupList = jsonObject.getJSONArray("groupList");
			for(int i=0;i<groupList.length();i++){
				List<Schedules> listSche = new ArrayList<Schedules>();
				JSONObject chanel = groupList.getJSONObject(i);
				String chanel_id = chanel.getString("id");
				String chanelName = chanel.getString("name");
				Log.i("ChannelName",chanelName);
				String logoUrl = chanel.getString("logoUrl");
				
				JSONArray chanelList = chanel.getJSONArray("channelList");
				for(int j=0;j<chanelList.length();j++){
					List<Link> linkList=new ArrayList<Link>();
					JSONObject chanelObject = chanelList.getJSONObject(j);
					String Sche_id=chanelObject.getString("id");
					String Sche_name=chanelObject.getString("name");
					String Sche_des=chanelObject.getString("description");
					String Sche_logoUrl=chanelObject.getString("logoUrl");
					JSONArray linkObject = chanelObject.getJSONArray("linkList");
					for(int k=0;k<linkObject.length();k++){
						JSONObject linkOb = linkObject.getJSONObject(k);
						String link_id=linkOb.getString("id");
						String link_rank=linkOb.getString("rank");
						String link_url=linkOb.getString("url");
						String link_createdTime=linkOb.getString("createdTime");
						String link_updatedTime=linkOb.getString("updatedTime");
						linkList.add(k,new Link(Long.parseLong(link_id),Integer.parseInt(link_rank),link_url,link_createdTime,link_updatedTime));
					}
					listSche.add(j,new Schedules(null,null,Sche_name,Integer.parseInt(Sche_id),Sche_des,Sche_logoUrl,linkList,chanelName));
				}
               getListTV().add(i,new Chanel(null,null,chanelName,Long.parseLong(chanel_id),logoUrl,listSche,null));    				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getListTV();
		
		
	}
	

	public static List<Chanel> getChanelList() {
		return chanelList;
	}

	public static void setChanelList(List<Chanel> chanelList) {
		TVApiSender.chanelList = chanelList;
	}

	public static List<LichChieu> getLichchieuList() {
		return lichchieuList;
	}

	public static void setLichchieuList(List<LichChieu> lichchieuList) {
		TVApiSender.lichchieuList = lichchieuList;
	}

	public static List<Film> getFilmList() {
		return filmList;
	}

	public static void setFilmList(List<Film> filmList) {
		TVApiSender.filmList = filmList;
	}

	public static List<Chanel> getListTV() {
		return listTV;
	}

	public static void setListTV(List<Chanel> listTV) {
		TVApiSender.listTV = listTV;
	}
}
