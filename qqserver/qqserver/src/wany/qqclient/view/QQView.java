package wany.qqclient.view;

/*
客户端的菜单界面
 */
public class QQView {
    private boolean loop = true;
    private String key = "";//控制是否显示主菜单

    //显示主菜单
    private void mainMenu() {
        while (loop) {
            System.out.println("=======欢迎登陆网络通信系统======");
            System.out.println("\t\t 1 登陆系统");
            System.out.println("\t\t 9 退出系统");
        }
    }
}
