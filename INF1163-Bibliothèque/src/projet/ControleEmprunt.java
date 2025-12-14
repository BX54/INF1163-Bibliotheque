package projet;

import java.sql.SQLException;
import java.util.*;

public class ControleEmprunt {
	private final Index index;
	private final ServiceEmprunt serviceEmprunt;
	
	public ControleEmprunt(Index index, ServiceEmprunt serviceEmprunt) {
		this.index = index;
		this.serviceEmprunt = serviceEmprunt;
	}
	public ServiceEmprunt.ResEmprunt emprunter(String username, String pin, List<String> rfids, String receiptType) throws SQLException {
		for (String r : rfids) {
			vueExemplaire v = index.find(r);
			if (v == null) throw new IllegalArgumentException("RFID inconnu: " + r);
		}
		return serviceEmprunt.borrow(username, pin, rfids, receiptType);
	}
}

