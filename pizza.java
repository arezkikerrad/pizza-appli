import java.util.*;

public class pizza {
    private String taille;
    private String nom;
    private float prix;
    private ArrayList<ingredients> ingredients = new ArrayList<>();

    public pizza(String taille, String nom, float prix) {
        this.taille = taille;
        this.nom = nom;
        this.prix = prix;

        if (taille.equals("naine")) {
            this.prix = prix - (prix * (1.0f / 3.0f));
        } else if (taille.equals("humaine")) {
            this.prix = prix;
        } else if (taille.equals("ogresse")) {
            this.prix = prix + (prix * (1.0f / 3.0f));
        }
    }

    public String getTaille() {
        return taille;
    }

    public String getNom() {
        return nom;
    }

    public float getPrix() {
        return prix;
    }

    public ArrayList<ingredients> getIngredients() {
        return ingredients;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void ajouterIngredient(ingredients ing) {
        ingredients.add(ing);
    }

    public String toString() {
        String texte = "Pizza: " + nom + ", Taille: " + taille + ", Prix: " + prix + "€\nIngrédients: ";
        
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients ing = ingredients.get(i);
            texte += ing.getIngredient() + ", ";
        }

        return texte;
    }
}
