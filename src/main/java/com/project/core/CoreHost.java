package com.project.core;

import com.project.core.contract.IHost;
import com.project.core.model.Task;
import com.project.core.model.TaskStatus;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CoreHost implements IHost {
    private List<Task> dummyTasks = new ArrayList<>();

    public CoreHost(){
        dummyTasks.add(new Task("T001", "Nộp báo cáo tiến độ", TaskStatus.DONE));
        dummyTasks.add(new Task("T002", "Thiết kế Database", TaskStatus.IN_PROGRESS));
        dummyTasks.add(new Task("T003", "Viết Unit Test", TaskStatus.TODO));
        dummyTasks.add(new Task("T004", "Fix lỗi đăng nhập", TaskStatus.TODO));
        dummyTasks.add(new Task("T005", "Họp nhóm Sprint Review", TaskStatus.IN_PROGRESS));
    }

    @Override
    public List<Task> getAllTasks() {
        //Core cung cap du lieu cho Plugin
        return dummyTasks;
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    //Thuc hien xu ly loi
    @Override
    public void reportError(String message, Exception e) {
        // 1. In loi ra console
        e.printStackTrace();

        // 2. Hien thi thong bao loi cho nguoi dug
        String detailMessage = message + "\nChi tiết kỹ thuật: " + e.getMessage();

        JOptionPane.showMessageDialog(null,
                detailMessage,
                "Lỗi Hệ Thống",
                JOptionPane.ERROR_MESSAGE);
    }


    @Override
    public void createTask(Task task) {
        if (task != null) {
            dummyTasks.add(task);
            System.out.println("Đã thêm công việc: " + task.getTitle());
        }
    }
}
