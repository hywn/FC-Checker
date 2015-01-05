package com.Fawkes.FCChecker;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ch.jamiete.mcping.MinecraftPing;
import ch.jamiete.mcping.MinecraftPingOptions;
import ch.jamiete.mcping.MinecraftPingReply;

public class Panel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private JButton startButton;

	private JTextField freqInput;

	private JTextField serverIP;

	private JTextArea consle;

	private JLabel statusDisplay;

	private boolean running;

	private int sleepTime;

	private String ip;

	boolean serverUp = false;

	MinecraftPingReply data;

	MinecraftPing ping = new MinecraftPing();

	MinecraftPingOptions options;

	Thread thread;

	public Panel(int width, int height) {

		setSize(width, height);

		setLayout(new FlowLayout());

		startButton = new JButton("Start/Stop");

		freqInput = new JTextField("Frequency (ms)", 16);

		serverIP = new JTextField("Time between notes (seconds)", 20);

		consle = new JTextArea(11, 26);

		statusDisplay = new JLabel("Status");

		add(startButton);
		add(serverIP);
		add(freqInput);
		add(statusDisplay);
		add(new JScrollPane(consle));

		consle.setEditable(false);

		statusDisplay.setFont(new Font(Font.SERIF, Font.PLAIN, 70));

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				running = !running;

				if (running) {
					start();

				} else {
					stop();

				}

			}

		});

	}

	public void start() {

		running = true;

		try {

			sleepTime = Integer.parseInt(freqInput.getText());

		} catch (Exception e) {
			print("Cannot parse intger :" + freqInput.getText());

			return;

		}

		ip = serverIP.getText();

		// TODO: check server IP (formatting)

		options = new MinecraftPingOptions().setHostname(ip);

		thread = new Thread(this);

		thread.start();

		print("Now checking if " + ip + " is up.");

	}

	public void stop() {

		running = false;

		serverUp = false;

		alert();

		try {
			thread.join();

		} catch (Exception e) {
			// deal with it

		}

	}

	public void run() {

		while (running) {

			try {
				data = ping.getPing(options);

				// server is up!
				if (!serverUp) {
					serverUp = true;
					print(ip + " is now up!");
					alert();

				}

			} catch (Exception e) {
				
				e.printStackTrace();

				// server not up.

				print("Could not connect to " + ip + "... trying again.");

				if (serverUp) {
					print("Could not connect to " + ip + "... trying again.");

					try {
						Thread.sleep(sleepTime);

					} catch (InterruptedException e1) {
						e1.printStackTrace();

					}

					try {
						data = ping.getPing(options);

						print("Nevermind, it's up!");

					} catch (Exception e1) {
						e1.printStackTrace();
						print("Still could not connect to " + ip
								+ ". We think it may be down!");

						serverUp = false;

						alert();

					}
				}
			}

			try {
				Thread.sleep(sleepTime);

			} catch (InterruptedException e) {
				e.printStackTrace();

			}

		}

	}

	public void alert() {

		// make sound, flash icon in toolbar, and update status
		if (serverUp) {
			statusDisplay.setText("Online");

		} else {
			statusDisplay.setText("Offline");

		}

		Run.f.toFront();

	}

	public void print(String s) {
		consle.append(s + "\n");

	}
}
