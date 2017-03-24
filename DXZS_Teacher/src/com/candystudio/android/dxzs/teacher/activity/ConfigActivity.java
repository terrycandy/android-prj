package com.candystudio.android.dxzs.teacher.activity;

import com.candystudio.android.dxzs.teacher.activity.ConfigActivity;
import com.candystudio.android.dxzs.teacher.activity.LoginActivity;
import com.candystudio.android.dxzs.teacher.app.R;
import com.candystudio.android.dxzs.teacher.app.R.layout;
import com.candystudio.android.dxzs.teacher.app.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConfigActivity extends Activity implements OnClickListener {

	private Button saveButton;
	private EditText ipEditText;
	private EditText portEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		initLayoutMember();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}

	private void initLayoutMember() {
		ipEditText = (EditText) findViewById(R.id.config_ip_edittext);
		portEditText = (EditText) findViewById(R.id.config_port_edittext);
		saveButton = (Button) findViewById(R.id.config_save_button);
		saveButton.setOnClickListener(this);
	}

	private void saveConfig() {
		SharedPreferences sp = getSharedPreferences("server", MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString("ip", ipEditText.getText().toString());
		ed.putString("port", portEditText.getText().toString());
		ed.commit();
	}

	private void go2Login() {
//		Intent intent = new Intent(ConfigActivity.this, ExamActivity.class);
		Intent intent = new Intent(ConfigActivity.this, LoginActivity.class);
		// intent.putExtra(name, value)
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.config_save_button:
			saveConfig();
			go2Login();
			break;
		case R.id.config_exit_button:
			this.finish();
			break;
		default:
			break;
		}
	}

}
