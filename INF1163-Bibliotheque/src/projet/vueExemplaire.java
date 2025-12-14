package projet;

public class vueExemplaire {
	private final String rfid; //6 digits
	private final String titre; //titre du livre
	private final String auteur; //nom de l'auteur
	private final boolean emprunt; //statue de l'exemplaire
	
	public vueExemplaire(String rfid, String titre, String auteur, boolean emprunt) { //Constructeur de classe
		this.rfid = rfid;
		this.titre = titre;
		this.auteur = auteur;
		this.emprunt = emprunt;
	}
	
	//getters pour attributs de classe
	public String getRfid() { return rfid; }
	public String getTitre() { return titre; }
	public String getAuteur() { return auteur; }
	public boolean getEmprunt() { return emprunt; }
	
	@Override
	public String toString() {
		return rfid + " - " + titre + " (" + auteur + ") " + (emprunt ? "[Emprunté]" : "[Disponible]");
	}
}
