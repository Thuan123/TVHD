package com.mytv.slidingmenu;
import com.mytv.adapter.ListLichChieuAdapter;
import com.mytv.loaderimage.ImageLoader;
import com.mytv.model.Chanel;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class LichchieuFragment extends Fragment{
	private Chanel chanel;
	private ImageView img;
	private ListLichChieuAdapter lichchieuAdapter;
	private ListView listKenh;
	public static ImageLoader imageLoader;
	public LichchieuFragment(Chanel chanel) {
		super();
		this.chanel = chanel;		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.lichchieu_frament, null);
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		img=(ImageView)getActivity().findViewById(R.id.iconimg);
		imageLoader = new ImageLoader(getActivity().getApplicationContext());
		imageLoader.setIcon_temp(3);
		imageLoader.setStub();
		if(chanel.getLogoUrl()!=null){
			imageLoader.DisplayImage(chanel.getLogoUrl().toString(),img);
		}else{
			img.setImageBitmap(null);
		}
		listKenh=(ListView)getActivity().findViewById(R.id.list_kenh);
		lichchieuAdapter=new ListLichChieuAdapter(getActivity(),chanel.getList());
		listKenh.setAdapter(lichchieuAdapter);
	}
	public Chanel getChanel() {
		return chanel;
	}
	public void setChanel(Chanel chanel) {
		this.chanel = chanel;
	}
	
}
