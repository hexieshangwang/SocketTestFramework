package socket.io.bean;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import socket.io.SocketIO;

public abstract class Expect {

	private static Logger logger = Logger.getLogger(Expect.class);

	private SocketIO io;
	private long beginTime;
	private String operationDesc;
	private Lock lock;
	private Condition condition;
	private String expectedEvent;
	private String expectedMessage;
	private long endTime;
	/**
	 * 0£ºuncompleted£¬1£ºfinished£¬2£ºtimeout
	 */
	private int status = 0;
	/**
	 * 0:no time limited
	 */
	private long maxDelay;

	public Expect(SocketIO io, String operationDesc, String expectedEvent,
			String expectedMessage, long maxDelay) {
		this.io = io;
		this.operationDesc = operationDesc;
		this.expectedEvent = expectedEvent;
		this.expectedMessage = expectedMessage;
		this.maxDelay = maxDelay;
		this.lock = new ReentrantLock();
		this.condition = this.lock.newCondition();
		this.io.addExpect(this);
		this.beginTime = new Date().getTime();
	}

	public Expect(SocketIO io, String operationDesc, String expectedEvent,
			String expectedMessage) {
		this(io, operationDesc, expectedEvent, expectedMessage, 0);
	}

	public abstract boolean verify(String message);

	public void success() {
		this.lock.lock();
		try {
			if (this.status == 0) {
				this.endTime = new Date().getTime();
				logger.info("Expect Success£¬ Duration:" + (this.endTime - this.beginTime)
						+ "ms£º" + this.toString());
				this.status = 1;
				this.io.onExpectStatusChanged(this);
				this.condition.signal();
			}
		} finally {
			this.lock.unlock();
		}

	}

	public void timeout() {
		this.lock.lock();
		try {
			if (this.status == 0) {
				logger.info("Expect Timeout£º" + this.toString());
				this.status = 2;
				this.io.onExpectStatusChanged(this);
				this.condition.signal();
			}
		} finally {
			this.lock.unlock();
		}

	}

	public boolean isFinish() {
		return this.status != 0;
	}

	public boolean isSuccess() {
		return this.status == 1;
	}

	public boolean isTimeOut() {
		if (this.maxDelay <= 0) {
			return false;
		}
		return (new Date().getTime() - this.beginTime) > this.maxDelay;
	}

	public String toString() {
		return "Expect-Robot[" + this.io.toString() + "]-["
				+ this.operationDesc + "]-Event[" + this.expectedEvent
				+ "]-Message[" + this.expectedMessage + "]";
	}

	public void await() {
		lock.lock();
		try {
			while (!this.isFinish()) {
				if (this.maxDelay <= 0) {
					this.condition.await();
				} else {
					this.condition.await(this.maxDelay, TimeUnit.MILLISECONDS);
				}
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.warn("", e);
			Thread.currentThread().interrupt();
		} finally {
			lock.unlock();
		}

	}

	public void receive(String event, String message) {
		this.lock.lock();
		try {
			if (!this.isFinish()) {
				if (this.expectedEvent.equals(event) && this.verify(message)) {
					if (this.isTimeOut()) {
						this.timeout();
					} else {
						this.success();
					}
				}
			}
		} finally {
			this.lock.unlock();
		}
	}

	public long getBeginTime() {
		return beginTime;
	}

	public String getOperationDesc() {
		return operationDesc;
	}

	public Lock getLock() {
		return lock;
	}

	public Condition getCondition() {
		return condition;
	}

	public String getExpectedEvent() {
		return expectedEvent;
	}

	public String getExpectedMessage() {
		return expectedMessage;
	}

	public long getEndTime() {
		return endTime;
	}

	public int getStatus() {
		return status;
	}

	public long getMaxDelay() {
		return maxDelay;
	}

}