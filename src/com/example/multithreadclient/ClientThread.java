package com.example.multithreadclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

public class ClientThread implements Runnable {

	private Socket s;	//该线程负责处理的Socket
	private Handler handler;
	//该线程所处理的Socket对应的输入流
	BufferedReader br = null;

	public ClientThread(Socket s, Handler handler) throws IOException {
		this.s = s;
		this.handler = handler;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String content = null;
			//不断读取Socket输入流中的内容
			while ((content = br.readLine()) != null) {
				//每当读到来自服务器的数据后，发送消息通知程序界面显示数据
				Message msg = new Message();
				msg.what = 0x123;
				msg.obj = content;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
