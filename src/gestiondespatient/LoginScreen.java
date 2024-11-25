package gestiondespatient;


import gestiondespatient.CreateAccountScreen;



import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;

import metier.entity.User;
import dao.IUserDAOImpl;


public class LoginScreen extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;
    private JLabel backgroundLabel;
    private ImageIcon backgroundImageIcon;

    public LoginScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Login");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Background
        backgroundImageIcon = new ImageIcon("C:\\Users\\ctiza\\Downloads\\313971842_655199809449191_5431308033932161324_n.jpg");
        backgroundLabel = new JLabel(backgroundImageIcon);
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel, BorderLayout.CENTER);

        // Panel principal pour les composants avec BoxLayout
        JPanel loginPanel = new JPanel();
        loginPanel.setOpaque(false);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 150, 100));

        // Panel pour le titre avec FlowLayout
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Welcome") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.GRAY); // Couleur de l'ombre
                g2d.drawString(getText(), 3, 82); // Décalage de l'ombre
                g2d.setColor(getForeground()); // Couleur du texte (définie par setForeground)
                g2d.drawString(getText(), 0, 85); // Position du texte
                g2d.dispose();
            }
        };
        titleLabel.setFont(new Font("Arial", Font.BOLD, 80));
        titleLabel.setForeground(Color.WHITE);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 80));
        titleLabel.setForeground(Color.black);
        titlePanel.add(titleLabel);
        loginPanel.add(titlePanel);

        // Ajouter de l'espace entre le titre et le champ username
        loginPanel.add(Box.createVerticalStrut(70));

        // Panel pour le nom d'utilisateur avec FlowLayout
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.setOpaque(false);
        JLabel emailLabel = new JLabel("Email:   ") {
            @Override
            protected void paintComponent(Graphics g) {
                String text = getText();
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Applique l'anti-aliasing pour un rendu de texte plus lisse
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                
                // Prépare le texte et le contour
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2; // Centre le texte horizontalement
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent(); // Centre le texte verticalement
                
                // Dessine le contour
                g2d.setColor(Color.BLACK); // Couleur du contour
                g2d.drawString(text, x + 1, y + 1); // Décalage léger pour l'effet de contour
                
                // Dessine le texte
                g2d.setColor(getForeground());
                g2d.drawString(text, x, y);
                
                g2d.dispose();
            }
        };
        emailLabel.setFont(new Font("Arial", Font.BOLD, 40));
        emailLabel.setForeground(Color.LIGHT_GRAY);
        emailPanel.add(emailLabel);
        emailField = new JTextField();
        RoundedField roundedemailField = new RoundedField(50);

        roundedemailField.setPreferredSize(new Dimension(1000,55));
        emailPanel.add(roundedemailField);
        loginPanel.add(emailPanel);

        // Ajouter de l'espace entre le champ username et le champ password
        loginPanel.add(Box.createVerticalStrut(20));

        // Panel pour le mot de passe avec FlowLayout
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setOpaque(false);
        JLabel passwordLabel = new JLabel("Password:   ") {
            @Override
            protected void paintComponent(Graphics g) {
                String text = getText();
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Applique l'anti-aliasing pour un rendu de texte plus lisse
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                
                // Prépare le texte et le contour
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2; // Centre le texte horizontalement
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent(); // Centre le texte verticalement
                
                // Dessine le contour
                g2d.setColor(Color.BLACK); // Couleur du contour
                g2d.drawString(text, x + 1, y + 1); // Décalage léger pour l'effet de contour
                
                // Dessine le texte
                g2d.setColor(getForeground());
                g2d.drawString(text, x, y);
                
                g2d.dispose();
            }
        };        
        
        passwordLabel.setFont(new Font("Helvetica", Font.BOLD, 40));
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        passwordPanel.add(passwordLabel);
        passwordField = new JPasswordField();
        RoundedpasswordField passwordRoundedField = new RoundedpasswordField(50);

        passwordRoundedField.setPreferredSize(new Dimension(440,45));
        passwordPanel.add(passwordRoundedField);
        loginPanel.add(passwordPanel);

        // Ajouter de l'espace entre le champ password et les boutons
        loginPanel.add(Box.createVerticalStrut(100));

     // Panel principal pour les boutons avec FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0)); // 50 pixels d'espace horizontal entre les composants
        buttonPanel.setOpaque(false);

        // Ajout du bouton de connexion directement au buttonPanel
        loginButton = new RoundedButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 23));
        loginButton.setBackground(Color.DARK_GRAY);
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(300, 80));
        loginButton.addActionListener(e -> {
            String email = roundedemailField.getText();
            String password = new String(passwordRoundedField.getPassword());

            try {
                IUserDAOImpl userDAO = new IUserDAOImpl(); // Créez une instance de votre DAO
                // Utiliser votre DAO pour vérifier si l'email existe
                if (userDAO.existsEmail(email)) {
                    // Si l'e-mail existe, vérifier le mot de passe
                    if (userDAO.authenticateUser(email, password)) {
                        JOptionPane.showMessageDialog(LoginScreen.this, "Login successful!");
                        // Le mot de passe correspond, connectez-vous à l'application ou effectuez d'autres actions nécessaires
                        // Rediriger vers l'interface principale de l'application
                        dispose(); // Fermer la fenêtre actuelle
                        Acceuil accueilFrame = new Acceuil();
                        if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                            // Maximiser la fenêtre d'accueil si la fenêtre de login était maximisée
                            accueilFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        }
                        accueilFrame.setVisible(true); // Afficher la fenêtre d'accueil
                    } else {
                        JOptionPane.showMessageDialog(LoginScreen.this, "Mot de passe incorrect!");
                        // Le mot de passe ne correspond pas, affichez un message d'erreur
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Email introuvable!");
                    // L'e-mail n'existe pas dans la base de données, affichez un message d'erreur
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Affichez l'erreur dans la console pour le débogage
                JOptionPane.showMessageDialog(LoginScreen.this, "Erreur lors de la connexion à la base de données. Veuillez réessayer plus tard.");
            }
        });


        buttonPanel.add(loginButton);

        // Création d'un espace flexible entre les boutons
        Component horizontalStrut = Box.createHorizontalStrut(50); // Vous pouvez ajuster la valeur pour augmenter ou réduire l'espace
        buttonPanel.add(horizontalStrut);

        // Ajout du bouton de création de compte directement au buttonPanel
        createAccountButton = new RoundedButton("Create Account");
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 23));
        createAccountButton.setBackground(Color.LIGHT_GRAY);
        createAccountButton.setForeground(Color.BLACK);
        createAccountButton.setPreferredSize(new Dimension(300, 80));
        createAccountButton.addActionListener(e -> {
        	 // Créer une instance de l'interface de création de compte
            CreateAccountScreen createAccountScreen = new CreateAccountScreen();
            // Appeler la méthode pour définir les dimensions de l'interface de création de compte
            // en passant les dimensions de l'interface de connexion et l'état de plein écran
            createAccountScreen.setWindowSize(getWidth(), getHeight(), (getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH);
            // Masquer la fenêtre de connexion
            setVisible(false);
            // Rendre visible la fenêtre de création de compte
            createAccountScreen.setVisible(true);
            // Libérer les ressources lorsque la fenêtre de création de compte est fermée
            createAccountScreen.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    setVisible(true); // Afficher à nouveau la fenêtre de connexion lorsque la fenêtre de création de compte est fermée
                }
            });
        });
        buttonPanel.add(createAccountButton);

        // Ajouter buttonPanel au panneau principal de votre interface
        loginPanel.add(buttonPanel, BorderLayout.CENTER); // Ajustez en fonction de la disposition de votre UI
        backgroundLabel.add(loginPanel, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(loginPanel);
        scrollPane.setOpaque(false); // Rend le JScrollPane transparent
        scrollPane.getViewport().setOpaque(false); // Rend la vue du JScrollPane transparente
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        backgroundLabel.add(scrollPane, BorderLayout.CENTER);
        add(backgroundLabel, BorderLayout.CENTER);// Pour une meilleure expérience de défilement

        // Ajout d'un écouteur de composants pour redimensionner l'image de fond lorsque la fenêtre change de taille
         addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBackgroundImage(); // Appelle la méthode pour redimensionner l'image de fond
            }
        });
}

    // Méthode pour redimensionner l'image de fond en fonction de la taille de la fenêtre
    private void resizeBackgroundImage() {
        Image background = backgroundImageIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH); // Obtient l'image redimensionnée
        backgroundImageIcon.setImage(background); // Met à jour l'image de l'icône avec la version redimensionnée
        backgroundLabel.repaint(); // Redemande le dessin du label de fond pour afficher la nouvelle image
    }
    //pour le button l'effet radius
    class RoundedButton extends JButton {
        private static final int RADIUS = 50; // Rayon des coins arrondis
        private Shape shape;

        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false); // Nécessaire pour que le fond du bouton soit transparent
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Remplissage du bouton avec sa couleur de fond
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);

            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dessine une bordure arrondie autour du bouton
            g2.setColor(getForeground()); // Utilisez getForeground() pour la couleur de la bordure
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);

            g2.dispose();
        }

        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
            }
            return shape.contains(x, y);
        }
    }

//pour le text field effet radius
    
    class RoundedpasswordField extends JPasswordField {
        private Shape shape;
        private Color backgroundColor = new Color(255, 255, 255, 255); // Exemple d'arrière-plan semi-transparent
        private int radius = 25; // Rayon des coins arrondis

        public RoundedpasswordField(int cols) {
            super(cols);
            setOpaque(false); // Rend le champ de texte transparent
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dessine l'arrière-plan personnalisé
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);

            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dessine la bordure personnalisée
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);

            g2.dispose();
        }

        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            }
            return shape.contains(x, y);
        }
    }

    class RoundedField extends JTextField {
        private Shape shape;
        private Color backgroundColor = new Color(255, 255, 255, 255); // Exemple d'arrière-plan semi-transparent
        private int radius = 25; // Rayon des coins arrondis

        public RoundedField(int cols) {
            super(cols);
            setOpaque(false); // Rend le champ de texte transparent
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dessine l'arrière-plan personnalisé
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);

            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dessine la bordure personnalisée
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);

            g2.dispose();
        }

        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            }
            return shape.contains(x, y);
        }
    }

    
    public void setWindowSize2(int width, int height , boolean isFullScreen) {
    	if (isFullScreen) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            setSize(width, height);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
        	LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        });
    }
}