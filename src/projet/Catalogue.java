package projet;

import java.sql.*;
import java.util.*;

public class Catalogue {
	/*Charge tous les exemplaires de livres depuis la base de données. Chacun est représenté comme un objet
	*vueExemplaire contenant le rfid, le titre, l'auteur et l'état d'emprunt. Retourne une liste de tous les
	*exemplaires.
	*/
	public List<vueExemplaire> loadAllCopies() throws SQLException {
		//Requête sql pour récupérer toutes les copies 
		String sql = """ 
				SELECT c.rfid, b.title, b.author, b.edition, c.is_borrowed 
				FROM Copies c
				JOIN Books b ON b.book_id = c.book_id
				ORDER BY c.rfid
		""";
		try (Connection conn = ConnectDatabase.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
					List<vueExemplaire> list = new ArrayList<>();
					while (rs.next()) {
						list.add(new vueExemplaire( //création d'un nouvel objet vueExemplaire et ajout à la liste
								rs.getString("rfid"),
								rs.getString("title"),
								rs.getString("author"),
								rs.getString("edition"),
								rs.getBoolean("is_borrowed")
								));
					}
					return list;
				}
	}
}
