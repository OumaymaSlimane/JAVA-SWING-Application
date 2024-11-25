package gestiondespatient;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {
    public SidePanel() {
        setupSidebar();
    }

    private void setupSidebar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(135, 206, 235)); // Couleur bleu ciel

        // Ajoutez l'image
        JLabel imageLabel = new JLabel();
        // Assurez-vous d'abahyvoir un fichier image.png dans le répertoire des ressources de votre projet
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\ctiza\\Downloads\\432394118_3520020911661417_672216854829229325_n.png"); // Remplacez "chemin/vers/votre/image.png" par le chemin réel de votre image
        imageLabel.setIcon(imageIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centre l'image horizontalement dans le panneau latéral

        // Ajouter un peu d'espace au-dessus de l'image si nécessaire
        add(Box.createRigidArea(new Dimension(0, 20)));
        // Ajouter l'image au panneau latéral
        add(imageLabel);
        // Ajouter de l'espace entre l'image et le premier bouton
        add(Box.createRigidArea(new Dimension(0, 30)));

        // Ajouter les boutons
        JButton accueilButton = new JButton("Accueil");
        JButton patientButton = new JButton("Patient");
        JButton listeafaire = new JButton("Liste du jour à faire");
        styleButton(accueilButton);
        styleButton(patientButton);
        styleButton(listeafaire);

        accueilButton.addActionListener(e -> {
            // Logique pour afficher l'accueil
            // Récupérer la fenêtre parente
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
         // Masquer la fenêtre parente
            parentFrame.setVisible(false);
            // Créer une instance de l'interface d'accueil
            Acceuil acceuil = new Acceuil();
            // Vérifier si la fenêtre parente est en plein écran
            if ((parentFrame.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0) {
                // Si oui, définir la nouvelle fenêtre en plein écran
                acceuil.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                // Sinon, définir la nouvelle fenêtre en taille normale
                acceuil.setSize(parentFrame.getSize());
            }
            // Afficher l'interface d'accueil
            acceuil.setVisible(true);
        });

        patientButton.addActionListener(e -> {
            // Logique pour afficher l'interface patient
            // Récupérer la fenêtre parente
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
         // Masquer la fenêtre parente
            parentFrame.setVisible(false);
            // Créer une instance de l'interface des patients
            PatientListInterface patientListInterface = new PatientListInterface();
            // Vérifier si la fenêtre parente est en plein écran
            if ((parentFrame.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0) {
                // Si oui, définir la nouvelle fenêtre en plein écran
                patientListInterface.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                // Sinon, définir la nouvelle fenêtre en taille normale
                patientListInterface.setSize(parentFrame.getSize());
            }
            // Afficher l'interface des patients
            patientListInterface.setVisible(true);
        });

        listeafaire.addActionListener(e -> {
            // Logique pour afficher la liste du jour à faire
            // Récupérer la fenêtre parente
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
         // Masquer la fenêtre parente
            parentFrame.setVisible(false);
            // Créer une instance de l'interface de la liste du jour à faire
            TaskManager taskmanager = new TaskManager();
            // Vérifier si la fenêtre parente est en plein écran
            if ((parentFrame.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0) {
                // Si oui, définir la nouvelle fenêtre en plein écran
                taskmanager.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                // Sinon, définir la nouvelle fenêtre en taille normale
                taskmanager.setSize(parentFrame.getSize());
            }
            // Afficher l'interface de la liste du jour à faire
            taskmanager.setVisible(true);
        });



        // Ajoutez les boutons au panneau latéral avec des espaces entre eux
        add(accueilButton);
        add(Box.createRigidArea(new Dimension(0, 10))); // Espace entre accueilButton et patientButton
        add(patientButton);
        add(Box.createRigidArea(new Dimension(0, 10))); // Espace entre patientButton et listeafaire
        add(listeafaire);
        add(Box.createVerticalGlue()); // Pousse tout vers le haut
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centre le bouton horizontalement
        button.setFont(new Font("Serif", Font.ROMAN_BASELINE, 25)); // Ajustez la taille de la police selon vos besoins
        button.setForeground(new Color(255, 255, 255)); // Texte en blanc pour un meilleur contraste
        button.setBackground(new Color(28, 130, 185)); // Couleur de fond bleue
        button.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Ajuste l'espacement intérieur
        button.setContentAreaFilled(false); // Nécessaire pour les coins arrondis
        button.setOpaque(true); // Assurez-vous que le bouton est opaque pour voir la couleur de fond
        // Effet au survol du bouton (hover)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().darker()); // Assombrir la couleur au survol
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(28, 130, 185)); // Rétablir la couleur originale
            }
        });
        // Pas de bordure peinte et pas de focus peint pour un look plus propre
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50)); // Ajustez (largeur, hauteur) selon vos besoins
    }
}

