package com.candystudio.android.dxzs.teacher.task;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import com.candystudio.android.dxzs.teacher.tool.CommunicationTool;

import android.os.AsyncTask;

public class SendMsgTask extends AsyncTask<String, Integer, Boolean> {

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		sendamsg();
		return null;
	}
	
	private void sendamsg()
	{
		
		String ToWho="chengguo@work-pc/Smack";
		String MSG="hello first message";
		MessageListener msgListener = new MessageListener() {
			@Override
			public void processMessage(Chat chat, Message message) {
//				LogUtil.i("zzh", message.toXML());
			}
		};
		CommunicationTool.SendMessage(ToWho, MSG, msgListener);
	}

}
