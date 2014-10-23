package socket.io.config;
/**
 * @author QingWang
 * @date 2014-10-1 20:48:22
 */
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;


public class ResourceManager {
	
	private static Logger logger = Logger.getLogger(ResourceManager.class);
	private static ResourceManager instance = new ResourceManager();

	public static ResourceManager getInstance() {
		return instance;
	}
	
	private Properties configProperties = new Properties();
	
	private void initConfig() throws IOException{
		this.configProperties.load(this.getClass().getClassLoader().getResourceAsStream("config/app.properties"));
	}
	
	private ResourceManager(){
		try {
			this.initConfig();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}
	
	public String getServerHost(){
		return this.configProperties.getProperty("server.host");
	}
	
	public int getServerPort(){
		return Integer.valueOf(this.configProperties.getProperty("server.port"));
	}
	
	public String getCipherExtendField(){
		return this.configProperties.getProperty("cipherExtendField");
	}
	
	public int getThreadPoolNum(){
		return Integer.valueOf(this.configProperties.getProperty("threadpool.num"));
	}
	
	public String getIsAutoLogin(){
		return this.configProperties.getProperty("isAutoLogin");
	}
	
	public String getMySQLHost(){
		return this.configProperties.getProperty("mysql.host");
	}
	
	public String getMySQLPort(){
		return this.configProperties.getProperty("mysql.port");
	}
	
	public String getMySQLUserName(){
		return this.configProperties.getProperty("mysql.username");
	}
	
	public String getMySQLPassword(){
		return this.configProperties.getProperty("mysql.password");
	}
	
	public String getMySQLDB(){
		return this.configProperties.getProperty("mysql.db");
	}
}
