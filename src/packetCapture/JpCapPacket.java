package packetCapture;

import jpcap.JpcapCaptor;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

import java.sql.Timestamp;
import java.util.Vector;

public class JpCapPacket {
    private JpcapCaptor jpcap;

    public JpCapPacket(JpcapCaptor jpcap) {
        this.jpcap = jpcap;
    }

    void capture() throws InterruptedException {
        int i = 0;
        while (true) {
            synchronized (JpCapMain.getThread()) {
                if (JpCapMain.isPause()) {
                    JpCapMain.getThread().wait();
                }
            }
            Packet packet = jpcap.getPacket();
            if (packet instanceof IPPacket && ((IPPacket) packet).version == 4) {
                i++;
                IPPacket ip = (IPPacket) packet;//强转

                String protocol = "";
                switch (Integer.valueOf(ip.protocol)) {
                    case 1:
                        protocol = "ICMP";
                        break;
                    case 2:
                        protocol = "IGMP";
                        break;
                    case 6:
                        protocol = "TCP";
                        break;
                    case 8:
                        protocol = "EGP";
                        break;
                    case 9:
                        protocol = "IGP";
                        break;
                    case 17:
                        protocol = "UDP";
                        break;
                    case 41:
                        protocol = "IPv6";
                        break;
                    case 89:
                        protocol = "OSPF";
                        break;
                    default:
                        break;
                }

                String filterInput = JpCapFrame.getFilterField().getText();
                if (filterInput.equals(ip.src_ip.getHostAddress()) ||
                        filterInput.equals(ip.dst_ip.getHostAddress()) ||
                        filterInput.equals(protocol) ||
                        filterInput.equals("")) {
                    Vector dataVector = new Vector();
                    Timestamp timestamp = new Timestamp((packet.sec * 1000) + (packet.usec / 1000));

                    dataVector.addElement(i + "");
                    dataVector.addElement(timestamp.toString());//数据包时间
                    dataVector.addElement(ip.src_ip.getHostAddress());
                    dataVector.addElement(ip.dst_ip.getHostAddress());
                    dataVector.addElement(protocol);
                    dataVector.addElement(packet.data.length);

                    String strtmp = "";
                    for (int j = 0; j < packet.data.length; j++) {
                        strtmp += Byte.toString(packet.data[j]);
                    }
                    dataVector.addElement(strtmp); //数据内容

                    JpCapFrame.getModel().addRow(dataVector);
                }
            }
        }
    }
}
