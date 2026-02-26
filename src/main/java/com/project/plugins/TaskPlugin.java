package com.project.plugins;

import com.project.app.CreateTaskDialog;
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

        // 1. Cấu hình bảng hiển thị (Table)
        String[] columnNames = {"ID", "Title", "Status", "Deadline"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);

        // 2. Tạo Toolbar và các nút chức năng
        JToolBar toolBar = new JToolBar();

        // Nút Load dữ liệu bất đồng bộ
        btnLoad = new JButton("Load Tasks (Async)");
        btnLoad.addActionListener(e -> loadTasksWithSwingWorker());

        // Nút Thêm mới Task
        JButton btnCreate = new JButton("Create Task");
        btnCreate.addActionListener(e -> {
            // Lấy Frame cha (DashboardFrame) để làm owner cho Dialog
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(panel);

            // Mở Dialog nhập liệu
            CreateTaskDialog dialog = new CreateTaskDialog(parentFrame);
            dialog.setVisible(true);

            // Lấy kết quả từ Dialog sau khi người dùng bấm Lưu
            Task newTask = dialog.getNewTask();
            if (newTask != null) {
                // Lưu vào danh sách trong CoreHost
                host.createTask(newTask);

                // Cập nhật lại giao diện bảng ngay lập tức để người dùng thấy kết quả
                updateTable(host.getAllTasks());

                JOptionPane.showMessageDialog(panel, "Thêm công việc mới thành công!");
            }
        });

        // Thêm các nút vào Toolbar
        toolBar.add(btnLoad);
        toolBar.addSeparator(); // Khoảng cách nhỏ giữa các nút
        toolBar.add(btnCreate);

        // 3. Sắp xếp bố cục cho Panel chính
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(taskTable), BorderLayout.CENTER);

        return panel;
    }


    private void loadTasksWithSwingWorker() {
        // Vô hiệu hóa nút bấm để tránh bấm nhiều lần
        btnLoad.setEnabled(false);
        btnLoad.setText("Loading...");

        // Tạo Worker
        SwingWorker<List<Task>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Task> doInBackground() throws Exception {

                Thread.sleep(2000);

                return host.getAllTasks();
            }

            @Override
            protected void done() {
                try {
                    List<Task> tasks = get();
                    updateTable(tasks);
                } catch (InterruptedException | ExecutionException e) {
                    host.reportError("Không thể tải danh sách công việc!", e);
                } finally {
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