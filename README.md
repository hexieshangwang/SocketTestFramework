API-1：SocketIO

1.Create Socket IO
SocketIO io = new SocketIO(String host, int port)

2.Send Message（not include expects）
io.send(String message):void

3.Send Message（include expects）
io.send(String message, Expect[] expects):boolean

4.Receive Message（no time limited）
io.receive():String

5.Receive Message（time limited）
io.receive(long time):String

6.Close Socket IO
io.close():void

7.Get Message Type
io.getMessageType(String message):String

8.Get Server Time Synchronization
io.getNonce(String message):int

API-2：Expect

1.Create Expects(according to the custom check rules)
Expect expect1 = new Expect((SocketIO io, String operationDesc, String expectedEvent,
			String expectedMessage){
			@Override
			public boolean verify(String message) {
				return self-definition;
			}
		};
…
Expect[] expects = { expect1, …};

API-3：MD5

1.Create MD5 Abstract
MD5.encrypt(int nonce):String

API-4：MySQL

1.Create Connection
MySQL.connect():void

2.Query Result Set
MySQL.query(String sql):void

3.Get Column Field Value
MySQL.getObjectValue(String columnName):Object

4.Close Connection
MySQL.close():void
