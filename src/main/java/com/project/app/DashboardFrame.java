package com.project.app;

import com.project.core.contract.IPlugin;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private JTabbedPane tabbedPane;

    public DashboardFrame() {
        setTitle("Project Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // can giua

        // tao menu bar
        initMenu();

        //layout chinh
        setLayout(new BorderLayout());

        //Noi chua cac plugin
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        //Thanh trang thai (Status bar) ben duoi
        JLabel statusLabel = new JLabel(" Ready");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }
        private void initMenu() {
            JMenuBar menuBar = new JMenuBar();

            // Menu File
            JMenu fileMenu = new JMenu("File");
            JMenuItem exitItem = new JMenuItem("Exit");
            exitItem.addActionListener(e -> System.exit(0));
            fileMenu.add(exitItem);

            // Menu Help
            JMenu helpMenu = new JMenu("Help");
            JMenuItem aboutItem = new JMenuItem("About");
            aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đồ án Java Swing - Version 1.0"));
            helpMenu.add(aboutItem);

            menuBar.add(fileMenu);
            menuBar.add(helpMenu);
            setJMenuBar(menuBar);
        }
        // Hàm để thêm Plugin vào giao diện
        public void addPlugin(IPlugin plugin) {
            tabbedPane.addTab(plugin.getName(), plugin.getMainView());
        }

}
