package com.project.app;

import com.project.core.model.Task;
import com.project.core.model.TaskStatus;

import javax.swing.*;
import java.awt.*;

public class CreateTaskDialog extends JDialog {
    private JTextField txtId, txtTitle;
    private JComboBox<TaskStatus> cbStatus;
    private JButton btnSave;
    private Task newTask;

    public CreateTaskDialog(Frame owner) {
        super(owner, "Tạo Task Mới", true);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("ID:"));
        txtId = new JTextField();
        add(txtId);

        add(new JLabel("Tiêu đề:"));
        txtTitle = new JTextField();
        add(txtTitle);

        add(new JLabel("Trạng thái:"));
        cbStatus = new JComboBox<>(TaskStatus.values());
        add(cbStatus);

        btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            // Sử dụng constructor 3 tham số chúng ta vừa thêm ở bước trước
            newTask = new Task(txtId.getText(), txtTitle.getText(), (TaskStatus) cbStatus.getSelectedItem());
            dispose();
        });
        add(btnSave);

        pack();
        setLocationRelativeTo(owner);
    }

    public Task getNewTask() { return newTask; }
}