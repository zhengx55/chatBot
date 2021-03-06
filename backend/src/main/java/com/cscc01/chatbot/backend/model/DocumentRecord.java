package com.cscc01.chatbot.backend.model;

import javax.persistence.*;

/**
 * DocumentRecord model
 */
@Entity
public class DocumentRecord {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    private String discoveryId;
    private String lastModified;
    private String lastModifiedUser;

    public DocumentRecord(){}

    public DocumentRecord(String name){
        this.name = name;
    }

    /**
     * @return current document record's name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name name of the document record
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return current document record's recent modification time
     */
    public String getLastModified() {
        return lastModified;
    }

    /**
     * @param lastModified recent modification time
     */
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * @return  the user who recently modified current document record's 
     */
    public String getLastModifiedUser() {
        return lastModifiedUser;
    }

    /**
     * @param lastModifiedUser the user who recently modified document record
     */
    public void setLastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }

    /**
     * @return current document record's id on IBM discovery
     */
    public String getDiscoveryId() {
        return discoveryId;
    }

    /**
     * @param discoveryId IBM discovery ID
     */
    public void setDiscoveryId(String discoveryId) {
        this.discoveryId = discoveryId;
    }
}
