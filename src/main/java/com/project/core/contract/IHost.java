package com.project.core.contract;

import com.project.core.model.Task;

import java.util.List;

public interface IHost {
    //Lay danh sach cong viec
    List<Task> getAllTasks();

    //Ham nay yeu cau core hien thi mot thong bao
    void showMessage(String message);

    //Ham de plugin bao loi ve cho core xu ly
    void reportError(String message, Exception e) ;

    void createTask(Task task);
}
