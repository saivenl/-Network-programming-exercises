package wany.qqclient.service;

import wany.qqcommon.Message;
import wany.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/*
改类/对象，提供和消息相关的服务方法
 */
public class MessageClientService {
    /*
    @param content 内容
    @param senderId 发送用户id
     */

    public void sendMessageToAll (String content, String senderId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES); //群发的聊天消息类型
        message.setSender(senderId);
        message.setContent(content);
        message.setSendTime(new Date().toString()); //发送时间设置到message对象
        System.out.println(senderId + " 对大家说 " + content);
        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.
                    getClientConnectSeverThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    @param content 内容
    @param senderId 发送用户id
    @param getterId 接收用户id

     */
    public void sendMessageToOne(String content, String senderId, String getterId) {
        //构建message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES); //普通的聊天消息类型
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString()); //发送时间设置到message对象
        System.out.println(senderId + " 对 " + getterId + " 说 " + content);
        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.
                    getClientConnectSeverThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
