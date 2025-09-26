package com.example.NetworkPacket.GUI;

import com.example.NetworkPacket.sniffer.Packet;
import com.example.NetworkPacket.sniffer.PacketGenerator;
import com.example.NetworkPacket.sniffer.PacketTableModel;
import com.example.NetworkPacket.sniffer.Sniffer;
import com.example.NetworkPacket.Filter.PacketFilter;
import javax.swing.*;
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

    public SnifferGUI(Sniffer sniffer) {
        super("Packet Sniffer - Simulation");
        this.sniffer = sniffer;

        tableModel = new PacketTableModel(sniffer.getAll());
        table = new JTable(tableModel);
        setupLayout();
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopGenerator();
                if (executor != null) executor.shutdownNow();
            }
        });
    }

    private void setupLayout() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane sp = new JScrollPane(table);
        panel.add(sp, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JButton startBtn = new JButton("Start Capture");
        JButton stopBtn = new JButton("Stop Capture");
        JButton tcpBtn = new JButton("Show TCP");
        JButton udpBtn = new JButton("Show UDP");
        JButton allBtn = new JButton("Show All");
        JButton saveBtn = new JButton("Save Log");
        JButton clearBtn = new JButton("Clear Log");

        controls.add(startBtn);
        controls.add(stopBtn);
        controls.add(tcpBtn);
        controls.add(udpBtn);
        controls.add(allBtn);
        controls.add(saveBtn);
        controls.add(clearBtn);

        panel.add(controls, BorderLayout.SOUTH);

        getContentPane().add(panel);

        startBtn.addActionListener(e -> startGenerator());
        stopBtn.addActionListener(e -> stopGenerator());

        tcpBtn.addActionListener(e -> refreshTable(p -> p.getType().equals("TCP")));
        udpBtn.addActionListener(e -> refreshTable(p -> p.getType().equals("UDP")));
        allBtn.addActionListener(e -> refreshTable(p -> true));

        saveBtn.addActionListener(e -> {
            try { saveLog(); JOptionPane.showMessageDialog(this, "Saved to packets.txt"); }
            catch (IOException ex) { JOptionPane.showMessageDialog(this, "Error saving: " + ex.getMessage()); }
        });

        clearBtn.addActionListener(e -> { sniffer.clear(); refreshTable(p -> true); });

        // Timer to refresh UI
        Timer t = new Timer(1000, e -> refreshTable(p -> true));
        t.start();
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
    }

    private void saveLog() throws IOException {
        List<Packet> list = sniffer.getAll();
        try (FileWriter fw = new FileWriter("packets.txt")) {
            for (Packet p : list) {
                fw.write(p.getSummary() + System.lineSeparator());
            }
        }
    }
}
