import java.io.*;
import java.util.*;

public class pizzeria {
    private String adresse;
    private String nom_pizzeria;
    private ArrayList<commande> commandes = new ArrayList<commande>();
    private ArrayList<Livreur> livreurs = new ArrayList<Livreur>();
    private ArrayList<Client> clients = new ArrayList<Client>();
    private Catalogue catalogue;

    public pizzeria(String adresse, String nom_pizzeria, Catalogue catalogue, ArrayList<Livreur> livreurs, ArrayList<Client> clients) {
        this.adresse = adresse;
        this.nom_pizzeria = nom_pizzeria;
        this.catalogue = catalogue;
        this.livreurs = livreurs;
        this.clients = clients;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNomPizzeria() {
        return nom_pizzeria;
    }

    public void setNomPizzeria(String nom_pizzeria) {
        this.nom_pizzeria = nom_pizzeria;
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public ArrayList<Livreur> ajouterLivreur(Livreur liv) {
        for (Livreur l : livreurs) {
            if (l.equals(liv)) {
                System.out.println("Le livreur est déjà enregistré.");
                return livreurs;
            }
        }
        livreurs.add(liv);
        return livreurs;
    }

    public ArrayList<Client> ajouterClient(Client c) {
        for (Client cl : clients) {
            if (cl.equals(c)) {
                System.out.println("Le client est déjà enregistré.");
                return clients;
            }
        }
        clients.add(c);
        return clients;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public ArrayList<Livreur> getLivreurs() {
        return livreurs;
    }

    public void retirerClient(Client c) {
        clients.remove(c);
    }

    public void retirerLivreur(Livreur liv) {
        livreurs.remove(liv);
    }
}
