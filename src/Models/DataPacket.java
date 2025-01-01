/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Models.Account;
import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class DataPacket implements Serializable {

    private Account fromAccount;

    private String to;
    private String cc;
    private String subject;
    private String body;
    private String attachments;
    private byte[] file;

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] File) {
        this.file = File;
    }

    public DataPacket(Account from, String to, String cc, String subject, String body, String attachments) {
        this.fromAccount = from;
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.body = body;
        this.attachments = attachments;
    }

    public DataPacket(Account from, String to, String subject, String body, String attachments) {
        this.fromAccount = from;
        this.to = to;
        this.cc = "";
        this.subject = subject;
        this.body = body;
        this.attachments = attachments;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Account getFrom() {
        return fromAccount;
    }

    public void setFrom(Account from) {
        this.fromAccount = from;
    }

    public String getTo() {
        return to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
