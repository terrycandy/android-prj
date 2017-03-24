package com.candystudio.android.dxzs.teacher.service;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import com.candystudio.android.dxzs.teacher.ultility.LogUtil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class ListenMessageService extends Service
{

	private static MyMessageListener mMyessageListener;
	private ChatManager chatManager;
	ChatManagerListener chatManagerListener;
	
	public static void actionStart(Context context,MyMessageListener myMessageListener)
	{
		Intent intent=new Intent(context,ListenMessageService.class);
		context.startService(intent);
		mMyessageListener=myMessageListener;
	}
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		chatManagerListener=new ChatManagerListener()
		{
			
			@Override
			public void chatCreated(Chat chat, boolean createdLocally)
			{
				chat.addMessageListener(new MessageListener()
				{
					
					@Override
					public void processMessage(Chat chat, Message message)
					{
						LogUtil.i("zzh", "in listen service get mesage from"+message.toXML());
						mMyessageListener.OnMessageListener(message);
					}
				});
				
			}
		};
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				
//			    chatManager=ConnectionTool.getConnection().getChatManager();
//				
//				chatManager.addChatListener(chatManagerListener );
				
			}
		}).start();
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		chatManager.removeChatListener(chatManagerListener);
		super.onDestroy();
	}
	
	public interface MyMessageListener
	{
		void OnMessageListener(Message message);
	}

}
