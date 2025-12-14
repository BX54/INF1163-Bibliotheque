package projet;

import java.util.*;
/* Index en mémoire des exemplaires chargés depuis la base de données.
 * Stocke et indexe les données pour être accessibles plus rapidement. Fait partie de la couche
 * modèle dans le MVC.
 */
public class Index {
	private final Map<String, vueExemplaire> byRfid = new HashMap<>();
	//charge l'index à partir d'une collection d'exemplaires. Efface l'index existant puis
	//insère chaque exemplaire avec son rfid comme clé
	public void load(Iterable<vueExemplaire> copies) { 
		byRfid.clear();
		for (vueExemplaire v : copies) byRfid.put(v.getRfid(), v);
	}
	
	public vueExemplaire find(String rfid) { //recherche un exemplaire dans l'index à partir de son rfid.
		return byRfid.get(rfid);
	}
	
	public int size() { //retourne le nombre d'exemplaires actuellement stockés dans l'index
		return byRfid.size();
	}
	
	public Collection<vueExemplaire> all() {
		return Collections.unmodifiableCollection(byRfid.values()); //donne accès à la collection entière, en lecture seule
	}
}
