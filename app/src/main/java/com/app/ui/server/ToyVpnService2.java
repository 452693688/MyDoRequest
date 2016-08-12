package com.app.ui.server;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

/**
 * 参考：http://www.ithao123.cn/content-8040762.html
 * Created by Administrator on 2016/8/10.
 */
public class ToyVpnService2 extends VpnService {


    private static final String TAG = "ToyVpnService2";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void initPort() {
        Builder builder = new Builder();
        //即表示虚拟网络端口的最大传输单元，如果发送的包长度超过这个数字，则会被分包；
        builder.setMtu(100);
        //Address，即这个虚拟网络端口的IP地址；
        builder.addAddress(192.168.1);
        //其实这里并不是用来修改Android设备上的路由表，
        // 而是用来改iptables的NAT表，只有匹配上的IP包，
        // 才会被转发到虚拟端口上去。如果是0.0.0.0/0的话，
        // 则会将所有的IP包都通过NAT转发到虚拟端口上去；
        builder.addRoute(...);
        //就是该端口的DNS服务器地址；
        builder.addDnsServer(...);
        //就是添加DNS域名的自动补齐。DNS服务器必须通过全域名进行搜索，
        // 但每次查找都输入全域名太麻烦了，可以通过配置域名的自动补齐规则予以简化；
        builder.addSearchDomain(...);
        //就是你要建立的VPN连接的名字，
        // 它将会在系统管理的与VPN连接相关的通知栏和对话框中显示出来；
        builder.setSession(...);
        //这个intent指向一个配置页面，用来配置VPN链接。它不是必须的，
        // 如果没设置的话，则系统弹出的VPN相关对话框中不会出现配置按钮。
        builder.setConfigureIntent(...);
        //如果一切正常的话，tun0虚拟网络接口就建立完成了。
        // 并且，同时还会通过iptables命令，修改NAT表，将所有数据转发到tun0接口上。
        ParcelFileDescriptor interfacePro = builder.establish();
    }

    private void test() {
    }
}
