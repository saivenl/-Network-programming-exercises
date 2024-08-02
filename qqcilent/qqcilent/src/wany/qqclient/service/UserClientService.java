package wany.qqclient.service;

import wany.qqcommon.Message;
import wany.qqcommon.MessageType;
import wany.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/*
完成用户登录注册功能的类
 */
public class UserClientService {
    private User u = new User();//User做成属性，因为可能在其他地方使用user信息
    private Socket socket;
    public boolean checkUser(String userId, String pwd){
        boolean b = false;
        //创建user对象
        u.setUserId(userId);
        u.setPasswd(pwd);

        try {
            //连接到服务端，发送u对象
             socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
             //得到ObjectOutputStream对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u); //发送user对象
            //读取从服务器回复的message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();
            if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCESS)){
                b = true;
                //创建一个和服务器端保持通信的线程->创建一个类ClientConnectSeverThread
                ClientConnectServerThread clientConnectSeverThread = new ClientConnectServerThread(socket);
                //启动客户端的线程
                clientConnectSeverThread.start();
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectSeverThread);
                b = true;


            } else {
                //如果登陆失败，就不能启动和服务器通信的线程，关闭socket
                socket.close();

            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return b;

        }
        //向服务器端请求在线用户列表
        public void onlineFriendList() {
            //发送一个 Message,类型MESSAGE_GET_ONLINE_FRIEND
            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
            message.setSender(u.getUserId());
            //发送给服务器
            //应该得到当前线程的Socket 对应的ObjectOutputStream对象
            try {
                //从管理线程的集合中，通过userid得到这个线程
                ClientConnectServerThread clientConnectSeverThread =
                        ManageClientConnectServerThread.getClientConnectSeverThread(u.getUserId());
                //得到这个线程得到关联的socket
                Socket socket = clientConnectSeverThread.getSocket();
                ObjectOutputStream oos = new ObjectOutputStream
                        (socket.getOutputStream());
                oos.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //编写方法，退出客户端，并给服务端发送一个退出系统的message对象
    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());//一定要指定我是哪个客户端id

        //发送message
        try {
            //ObjectOutputStream oos =
                    new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.
                            getClientConnectSeverThread(u.getUserId()).
                            getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId() + "退出系统");
            System.exit(0);//结束进程
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
