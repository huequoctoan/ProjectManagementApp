package com.project.core.contract;

import javax.swing.*;

public interface IPlugin {
    //ten plugin (de hien thi tren menu)
    String getName();

    //ham khoi tao
    void initialize(IHost host);

    // giao dien chinh cua plugin
    JComponent getMainView();
}
