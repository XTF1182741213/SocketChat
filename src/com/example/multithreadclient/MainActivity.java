package com.example.multithreadclient;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;



import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText input, show,ip;
	TextView mode;
	Button send,smode,cmode;
	OutputStream os;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		input = (EditText) findViewById(R.id.input);
		send = (Button) findViewById(R.id.send);
		show = (EditText) findViewById(R.id.show);
		ip = (EditText) findViewById(R.id.iip);
		
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 0x123) {
					show.append("\n" + msg.obj.toString());
				}
			};
		};
		new AsyncTask<String, String, String>(){

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Socket s;
				try {
					/**@ param param1 param2
					 * param1: 服务器ip地址，本例中，即pc机的ip地址 cmd输入ipconfig -all可查询
					 * param2：端口号 注意和服务器源码中的匹配
					 * */
					s = new Socket(ip.getText().toString(), 60000);
					//客户端启动ClientThread线程，不断读取来自服务器的数据
					new Thread(new ClientThread(s, handler)).start();
					os = s.getOutputStream();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
		}.execute("");
		
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					//将用户在文本框中输入的内容写入网络
					os.write((input.getText().toString() + "\r\n")
							.getBytes("utf-8"));
					input.setText("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			os.write("".getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
