package com.cscc01.chatbot.backend.model;

import javax.persistence.*;

/**
 * User Model
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String username;

    private String encryptedPassword;

    private String role;

    /**
     * @return current user's id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return current user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return string representation of current user
     */
    @Override
    public String toString() { 
        return String.format("This is " + username); 
    }

    /**
     * @return current user's role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role role to distinguish admin and normal user
     */
    public void setRole(String role) {
        this.role = role;
    }

    public UserDto toUserDto() {
        UserDto dto = new UserDto();
        dto.setRole(this.role);
        dto.setUsername(this.username);
        return dto;
    }

    /**
     * @return current user's encrypted password
     */
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    /**
     * @param encryptedPassword encrypted password for the user
     */
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
