import java.io.*;
import java.util.*;

public class Catalogue {
    private ArrayList<pizza> catalogue_pizzas = new ArrayList<pizza>();

    public Catalogue(ArrayList<pizza> pizzas) {
        this.catalogue_pizzas = pizzas;
    }

    public ArrayList<pizza> addPizzaCatalogue(pizza pizza) {
        for (pizza p : catalogue_pizzas) {
            if (p.equals(pizza)) {
                System.out.println("La pizza est déjà présente dans le catalogue.");
                return catalogue_pizzas;
            }
        }
        catalogue_pizzas.add(pizza);
        return catalogue_pizzas;
    }

    public ArrayList<pizza> getCatalogue() {
        return catalogue_pizzas;
    }

    public void retirerPizzaCatalogue(pizza pizza) {
        catalogue_pizzas.remove(pizza);
    }
}
