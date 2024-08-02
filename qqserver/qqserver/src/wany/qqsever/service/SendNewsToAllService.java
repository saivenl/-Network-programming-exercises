package wany.qqsever.service;

import wany.qqclient.Utility.Utility;
import wany.qqcommon.Message;
import wany.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class SendNewsToAllService implements  Runnable{

    @Override
    public void run() {
       //为了推送多次新闻，使用while
        while(true){

        System.out.println("请输入服务器要推送的新闻/消息[输入exit表示退出推送服务线程]");
        String news = Utility.readString(100);

        if("exit".equals(news)){
            System.out.println("退出推送服务线程");
            break;
        }
        //构建一个消息，群发消息
        Message message = new Message();

        message.setSender("服务器");
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES); // 设置消息类型
        message.setContent(news);
        message.setSendTime(new Date().toString());
        System.out.println("服务器推送消息给所有人 说: " + news);
        //遍历当前所有的通信线程，得到socket，并发送message
        HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
        Iterator<String> iterator = hm.keySet().iterator();
        while (iterator.hasNext()) {
            String onLineUserId = iterator.next().toString();
            ServerConnectClientThread clientThread = hm.get(onLineUserId);
            try {
                ObjectOutputStream oos = new ObjectOutputStream
                        (hm.get(onLineUserId).getSocket().getOutputStream());
                oos.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }
    }
}
