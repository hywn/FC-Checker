package com.Fawkes.FCChecker;

import javax.swing.JFrame;

public class Run {
	public static JFrame f;

	public static void main(String[] args) {

		f = new JFrame();

		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// int width = screenSize.width / 3;
		// int height = screenSize.height / 3;

		int width = 300;
		int height = 400;

		Panel panel = new Panel(width, height);

		f.add(panel);
		f.setSize(width, height);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setResizable(false);

		f.setLocationRelativeTo(null);

	}
}
