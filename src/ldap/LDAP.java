package ldap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import javax.naming.directory.DirContext;

public class LDAP {

	private DirContext ctx;

	public LDAP() {
		super();
		
		Properties prop = new Properties();
		
		try {
			FileInputStream inputStream = new FileInputStream("src/conf/config.properties");
			prop.load(inputStream);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
