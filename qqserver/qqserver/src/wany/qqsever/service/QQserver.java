package wany.qqsever.service;

import wany.qqcommon.Message;
import wany.qqcommon.MessageType;
import wany.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Provider;
import java.util.HashMap;

/*
这是服务端，在监听9999，等待客户端连接，并保持通信
 */
public class QQserver {

    private ServerSocket ss = null;
    //创建一个集合，存放多个用户，如果是这些用户登陆，就认为是合法
    //这里我们也可以使用concurrentHashMap，可以处理并发的集合，没有线程安全
    //HashMap没有处理线程安全，因此在多线程情况下不安全
    //ConcurrentHashMap处理的线程安全，即线程同步处理，在多线程情况下是安全的
    private static HashMap<String, User> validUsers = new HashMap<>();
    static { //在静态代码块，初始化validUsers
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("至尊宝", new User("至尊宝", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("老祖", new User("老祖", "123456"));

    }
    //验证用户是否有效的方法
    private boolean checkUser(String userId, String password){
        User user = validUsers.get(userId);
        //过关的验证方式

        if (user == null){ //说明userid没有存在validUser的key中
            return false;
        }
        if (!user.getPassword().equals(password)){ //userid存在但是密码错误
            return false;
        }
        return true;
    }


    public QQserver() {

        try {
            System.out.println("服务端在9999端口监听");
            //启动推送新闻的线程
            new Thread(new SendNewsToAllService()).start();
            ss = new ServerSocket(9999);
            while (true) {//当和某个客户端连接后，会继续监听，因此while
                Socket socket = ss.accept(); //如果没有客户端连接，就会阻塞在这里
                //得到socket关联的对象输入流
                ObjectInputStream ois =
                        new ObjectInputStream(socket.getInputStream());
                //得到socket关联的对象输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u = (User) ois.readObject();//读取客户端发送的User对象
                //创建一个message对象，准备回复客户端
                Message message = new Message();
                //验证用户 方法
                if (checkUser(u.getUserId(), u.getPassword())) {//合法
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCESS);
                    //将Message对象回复客户端

                    oos.writeObject(message);
                    //创建一个线程，和客户端保持通信，该线程需要持有socket对象
                    ServerConnectClientThread serverConnectClientThread =
                            new ServerConnectClientThread(socket, u.getUserId());
                    //启动该线程
                    serverConnectClientThread.start();
                    //把该线程对象放到一个集合中进行管理
                    ManageClientThreads.addClientThread(u.getUserId(), serverConnectClientThread);

                } else { //登陆失败
                    System.out.println("用户 id=" + u.getUserId() + "pwd=" + u.getPassword()+ "验证失败");
                   message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                   oos.writeObject(message);
                   //关闭socket
                    socket.close();
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        } finally {
            //如果服务器推出了while，说明服务器不在监听，因此关闭ServerSocket
            try {
                if (ss != null) {
                    ss.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
