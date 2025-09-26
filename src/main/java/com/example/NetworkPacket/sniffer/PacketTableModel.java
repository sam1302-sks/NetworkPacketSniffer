package com.example.NetworkPacket.sniffer;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class PacketTableModel extends AbstractTableModel {
    private final String[] cols = {"Time", "Type", "Source", "Destination", "Info"};
    private List<Packet> packets;
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

    public PacketTableModel(List<Packet> packets) {
        this.packets = packets;
    }

    public void setPackets(List<Packet> packets) {
        this.packets = packets;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() { return packets == null ? 0 : packets.size(); }

    @Override
    public int getColumnCount() { return cols.length; }

    @Override
    public String getColumnName(int column) { return cols[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Packet p = packets.get(rowIndex);
        switch (columnIndex) {
            case 0: return df.format(p.getTimestamp());
            case 1: return p.getType();
            case 2: return p.getSrcIP();
            case 3: return p.getDestIP();
            case 4: return p.getData();
            default: return "";
        }
    }
}
