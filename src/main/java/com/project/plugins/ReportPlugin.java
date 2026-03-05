package com.project.plugins;

import com.project.core.contract.IHost;
import com.project.core.contract.IPlugin;
import com.project.core.model.Task;
import com.project.core.model.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportPlugin implements IPlugin {
    private IHost host;
    private JPanel mainPanel;
    private JComboBox<String> reportTypeCombo;
    private JComboBox<Project> projectCombo;
    private JTextArea reportArea;
    private JButton btnGenerate;
    private JButton btnExport;
    private JProgressBar progressBar;

    @Override
    public String getName() {
        return "Báo cáo & Thống kê";
    }

    @Override
    public void initialize(IHost host) {
        this.host = host;
    }

    @Override
    public JComponent getMainView() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel điều khiển phía trên
        JPanel controlPanel = createControlPanel();
        
        // Panel hiển thị báo cáo
        JPanel reportPanel = createReportPanel();
        
        // Thanh tiến trình
        progressBar = new JProgressBar();
        progressBar.setVisible(false);

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(reportPanel, BorderLayout.CENTER);
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Tùy chọn báo cáo"));

        // Loại báo cáo
        panel.add(new JLabel("Loại báo cáo:"));
        String[] reportTypes = {
            "Tổng quan dự án", 
            "Tiến độ công việc", 
            "Hiệu suất nhân viên",
            "Công việc quá hạn",
            "Thống kê theo trạng thái"
        };
        reportTypeCombo = new JComboBox<>(reportTypes);
        panel.add(reportTypeCombo);

        // Chọn dự án
        panel.add(new JLabel("Dự án:"));
        projectCombo = new JComboBox<>();
        refreshProjectList();
        panel.add(projectCombo);

        // Nút tạo báo cáo
        btnGenerate = new JButton("📊 Tạo báo cáo");
        btnGenerate.addActionListener(this::generateReport);
        panel.add(btnGenerate);

        // Nút xuất báo cáo
        btnExport = new JButton("💾 Xuất file");
        btnExport.addActionListener(this::exportReport);
        btnExport.setEnabled(false);
        panel.add(btnExport);

        return panel;
    }

    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nội dung báo cáo"));

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void refreshProjectList() {
        projectCombo.removeAllItems();
        projectCombo.addItem(null); // Tất cả dự án
        List<Project> projects = host.getAllProjects();
        if (projects != null) {
            projects.forEach(projectCombo::addItem);
        }
    }

    private void generateReport(ActionEvent e) {
        String reportType = (String) reportTypeCombo.getSelectedItem();
        Project selectedProject = (Project) projectCombo.getSelectedItem();

        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        btnGenerate.setEnabled(false);

        // Tạo thread riêng để xử lý báo cáo
        new Thread(() -> {
            String report = "";
            switch (reportType) {
                case "Tổng quan dự án":
                    report = generateProjectOverview(selectedProject);
                    break;
                case "Tiến độ công việc":
                    report = generateTaskProgress(selectedProject);
                    break;
                case "Hiệu suất nhân viên":
                    report = generateUserPerformance(selectedProject);
                    break;
                case "Công việc quá hạn":
                    report = generateOverdueTasks(selectedProject);
                    break;
                case "Thống kê theo trạng thái":
                    report = generateStatusStatistics(selectedProject);
                    break;
            }

            String finalReport = report;
            SwingUtilities.invokeLater(() -> {
                reportArea.setText(finalReport);
                progressBar.setVisible(false);
                btnGenerate.setEnabled(true);
                btnExport.setEnabled(true);
            });
        }).start();
    }

    private String generateProjectOverview(Project project) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(80)).append("\n");
        sb.append("BÁO CÁO TỔNG QUAN DỰ ÁN\n");
        sb.append("=".repeat(80)).append("\n\n");
        
        List<Project> projects = project != null ? List.of(project) : host.getAllProjects();
        
        for (Project p : projects) {
            sb.append("Dự án: ").append(p.getName()).append("\n");
            sb.append("Mô tả: ").append(p.getDescription()).append("\n");
            sb.append("Quản lý: ").append(p.getManager().getUsername()).append("\n");
            
            List<Task> tasks = host.getTasksByProject(p.getId());
            long totalTasks = tasks.size();
            long completed = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
            long inProgress = tasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
            long todo = tasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
            
            sb.append("Tổng số công việc: ").append(totalTasks).append("\n");
            sb.append("Hoàn thành: ").append(completed).append("\n");
            sb.append("Đang thực hiện: ").append(inProgress).append("\n");
            sb.append("Chờ xử lý: ").append(todo).append("\n");
            sb.append("Tiến độ: ").append(String.format("%.1f%%", totalTasks > 0 ? (completed * 100.0 / totalTasks) : 0)).append("\n");
            sb.append("-".repeat(40)).append("\n\n");
        }
        
        return sb.toString();
    }

    private String generateTaskProgress(Project project) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(80)).append("\n");
        sb.append("BÁO CÁO TIẾN ĐỘ CÔNG VIỆC\n");
        sb.append("=".repeat(80)).append("\n\n");

        List<Task> tasks;
        if (project != null) {
            tasks = host.getTasksByProject(project.getId());
            sb.append("Dự án: ").append(project.getName()).append("\n\n");
        } else {
            tasks = host.getAllTasks();
            sb.append("Tất cả dự án\n\n");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Task task : tasks) {
            sb.append("📌 ").append(task.getTitle()).append("\n");
            sb.append("   Trạng thái: ").append(task.getStatus().getLabel()).append("\n");
            sb.append("   Người thực hiện: ").append(task.getAssignedTo().getUsername()).append("\n");
            sb.append("   Hạn chót: ").append(task.getDeadline().format(formatter)).append("\n");
            
            // Tính % tiến độ dựa trên thời gian
            LocalDate now = LocalDate.now();
            LocalDate deadline = task.getDeadline();
            long totalDays = java.time.temporal.ChronoUnit.DAYS.between(task.getCreatedDate(), deadline);
            long daysPassed = java.time.temporal.ChronoUnit.DAYS.between(task.getCreatedDate(), now);
            int progress = (int) ((daysPassed * 100) / totalDays);
            progress = Math.min(100, Math.max(0, progress));
            
            sb.append("   Tiến độ: ").append(progress).append("% ");
            sb.append("█".repeat(progress / 5)).append("\n\n");
        }
        
        return sb.toString();
    }

    private String generateUserPerformance(Project project) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(80)).append("\n");
        sb.append("BÁO CÁO HIỆU SUẤT NHÂN VIÊN\n");
        sb.append("=".repeat(80)).append("\n\n");

        List<Task> tasks;
        if (project != null) {
            tasks = host.getTasksByProject(project.getId());
            sb.append("Dự án: ").append(project.getName()).append("\n\n");
        } else {
            tasks = host.getAllTasks();
            sb.append("Tất cả dự án\n\n");
        }

        // Nhóm tasks theo người thực hiện
        Map<User, List<Task>> tasksByUser = tasks.stream()
            .collect(Collectors.groupingBy(Task::getAssignedTo));

        for (Map.Entry<User, List<Task>> entry : tasksByUser.entrySet()) {
            User user = entry.getKey();
            List<Task> userTasks = entry.getValue();
            
            long totalTasks = userTasks.size();
            long completed = userTasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
            long overdue = userTasks.stream()
                .filter(t -> t.getStatus() != TaskStatus.DONE && t.getDeadline().isBefore(LocalDate.now()))
                .count();
            
            sb.append("👤 ").append(user.getUsername()).append(" (").append(user.getRole()).append(")\n");
            sb.append("   Tổng công việc: ").append(totalTasks).append("\n");
            sb.append("   Đã hoàn thành: ").append(completed).append("\n");
            sb.append("   Đang thực hiện: ").append(userTasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count()).append("\n");
            sb.append("   Quá hạn: ").append(overdue).append("\n");
            sb.append("   Hiệu suất: ").append(String.format("%.1f%%", totalTasks > 0 ? (completed * 100.0 / totalTasks) : 0)).append("\n");
            sb.append("-".repeat(40)).append("\n");
        }
        
        return sb.toString();
    }

    private String generateOverdueTasks(Project project) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(80)).append("\n");
        sb.append("BÁO CÁO CÔNG VIỆC QUÁ HẠN\n");
        sb.append("=".repeat(80)).append("\n\n");

        List<Task> allTasks;
        if (project != null) {
            allTasks = host.getTasksByProject(project.getId());
            sb.append("Dự án: ").append(project.getName()).append("\n\n");
        } else {
            allTasks = host.getAllTasks();
            sb.append("Tất cả dự án\n\n");
        }

        LocalDate now = LocalDate.now();
        List<Task> overdueTasks = allTasks.stream()
            .filter(t -> t.getStatus() != TaskStatus.DONE && t.getDeadline().isBefore(now))
            .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        if (overdueTasks.isEmpty()) {
            sb.append("✅ Không có công việc quá hạn!\n");
        } else {
            sb.append("⚠️ Có ").append(overdueTasks.size()).append(" công việc quá hạn:\n\n");
            for (Task task : overdueTasks) {
                long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(task.getDeadline(), now);
                sb.append("❌ ").append(task.getTitle()).append("\n");
                sb.append("   Dự án: ").append(task.getProject().getName()).append("\n");
                sb.append("   Người thực hiện: ").append(task.getAssignedTo().getUsername()).append("\n");
                sb.append("   Hạn chót: ").append(task.getDeadline().format(formatter)).append("\n");
                sb.append("   Quá hạn: ").append(daysOverdue).append(" ngày\n\n");
            }
        }
        
        return sb.toString();
    }

    private String generateStatusStatistics(Project project) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(80)).append("\n");
        sb.append("THỐNG KÊ CÔNG VIỆC THEO TRẠNG THÁI\n");
        sb.append("=".repeat(80)).append("\n\n");

        List<Task> tasks;
        if (project != null) {
            tasks = host.getTasksByProject(project.getId());
            sb.append("Dự án: ").append(project.getName()).append("\n\n");
        } else {
            tasks = host.getAllTasks();
            sb.append("Tất cả dự án\n\n");
        }

        Map<TaskStatus, Long> statusCount = tasks.stream()
            .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

        long total = tasks.size();
        
        for (TaskStatus status : TaskStatus.values()) {
            long count = statusCount.getOrDefault(status, 0L);
            double percentage = total > 0 ? (count * 100.0 / total) : 0;
            
            sb.append(String.format("%-20s: %3d công việc (%5.1f%%) ", 
                status.getLabel(), count, percentage));
            
            // Vẽ biểu đồ thanh
            int barLength = (int) (percentage / 2);
            sb.append("█".repeat(barLength)).append("\n");
        }
        
        return sb.toString();
    }

    private void exportReport(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Xuất báo cáo");
        
        if (fileChooser.showSaveDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".txt")) {
                filePath += ".txt";
            }
            
            try (java.io.PrintWriter writer = new java.io.PrintWriter(filePath)) {
                writer.write(reportArea.getText());
                JOptionPane.showMessageDialog(mainPanel, 
                    "Đã xuất báo cáo thành công!\n" + filePath,
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Lỗi khi xuất báo cáo: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}