package com.candystudio.android.dxzs.teacher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.candystudio.android.dxzs.teacher.app.MyApplication;
import com.candystudio.android.dxzs.teacher.app.R;
import com.candystudio.android.dxzs.teacher.task.LoginThread;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText usernameEditText;
	private EditText passwordEditText;
	private Button loginButton;
	private Button registerButton;
	private Button exitButton;
	private LoginThread loginThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initLayoutMember();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private void initLayoutMember() {
		usernameEditText = (EditText) findViewById(R.id.login_username_edittext);
		passwordEditText = (EditText) findViewById(R.id.login_password_edittext);
		loginButton = (Button) findViewById(R.id.login_login_button);
		registerButton = (Button) findViewById(R.id.login_register_button);
		exitButton = (Button) findViewById(R.id.login_exit_button);
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);
	}

	private void login() {

		///≤‚ ‘£∫Ã¯π˝—È÷§
		go2Manu();
		///
		
//		String userName = usernameEditText.getText().toString();
//		String password = passwordEditText.getText().toString();
//
//		if (userName == "" || password == "") {
//			Toast.makeText(this, "Username or password is empty!",
//					Toast.LENGTH_LONG).show();
//			return;
//		} else {
//			try {
//				loginThread = new LoginThread(userName, password);
//				loginThread.start();
//				loginThread.join();
//				if (loginThread.inflag == 1) {
//					Toast.makeText(this, "Login success!", Toast.LENGTH_LONG)
//							.show();
//					go2Manu();
//				}
//
//				else if (loginThread.inflag == 2) {
//					Toast.makeText(this, "Username or password is wrong!",
//							Toast.LENGTH_LONG).show();
//				} else if (loginThread.inflag == 3) {
//					Toast.makeText(this, "Server socket is closed!",
//							Toast.LENGTH_LONG).show();
//				} else if (loginThread.inflag == 4) {
//					Toast.makeText(this, "Server IO is closed!",
//							Toast.LENGTH_LONG).show();
//				}
//
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
//////
		// LoginTask task = new LoginTask();
		// task.execute(userName, password);

	}

	private void go2Manu() {
		Intent intent = new Intent(LoginActivity.this, ManuActivity.class);
		// intent.putExtra(name, value)
		startActivity(intent);
	}

	private void go2Register() {
		// Intent intent = new Intent(LoginActivity.this, Register.class);
		// intent.putExtra(name, value)
		// startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_login_button:
			login();
			break;
		case R.id.login_register_button:
			go2Register();
			break;
		case R.id.login_exit_button:

			break;
		default:
			break;
		}
	}

}
