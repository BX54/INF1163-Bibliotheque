package projet;

public class BibliothequeApp {
	private final Index index = new Index();
	private ControleEmprunt controleEmprunt;
	
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
