package socket.io.ui;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import socket.io.SocketIO;
import socket.io.bean.Expect;
import socket.io.common.MD5;
import socket.io.config.ResourceManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class JunitTest extends TestCase{

	private SocketIO io;

	@Before
	public void setUp() throws Exception {
		io = new SocketIO(ResourceManager.getInstance().getServerHost(),
				ResourceManager.getInstance().getServerPort());
	}

	@Test
	public void test() {
		String chiper = MD5.encrypt(io.getNonce(io.receive(100)));
		String msg = "{\"C2S\":\"Login\",\"channel\":{\"ID\":300001},\"player\":{\"ID\":900001,\"name\":\"loginOwner\",\"isHost\":true},\"auth\":{\"cipher\":\""
				+ chiper + "\"}}";

		Expect expect1 = new Expect(io, "Login TestCase", "Login", "0", 5000) {

			@Override
			public boolean verify(String message) {
				// TODO Auto-generated method stub
				return this.getExpectedMessage().equals(
						new Gson().fromJson(message, JsonArray.class)
								.get(0).getAsJsonObject().get("retcode")
								.getAsString());
			}

		};
		
		Expect expect2 = new Expect(io, "Login TestCase", "RoomInfo", "0", 5000) {

			@Override
			public boolean verify(String message) {
				// TODO Auto-generated method stub
				return this.getExpectedMessage().equals(
						new Gson().fromJson(io.receive(), JsonArray.class)
								.get(0).getAsJsonObject().get("status")
								.getAsString());
			}

		};

		Expect[] expects = { expect1, expect2};
		
		Assert.assertEquals(true, io.send(msg, expects));
	}

	@After
	public void tearDown() throws Exception {
		io.close();
	}

}
