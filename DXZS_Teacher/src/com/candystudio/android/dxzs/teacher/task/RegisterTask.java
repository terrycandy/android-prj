package com.candystudio.android.dxzs.teacher.task;

import com.candystudio.android.dxzs.teacher.tool.CommunicationTool;

import android.os.AsyncTask;

public class RegisterTask extends AsyncTask<String, Integer, Boolean> {

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub

		CommunicationTool.Register(params[0], params[1]);
		return true;

	}

	@Override
	protected void onPostExecute(final Boolean success) {
		// Toast.makeText(LoginActivity.this, "Login Successful", 2000).show();
	}

	@Override
	protected void onCancelled() {

	}

}
