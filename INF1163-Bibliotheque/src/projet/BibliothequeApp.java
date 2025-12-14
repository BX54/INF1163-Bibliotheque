package projet;

/*classe d'initialisation de la bibliothèque
 * son rôle est de démarrer le système, construire les objets principaux et donne un point d'accès pour la vue
 */
public class BibliothequeApp {
	//Index en mémoire pour retrouver rapidement un exemplaire
	private final Index index = new Index();
	//Contrôleur responsable des opérations
	private ControleEmprunt controleEmprunt;
	
	//démarrage du système. Charge les exemplaires depuis le catalogue et charge l'index
	//puis instancie les services et contrôleurs
	public void demarrerSysteme() throws Exception {
		//chargement du catalogue et de l'index
		Catalogue catalogue = new Catalogue();
		index.load(catalogue.loadAllCopies());
		
		//création du contrôleur
		controleEmprunt = new ControleEmprunt(index, new ServiceEmprunt());
		
		System.out.println("Système démarré. Catalogue chargé: " + index.size() + " exemplaires.");
	}
	public ControleEmprunt getControleEmprunt() {
		if (controleEmprunt == null) {
			throw new IllegalStateException("Le système n'est pas démarré.");
		}
		return controleEmprunt;
	}
}
