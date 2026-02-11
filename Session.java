import java.sql.Time;
import java.util.ArrayList;

public class Session {
    public static Client clientConnecte = null;
    public static ArrayList<Client> listeClients = new ArrayList<>();
    public static ArrayList<pizza> catalogue = new ArrayList<>();
    public static ArrayList<commande> commandesActives = new ArrayList<>();
    public static ArrayList<commande> historique = new ArrayList<>();
    public static ArrayList<commande> commandesArchivees = new ArrayList<>();
    public static ArrayList<Livreur> livreurs = new ArrayList<>();
    private static int compteurNumeroCommande = 1;

    public static void initialiser() {
        
        listeClients.add(new Client("0612345678", "Enzo", 150.0f));
        listeClients.add(new Client("0623456789", "Marie", 30.0f));
        listeClients.add(new Client("0634567890", "Luc", 70.0f));

       
        pizza margherita = new pizza("humaine", "Margherita", 8.5f);
        margherita.ajouterIngredient(new ingredients("Sauce tomate"));
        margherita.ajouterIngredient(new ingredients("Mozzarella"));
        margherita.ajouterIngredient(new ingredients("Basilic"));
        catalogue.add(margherita);

        pizza pepperoni = new pizza("humaine", "Pepperoni", 9.0f);
        pepperoni.ajouterIngredient(new ingredients("Sauce tomate"));
        pepperoni.ajouterIngredient(new ingredients("Mozzarella"));
        pepperoni.ajouterIngredient(new ingredients("Pepperoni"));
        catalogue.add(pepperoni);

        pizza vegetarienne = new pizza("humaine", "Végétarienne", 9.5f);
        vegetarienne.ajouterIngredient(new ingredients("Sauce tomate"));
        vegetarienne.ajouterIngredient(new ingredients("Mozzarella"));
        vegetarienne.ajouterIngredient(new ingredients("Poivrons"));
        vegetarienne.ajouterIngredient(new ingredients("Champignons"));
        vegetarienne.ajouterIngredient(new ingredients("Oignons"));
        catalogue.add(vegetarienne);
        
        
        livreurs.add(new Livreur("Voiture", 1, null, null, "Pierre"));
        livreurs.add(new Livreur("Moto", 2, null, null, "Sophie"));
        
        
       
        commandesActives = new ArrayList<>();
        commandesArchivees = new ArrayList<>();
    }
    
    
    public static commande creerNouvelleCommande() {
        Time heureActuelle = new Time(System.currentTimeMillis());
        commande nouvelleCommande = new commande(compteurNumeroCommande++, heureActuelle);
        commandesActives.add(nouvelleCommande);
        return nouvelleCommande;
    }
    
   
public static void archiverCommande(commande cmd) {
    commandesActives.remove(cmd);
    historique.add(cmd);
    commandesArchivees.add(cmd);
    
    
}
}