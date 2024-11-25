package dao;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import metier.entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class IUserDAOImpl implements IUserDAO {
	
	private Connection cnx;
	

	public IUserDAOImpl()throws SQLException {
		cnx=SingletonConnection.getInstance();
	}


	@Override
	public void addUser(User user)throws SQLException {
	   
	        PreparedStatement ps = cnx.prepareStatement("INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)");
	        ps.setString(1, user.getUsername());
	        ps.setString(2, user.getEmail());
	        ps.setString(3, hashPassword(user.getPassword())); // Hasher le mot de passe
	        ps.executeUpdate();
	    
	}


    @Override
    public User findUserByUsername(String username)throws SQLException {
        User user = null;
            PreparedStatement ps = cnx.prepareStatement("SELECT * FROM users WHERE username = ?");
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                }
            }
        
        return user;
    }

    @Override
    public User findUserByEmail(String email)throws SQLException {
        User user = null;
            PreparedStatement ps = cnx.prepareStatement("SELECT * FROM users WHERE email = ?");
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                }
            }
        
        return user;
    }

    @Override
    public boolean existsEmail(String email)throws SQLException {
        System.out.println("Checking if email exists: " + email);
        try (PreparedStatement ps = cnx.prepareStatement("SELECT count(*) FROM users WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean exists = rs.getInt(1) > 0;
                    System.out.println("Email exists: " + exists);
                    return exists;
                }
            }
        }
        return false;
    }

    @Override
    public boolean authenticateUser(String email, String password)throws SQLException {
            PreparedStatement ps = cnx.prepareStatement("SELECT * FROM users WHERE email = ?");
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password_hash");
                    // Vérifie si le mot de passe fourni correspond au mot de passe haché dans la base de données
                    return BCrypt.checkpw(password, hashedPassword);
                }
            }
        
        return false; 
    }

    
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
