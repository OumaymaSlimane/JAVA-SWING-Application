package gestiondespatient;



import javax.swing.*;


import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.AppointmentImpl;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import metier.entity.Appointment;


public class Acceuil extends JFrame {
    private final DefaultTableModel model;
    private final JTable table;
    private final LocalDate now = LocalDate.now();
    private YearMonth currentMonth = YearMonth.of(now.getYear(), now.getMonth());
    private BackgroundPanel detailsPanel; // Modification ici pour utiliser BackgroundPanel
    private final JScrollPane detailsScrollPane = new JScrollPane(detailsPanel);
    private final JLabel monthLabel = new JLabel();
    private final List<Appointment> appointments = new ArrayList<>();

    
    private final JPanel panelfixe = new JPanel();

    public Acceuil() {
        setTitle("Calendrier Agenda");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        model = new DefaultTableModel();
        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
       
        
        constructCalendar(currentMonth);
        styleTable();
     // Initialisation de detailsPanel avec l'image de fond
        detailsPanel = new BackgroundPanel("C:\\Users\\ctiza\\Downloads\\434222438_793641225713122_8876138553393544165_n.png");
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsScrollPane.setViewportView(detailsPanel); // Cette ligne assure que le JScrollPane est bien lié à detailsPanel.

        
        SidePanel sidePanel = new SidePanel();

        // Ajoutez sidePanel à l'interface principale
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidePanel, BorderLayout.WEST);
        
        JScrollPane scrollPane = new JScrollPane(table);
        JSplitPane splitPaneRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, detailsScrollPane);
        splitPaneRight.setDividerLocation(500);
        
        

        add(splitPaneRight, BorderLayout.CENTER);
        
        
        
        JSplitPane splitPaneMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelfixe, splitPaneRight);
        splitPaneMain.setDividerLocation(0); // Ajustez cette valeur selon la largeur que vous souhaitez pour la sidebar
        add(splitPaneMain, BorderLayout.CENTER);
        
        
        
         

        setupDetailsPanel();
        setupMouseListener();
        setupNavigationButtons();
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        updateMonthLabel(currentMonth);
        add(monthLabel, BorderLayout.NORTH);
        
        
        
        
        chargerRendezVousDansPanel();
    }
    

    
   
        
    
    
   
    //pour les buutton de rendez vous supp + rappel
    private void styleButton2(JButton button) {
  	  button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centre le bouton horizontalement
  	    button.setFont(new Font("Serif", Font.ROMAN_BASELINE, 16)); // Ajustez la taille de la police selon vos besoins
  	    button.setForeground(new Color(255, 255, 255)); // Texte en blanc pour un meilleur contraste
  	    
  	    // Utiliser des couleurs de fond plus vives ou plus subtiles selon le design de l'application
  	    button.setBackground(new Color(28, 130, 185)); // Un bleu moderne, par exemple, Bootstrap primary
  	    
  	    // Créer une bordure personnalisée pour les boutons
  	    button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15)); // Ajuste l'espacement intérieur
  	    
  	    // Pour rendre les coins des boutons arrondis (nécessite d'outrepasser la méthode paintComponent si nécessaire)
  	    button.setContentAreaFilled(false); // Nécessaire pour les coins arrondis
  	    button.setOpaque(true); // Assurez-vous que le bouton est opaque pour voir la couleur de fond
  	    
  	    // Effet au survol du bouton (hover)
  	    button.addMouseListener(new MouseAdapter() {
  	        public void mouseEntered(MouseEvent e) {
  	            button.setBackground(button.getBackground().darker()); // Assombrir la couleur au survol
  	        }

  	        public void mouseExited(MouseEvent e) {
  	            button.setBackground(new Color(28, 130, 185)); // Rétablir la couleur originale
  	        }
  	    });
  	    
  	    // Pas de bordure peinte et pas de focus peint pour un look plus propre
  	    button.setBorderPainted(false);
  	    button.setFocusPainted(false);
  	    button.setPreferredSize(new Dimension(100,50)); // Ajustez (largeur, hauteur) selon vos besoins

	}
    //pour les buttons de nextmois et mois precedent :
    private void styleButton3(JButton button) {
  	  button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centre le bouton horizontalement
  	    button.setFont(new Font("Serif", Font.ROMAN_BASELINE, 20)); // Ajustez la taille de la police selon vos besoins
  	    button.setForeground(new Color(255, 255, 255)); // Texte en blanc pour un meilleur contraste
  	    
  	    // Utiliser des couleurs de fond plus vives ou plus subtiles selon le design de l'application
  	    button.setBackground(new Color(28, 130, 185)); // Un bleu moderne, par exemple, Bootstrap primary
  	    
  	    // Créer une bordure personnalisée pour les boutons
  	    button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Ajuste l'espacement intérieur
  	    
  	    // Pour rendre les coins des boutons arrondis (nécessite d'outrepasser la méthode paintComponent si nécessaire)
  	    button.setContentAreaFilled(false); // Nécessaire pour les coins arrondis
  	    button.setOpaque(true); // Assurez-vous que le bouton est opaque pour voir la couleur de fond
  	    
  	    // Effet au survol du bouton (hover)
  	    button.addMouseListener(new MouseAdapter() {
  	        public void mouseEntered(MouseEvent e) {
  	            button.setBackground(button.getBackground().darker()); // Assombrir la couleur au survol
  	        }

  	        public void mouseExited(MouseEvent e) {
  	            button.setBackground(new Color(28, 130, 185)); // Rétablir la couleur originale
  	        }
  	    });
  	    
  	    // Pas de bordure peinte et pas de focus peint pour un look plus propre
  	    button.setBorderPainted(false);
  	    button.setFocusPainted(false);
  	    button.setPreferredSize(new Dimension(200,50)); // Ajustez (largeur, hauteur) selon vos besoins

	}



	private void setupDetailsPanel() {
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsScrollPane.setViewportView(detailsPanel); // Assurez-vous que detailsScrollPane affiche bien detailsPanel.
        detailsScrollPane.getViewport().setBackground(detailsPanel.getBackground()); // Pour que le viewport de JScrollPane ait la même couleur de fond
        
        
        
    }
	//pour la photo de bacground :
	class BackgroundPanel extends JPanel {
	    private Image backgroundImage;

	    public BackgroundPanel(String imagePath) {
	        try {
	            backgroundImage = new ImageIcon(imagePath).getImage();
	        } catch (Exception e) {
	            // Gestion des erreurs lors du chargement de l'image
	            e.printStackTrace();
	        }
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        // Dessine l'image en arrière-plan en s'adaptant à la taille du panneau.
	        if (backgroundImage != null) {
	            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
	        }
	    }
	}

//pour la partie de l'écriture de rendez vous:
    private void addAppointmentToPanel(Appointment appointment) {
        JPanel appointmentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        appointmentPanel.setOpaque(false); // Assurez-vous que le panel est transparent

        appointmentPanel.add(new JLabel(appointment.toString()));

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> {
            detailsPanel.remove(appointmentPanel);
            detailsPanel.revalidate();
            detailsPanel.repaint();
            appointments.remove(appointment);
            try {
                AppointmentImpl appointmentImpl = new AppointmentImpl();
                
                // Récupérer l'ID de l'objet Appointment avant de le supprimer
                int appointmentId = appointment.getId();
                
                int rowsDeleted = appointmentImpl.deleteAppointment(appointmentId); // Supprimer le rendez-vous de la base de données
                if (rowsDeleted > 0) {
                    System.out.println("L'appointment avec l'ID " + appointmentId + " a été supprimé de la base de données.");
                } else {
                    System.out.println("Aucun rendez-vous trouvé avec l'ID " + appointmentId + ".");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Gérer l'exception
            }
        });



        JButton envoyerButton = new JButton("Rappel");
        envoyerButton.addActionListener(e -> {
            String email = appointment.getEmail(); // Obtenez l'adresse email de l'appointment
            String sujet = "Rappel de rendez-vous"; // Sujet de l'email
            String contenu = "Bonjour " + appointment.getName() + ",\n\n" +
                    "Juste un rappel pour votre rendez-vous le " +
                    appointment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    " à " + appointment.getTime() + " avec le Dr Slimane. Veuillez confirmer votre présence.\n\n" +
                    "Cordialement,"; // Contenu de l'email

            final String username = "oumaimaryma48@gmail.com"; // Votre adresse email Gmail
            final String password = "tyff wleo dmcx fqae"; // Votre mot de passe Gmail ou le mot de passe d'application si la vérification en deux étapes est activée

         // Création d'un objet Properties pour stocker les paramètres de la session SMTP
            Properties prop = new Properties();

            // Configuration de l'hôte SMTP comme smtp.gmail.com
            prop.put("mail.smtp.host", "smtp.gmail.com");

            // Configuration du port SMTP comme 587 (le port TLS)
            prop.put("mail.smtp.port", "587");

            // Activation de l'authentification SMTP
            prop.put("mail.smtp.auth", "true");

            // Activation du démarrage de la session TLS
            prop.put("mail.smtp.starttls.enable", "true");

            // Création de la session SMTP en utilisant les propriétés définies ci-dessus
            Session session = Session.getInstance(prop, new Authenticator() {
                // Création d'une classe anonyme Authenticator pour fournir les informations d'authentification
                protected PasswordAuthentication getPasswordAuthentication() {
                    // Retourne les informations d'authentification (nom d'utilisateur et mot de passe)
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject(sujet);
                message.setText(contenu);

                Transport.send(message);

                JOptionPane.showMessageDialog(null, "Email envoyé avec succès à " + email);
            } catch (MessagingException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de l'envoi de l'email: " + ex.getMessage());
            }
        });
        deleteButton.setBackground(new Color(188, 64, 34)); // Un gris clair par exemple
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false); // Pour que la couleur de fond soit visible
        envoyerButton.setBackground(new Color(188, 64, 34)); // Même couleur pour le bouton envoyer
        envoyerButton.setOpaque(true);
        envoyerButton.setBorderPainted(false);

        appointmentPanel.add(deleteButton);
        appointmentPanel.add(envoyerButton);
        detailsPanel.add(appointmentPanel);
        detailsPanel.revalidate();
	    styleButton2(deleteButton);
	    styleButton2(envoyerButton);

    }

    private void setupMouseListener() {
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    int column = table.getSelectedColumn();
                    String dayValue = model.getValueAt(row, column).toString();

                    if (!dayValue.equals("")) {
                        JTextField nameField = new JTextField();
                        JTextField timeField = new JTextField();
                        JTextField emailField = new JTextField(); // Ajout du champ pour l'e-mail
                        JPanel panel = new JPanel(new GridLayout(0, 1));
                        panel.add(new JLabel("Nom :"));
                        panel.add(nameField);
                        panel.add(new JLabel("Heure (HH:mm) :"));
                        panel.add(timeField);
                        panel.add(new JLabel("Email :")); // Label pour l'e-mail
                        panel.add(emailField); // Champ pour l'e-mail
                        int result = JOptionPane.showConfirmDialog(null, panel, 
                                "Entrez les détails du rendez-vous pour le " + dayValue + " " + currentMonth.getMonth(), 
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            String name = nameField.getText();
                            String time = timeField.getText();
                            String email = emailField.getText(); // Récupération de l'e-mail
                            if (!name.trim().isEmpty() && !time.trim().isEmpty() && !email.trim().isEmpty()) {
                                LocalDate date = currentMonth.atDay(Integer.parseInt(dayValue));
                                Appointment appointment = new Appointment(name, time, email , date);
                             
                                
                                // Utilisation de la classe AppointmentImpl pour ajouter l'appointment
                                try {
                                    AppointmentImpl appointmentImpl = new AppointmentImpl();
                                 // Lorsque vous créez un rendez-vous et que vous l'ajoutez à la base de données
                                    int appointmentId = appointmentImpl.addAppointment(appointment);
                                    appointment.setId(appointmentId); // Associer l'ID généré à l'objet Appointment
                                    appointments.add(appointment);
                                    addAppointmentToPanel(appointment);
                                    
                                    // Mettre à jour l'affichage des rendez-vous
                                    updateAppointments();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                    // Gérer l'exception
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
    }

    private void setupNavigationButtons() {
        JButton previousButton = new JButton("Mois précédent");
        previousButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateMonthLabel(currentMonth);
            constructCalendar(currentMonth);
            updateAppointments();
        });
        styleButton3(previousButton);
        JButton nextButton = new JButton("Mois suivant");
        nextButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateMonthLabel(currentMonth);
            constructCalendar(currentMonth);
            updateAppointments();
        });
        styleButton3(nextButton);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.setBackground(new Color(210, 210, 225)); // Gris avec une teinte bleue


        add(buttonPanel, BorderLayout.SOUTH);
    }
    private void updateMonthLabel(YearMonth currentMonth) {

        monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.FRENCH) + " " + currentMonth.getYear());
        monthLabel.setFont(new Font("Serif", Font.BOLD, 30)); // Changer "Serif" par le nom de la police souhaitée, et 18 par la taille désirée

        // Modifier la couleur de fond de monthLabel
        monthLabel.setOpaque(true); // Cela permet de rendre le fond non transparent, de sorte que la couleur de fond soit visible
        monthLabel.setBackground(new Color(210, 210, 225)); // Couleur de fond gris clair avec une nuance bleue

    }
    private void constructCalendar(YearMonth currentMonth) {
        String[] daysOfWeek = {"Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam"};
        model.setColumnIdentifiers(daysOfWeek);
        
        LocalDate firstOfMonth = currentMonth.atDay(1);
        int dayOfWeekOfFirst = firstOfMonth.getDayOfWeek().getValue() % 7;
        
        LocalDate lastOfMonth = currentMonth.atEndOfMonth();
        int totalDays = lastOfMonth.getDayOfMonth();
       
        
        Vector<Vector<String>> data = new Vector<>();
        Vector<String> week = new Vector<>(8);
        for (int i = 0; i < dayOfWeekOfFirst; i++) {
            week.add("");
        }
        
        for (int day = 1; day <= totalDays; day++) {
            week.add(String.valueOf(day));
            if ((day + dayOfWeekOfFirst) % 7 == 0 || day == totalDays) {
                data.add(week);
                week = new Vector<>(8);
            }
        }
        
        
        
        model.setDataVector(data, new Vector<>(java.util.List.of(daysOfWeek)));
    }

    private void styleTable() {
        table.setFont(new Font("Serif", Font.BOLD, 30));
        table.setRowHeight(110);//pour la largeur de chaque ligne
        table.getTableHeader().setBackground(new Color(150, 150, 150)); // Gris moyen
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 14));
        table.setSelectionBackground(new Color(230, 230, 250)); // Lavande clair
        table.setSelectionForeground(Color.red);
        table.setGridColor(new Color(154, 205, 50));
        table.setFillsViewportHeight(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            table.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
    }

    private void updateAppointments() {
        detailsPanel.removeAll();
        for (Appointment appointment : appointments) {
            if (appointment.getDate().getYear() == currentMonth.getYear() &&
                appointment.getDate().getMonthValue() == currentMonth.getMonthValue()) {
                addAppointmentToPanel(appointment);
            }
        }
        detailsPanel.revalidate();
    }
    
    private void chargerRendezVousDansPanel() {
        try {
            AppointmentImpl appointmentImpl = new AppointmentImpl();
            List<Appointment> listeRendezVous = appointmentImpl.getAllAppointments();

            // Effacer la liste des rendez-vous existants sans supprimer les composants d'affichage
            appointments.clear();

            // Mettre à jour la liste des rendez-vous existants avec les nouveaux rendez-vous récupérés
            appointments.addAll(listeRendezVous);

            // Ajouter chaque rendez-vous récupéré de la base de données à detailsPanel
            for (Appointment rendezVous : listeRendezVous) {
                addAppointmentToPanel(rendezVous); //  ajouter le rendez-vous au panel
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des rendez-vous de la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }






    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Acceuil().setVisible(true));
    }
    
}

