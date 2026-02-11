import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class VoirSoldeFenetre extends JFrame {

    private JLabel labelSolde;
    private JButton btnAjouterArgent;
    private JButton btnVoirHistorique; 
    private JTextField textFieldMontant;
    private JButton btnConfirmer;
    private JLabel labelMontant;
    private Fenetre fenetrePrincipale; 

    public VoirSoldeFenetre(Fenetre fenetrePrincipale) {
        this.fenetrePrincipale = fenetrePrincipale;
        
        setTitle("Voir Son Compte");
        setSize(400, 350); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        
        String texteAffichage;
        if (Session.clientConnecte != null) {
            texteAffichage = "Votre solde est : " + Session.clientConnecte.getSolde() + " €";
        } else {
            texteAffichage = "Votre solde est : 0.00 €";
        }
        labelSolde = new JLabel(texteAffichage);
        
        labelSolde.setFont(new Font("Arial", Font.BOLD, 16));
        labelSolde.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelSolde);

        
        panel.add(Box.createVerticalStrut(20));

        
        btnAjouterArgent = new JButton("Ajouter de l'argent");
        styleButton(btnAjouterArgent);
        btnAjouterArgent.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnAjouterArgent);
        
        
        panel.add(Box.createVerticalStrut(10));
        
        
        btnVoirHistorique = new JButton("Voir Historique");
        styleButton(btnVoirHistorique);
        btnVoirHistorique.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnVoirHistorique);

        
        JPanel montantPanel = new JPanel();
        montantPanel.setLayout(new FlowLayout());

        
        textFieldMontant = new JTextField();
        textFieldMontant.setPreferredSize(new Dimension(150, 30));
        textFieldMontant.setVisible(false);  

        
        labelMontant = new JLabel("Montant : ");
        labelMontant.setFont(new Font("Arial", Font.PLAIN, 14));
        labelMontant.setVisible(false);  

        montantPanel.add(labelMontant);
        montantPanel.add(textFieldMontant);
        panel.add(montantPanel);

        
        btnConfirmer = new JButton("Confirmer l'ajout");
        styleButton(btnConfirmer);
        btnConfirmer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmer.setVisible(false);  
        panel.add(btnConfirmer);

        
        btnAjouterArgent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                labelMontant.setVisible(true);
                textFieldMontant.setVisible(true);
                btnConfirmer.setVisible(true);
                btnAjouterArgent.setEnabled(false);  
            }
        });

        
        btnVoirHistorique.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            
                new HistoriqueFenetre().setVisible(true);
            }
        });

        
        btnConfirmer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    
                    float montant = Float.parseFloat(textFieldMontant.getText());

                    if (montant <= 0) {
                        JOptionPane.showMessageDialog(null, "Veuillez entrer un montant valide.");
                        return;
                    }

                    
                    float nouveauSolde = Session.clientConnecte.getSolde() + montant;
                    Session.clientConnecte.setSolde(nouveauSolde);

                    
                    labelSolde.setText("Votre solde est : " + nouveauSolde + " €");

                    
                    if (fenetrePrincipale != null) {
                        fenetrePrincipale.mettreAJourSolde(nouveauSolde);
                    }

                    
                    textFieldMontant.setText("");

                    
                    textFieldMontant.setVisible(false);
                    labelMontant.setVisible(false);
                    btnConfirmer.setVisible(false);

                    btnAjouterArgent.setEnabled(true);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Veuillez entrer un montant valide.");
                }
            }
        });

        
        textFieldMontant.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnConfirmer.doClick();  
                }
            }
        });

        
        add(panel);
    }

    
    public VoirSoldeFenetre() {
        this(null); 
    }

    
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(80, 180, 100));  
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));  
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }
}