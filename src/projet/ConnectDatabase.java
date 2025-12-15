package projet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {
	
		private static final String url = "jdbc:sqlserver://localhost:1433;databaseName=BIBLIO;"
				+ "encrypt=true;trustServerCertificate=true;"; //Vérifiez votre port TCP IPAll pour avoir le bon host
		private static final String user = "biblio"; //votre user name dans votre serveur sql
		private static final String password = "inf1163"; //votre mot de passe
		
		public static Connection getConnection() throws SQLException{
			return DriverManager.getConnection(url, user, password);
		}
	
}

