package com.Fawkes.FCChecker;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

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

	Calendar currTime;

	private String ip;

	boolean serverUp = true;

	MinecraftPingReply data;

	MinecraftPing ping = new MinecraftPing();

	MinecraftPingOptions options;

	Thread thread;

	public Panel(int width, int height) {

		// make panel and gui stuff

		currTime = Calendar.getInstance();

		setSize(width, height);

		setLayout(new FlowLayout());

		startButton = new JButton("Start/Stop");

		freqInput = new JTextField("5000", 16);

		serverIP = new JTextField("hub.frostcast.net", 20);

		consle = new JTextArea(11, 26);

		statusDisplay = new JLabel("Status");

		add(startButton);
		add(serverIP);
		add(freqInput);
		add(statusDisplay);
		add(new JScrollPane(consle));

		consle.setEditable(false);

		statusDisplay.setFont(new Font(Font.SERIF, Font.PLAIN, 70));

		// button listener
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// toggles running on/off, starts if it's not already started,
				// stops otherwise.

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

		// sets all the vars to the fields provided

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

		// starts a new thread

		thread = new Thread(this);

		thread.start();

		print("Now checking if " + ip + " is up.");

	}

	public void stop() {

		// sets running to false so thread will stop

		running = false;

		serverUp = true;

		updateDisplay();

		// waits for thread to die
		try {
			thread.join();

		} catch (Exception e) {
			// deal with it

		}

		print("Stopped pinging " + ip);

	}

	public void run() {

		// bad loop

		while (running) {

			try {
				data = ping.getPing(options);

				print(ip + " is up.");

				// server is up!
				if (!serverUp) {
					serverUp = true;
					alert();

				}

			} catch (Exception e) {
				// server not up.

				print("Could not connect to " + ip);

				if (serverUp) {

					serverUp = false;

					alert();

				}
			}

			try {
				Thread.sleep(sleepTime);

			} catch (InterruptedException e) {
				e.printStackTrace();

			}

		}

	}

	// basically updates gui
	public void alert() {

		// make sound, flash icon in toolbar, and update status

		updateDisplay();

		Run.f.toFront();

	}

	public void updateDisplay() {
		if (serverUp) {
			statusDisplay.setText("Online");

		} else {
			statusDisplay.setText("Offline");

		}
	}

	// prints stuff out in console
	public void print(String s) {

		consle.append(String.format("[%s:%s:%s] %s\n",
				currTime.get(Calendar.HOUR_OF_DAY),
				currTime.get(Calendar.MINUTE), currTime.get(Calendar.SECOND), s));

		currTime = Calendar.getInstance();

	}
}
