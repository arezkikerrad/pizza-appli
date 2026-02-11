import java.util.ArrayList;
import java.util.List;

public class Client {
    public String telephone;
    public String nom;
    public float solde_client;
    public int nb_pizza_achete;
    public int compteur = 0;
    private List<commande> historique = new ArrayList<>();

    public Client(String telephone, String nom, float solde_client) {
        this.telephone = telephone;
        this.nom = nom;
        this.solde_client = solde_client;
        this.nb_pizza_achete = 0;
    }

    public String getTel() {
        return telephone;
    }

    public String getNom() {
        return nom;
    }

    public void ajouterCommandeHistorique(commande cmd) {
        historique.add(cmd);
    }

    public List<commande> getHistorique() {
        return historique;
    }

    public float getSolde() {
        return solde_client;
    }

    public int getNb_pizza_achete() {
        return nb_pizza_achete;
    }

    public void setTel(String telephone) {
        this.telephone = telephone;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setSolde(float solde_client) {
        this.solde_client = solde_client;
    }

    public void afficherSolde() {
        System.out.println("Solde du client " + nom + " : " + solde_client + "€");
    }

    public void afficherHistorique() {
        if (historique.isEmpty()) {
            System.out.println("Aucune commande dans l'historique.");
            return;
        }
        for (commande cmd : historique) {
            System.out.println(cmd); 
        }
    }

    public boolean verifierFidelite() {
        if (nb_pizza_achete >= 10) {
            nb_pizza_achete = nb_pizza_achete - 10; 
            System.out.println("Bravo ! Vous avez gagné une pizza gratuite !");
            return true;
        }
        return false;
    }
}