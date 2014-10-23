package socket.io;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import socket.io.bean.Expect;
import socket.io.common.ThreadPoolFactory;
import socket.io.common.Timer;
import socket.io.ui.GUI;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import socket.io.SocketIOCallback;

public class SocketIO {

	private Logger logger = Logger.getLogger(SocketIO.class);
	private Socket socket;
	private SocketIOCallback callback;
	private List<Expect> expects = new CopyOnWriteArrayList<Expect>();

	public SocketIO(String host, int port) {
		try {
			this.socket = new Socket(host, port);
			this.callback = new SocketIOCallback(this.socket);
			new Thread(this.callback).start();
			this.callback.setIo(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}

	/*
	 * Description : send data and end in a new line character
	 */
	public void send(String message) {
		try {
			PrintWriter pw = new PrintWriter(this.socket.getOutputStream());
			pw.write(message.trim());
			pw.write("\n");
			pw.flush();
			logger.info(Timer.getNowTime() + " " + this.toString()
					+ " Send > ::\t" + message);
			GUI.printOutputTextArea(Timer.getNowTime() + " " + "Send > ::\t"
					+ message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/*
	 * Description : add expects to a list
	 */
	public synchronized void addExpect(final Expect expect) {
		this.expects.add(expect);
		if (expect.getMaxDelay() > 0) {
			ThreadPoolFactory.getInstance().getScheduleThreadPool()
					.schedule(new Runnable() {

						public void run() {
							// TODO Auto-generated method stub
							expect.timeout();
						}

					}, expect.getMaxDelay(), TimeUnit.MILLISECONDS);
		}

	}

	/*
	 * Description : add expects to a list
	 */
	public boolean send(String message, Expect[] expects) {
		if (null != expects) {
			for (Expect expect : expects) {
				this.addExpect(expect);
			}
		}
		this.send(message);
		if (null != expects) {
			for (Expect expect : expects) {
				expect.await();
				if (!expect.isSuccess()) {
					return false;
				}
			}
		}
		return true;

	}

	public synchronized void onExpectStatusChanged(Expect expect) {
		if (expect.isFinish()) {
			this.expects.remove(expect);
		}
	}

	public synchronized void onSocketIOEvent(String event, String message) {
		for (Expect expect : this.expects) {
			expect.receive(event, message);
		}
	}

	/*
	 * Description : receive data
	 */
	public String receive() {
		if (null != this.callback) {
			String message = this.callback.getResponse();
			if (null != message && message.length() > 0) {
				return message;
			}
		}
		return null;
	}

	public String receive(long time) {
		if (null != this.callback) {
			sleep(time);
			String message = this.callback.getResponse();
			if (null != message && message.length() > 0) {
				return message;
			} else {
				return "not receive any messages from " + time
						+ " milliseconds";
			}
		}
		return null;
	}

	/*
	 * Description : close current socket
	 */
	public void close() {
		if (null != this.socket) {
			try {
				this.socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		}
	}

	public void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}

	/*
	 * Description : get message type
	 */
	public String getMessageType(String message) {
		if (null == message) {
			logger.info("parse message type error£ºnot timely receive a return message!");
		}
		return new Gson().fromJson(message, JsonArray.class).get(0)
				.getAsJsonObject().get("S2C").getAsString();
	}

	/*
	 * Description : get server time synchronization
	 */
	public int getNonce(String message) {
		if (null == message) {
			logger.info("parse server nonce error£ºnot timely receive a return message!");
		}
		return new Gson().fromJson(message, JsonArray.class).get(0)
				.getAsJsonObject().get("nonce").getAsInt();
	}
}
