package socket.io.common;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import java.io.FileInputStream;
import java.io.IOException;

import socket.io.bean.Message;

import com.thoughtworks.xstream.XStream;

public class Serializer {
	
	public static Serializer getInstance() {
		return new Serializer();
	}

	// DeSerialize
	public Message[] deSerializeFromXml() {
		XStream xStream = new XStream();
		xStream.alias("Message", Message.class);
		Message[] servers = null;
		try {
			FileInputStream flStream = new FileInputStream("message.xml");
			servers = (Message[]) xStream.fromXML(flStream);
			flStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return servers;
	}
}
