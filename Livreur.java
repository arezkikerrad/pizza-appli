import java.time.LocalDateTime;

public class Livreur {
    private String vehicule;
    private int id_livreur;
    private LocalDateTime h_depart;
    private LocalDateTime h_arrive;
    private String nom;

    public Livreur(String vehicule, int id_livreur, LocalDateTime h_depart, LocalDateTime h_arrive, String nom) {
        this.vehicule = vehicule;
        this.id_livreur = id_livreur;
        this.h_depart = h_depart;
        this.h_arrive = h_arrive;
        this.nom = nom;
    }

    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    public void setId_livreur(int id_livreur) {
        this.id_livreur = id_livreur;
    }

    public void setH_depart(LocalDateTime h_depart) {
        this.h_depart = h_depart;
    }

    public void setH_arrive(LocalDateTime h_arrive) {
        this.h_arrive = h_arrive;
    }

    public String getVehicule() {
        return this.vehicule;
    }

    public int getId_livreur() {
        return this.id_livreur;
    }

    public LocalDateTime getH_depart() {
        return this.h_depart;
    }

    public LocalDateTime getH_arrive() {
        return this.h_arrive;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
