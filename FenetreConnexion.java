import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FenetreConnexion extends JFrame {

    public FenetreConnexion() {
        setTitle("Connexion RaPizza");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel labelTel = new JLabel("Entrez votre numéro de téléphone :");
        JTextField fieldTel = new JTextField();
        JButton btnSeConnecter = new JButton("Se connecter");

        panel.add(labelTel);
        panel.add(fieldTel);
        panel.add(btnSeConnecter);

        add(panel);

        
        btnSeConnecter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tel = fieldTel.getText().trim();

                
                if (tel.isEmpty() || !tel.matches("\\d{10}")) {  
                    JOptionPane.showMessageDialog(null, "Veuillez entrer un numéro valide à 10 chiffres !");
                    return;
                }

                
                Client trouve = null;
                for (Client c : Session.listeClients) {
                    if (c.getTel().equals(tel)) {
                        trouve = c;
                        break;
                    }
                }

                if (trouve != null) {
                    
                    Session.clientConnecte = trouve;
                    JOptionPane.showMessageDialog(null, "Bienvenue " + trouve.getNom() + " !");
                    ouvrirMenuPrincipal();
                } else {
                    
                    int choix = JOptionPane.showConfirmDialog(null, "Client non trouvé. Voulez-vous créer un nouveau compte ?", "Créer compte", JOptionPane.YES_NO_OPTION);
                    if (choix == JOptionPane.YES_OPTION) {
                        
                        String nom = JOptionPane.showInputDialog("Entrez votre nom :");
                        String telCreation = JOptionPane.showInputDialog("Entrez votre numéro de téléphone :");

                        
                        if (nom != null && !nom.trim().isEmpty() && telCreation != null && telCreation.matches("\\d{10}")) {
                            Client nouveau = new Client(telCreation, nom.trim(), 0.0f); 
                            Session.listeClients.add(nouveau);
                            Session.clientConnecte = nouveau;
                            JOptionPane.showMessageDialog(null, "Compte créé avec succès !");
                            ouvrirMenuPrincipal();
                        } else {
                            JOptionPane.showMessageDialog(null, "Numéro de téléphone ou nom invalide. Abandon de la création.");
                        }
                    }
                }
            }
        });

       
        fieldTel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
               
                    btnSeConnecter.doClick();
                }
            }
        });

        setVisible(true);
    }

    private void ouvrirMenuPrincipal() {
        new Fenetre().setVisible(true);
        this.dispose(); 
    }
}
