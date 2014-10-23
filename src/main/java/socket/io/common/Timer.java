package socket.io.common;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer {
	public static String getNowTime() {
		return new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS")
				.format(new Date());
	}
}
