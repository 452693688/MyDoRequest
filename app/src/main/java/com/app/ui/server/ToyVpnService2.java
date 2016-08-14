package com.app.ui.server;

import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import com.app.ui.utile.DLog;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

/**
 * 参考：http://www.ithao123.cn/content-8040762.html
 * Created by Administrator on 2016/8/10.
 */
public class ToyVpnService2 extends VpnService {


    private static final String TAG = "ToyVpnService2";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DLog.e(TAG, "启动服务");
        run();
        return super.onStartCommand(intent, flags, startId);
    }

    private void run() {
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initPort();
            }
        });
    }

    private void initPort() {
        DLog.e(TAG, "出始化端口");
        Builder builder = new Builder();
        //即表示虚拟网络端口的最大传输单元，如果发送的包长度超过这个数字，则会被分包；
        builder.setMtu(1500);
        //Address，即这个虚拟网络端口的IP地址；
        builder.addAddress("114.255.40.212", 24);
        //其实这里并不是用来修改Android设备上的路由表，
        // 而是用来改iptables的NAT表，只有匹配上的IP包，
        // 才会被转发到虚拟端口上去。如果是0.0.0.0/0的话，
        // 则会将所有的IP包都通过NAT转发到虚拟端口上去；
        builder.addRoute("0.0.0.0", 0);
        //就是该端口的DNS服务器地址；
        // builder.addDnsServer(...);
        //就是添加DNS域名的自动补齐。DNS服务器必须通过全域名进行搜索，
        // 但每次查找都输入全域名太麻烦了，可以通过配置域名的自动补齐规则予以简化；
        // builder.addSearchDomain(...);
        //就是你要建立的VPN连接的名字，
        // 它将会在系统管理的与VPN连接相关的通知栏和对话框中显示出来；
        builder.setSession("guomin");
        //这个intent指向一个配置页面，用来配置VPN链接。它不是必须的，
        // 如果没设置的话，则系统弹出的VPN相关对话框中不会出现配置按钮。
        // builder.setConfigureIntent(...);
        //如果一切正常的话，tun0虚拟网络接口就建立完成了。
        // 并且，同时还会通过iptables命令，修改NAT表，将所有数据转发到tun0接口上。
        ParcelFileDescriptor mInterface = builder.establish();

        FileInputStream in = new FileInputStream(
                mInterface.getFileDescriptor());
        //b. Packets received need to be written to this output stream.
        FileOutputStream out = new FileOutputStream(
                mInterface.getFileDescriptor());
        try {
            //c. The UDP channel can be used to pass/get ip package to/from server
            DatagramChannel tunnel = DatagramChannel.open();
            // Connect to the server, localhost is used for demonstration only.
            tunnel.connect(new InetSocketAddress("127.0.0.1", 8087));
            //d. Protect this socket, so package send by it will not be feedback to the vpn service.
            protect(tunnel.socket());
            //e. Use a loop to pass packets.
            while (true) {
                //get packet with in
                //put packet to tunnel
                //get packet form tunnel
                //return packet with out
                //sleep is a must
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            DLog.e(TAG, "关闭端口");
            closeInterface(mInterface);
        }

    }

    private void closeInterface(ParcelFileDescriptor mInterface) {
        if (mInterface == null) {
            return;
        }
        try {
            mInterface.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mInterface = null;
            DLog.e(TAG, "关闭接口");
        }
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, ToyVpnService.class);
        context.startService(intent);
    }
}
