package com.example.NetworkPacket;

import com.example.NetworkPacket.GUI.SnifferGUI;
import com.example.NetworkPacket.sniffer.Sniffer;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		Sniffer sniffer = new Sniffer();

		// Create GUI on EDT
		SwingUtilities.invokeLater(() -> {
			SnifferGUI gui = new SnifferGUI(sniffer);
			gui.setLocationRelativeTo(null);
			gui.setVisible(true);
		});
	}
}
