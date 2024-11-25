package gestiondespatient;


import javax.swing.*;

import javax.swing.text.JTextComponent;

import metier.entity.User;
import dao.IUserDAOImpl;


import gestiondespatient.LoginScreen.RoundedButton;
import gestiondespatient.LoginScreen.RoundedField;
import gestiondespatient.LoginScreen.RoundedpasswordField;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;

public class CreateAccountScreen extends JFrame{
	private JTextField emailField;

	
	private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmpasswordField;
    private JButton backbutton;


    private JButton loginButton;
    private JButton createAccountButton;
    private JLabel backgroundLabel;
    private ImageIcon backgroundImageIcon;

    public CreateAccountScreen() {
        initUI();
    }

    private void initUI() {
        setTitle("Inscription");
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
        loginPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 2, 100));

        // Panel pour le titre avec FlowLayout
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Create Account") {
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
        titleLabel.setForeground(Color.black);
        titlePanel.add(titleLabel);
        loginPanel.add(titlePanel);

        // Ajouter de l'espace entre le titre et le champ username
        loginPanel.add(Box.createVerticalStrut(70));

        

     // Panel pour l'email d'utilisateur avec FlowLayout
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.setOpaque(false);
        JLabel emailLabel = new JLabel("EMAIL:         ") {
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
        emailLabel.setFont(new Font("Arial", Font.BOLD, 30));
        emailLabel.setForeground(Color.LIGHT_GRAY);
        emailPanel.add(emailLabel);
        emailField = new JTextField();
        RoundedField roundedEmailField = new RoundedField(50);

        roundedEmailField.setPreferredSize(new Dimension(1000,42));
        emailPanel.add(roundedEmailField);
        loginPanel.add(emailPanel);

        // Ajouter de l'espace entre le champ username et le champ password
        loginPanel.add(Box.createVerticalStrut(50));
        
     // Panel pour le nom d'utilisateur avec FlowLayout
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        usernamePanel.setOpaque(false);
        JLabel usernameLabel = new JLabel("Username:   ") {
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
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 30));
        usernameLabel.setForeground(Color.LIGHT_GRAY);
        usernamePanel.add(usernameLabel);
        usernameField = new JTextField();
        RoundedField roundedUsernameField = new RoundedField(50);

        roundedUsernameField.setPreferredSize(new Dimension(1000,50));
        usernamePanel.add(roundedUsernameField);
        loginPanel.add(usernamePanel);
        
        loginPanel.add(Box.createVerticalStrut(50));

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
        
        passwordLabel.setFont(new Font("Helvetica", Font.BOLD, 30));
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        passwordPanel.add(passwordLabel);
        passwordField = new JPasswordField();
        RoundedpasswordField passwordRoundedField = new RoundedpasswordField(50);

        passwordRoundedField.setPreferredSize(new Dimension(440,42));
        passwordPanel.add(passwordRoundedField);
        loginPanel.add(passwordPanel);

        loginPanel.add(Box.createVerticalStrut(50));
        
     // Panel pour le confirmationmot de passe avec FlowLayout
        JPanel confirmpasswordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        confirmpasswordPanel.setOpaque(false);
        JLabel confirmpasswordLabel = new JLabel("confirm your Password:   ") {
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
        
        confirmpasswordLabel.setFont(new Font("Helvetica", Font.BOLD, 30));
        confirmpasswordLabel.setForeground(Color.LIGHT_GRAY);
        confirmpasswordPanel.add(confirmpasswordLabel);
        confirmpasswordField = new JPasswordField();
        RoundedpasswordField confirmpasswordRoundedField = new RoundedpasswordField(50);

        confirmpasswordRoundedField.setPreferredSize(new Dimension(440,42));
        confirmpasswordPanel.add(confirmpasswordRoundedField);
        loginPanel.add(confirmpasswordPanel);

        // Ajouter de l'espace entre le champ password et les boutons
        loginPanel.add(Box.createVerticalStrut(100));

     // Panel principal pour les boutons avec FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 50 pixels d'espace horizontal entre les composants
        buttonPanel.setOpaque(false);

   


        // Ajout du bouton de création de compte directement au buttonPanel
        createAccountButton = new RoundedButton("inscription");
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 23));
        createAccountButton.setBackground(Color.RED);
        createAccountButton.setForeground(Color.BLACK);
        createAccountButton.setPreferredSize(new Dimension(200, 80));
        createAccountButton.addActionListener(e -> {
        	KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    	    Component comp = manager.getFocusOwner();
    	    if (comp instanceof JTextComponent) {
    	        comp.dispatchEvent(new FocusEvent(comp, FocusEvent.FOCUS_LOST));
    	    }
    		 String email = roundedEmailField.getText().trim();
    		    String username = roundedUsernameField .getText().trim();
    		    String password = new String(passwordRoundedField.getPassword()).trim();
    		    String confirmPassword = new String(confirmpasswordRoundedField.getPassword()).trim();

    		    // Vérifier que les mots de passe correspondent
    		    if (!password.equals(confirmPassword)) {
    		        JOptionPane.showMessageDialog(null, "Les mots de passe ne correspondent pas");
    		        return;
    		    }

    		    // Débogage : imprimez les valeurs pour confirmer ce qui est reçu des champs
    		    System.out.println("Email: " + email);
    		    System.out.println("Username: " + username);
    		    System.out.println("Password: " + password);

    		    if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
    		        JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs");
    		        return;
    		    }

    	    // Création de l'objet User
    	    User newUser = new User();
    	    newUser.setUsername(username);
    	    newUser.setEmail(email);
    	    newUser.setPassword(password); // Ici, vous devriez normalement hasher le mot de passe

    	 // Déclaration de userDao en dehors du bloc try initial
    	    IUserDAOImpl userDao = null;
    	    try {
    	    	 userDao = new IUserDAOImpl();
    	        // Utilisation de userDao pour vérifier l'existence de l'e-mail et ajouter un nouvel utilisateur...
    	    } catch (SQLException ex) {
    	        ex.printStackTrace(); // Affichez l'erreur dans la console pour le débogage
    	        JOptionPane.showMessageDialog(null, "Erreur lors de la connexion à la base de données. Veuillez réessayer plus tard.");
    	    }

    	    // Utilisation de userDao pour vérifier l'existence de l'e-mail et ajouter un nouvel utilisateur...
    	    try {
    	        if (userDao.existsEmail(email)) {
    	            JOptionPane.showMessageDialog(null, "Email déjà utilisé");
    	            return;
    	        }
    	        
    	        userDao.addUser(newUser);
    	        JOptionPane.showMessageDialog(null, "Compte créé avec succès!");
    	    } catch (SQLException ex) {
    	        ex.printStackTrace(); // Affichez l'erreur dans la console pour le débogage
    	        JOptionPane.showMessageDialog(null, "Erreur lors de la création du compte. Veuillez réessayer plus tard.");
    	    }



    	    // Fermer la fenêtre actuelle et ouvrir la fenêtre d'accueil
    	    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(createAccountButton);
    	    currentFrame.dispose();
    	    LoginScreen login = new LoginScreen();
    	    if (currentFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
    	        // Maximiser la fenêtre d'accueil
    	        login.setExtendedState(JFrame.MAXIMIZED_BOTH);
    	    }
    	    login.setVisible(true);
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
        add(backgroundLabel, BorderLayout.CENTER); // Pour une meilleure expérience de défilement

     // Création du bouton de retour
        ImageIcon icon = new ImageIcon("C:\\Users\\ctiza\\Downloads\\418858296_1226310645001110_5532990036752262399_n.png");

     // Redimensionnez l'icône à la nouvelle largeur et hauteur désirées
        Image image = icon.getImage(); // transformez-la
        Image newimg = image.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        icon = new ImageIcon(newimg);  // transformez-la en ImageIcon

        // Maintenant, vous pouvez l'ajouter à votre bouton
        backbutton = new JButton(icon);
        backbutton.setBorderPainted(false);
        backbutton.setContentAreaFilled(false);
        backbutton.setFocusPainted(false);
        backbutton.setOpaque(false);
        
     // Positionnement du bouton de retour dans le panel principal
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setOpaque(false);
        backPanel.add(backbutton);
        loginPanel.add(backPanel, 0); // Ajoutez le backPanel en première position dans loginPanel
        backbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// Créer une instance de l'interface de création de compte
                LoginScreen loginscreen = new LoginScreen();
                // Appeler la méthode pour définir les dimensions de l'interface de création de compte
                // en passant les dimensions de l'interface de connexion et l'état de plein écran
                loginscreen.setWindowSize2(getWidth(), getHeight(), (getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH);
                // Masquer la fenêtre de connexion
                setVisible(false);
                // Rendre visible la fenêtre de création de compte
                loginscreen.setVisible(true);
                // Libérer les ressources lorsque la fenêtre de création de compte est fermée
                loginscreen.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setVisible(true); // Afficher à nouveau la fenêtre de connexion lorsque la fenêtre de création de compte est fermée
                    }
                });
            }
            });
        
       

        
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
    public void setWindowSize(int width, int height , boolean isFullScreen) {
    	if (isFullScreen) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            setSize(width, height);
        }
    }

    


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
        	CreateAccountScreen insScreen = new CreateAccountScreen();
            insScreen.setVisible(true);
        });
    }


}