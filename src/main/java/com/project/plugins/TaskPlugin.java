package com.project.plugins;

import com.project.core.contract.IHost;
import com.project.core.contract.IPlugin;
import com.project.core.model.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TaskPlugin implements IPlugin {
    private IHost host;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JButton btnLoad;

    @Override
    public String getName() {
        return "Task Management";
    }

    @Override
    public void initialize(IHost host) {
        this.host = host;
    }

    @Override
    public JComponent getMainView() {
        JPanel panel = new JPanel(new BorderLayout());

        // 1. Tạo bảng hiển thị dữ liệu (Table)
        String[] columnNames = {"ID", "Title", "Status", "Deadline"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);

        // 2. Toolbar chứa nút bấm
        JToolBar toolBar = new JToolBar();
        btnLoad = new JButton("Load Tasks (Async)");

        // Sự kiện bấm nút: Gọi SwingWorker
        btnLoad.addActionListener(e -> loadTasksWithSwingWorker());

        toolBar.add(btnLoad);
        toolBar.add(new JButton("Create Task")); // Placeholder

        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(taskTable), BorderLayout.CENTER);

        return panel;
    }

    // --- PHẦN QUAN TRỌNG: SWING WORKER (ĐA LUỒNG) ---
    private void loadTasksWithSwingWorker() {
        // Vô hiệu hóa nút bấm để tránh bấm nhiều lần
        btnLoad.setEnabled(false);
        btnLoad.setText("Loading...");

        // Tạo Worker
        SwingWorker<List<Task>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Task> doInBackground() throws Exception {
                // GIẢ LẬP ĐỘ TRỄ MẠNG/DATABASE (2 giây)
                // Đây là nơi xử lý logic nặng, chạy ngầm, không làm đơ giao diện
                Thread.sleep(2000);

                // Lấy dữ liệu từ Core
                return host.getAllTasks();
            }

            @Override
            protected void done() {
                // Hàm này tự động chạy trên luồng giao diện (EDT) khi xong việc
                try {
                    List<Task> tasks = get(); // Lấy kết quả từ doInBackground
                    updateTable(tasks);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi tải dữ liệu!");
                } finally {
                    // Mở lại nút bấm
                    btnLoad.setEnabled(true);
                    btnLoad.setText("Load Tasks (Async)");
                }
            }
        };

        // Kích hoạt worker
        worker.execute();
    }

    private void updateTable(List<Task> tasks) {
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);
        // Thêm dữ liệu mới
        for (Task t : tasks) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getTitle(),
                    t.getStatus().getLabel(),
                    t.getDeadline() != null ? t.getDeadline().toString() : ""
            });
        }
    }
}