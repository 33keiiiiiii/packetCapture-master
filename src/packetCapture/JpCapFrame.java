package packetCapture;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;



public class JpCapFrame extends JFrame {
    private static DefaultTableModel model;
    private static JTextField filterField;
    private JTextArea showArea;
    private JButton startBtn;
    private JButton exitBtn;
    private JButton clearBtn;
    private JComboBox choice;

    public JpCapFrame() {
        super();
        initGUI();
    }

    public static DefaultTableModel getModel() {
        return model;
    }

    public JTextArea getShowArea() {
        return showArea;
    }

    public JButton getStartBtn() {
        return startBtn;
    }//开始

    public JComboBox getCheckBtn() { return choice; }//选择网卡

    public JButton getExitBtn() {
        return exitBtn;
    }//退出

    public JButton getClearBtn() { return clearBtn; }//清空

    public static JTextField getFilterField() {
        return filterField;
    }

    public static ImageIcon SetButtonImg(String imgPath,int width,int height)
    {
        ImageIcon icon = new ImageIcon(imgPath);
        Image temp = icon.getImage().getScaledInstance(width, height, icon.getImage().SCALE_DEFAULT);
        icon = new ImageIcon(temp);
        return icon;
    }

    private void initGUI() {
        
        Font font1 = new Font("宋体", Font.BOLD, 15);
        Font font4 = new Font("宋体", Font.BOLD, 14);
        Font font2 = new Font("宋体", Font.PLAIN, 16);
        Font font3 = new Font("微软雅黑", Font.PLAIN, 16);

        //界面
        setSize(800, 600);
        setVisible(true);
        setTitle("Captor");
        Container container = this.getContentPane();

        //顶部
        JPanel pane = new JPanel();
        pane.setBounds(0, 0, 775, 800);
        pane.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pane.setPreferredSize(new Dimension(775, 80));

        //网卡选择
        JPanel netPanel = new JPanel();
        FlowLayout netPanelLayout = new FlowLayout();
        netPanelLayout.setAlignOnBaseline(true);
        netPanel.setBorder(BorderFactory.createTitledBorder("选择网卡:"));
        netPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pane.add(netPanel);
        netPanel.setLayout(netPanelLayout);
        {
            NetworkInterface[] devices = JpcapCaptor.getDeviceList();
            String[] names = new String[devices.length];
            for (int i = 0; i < names.length; i++) {
                names[i] = (devices[i].description == null ? devices[i].name
                        : devices[i].description);
            }
            choice = new JComboBox(names);
            choice.setFont(font4);
            netPanel.add(choice);
        }

        //开始
        startBtn = new JButton();
        startBtn.setPreferredSize(new Dimension(35,35));
        startBtn.setText(null);
        startBtn.setIcon(SetButtonImg("src/images/start.jpg",35,35));
        pane.add(startBtn);

        //清空
        clearBtn = new JButton();
        clearBtn.setPreferredSize(new Dimension(35,35));
        clearBtn.setText(null);
        clearBtn.setIcon(SetButtonImg("src/images/clear.jpg",35,35));
        pane.add(clearBtn);

        //退出
        exitBtn = new JButton();
        exitBtn.setPreferredSize(new Dimension(35,35));
        exitBtn.setText(null);
        exitBtn.setIcon(SetButtonImg("src/images/end.jpg",35,35));
        pane.add(exitBtn);

        //过滤器
        JPanel cardPanel = new JPanel();
        FlowLayout cardPanelLayout = new FlowLayout();
        cardPanelLayout.setAlignOnBaseline(true);
        cardPanel.setBorder(BorderFactory.createTitledBorder("捕获过滤:"));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pane.add(cardPanel);
        cardPanel.setLayout(cardPanelLayout);
        {

            filterField = new JTextField(25);
            filterField.setBounds(200, 0, 500, 0);
            cardPanel.add(filterField);
        }

        //中部主体内容显示区
        String[] name = {"No.", "Time", "Source", "Destination", "Protocol", "Length", "Info"};
        JTable table = new JTable(model);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(font1);
        table.setFont(font2);
        table.setRowHeight(20);
        model = (DefaultTableModel) table.getModel();
        model.setColumnIdentifiers(name);
        table.setEnabled(false);
        JScrollPane jScrollPane = new JScrollPane(table);
        jScrollPane.setBounds(0, 300, 1550, 600);

        //底部
        JPanel pane2 = new JPanel();
        pane2.setLayout(new BorderLayout());
        pane2.setPreferredSize(new Dimension(1550, 200));

        showArea = new JTextArea(5, 5);
        showArea.setEditable(false);
        showArea.setLineWrap(false);
        showArea.setFont(font3);
        pane2.setSize(10, 10);
        pane2.setBounds(0, 0, 1, 1);

        //给textArea添加滚动条
        JScrollPane scrollPane = new JScrollPane(showArea);
        scrollPane.setBounds(0, 0, 1, 1);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        pane2.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(showArea);

        container.add(jScrollPane, BorderLayout.CENTER);
        container.add(pane, BorderLayout.NORTH);
        container.add(pane2, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
