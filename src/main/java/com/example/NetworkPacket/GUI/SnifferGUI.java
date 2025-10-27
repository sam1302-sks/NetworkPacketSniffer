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
    private JButton startBtn, stopBtn, saveBtn, clearBtn;
    private JComboBox<String> filterDropdown;

    // Dark Mode Professional Color Scheme
    private final Color BG_DARK = new Color(18, 18, 18);           // Deep black background
    private final Color BG_SECONDARY = new Color(28, 28, 30);      // Secondary background
    private final Color BG_ELEVATED = new Color(38, 38, 42);       // Elevated surfaces
    private final Color ACCENT_PRIMARY = new Color(10, 132, 255);  // Modern blue
    private final Color ACCENT_SUCCESS = new Color(48, 209, 88);   // Green for success
    private final Color ACCENT_WARNING = new Color(255, 159, 10);  // Orange for warning
    private final Color ACCENT_DANGER = new Color(255, 69, 58);    // Red for danger
    private final Color TEXT_PRIMARY = new Color(255, 255, 255);   // White text
    private final Color TEXT_SECONDARY = new Color(152, 152, 157); // Gray text
    private final Color BORDER_COLOR = new Color(58, 58, 60);      // Subtle borders
    private final Color TABLE_GRID = new Color(48, 48, 52);        // Table grid
    private final Color SELECTION_BG = new Color(10, 132, 255, 40); // Selection highlight

    public SnifferGUI(Sniffer sniffer) {
        super("Network Packet Analyzer");
        this.sniffer = sniffer;

        // Set dark theme properties
        setDarkTheme();

        tableModel = new PacketTableModel(sniffer.getAll());
        table = new JTable(tableModel);
        setupModernDarkUI();
        setupLayout();
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopGenerator();
                if (executor != null) executor.shutdownNow();
            }
        });
    }

    private void setDarkTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Set dark theme for various UI components
            UIManager.put("OptionPane.background", BG_SECONDARY);
            UIManager.put("Panel.background", BG_SECONDARY);
            UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
            UIManager.put("Button.background", BG_ELEVATED);
            UIManager.put("Button.foreground", TEXT_PRIMARY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupModernDarkUI() {
        // Table styling with dark theme
        table.setBackground(BG_SECONDARY);
        table.setForeground(TEXT_PRIMARY);
        table.setRowHeight(36);
        table.setShowGrid(true);
        table.setGridColor(TABLE_GRID);
        table.setSelectionBackground(SELECTION_BG);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setIntercellSpacing(new Dimension(0, 0));

        // Custom table header with dark theme
        JTableHeader header = table.getTableHeader();
        header.setBackground(BG_ELEVATED);
        header.setForeground(TEXT_SECONDARY);
        header.setFont(new Font("SF Pro Display", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        // Custom cell renderer for dark theme
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? BG_SECONDARY : BG_DARK);
                    c.setForeground(TEXT_PRIMARY);
                } else {
                    c.setBackground(SELECTION_BG);
                    c.setForeground(TEXT_PRIMARY);
                }

                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BG_DARK);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BG_DARK);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        // Timer to refresh UI
        Timer t = new Timer(1000, e -> {
            refreshTable(getFilterFromDropdown());
            updateStatus();
        });
        t.start();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_SECONDARY);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(20, 30, 20, 30)
        ));

        // Left side - Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(BG_SECONDARY);

        JLabel iconLabel = new JLabel("◈");
        iconLabel.setFont(new Font("Dialog", Font.BOLD, 28));
        iconLabel.setForeground(ACCENT_PRIMARY);
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 12));

        JLabel titleLabel = new JLabel("Network Packet Analyzer");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_PRIMARY);

        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);

        // Right side - Status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        statusPanel.setBackground(BG_SECONDARY);

        packetCountLabel = new JLabel("0 packets");
        packetCountLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
        packetCountLabel.setForeground(TEXT_SECONDARY);

        statusLabel = new JLabel("● Stopped");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        statusLabel.setForeground(TEXT_SECONDARY);

        statusPanel.add(packetCountLabel);
        statusPanel.add(createDivider());
        statusPanel.add(statusLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(statusPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JLabel createDivider() {
        JLabel divider = new JLabel("│");
        divider.setForeground(BORDER_COLOR);
        divider.setFont(new Font("SansSerif", Font.PLAIN, 18));
        return divider;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 0));
        tablePanel.setBackground(BG_DARK);
        tablePanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(BG_SECONDARY);
        scrollPane.setBackground(BG_SECONDARY);

        // Style scrollbars
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);
        verticalScrollBar.setBackground(BG_ELEVATED);
        verticalScrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(80, 80, 85);
                this.trackColor = BG_ELEVATED;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout(0, 0));
        controlPanel.setBackground(BG_DARK);
        controlPanel.setBorder(new EmptyBorder(15, 20, 20, 20));

        // Main controls container
        JPanel controlsContainer = new JPanel(new BorderLayout(20, 0));
        controlsContainer.setBackground(BG_SECONDARY);
        controlsContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(16, 20, 16, 20)
        ));

        // Left side - Filter dropdown
        JPanel leftControls = createFilterPanel();

        // Right side - Action buttons
        JPanel rightControls = createButtonPanel();

        controlsContainer.add(leftControls, BorderLayout.WEST);
        controlsContainer.add(rightControls, BorderLayout.EAST);

        controlPanel.add(controlsContainer);
        return controlPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        filterPanel.setBackground(BG_SECONDARY);

        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        filterLabel.setForeground(TEXT_SECONDARY);

        // Create modern dropdown
        String[] filters = {"All Packets", "TCP Only", "UDP Only"};
        filterDropdown = new JComboBox<>(filters);
        filterDropdown.setFont(new Font("SansSerif", Font.PLAIN, 13));
        filterDropdown.setBackground(BG_ELEVATED);
        filterDropdown.setForeground(TEXT_PRIMARY);
        filterDropdown.setPreferredSize(new Dimension(150, 36));
        filterDropdown.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        filterDropdown.setFocusable(false);

        // Custom renderer for dropdown
        filterDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? ACCENT_PRIMARY : BG_ELEVATED);
                setForeground(TEXT_PRIMARY);
                setBorder(new EmptyBorder(8, 12, 8, 12));
                return this;
            }
        });

        filterDropdown.addActionListener(e -> refreshTable(getFilterFromDropdown()));

        filterPanel.add(filterLabel);
        filterPanel.add(filterDropdown);

        return filterPanel;
    }

    private PacketFilter getFilterFromDropdown() {
        String selected = (String) filterDropdown.getSelectedItem();
        switch (selected) {
            case "TCP Only":
                return p -> p.getType().equals("TCP");
            case "UDP Only":
                return p -> p.getType().equals("UDP");
            default:
                return p -> true;
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setBackground(BG_SECONDARY);

        // Create modern buttons
        startBtn = createModernButton("Start", ACCENT_SUCCESS, "▶");
        stopBtn = createModernButton("Stop", ACCENT_DANGER, "■");
        clearBtn = createModernButton("Clear", ACCENT_WARNING, "✕");
        saveBtn = createModernButton("Export", ACCENT_PRIMARY, "↓");

        stopBtn.setEnabled(false);

        buttonPanel.add(startBtn);
        buttonPanel.add(stopBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(saveBtn);

        setupButtonActions();

        return buttonPanel;
    }

    private JButton createModernButton(String text, Color color, String icon) {
        JButton button = new JButton(icon + "  " + text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(110, 36));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // Smooth hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private Timer hoverTimer;
            private float alpha = 1.0f;

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(10, e -> {
                    alpha = Math.max(0.85f, alpha - 0.03f);
                    button.setBackground(adjustAlpha(color, alpha));
                    if (alpha <= 0.85f) ((Timer) e.getSource()).stop();
                });
                hoverTimer.start();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(10, e -> {
                    alpha = Math.min(1.0f, alpha + 0.03f);
                    button.setBackground(adjustAlpha(color, alpha));
                    if (alpha >= 1.0f) ((Timer) e.getSource()).stop();
                });
                hoverTimer.start();
            }

            private Color adjustAlpha(Color c, float factor) {
                return new Color(
                        (int) (c.getRed() * factor),
                        (int) (c.getGreen() * factor),
                        (int) (c.getBlue() * factor)
                );
            }
        });

        return button;
    }

    private void setupButtonActions() {
        startBtn.addActionListener(e -> {
            startGenerator();
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            statusLabel.setText("● Capturing");
            statusLabel.setForeground(ACCENT_SUCCESS);
        });

        stopBtn.addActionListener(e -> {
            stopGenerator();
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            statusLabel.setText("● Stopped");
            statusLabel.setForeground(TEXT_SECONDARY);
        });

        saveBtn.addActionListener(e -> {
            try {
                saveLog();
                showModernDialog("Export Successful",
                        "Packet log exported to 'packets.txt'",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                showModernDialog("Export Failed",
                        "Error: " + ex.getMessage(),
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        clearBtn.addActionListener(e -> {
            int result = showModernConfirmDialog(
                    "Clear All Packets?",
                    "This will permanently delete all captured packets from memory.",
                    "Clear", "Cancel"
            );

            if (result == JOptionPane.YES_OPTION) {
                sniffer.clear();
                refreshTable(getFilterFromDropdown());
                updateStatus();
            }
        });
    }

    private void showModernDialog(String title, String message, int type) {
        JOptionPane pane = new JOptionPane(message, type);
        JDialog dialog = pane.createDialog(this, title);
        dialog.getContentPane().setBackground(BG_SECONDARY);
        dialog.setVisible(true);
    }

    private int showModernConfirmDialog(String title, String message, String yesText, String noText) {
        Object[] options = {yesText, noText};
        JOptionPane pane = new JOptionPane(
                message,
                JOptionPane.WARNING_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                options,
                options[1]
        );
        JDialog dialog = pane.createDialog(this, title);
        dialog.getContentPane().setBackground(BG_SECONDARY);
        dialog.setVisible(true);

        Object selected = pane.getValue();
        if (selected == null) return JOptionPane.CLOSED_OPTION;

        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selected)) {
                return i == 0 ? JOptionPane.YES_OPTION : JOptionPane.NO_OPTION;
            }
        }
        return JOptionPane.CLOSED_OPTION;
    }

    private void startGenerator() {
        if (executor != null && !executor.isShutdown()) return;
        executor = Executors.newSingleThreadExecutor();
        generator = new PacketGenerator(sniffer, 500);
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
        packetCountLabel.setText(count + " packet" + (count != 1 ? "s" : ""));

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
}

