package projet;

public class vueExemplaire {
	private final String rfid; //6 digits
	private final String titre; //titre du livre
	private final String auteur; //nom de l'auteur
	private final String edition; //l'édition du livre
	private final boolean emprunt; //statue de l'exemplaire
	
	public vueExemplaire(String rfid, String titre, String auteur, String edition, boolean emprunt) { //Constructeur de classe
		this.rfid = rfid;
		this.titre = titre;
		this.auteur = auteur;
		this.edition = edition;
		this.emprunt = emprunt;
	}
	
	//getters pour attributs de classe
	public String getRfid() { return rfid; }
	public String getTitre() { return titre; }
	public String getAuteur() { return auteur; }
	public String getEdition() { return edition; }
	public boolean getEmprunt() { return emprunt; }
	
	@Override
	public String toString() {
		return rfid + " - " + titre + " (" + auteur + ") " + " édition " + " (" + edition + " )" + (emprunt ? "[Emprunté]" : "[Disponible]");
	}
}
