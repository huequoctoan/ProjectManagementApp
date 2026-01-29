package com.project;

import com.project.app.DashboardFrame;
import com.project.core.CoreHost;
import com.project.core.PluginLoader;
import com.project.core.contract.IPlugin;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            CoreHost host = new CoreHost();
            PluginLoader loader = new PluginLoader();

            DashboardFrame dashboard = new DashboardFrame();

            // Nap plugin
            List<IPlugin> plugins = loader.loadPlugins();
            for (IPlugin plugin : plugins) {
                plugin.initialize(host);
                dashboard.addPlugin(plugin); // them vao dashboard
            }


            dashboard.setVisible(true);
        });

    }
}