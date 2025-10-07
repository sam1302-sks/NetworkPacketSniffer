package com.example.NetworkPacket.GUI;

import com.example.NetworkPacket.sniffer.Packet;
import com.example.NetworkPacket.sniffer.PacketGenerator;
import com.example.NetworkPacket.sniffer.PacketTableModel;
import com.example.NetworkPacket.sniffer.Sniffer;
import com.example.NetworkPacket.Filter.PacketFilter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SnifferGUI extends JFrame {
    private final Sniffer sniffer;
    private final PacketTableModel tableModel;
    private final JTable table;
    private ExecutorService executor;
    private PacketGenerator generator;
    private JLabel statusLabel;
    private JLabel packetCountLabel;
    private JButton startBtn, stopBtn, tcpBtn, udpBtn, allBtn, saveBtn, clearBtn;

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color ACCENT_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color TABLE_HEADER_COLOR = new Color(52, 73, 94);

    public SnifferGUI(Sniffer sniffer) {
        super("🌐 Packet Sniffer - Network Analyzer");
        this.sniffer = sniffer;

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableModel = new PacketTableModel(sniffer.getAll());
        table = new JTable(tableModel);
        setupModernUI();
        setupLayout();
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopGenerator();
                if (executor != null) executor.shutdownNow();
            }
        });
    }

    private void setupModernUI() {
        // Custom table styling
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(SECONDARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Custom table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table Panel with styled border
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        // Timer to refresh UI
        Timer t = new Timer(1000, e -> {
            refreshTable(p -> true);
            updateStatus();
        });
        t.start();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        // Title
        JLabel titleLabel = new JLabel("🌐 Network Packet Sniffer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);

        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        statusPanel.setBackground(Color.WHITE);

        statusLabel = new JLabel("⏹️ Stopped");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(DANGER_COLOR);

        packetCountLabel = new JLabel("📊 Packets: 0");
        packetCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        statusPanel.add(statusLabel);
        statusPanel.add(packetCountLabel);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(statusPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Captured Packets",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY_COLOR
        ));
        tablePanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Add some nice styling to scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);
        verticalScrollBar.setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(BACKGROUND_COLOR);

        // Filter Panel
        JPanel filterPanel = createFilterPanel();
        controlPanel.add(filterPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        controlPanel.add(buttonPanel, BorderLayout.CENTER);

        return controlPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Packet Filters",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                PRIMARY_COLOR
        ));

        // Create styled filter buttons
        tcpBtn = createStyledButton("🔴 TCP Only", SECONDARY_COLOR);
        udpBtn = createStyledButton("🟢 UDP Only", SECONDARY_COLOR);
        allBtn = createStyledButton("🔄 Show All", ACCENT_COLOR);

        filterPanel.add(tcpBtn);
        filterPanel.add(udpBtn);
        filterPanel.add(allBtn);

        return filterPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Create styled action buttons
        startBtn = createStyledButton("▶️ Start Capture", ACCENT_COLOR);
        stopBtn = createStyledButton("⏹️ Stop Capture", DANGER_COLOR);
        clearBtn = createStyledButton("🗑️ Clear Log", new Color(241, 196, 15));
        saveBtn = createStyledButton("💾 Save Log", new Color(155, 89, 182));

        // Initially disable stop button
        stopBtn.setEnabled(false);

        buttonPanel.add(startBtn);
        buttonPanel.add(stopBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(saveBtn);

        // Add action listeners
        setupButtonActions();

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));

        // Add hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void setupButtonActions() {
        startBtn.addActionListener(e -> {
            startGenerator();
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            statusLabel.setText("🔴 Capturing...");
            statusLabel.setForeground(ACCENT_COLOR);
        });

        stopBtn.addActionListener(e -> {
            stopGenerator();
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            statusLabel.setText("⏹️ Stopped");
            statusLabel.setForeground(DANGER_COLOR);
        });

        tcpBtn.addActionListener(e -> refreshTable(p -> p.getType().equals("TCP")));
        udpBtn.addActionListener(e -> refreshTable(p -> p.getType().equals("UDP")));
        allBtn.addActionListener(e -> refreshTable(p -> true));

        saveBtn.addActionListener(e -> {
            try {
                saveLog();
                JOptionPane.showMessageDialog(this,
                        "Packet log saved successfully to 'packets.txt'",
                        "Save Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving file: " + ex.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        clearBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to clear all captured packets?",
                    "Confirm Clear",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                sniffer.clear();
                refreshTable(p -> true);
                updateStatus();
            }
        });
    }

    private void startGenerator() {
        if (executor != null && !executor.isShutdown()) return;
        executor = Executors.newSingleThreadExecutor();
        generator = new PacketGenerator(sniffer, 500); // 500 ms delay
        executor.submit(generator);
    }

    private void stopGenerator() {
        if (generator != null) generator.stop();
        if (executor != null) executor.shutdownNow();
        executor = null;
    }

    private void refreshTable(PacketFilter filter) {
        List<Packet> list = sniffer.filter(filter);
        tableModel.setPackets(list);
        updateStatus();
    }

    private void updateStatus() {
        int count = sniffer.getAll().size();
        packetCountLabel.setText("📊 Packets: " + count);

        // Update button states based on packet count
        clearBtn.setEnabled(count > 0);
        saveBtn.setEnabled(count > 0);
    }

    private void saveLog() throws IOException {
        List<Packet> list = sniffer.getAll();
        try (FileWriter fw = new FileWriter("packets.txt")) {
            fw.write("=== Network Packet Capture Log ===\n");
            fw.write("Total packets: " + list.size() + "\n");
            fw.write("Generated by: Packet Sniffer GUI\n\n");

            for (Packet p : list) {
                fw.write(p.getSummary() + System.lineSeparator());
            }

            fw.write("\n=== End of Log ===");
        }
    }

//    public static void main(String[] args) {
//        // Example usage
//        SwingUtilities.invokeLater(() -> {
//            Sniffer sniffer = new Sniffer();
//            new SnifferGUI(sniffer).setVisible(true);
//        });
    }
