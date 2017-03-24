package com.candystudio.android.dxzs.teacher.activity;

import org.apache.commons.logging.Log;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import com.candystudio.android.dxzs.teacher.app.R;
import com.candystudio.android.dxzs.teacher.app.R.layout;
import com.candystudio.android.dxzs.teacher.app.R.menu;
import com.candystudio.android.dxzs.teacher.tool.CommunicationTool;
import com.candystudio.android.dxzs.teacher.ultility.LogUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ManuActivity extends Activity implements OnClickListener {

	private EditText msgEditText;
	private Button examButton;
	private Button setPointButton;
	private Button setRouteButton;
	private Button pointLibButton;
	private Button routeLibButton;
	private ChatManagerListener chatManagerListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manu);
		initLayoutMember();
		// startListening();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.manu, menu);
		return true;
	}

	private void initLayoutMember() {

		examButton = (Button) findViewById(R.id.manu_exam_button);
		setPointButton = (Button) findViewById(R.id.manu_setpoint_button);
		setRouteButton = (Button) findViewById(R.id.manu_setroute_button);
		pointLibButton = (Button) findViewById(R.id.manu_pointlib_button);
		routeLibButton = (Button) findViewById(R.id.manu_routelib_button);
		examButton.setOnClickListener(this);
		setPointButton.setOnClickListener(this);
		setRouteButton.setOnClickListener(this);
		pointLibButton.setOnClickListener(this);
		routeLibButton.setOnClickListener(this);
	}

	private void go2Exam() {
		Intent intent = new Intent(ManuActivity.this, ExamActivity.class);
		startActivity(intent);
	}

	// to be done
	private void go2SetPoint() {
		Intent intent = new Intent(ManuActivity.this, SetPointActivity.class);
		startActivity(intent);
	}

	private void go2SetRoute() {
		// Intent intent = new Intent(ManuActivity.this, ExamActivity.class);
		// startActivity(intent);
	}

	private void go2PointLib() {
		Intent intent = new Intent(ManuActivity.this, PointLibActivity.class);
		startActivity(intent);
	}

	private void go2RouteLib() {
		// Intent intent = new Intent(ManuActivity.this, ExamActivity.class);
		// startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.manu_exam_button:
			go2Exam();
			break;
		case R.id.manu_setpoint_button:
			go2SetPoint();
			break;
		case R.id.manu_setroute_button:
			go2SetRoute();
			break;
		case R.id.manu_pointlib_button:
			go2PointLib();
			break;
		case R.id.manu_routelib_button:
			go2RouteLib();
			break;
		default:
			break;
		}
	}
}
