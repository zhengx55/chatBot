package com.cscc01.chatbot.backend.model;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

/**
 * Feedback model
 */
@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "message", nullable = false)
    private String message;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    public Feedback(){}

    public Feedback(String message){
        this.message = message;
    }

    /**
     * @return current feedback's message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message of the feedback
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return current feedback's creation time
     */
    public LocalDateTime getCreateDateTime(){
        return createDateTime;
    }

    /**
     * @param createDateTime localtime of the feedback created
     */
    public void setCreateDateTime(LocalDateTime createDateTime){
        this.createDateTime = createDateTime;
    }

    /**
     * @return string representation of current feedback
     */
    @Override
    public String toString(){
        return this.message;
    }
}
