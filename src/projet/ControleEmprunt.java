package projet;

import java.sql.SQLException;
import java.util.*;

/* Reçoit la demande d'emprunt depuis la vue
 * valide les données côté application et appelle le service d'emprunt
 */
public class ControleEmprunt {
	private final Index index;
	private final ServiceEmprunt serviceEmprunt;
	//Constructeur du contrôleur d'emprunt
	public ControleEmprunt(Index index, ServiceEmprunt serviceEmprunt) {
		this.index = index;
		this.serviceEmprunt = serviceEmprunt;
	}
	//Demande l'emprunt d'une liste d'exemplaires pour l'utilisateur en vérifiant que chaque
	//rfid demandé existe dans l'index et appelle le service d'emprunt
	public ServiceEmprunt.ResEmprunt emprunter(String username, String pin, List<String> rfids, String receiptType) throws SQLException {
		for (String r : rfids) {
			vueExemplaire v = index.find(r);
			if (v == null) throw new IllegalArgumentException("RFID inconnu: " + r);
		}
		return serviceEmprunt.borrow(username, pin, rfids, receiptType);
	}
}

