package wany.qqclient.service;

import java.util.HashMap;

public class ManageClientConnectServerThread {
    //吧多个线程放入一个hashmap集合，key是用户id，value就是线程
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();
    //将某个线程加入到集合
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread){
        hm.put(userId, clientConnectServerThread);

    }
    //通过uesrid可以得到对应线程
    static ClientConnectServerThread getClientConnectSeverThread(String userId) {
        return hm.get(userId);
    }
}
