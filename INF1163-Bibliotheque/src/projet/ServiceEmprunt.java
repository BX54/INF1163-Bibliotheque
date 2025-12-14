package projet;

import java.sql.*;
import java.util.List;

public class ServiceEmprunt {
	
	public ResEmprunt borrow(String username, String pin, List<String> rfids, String receiptType) throws SQLException {
		if (rfids == null || rfids.isEmpty()) {
			throw new IllegalArgumentException("Aucun RFID fourni.");
		}
		try (Connection conn = ConnectDatabase.getConnection()) {
			conn.setAutoCommit(false);
			
			try {
				try (Statement st = conn.createStatement()) {
					st.execute("CREATE TABLE #rfids (rfid CHAR(6) NOT NULL PRIMARY KEY);");
				}
				try (PreparedStatement ps = conn.prepareStatement("INSERT INTO #rfids(rfid) VALUES (?)")) {
					for (String rfid : rfids) {
						ps.setString(1, rfid);
						ps.addBatch();
					}
					ps.executeBatch();
				}
				String sql = """
						DECLARE @list dbo.RfidList;
						INSERT INTO @list(rfid) SELECT rfid FROM #rfids;
						EXEC dbo.BorrowCopies @username = ?, @pin = ?, @rfids = @list, @receipt_type = ?;
				""";
				int loanId;
				int nb;
				Date dueDate;
				
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
				conn.commit();
				return new ResEmprunt(loanId, nb, dueDate);
			} catch (SQLException ex) {
				conn.rollback();
				throw ex;
			} finally {
				conn.setAutoCommit(true);
			}
		}
	}
	public record ResEmprunt(int loanId, int nbExemplaires, Date dueDate) {}
}
