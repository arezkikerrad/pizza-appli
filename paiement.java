import java.io.*;
import java.util.*;

public class paiement {
    private int num_client;
    private int montant;
    private boolean statut;

    public paiement(int num_client, int montant, boolean statut) {
        this.num_client = num_client;
        this.montant = montant;
        this.statut = statut;
    }

    public int getNum_client() {
        return num_client;
    }

    public int getMontant() {
        return montant;
    }

    public boolean getStatut() {
        return statut;
    }

    public void setNum_client(int num_client) {
        this.num_client = num_client;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }
}
