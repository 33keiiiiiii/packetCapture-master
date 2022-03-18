package packetCapture;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static packetCapture.JpCapFrame.SetButtonImg;

public class JpCapMain implements Runnable {
    JpCapFrame frame;
    JpcapCaptor jpcap = null;
    private static Thread thread = null;
    private static boolean pause = true;

    public JpCapMain() {
        //创建界面
        frame = new JpCapFrame();
        frame.setVisible(true);

        //绑定网络设备
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();

        int caplen = 1512;
        boolean promiscCheck = true;

        int device = 1;
        try {
            jpcap = JpcapCaptor.openDevice(devices[device], caplen, promiscCheck, 50);
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.getCheckBtn().addActionListener(e -> {
            frame.getShowArea().append("当前设备的网络设备信息为： \n");
            for (NetworkInterface n : devices) {
                System.out.println(n.description+"   :   "+n.name);
                frame.getShowArea().append(n.description+"   :   "+n.name + "\n");
            }
            frame.getShowArea().append("\n当前使用网卡信息： " + devices[device].description+"   :   "+devices[device].name + "\n");
            frame.getShowArea().append(printSeparator(110, 1));
        });

        frame.getStartBtn().addActionListener(e -> {
            if (pause) {
                if (thread == null) {
                    frame.getShowArea().append("开始抓包\n");
                    thread = new Thread(this);
                    thread.setPriority(Thread.MIN_PRIORITY);
                    thread.start();
                    pause = false;//开始按钮修改为暂停
                    frame.getStartBtn().setText(null);
                    frame.getStartBtn().setIcon(SetButtonImg("src/images/pause.jpg",35,35));
                } else {//暂停
                    frame.getStartBtn().setText(null);
                    frame.getStartBtn().setIcon(SetButtonImg("src/images/pause.jpg",35,35));
                    pause = false;
                    if(JpCapFrame.getFilterField().getText().equals(""))
                        frame.getShowArea().append("继续抓包\n");
                    else
                        frame.getShowArea().append("继续抓包,抓取范围为：" + JpCapFrame.getFilterField().getText() + "\n");
                    synchronized (thread) {
                        thread.notify();
                    }
                }
            } else {//暂停按钮修改为开始
                pause = true;
                frame.getStartBtn().setText(null);
                frame.getStartBtn().setIcon(SetButtonImg("src/images/start.jpg",35,35));
                frame.getShowArea().append("暂停抓包\n");
            }
        });

        frame.getClearBtn().addActionListener(e -> {
            frame.getShowArea().setText("");
            frame.getModel().setRowCount(0);
        });

        frame.getExitBtn().addActionListener(e -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        new JpCapMain();
    }

    @Override
    public void run() {
        try {
            new JpCapPacket(jpcap).capture();
            thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param separator "-"的数量
     * @param line      "\n"的数量
     * @return
     */
    public String printSeparator(int separator, int line) {
        String s = "";
        String l = "";

        for (int i = 0; i < separator; i++) {
            s += "-";
        }

        for (int i = 0; i < line; i++) {
            l += "\n";
        }
        return s + l;
    }

    public static Thread getThread() {
        return thread;
    }

    public static boolean isPause() {
        return pause;
    }
}