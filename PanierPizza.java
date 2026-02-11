public class PanierPizza {
    private String nomPizza;
    private String taille;
    private int quantite;
    private float prixUnitaire;
    private float prixTotal;
    private java.util.ArrayList<String> ingredientsSupplementaires;
    
    public PanierPizza(String nomPizza, String taille, int quantite, float prixUnitaire, 
                       java.util.ArrayList<String> ingredientsSupplementaires) {
        this.nomPizza = nomPizza;
        this.taille = taille;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.ingredientsSupplementaires = ingredientsSupplementaires;
        calculerPrixTotal();
    }
    
    private void calculerPrixTotal() {
        this.prixTotal = prixUnitaire * quantite;
    }
    
    public String getNomPizza() { return nomPizza; }
    public String getTaille() { return taille; }
    public int getQuantite() { return quantite; }
    public float getPrixUnitaire() { return prixUnitaire; }
    public float getPrixTotal() { return prixTotal; }
    public java.util.ArrayList<String> getIngredientsSupplementaires() { return ingredientsSupplementaires; }
    
    public String toString() {
        return quantite + " x " + nomPizza + " (" + taille + ") - " + String.format("%.2f €", prixTotal);
    }
}