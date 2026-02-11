import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class commande {
    private int numero_commande;
    private int prix_total;
    private String statut_commande;
    private Time heure_commande;
    private Time heure_livraison;
    private int compteur;
    private ArrayList<LigneCommande> lignes = new ArrayList<>();
    private Livreur livreur;
    private int tempsEstimeLivraison;
    private LocalDateTime heureDebutLivraison;
    private boolean commandeRemboursee = false;

    public commande(int numero_commande, Time heure_commande) {
        this.numero_commande = numero_commande;
        this.heure_commande = heure_commande;
        this.statut_commande = "en attente";
        this.compteur = 0;
        this.livreur = null;
        this.tempsEstimeLivraison = calculerTempsEstimeLivraison();
    }

    private int calculerTempsEstimeLivraison() {
    Random rand = new Random();
    if (livreur == null) return 20;

    String vehicule = livreur.getVehicule().toLowerCase();
    if (vehicule.equals("voiture")) {
        return 0 + rand.nextInt(6);
    } else  {
        return 0 + rand.nextInt(9);
    }
}

    
    public void ajouterLigneCommande(pizza pizza, int quantite, String taille) {
        lignes.add(new LigneCommande(pizza, quantite, taille));
    }

    public ArrayList<LigneCommande> getLignes() {
        return lignes;
    }

    public int getNumero_commande() {
        return numero_commande;
    }

    public int getPrix_total() {
        return prix_total;
    }

    public String getStatut() {
        return statut_commande;
    }

    public Time getHeure_commande() {
        return heure_commande;
    }

    public Time getHeure_livraison() {
        return heure_livraison;
    }

    public void setNumero_commande(int numero_commande) {
        this.numero_commande = numero_commande;
    }

    public void setPrix_total(int prix_total) {
        this.prix_total = prix_total;
    }

    public void setStatutCommande(String statut_commande) {
        this.statut_commande = statut_commande;
    }

    public void setHeure_commande(Time heure_commande) {
        this.heure_commande = heure_commande;
    }

    public void setHeure_livraison(Time heure_livraison) {
        this.heure_livraison = heure_livraison;
    }

    public Livreur getLivreur() {
        return livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
        if (this.tempsEstimeLivraison == 0 || this.tempsEstimeLivraison == 20) {
            this.tempsEstimeLivraison = calculerTempsEstimeLivraison();
        }
        
        
        verifierEtRembourserSiNecessaire();
    }

    
    private void verifierEtRembourserSiNecessaire() {
        if (this.tempsEstimeLivraison > 30 && !commandeRemboursee) {
            this.prix_total = 0;
            this.commandeRemboursee = true;
            System.out.println("Temps estimé supérieur à 30 minutes → commande #" + numero_commande + " remboursée.");
        }
    }

    public String toString() {
        String texte = "----------------------------------------------------------------------------------------------\n";
        texte += "Commande n°" + numero_commande
              + ", Prix total: " + prix_total
              + ", Statut: " + statut_commande
              + ", Heure de commande: " + heure_commande + "\n";
        
        if (livreur != null) {
            texte += "Livreur: " + livreur.getNom() + ", Temps estimé: " + tempsEstimeLivraison + " minutes\n";
        }
        
        texte += "Pizzas commandées :\n";
        
        for (LigneCommande ligne : lignes) {
            texte += "- " + ligne.getPizza().getNom()
                  + ", Taille: " + ligne.getTaille()
                  + ", Quantité: " + ligne.getQuantite() + "\n";
        }

        return texte;
    }

    public void marquerCommeLivree() {
        if (statut_commande.equals("en livraison")) {
            setStatutCommande("livrée");
            this.heure_livraison = Time.valueOf(LocalDateTime.now().toLocalTime());
            verifierRetardLivraison();
            System.out.println("La commande a été livrée !");
            if (livreur != null) {
                livreur.setH_arrive(null);
                livreur.setH_depart(null);
            }
        } else {
            System.out.println("La commande n'est pas en livraison.");
        }
    }

    public void commencerLivraison() {
        if (statut_commande.equals("en attente")) {
            setStatutCommande("en livraison");
            if (Session.livreurs != null && !Session.livreurs.isEmpty()) {
                Random rand = new Random();
                Livreur livreurAssigne = Session.livreurs.get(rand.nextInt(Session.livreurs.size()));
                this.livreur = livreurAssigne;
                this.heureDebutLivraison = LocalDateTime.now();
                livreurAssigne.setH_depart(this.heureDebutLivraison);
                this.tempsEstimeLivraison = calculerTempsEstimeLivraison();

                
                verifierEtRembourserSiNecessaire();

                System.out.println("La commande est en cours de livraison par " + livreurAssigne.getNom() + ".");
                System.out.println("Temps estimé de livraison : " + this.tempsEstimeLivraison + " minutes.");
            } else {
                System.out.println("La commande est en cours de livraison, mais aucun livreur n'est disponible.");
            }
        } else {
            System.out.println("La commande est déjà " + statut_commande);
        }
    }

    public boolean verifierEtDebiter(Client client) {
        float total = 0;
    
        for (LigneCommande ligne : lignes) {
            float prixPizza = ligne.getPizza().getPrix();
    
           
            if (ligne.getTaille().equals("naine")) {
                prixPizza -= prixPizza * (1.0f / 3.0f);
            } else if (ligne.getTaille().equals("ogresse")) {
                prixPizza += prixPizza * (1.0f / 3.0f);
            }
            total += prixPizza * ligne.getQuantite();
        }
    
        if (client.getSolde() >= total) {
            client.setSolde(client.getSolde() - total);
            this.prix_total = (int) total; 
            System.out.println("Paiement accepté, commande validée !");
            client.ajouterCommandeHistorique(this);
    
            int totalPizzas = 0;
            for (LigneCommande ligne : lignes) {
                totalPizzas += ligne.getQuantite();
            }
    
            client.nb_pizza_achete += totalPizzas;
    
            if (client.verifierFidelite()) {
                System.out.println("Félicitations, vous bénéficiez d'une pizza gratuite !");
            }
    
            return true;
        } else {
            System.out.println("Solde insuffisant, commande refusée.");
            return false;
        }
    }

    public void verifierRetardLivraison() {
        if (heure_commande == null || heure_livraison == null) {
            System.out.println("Heure de commande ou de livraison non renseignée.");
            return;
        }
    
        long diffMillis = heure_livraison.getTime() - heure_commande.getTime(); 
        long diffMinutes = diffMillis / (60 * 1000); 
        if (diffMinutes > 30 && !commandeRemboursee) {
            System.out.println("Livraison en retard ! La commande #" + numero_commande + " est gratuite.");
            this.prix_total = 0;
            this.commandeRemboursee = true;
        } else {
            System.out.println("Livraison dans les temps !");
        }
    }

    public int getTempsEstimeLivraison() {
        return this.tempsEstimeLivraison;
    }

    public int getTempsRestant() {
        if (statut_commande.equals("livrée")) return 0;
        if (statut_commande.equals("en attente") || livreur == null || heureDebutLivraison == null)
            return getTempsEstimeLivraison();

        LocalDateTime maintenant = LocalDateTime.now();
        Duration duree = Duration.between(heureDebutLivraison, maintenant);
        int minutesEcoulees = (int) duree.toMinutes();
        int tempsRestant = getTempsEstimeLivraison() - minutesEcoulees;

        if (tempsRestant <= 0) {
            if (!statut_commande.equals("livrée")) {
                setStatutCommande("livrée");
                this.heure_livraison = Time.valueOf(LocalDateTime.now().toLocalTime());
                verifierRetardLivraison();
                System.out.println("La commande #" + numero_commande + " a été livrée automatiquement!");
            }
            return 0;
        }

        return tempsRestant;
    }

    public int getTempsRestantSecondes() {
        if (statut_commande.equals("livrée")) return 0;
        if (statut_commande.equals("en attente") || livreur == null || heureDebutLivraison == null)
            return getTempsEstimeLivraison() * 60;

        LocalDateTime maintenant = LocalDateTime.now();
        Duration duree = Duration.between(heureDebutLivraison, maintenant);
        long secondesEcoulees = duree.getSeconds();
        int tempsRestantSecondes = getTempsEstimeLivraison() * 60 - (int) secondesEcoulees;

        if (tempsRestantSecondes <= 0) {
            if (!statut_commande.equals("livrée")) {
                setStatutCommande("livrée");
                this.heure_livraison = Time.valueOf(LocalDateTime.now().toLocalTime());
                verifierRetardLivraison();
                System.out.println("La commande #" + numero_commande + " a été livrée automatiquement!");
            }
            return 0;
        }

        return tempsRestantSecondes;
    }

    public String getTempsRestantFormate() {
        int t = getTempsRestantSecondes();
        int min = t / 60;
        int sec = t % 60;
        return String.format("%02d:%02d", min, sec);
    }
    
    public boolean isCommandeRemboursee() {
        return commandeRemboursee;
    }
    
    public void setCommandeRemboursee(boolean rembourse) {
        this.commandeRemboursee = rembourse;
    }

    
    public static class LigneCommande {
        private pizza pizza;
        private int quantite;
        private String taille;

        public LigneCommande(pizza pizza, int quantite, String taille) {
            this.pizza = pizza;
            this.quantite = quantite;
            this.taille = taille;
        }

        public pizza getPizza() {
            return pizza;
        }

        public int getQuantite() {
            return quantite;
        }

        public String getTaille() {
            return taille;
        }
    }
}