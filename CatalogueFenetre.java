import java.awt.*;
import javax.swing.*;

public class CatalogueFenetre extends JFrame {

    public CatalogueFenetre() {
        setTitle("Catalogue des Pizzas");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        // Utiliser une simple chaîne de caractères
        String contenu = "";
        for (pizza pizza : Session.catalogue) {
            contenu += "- " + pizza.getNom() + " (" + pizza.getTaille() + ") : " + pizza.getPrix() + "€\n";
        }

        textArea.setText(contenu);

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
    }
}
