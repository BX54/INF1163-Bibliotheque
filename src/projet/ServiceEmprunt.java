package projet;

import java.sql.*;
import java.util.List;

/* Service responsable de l'opération d'emprunt. Valide les entrées de l'utilisateur, ouvre une transaction
 * transmet la demande  à la base de données à travers la procédure borrowCopies stockée à l'intérieur de celle-ci
 * retourne ResEmprunt
 */
public class ServiceEmprunt {
	//validation de l'entrée: username, pin, liste des exemplaires demandés et le reçu
	public ResEmprunt borrow(String username, String pin, List<String> rfids, String receiptType) throws SQLException {
		if (rfids == null || rfids.isEmpty()) {
			throw new IllegalArgumentException("Aucun RFID fourni.");
		}
		//Connexion BD + gestion de la transaction
		try (Connection conn = ConnectDatabase.getConnection()) {
			conn.setAutoCommit(false);
			
			try {
				//Table temporaire pour recevoir la liste  des rfid
				try (Statement st = conn.createStatement()) {
					st.execute("CREATE TABLE #rfids (rfid CHAR(6) NOT NULL PRIMARY KEY);");
				}
				//Insertion batch des rfids
				try (PreparedStatement ps = conn.prepareStatement("INSERT INTO #rfids(rfid) VALUES (?)")) {
					for (String rfid : rfids) {
						ps.setString(1, rfid);
						ps.addBatch();
					}
					ps.executeBatch();
				}
				//Conversion vers le type table dbo.Rfidlist et appel de la procédure stockée
				String sql = """
						DECLARE @list dbo.RfidList;
						INSERT INTO @list(rfid) SELECT rfid FROM #rfids;
						EXEC dbo.BorrowCopies @username = ?, @pin = ?, @rfids = @list, @receipt_type = ?;
				""";
				int loanId;
				int nb;
				Date dueDate;
				
				//Exécution et lecture du résultat renvoyé par la procédure
				try (PreparedStatement ps = conn.prepareStatement(sql)) {
					ps.setString(1, username);
					ps.setString(2, pin);
					ps.setString(3, receiptType);
					
					try (ResultSet rs = ps.executeQuery()) {
						if (!rs.next()) throw new SQLException("BorrowCopies n'a retourné aucun résultat.");
						loanId = rs.getInt("loan_id");
						nb = rs.getInt("nb_exemplaires");
						dueDate = rs.getDate("due_date_default");
					}
				}
				//Si tout est correct, la transaction en commited
				conn.commit();
				return new ResEmprunt(loanId, nb, dueDate);
			} catch (SQLException ex) {
				//En cas d'erreur la procédure est annulée 
				conn.rollback();
				throw ex;
			} finally {
				//Restaure l'auto commit 
				conn.setAutoCommit(true);
			}
		}
	}
	public record ResEmprunt(int loanId, int nbExemplaires, Date dueDate) {}
}
