package socket.io;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Logger;

import socket.io.bean.Message;
import socket.io.common.Serializer;
import socket.io.common.Timer;
import socket.io.ui.GUI;

public class SocketIOCallback implements Runnable {

	private static Logger logger = Logger.getLogger(SocketIOCallback.class);
	private Socket socket;
	private String response;
	private SocketIO io;

	public SocketIO getIo() {
		return io;
	}

	public void setIo(SocketIO io) {
		this.io = io;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public SocketIOCallback(Socket socket) {
		this.socket = socket;
	}

	public SocketIOCallback() {
		// TODO Auto-generated constructor stub
	}

	public Message getMsgObject(String type) {
		Message[] messages = Serializer.getInstance().deSerializeFromXml();
		if (null != messages && messages.length > 0) {
			for (Message message : messages) {
				if (type.equalsIgnoreCase(message.getType().trim())) {
					return message;
				}
			}
		}

		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				if (!this.socket.isClosed()) {
					InputStream is = this.socket.getInputStream();
					InputStreamReader isr = new InputStreamReader(is, "utf-8");
					BufferedReader br = new BufferedReader(isr);
					String reply = null;
					while (!"\n".equals(reply = br.readLine())) {
						this.setResponse(reply);
						if (null != reply) {
							GUI.printOutputTextArea(Timer.getNowTime()+" "+"Receive < :: \t" + reply);
							logger.info(Timer.getNowTime()+" "+this.io.toString()+" Receive < :: \t" + reply);

							String type = this.io.getMessageType(reply);
							
							//receive message and write local cache
							this.io.onSocketIOEvent(type, reply);
						}
					}
					br.close();
					isr.close();
					is.close();
				} else {
					logger.info("Stream read failed£ºconnection is closed!");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		}

	}
}
