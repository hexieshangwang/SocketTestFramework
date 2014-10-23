package socket.io.common;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import socket.io.config.ResourceManager;

public class ThreadPoolFactory {

	private static ThreadPoolFactory instance = new ThreadPoolFactory();

	public static ThreadPoolFactory getInstance() {
		return instance;
	}

	private ScheduledExecutorService scheduleThreadPool;

	private ThreadPoolFactory() {
		this.scheduleThreadPool = Executors
				.newScheduledThreadPool(ResourceManager.getInstance()
						.getThreadPoolNum());
	}

	public ScheduledExecutorService getScheduleThreadPool() {
		return this.scheduleThreadPool;
	}
}
