package com.candystudio.android.dxzs.teacher.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.candystudio.android.dxzs.teacher.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PointLibActivity extends Activity {
	
	EditText et_search;
	Button btn_search;
	ListView listview;
	MyListener ViewOnClickListener=new MyListener();
	ArrayList<HashMap<String, Object>> contentlist=new ArrayList<HashMap<String,Object>>();

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pointlib);
		init();
		
	}
	private void init() {
		et_search=(EditText)findViewById(R.id.et_search);
		btn_search=(Button)findViewById(R.id.btn_search);
		listview=(ListView)findViewById(R.id.listview);
		btn_search.setOnClickListener(ViewOnClickListener);
		
	}
	
	private class MyListener implements OnClickListener {		
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_search:			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("pic", R.drawable.ic_launcher);
			contentlist.add(map);
			SimpleAdapter mAdapter=new SimpleAdapter(PointLibActivity.this,contentlist,R.layout.list_item,new String[]{"pic"},new int[]{R.id.img});		
			listview.setAdapter(mAdapter);		
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					////µã»÷listµÄ²Ù×÷
				}
			});
			break;
			
		default:
			break;
		}
	}
}

}
