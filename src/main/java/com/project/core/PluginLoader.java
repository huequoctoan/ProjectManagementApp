package com.project.core;

import com.project.core.contract.IPlugin;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PluginLoader {

    public List<IPlugin> loadPlugins(){
        List<IPlugin> plugins = new ArrayList<>();

        try{
            String packageName = "com.project.plugins";
            String path = packageName.replace('.','/');
            URL resource = ClassLoader.getSystemClassLoader().getResource(path);

            if (resource == null) {
                System.out.println("Không tìm thấy thư mục plugins!");
                return plugins;
            }
            //chuan hoa duong dan file
            File dir = new File(resource.toURI());

            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".class")) {

                    // Lay ten class, bo duoi .class. Thay the
                    String className = packageName + "." + file.getName().replace(".class", "");

                    // dung reflection de nap class vao bo nho
                    Class<?> loadedClass = Class.forName(className);

                    // kiem tra xem day co phai la plugin hay khong
                    // tranh truong hop nap nham class rac (khong phai plugin)
                    if (IPlugin.class.isAssignableFrom(loadedClass) && !loadedClass.isInterface()) {

                        // tao mot instance moi tu class do
                        IPlugin plugin = (IPlugin) loadedClass.getDeclaredConstructor().newInstance();
                        plugins.add(plugin);

                        System.out.println("Đã nạp thành công Plugin: " + plugin.getName());
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return plugins;
    }
}
