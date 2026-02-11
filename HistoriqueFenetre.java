import java.awt.*;
import java.util.List;
import javax.swing.*;

public class HistoriqueFenetre extends JFrame {

    public HistoriqueFenetre() {
        setTitle("Historique des commandes");
        setSize(800, 500);  
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textAreaHistorique = new JTextArea();
        textAreaHistorique.setEditable(false);
        textAreaHistorique.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textAreaHistorique);
        panel.add(scrollPane, BorderLayout.CENTER);

        if (Session.clientConnecte != null) {
            List<commande> historique = Session.clientConnecte.getHistorique();
            
           
            System.out.println("Nombre de commandes dans l'historique: " + historique.size());
            
            if (historique == null || historique.isEmpty()) {
                textAreaHistorique.setText("Aucune commande dans l'historique.");
            } else {
                StringBuilder texte = new StringBuilder();
                texte.append("Historique des commandes pour: ").append(Session.clientConnecte.getNom()).append("\n\n");
                
                for (commande cmd : historique) {
                    texte.append(cmd.toString()).append("\n");
                }
                textAreaHistorique.setText(texte.toString());
            }
        } else {
            textAreaHistorique.setText("Aucun client connecté.");
        }

        
        JButton btnRafraichir = new JButton("Rafraîchir l'historique");
        btnRafraichir.addActionListener(e -> {
            if (Session.clientConnecte != null) {
                List<commande> historique = Session.clientConnecte.getHistorique();
                System.out.println("Nombre de commandes dans l'historique après rafraîchissement: " + historique.size());
                
                if (historique == null || historique.isEmpty()) {
                    textAreaHistorique.setText("Aucune commande dans l'historique.");
                } else {
                    StringBuilder texte = new StringBuilder();
                    texte.append("Historique des commandes pour: ").append(Session.clientConnecte.getNom()).append("\n\n");
                    
                    for (commande cmd : historique) {
                        texte.append(cmd.toString()).append("\n");
                    }
                    textAreaHistorique.setText(texte.toString());
                }
            } else {
                textAreaHistorique.setText("Aucun client connecté.");
            }
        });
        
        JPanel panelBas = new JPanel();
        panel.add(panelBas, BorderLayout.SOUTH);

        add(panel);
    }
}