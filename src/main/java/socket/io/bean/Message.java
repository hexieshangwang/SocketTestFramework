package socket.io.bean;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable  {
	
	private String type;
	private String reply;
	private boolean auto;
	
	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public Message(){
		
	}
	
	public Message(String type, String reply){
		this.type = type;
		this.reply = reply;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
}
