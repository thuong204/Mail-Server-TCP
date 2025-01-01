/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Models.Account;
import Models.Mail;
import Models.DataPacket;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import Helpers.connect;

public class MailDAO {

    AccountDAO accountDAO = new AccountDAO();

    
    //luu mail
    public boolean saveEmail(DataPacket dp) {
        Account receiverAcc = accountDAO.getByUsername(dp.getTo());
        if (receiverAcc != null) {
            Mail mail = new Mail(dp.getFrom().getId(), receiverAcc.getId(), dp.getSubject(), dp.getBody(), new Date(), dp.getCc(), dp.getAttachments());
            String query = "INSERT INTO Mail (SenderId, ReceiverId, Subject, Body, SentTime, State, IsDeleted, Cc, Attachments) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (
                Connection conn =  connect.connect();
                PreparedStatement pstmt = conn.prepareStatement(query)
            ) {
                pstmt.setInt(1, mail.getSenderId());
                pstmt.setInt(2, mail.getReceiverId());
                pstmt.setString(3, mail.getSubject());
                pstmt.setString(4, mail.getBody());
                pstmt.setTimestamp(5, new Timestamp(mail.getSentTime().getTime()));
                pstmt.setString(6, mail.getState());
                pstmt.setBoolean(7, mail.isIsDeleted());
                pstmt.setString(8, mail.getCc());
                pstmt.setString(9, mail.getAttachments());
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //lay ra hop thu den
    public ArrayList<Mail> getInbox(Account account) {
        ArrayList<Mail> inboxMail = new ArrayList<>();
        String query = "SELECT * FROM Mail WHERE (ReceiverId = ? OR Cc LIKE ?) AND IsDeleted = 0 ORDER BY SentTime DESC";
        try (
            Connection conn = connect.connect();
            PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setInt(1, account.getId());
            pstmt.setString(2, "%" + account.getUsername() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Mail mail = new Mail(
                        rs.getInt("Id"),
                        rs.getInt("SenderId"),
                        rs.getInt("ReceiverId"),
                        rs.getString("Subject"),
                        rs.getString("Body"),
                        new Date(rs.getTimestamp("SentTime").getTime()),
                        rs.getString("State"),
                        rs.getBoolean("IsDeleted"),
                        rs.getString("Cc"),
                        rs.getString("Attachments")
                    );
                    inboxMail.add(mail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inboxMail;
    }
    
    //lay ra hop thu di
    public ArrayList<Mail> getSent(Account account) {
        ArrayList<Mail> sentMail = new ArrayList<>();
        String query = "SELECT * FROM Mail WHERE SenderId = ? AND IsDeleted = 0 ORDER BY SentTime DESC";
        try (
            Connection conn =  connect.connect();
            PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setInt(1, account.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Mail mail = new Mail(
                        rs.getInt("Id"),
                        rs.getInt("SenderId"),
                        rs.getInt("ReceiverId"),
                        rs.getString("Subject"),
                        rs.getString("Body"),
                        new Date(rs.getTimestamp("SentTime").getTime()),
                        rs.getString("State"),
                        rs.getBoolean("IsDeleted"),
                        rs.getString("Cc"),
                        rs.getString("Attachments")
                    );
                    sentMail.add(mail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sentMail;
    }
}
