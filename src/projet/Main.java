package projet;

import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws Exception {
	    try (Connection c = ConnectDatabase.getConnection()) {
	        System.out.println("✅ Connexion JDBC OK");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    BibliothequeApp app = new BibliothequeApp();
	    app.demarrerSysteme();
	    
	    Scanner scanner = new Scanner(System.in);
	    
	    System.out.print("Compte: ");
	    String username = scanner.nextLine().trim();
	    
	    System.out.print("Pin à 4 chiffres: ");
	    String pin = scanner.nextLine().trim();
	    
	    List<String> rfids = new ArrayList<>();
	    while(true) {
	    	System.out.print("Entrer le RFID à 6 chiffes du livre à emprunter: ");
	    	String r = scanner.nextLine().trim();
	    	if (r.isEmpty()) break;
	    	rfids.add(r);
	    }
	    
	    System.out.print("Type reçu (print): ");
	    String receipt = scanner.nextLine().trim();
	    if (receipt.isEmpty()) receipt = "NONE";
	    
	    try {
	    	var res = app.getControleEmprunt().emprunter(username, pin, rfids, receipt);
	    	System.out.println("\n===REÇU===");
	    	System.out.println("ID d'emprunt: " + res.loanId());
	    	System.out.println("Nombre d'exemplaires: " + res.nbExemplaires());
	    	System.out.println("Date de retour: " + res.dueDate());
	    	System.out.println("===========\n");
	    	scanner.close();

	    } catch (Exception e) {
	    	System.out.println("X Erreur: " + e.getMessage());
	    }
	}

}
