/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Dao.AccountDAO;
import Models.Account;
import Models.Mail;
import Models.DataPacket;
import Models.Response;
import Server.Server;
import java.awt.Cursor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class MailClient extends javax.swing.JFrame {

    private final int serverPort = 7777;
    private final String serverIP = "127.0.0.1";
    private final Account account;
    private Socket socket;
    private ArrayList<Mail> inboxMail;
    private ArrayList<Mail> sentMail;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String emailToSelected;

    /**
     * Creates new form MailClient
     *
     * @param account
     */
    public MailClient(Account account) {
        this.account = account;
        initComponents();
        txtAttachments.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setTitle(account.getUsername());
        initSocket();
        getInboxMail();
    }
    

    //nhan yeu cau tu sever hien thi ra danh sach thu den
    public void getInbox(Response r) {
        inboxMail = (ArrayList<Mail>) r.getT();
        DefaultListModel model = new DefaultListModel();
        inboxMail.forEach((mail) -> {
            model.addElement(mail.getSubject());
        });
        listInbox.setModel(model);
        if (model.size() > 0) {
            setMailIndex(0);
            listInbox.setSelectedIndex(0);
        }
    }

  //nhan yeu cau tu sever hien thi ra danh sach thu da gui
    public void getSent(Response r) {
        sentMail = (ArrayList<Mail>) r.getT();
        DefaultListModel model = new DefaultListModel();
        sentMail.forEach((mail) -> {
            model.addElement(mail.getSubject());
        });
        listOutbox.setModel(model);
        if (model.size() > 0) {
            setMailIndex(0);
            listOutbox.setSelectedIndex(0);
        }
    }

    //hop thu den moi
    public void getNewEmail(Response r) {
        System.out.println("get new email");
        ArrayList<Mail> mails = (ArrayList<Mail>) r.getT();
        Mail mail = mails.get(0);
        inboxMail.add(0, mail);
        DefaultListModel model = new DefaultListModel();
        inboxMail.forEach((m) -> {
            model.addElement(m.getSubject());
        });
        listInbox.setModel(model);

    }

    //hop thu di moi
    public void getNewOutbox(Response r) {
        System.out.println("get new outbox");
        ArrayList<Mail> mails = (ArrayList<Mail>) r.getT();
        Mail mail = mails.get(0);
        sentMail.add(0, mail);
        DefaultListModel model = new DefaultListModel();
        sentMail.forEach((m) -> {
            model.addElement(m.getSubject());
        });
        listOutbox.setModel(model);

    }

    /**
     * Tải về file đính kèm
     *
     * @param r
     */
    
    // tai file xuong
    public void downloadFile(Response r) {
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (f.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f.getSelectedFile().getAbsolutePath() + "/" + txtAttachments.getText());
                fos.write((byte[]) r.getT());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    private void getInboxMail() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    ois = new ObjectInputStream(socket.getInputStream());

                    while (true) {
                        System.out.println("waiting response from server");
                        try {
                            Response r = (Response) ois.readObject();
                            switch (r.getType()) {
                                case "##GETINBOX##": {
                                    getInbox(r);
                                    break;
                                }
                                case "##GETSENT##": {
                                    getSent(r);
                                    break;
                                }
                                case "##NEWEMAIL##": {
                                    getNewEmail(r);
                                    break;
                                }
                                case "##NEWOUTBOX##": {
                                    getNewOutbox(r);
                                    break;
                                }
                                case "##DOWNLOADFILE##": {
                                    downloadFile(r);
                                    break;
                                }
                                case "##ERRORNOTEXIST##": {
                                    JOptionPane.showMessageDialog(null, "Email người nhận không tồn tại");
                                }
                            }
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(MailClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MailClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        new Thread(runnable).start();
    }

    private void initSocket() {
        try {
            socket = new Socket(serverIP, serverPort);
            // Chỉ khởi tạo ObjectOutputStream một lần tại đây
            oos = new ObjectOutputStream(socket.getOutputStream());

            DataPacket data = new DataPacket(this.account, "", "##PING##", "", "");
            oos.writeObject(data);

        } catch (IOException ex) {
            Logger.getLogger(ComposeMail.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Can't connect to Server.\nMaybe Server is down");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        listInbox = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        listOutbox = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        txtTime = new javax.swing.JLabel();
        txtFrom = new javax.swing.JLabel();
        txtSubject = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtBody = new javax.swing.JTextArea();
        txtCc = new javax.swing.JLabel();
        txtAttachments = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 500));

        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 500));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jButton2.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        jButton2.setText("Thư mới");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, java.awt.BorderLayout.PAGE_START);

        listInbox.setPreferredSize(null);
        listInbox.setRequestFocusEnabled(false);
        listInbox.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listInboxValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listInbox);

        jTabbedPane1.addTab("Hộp thư đến", jScrollPane1);

        listOutbox.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listOutboxValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(listOutbox);

        jTabbedPane1.addTab("Hộp thư đi", jScrollPane3);

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        txtTime.setText(" ");

        txtFrom.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        txtFrom.setText(" ");

        txtSubject.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        txtSubject.setText(" ");

        txtBody.setEditable(false);
        txtBody.setColumns(20);
        txtBody.setLineWrap(true);
        txtBody.setRows(5);
        txtBody.setBorder(null);
        jScrollPane2.setViewportView(txtBody);

        txtCc.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        txtCc.setText(" ");

        txtAttachments.setFont(new java.awt.Font("sansserif", 2, 12)); // NOI18N
        txtAttachments.setForeground(new java.awt.Color(0, 153, 255));
        txtAttachments.setText(" ");
        txtAttachments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAttachmentsMouseClicked(evt);
            }
        });

        jLabel1.setText("Tệp đính kèm");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)
                    .addComponent(txtTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSubject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtAttachments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtSubject)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCc)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAttachments)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int y = this.getY() + this.getHeight() - 410;
        int x = this.getX() + this.getWidth() - 415;
        ComposeMail cm = new ComposeMail(account, socket, x, y);
        cm.setVisible(true);
    }

    private void listInboxValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listInboxValueChanged
        if (listInbox.getSelectedIndex() != -1) {
            this.setMailIndex(listInbox.getSelectedIndex());
        }
    }

    private void listOutboxValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listOutboxValueChanged
        if (listOutbox.getSelectedIndex() != -1) {
            this.setOutMailIndex(listOutbox.getSelectedIndex());
        }
    }

    
    //tai file xuong
    private void txtAttachmentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAttachmentsMouseClicked

        try {
            DataPacket data = new DataPacket(this.account, this.emailToSelected, "##DOWNLOADFILE##", "", txtAttachments.getText());
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(data);
        } catch (IOException ex) {
            Logger.getLogger(MailClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void setMailIndex(int i) {
        Mail m = inboxMail.get(i);
        txtSubject.setText(m.getSubject());
        String partern = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(partern);
        txtTime.setText(sdf.format(m.getSentTime()));
        Account sender = new AccountDAO().getById(m.getSenderId());
        txtFrom.setText("From: " + sender.getUsername());
        txtBody.setText(m.getBody());
        txtCc.setText("CC: " + m.getCc());
        txtAttachments.setText(m.getAttachments());
        emailToSelected = new AccountDAO().getById(m.getReceiverId()).getUsername();
    }

    public void setOutMailIndex(int i) {
        Mail m = sentMail.get(i);
        txtSubject.setText(m.getSubject());
        String partern = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(partern);
        txtTime.setText(sdf.format(m.getSentTime()));
        Account sender = new AccountDAO().getById(m.getSenderId());
        txtFrom.setText("From: " + sender.getUsername());
        txtBody.setText(m.getBody());
        txtCc.setText("CC: " + m.getCc());
        txtAttachments.setText(m.getAttachments());
        emailToSelected = new AccountDAO().getById(m.getReceiverId()).getUsername();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MailClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MailClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MailClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MailClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList<String> listInbox;
    private javax.swing.JList<String> listOutbox;
    private javax.swing.JLabel txtAttachments;
    private javax.swing.JTextArea txtBody;
    private javax.swing.JLabel txtCc;
    private javax.swing.JLabel txtFrom;
    private javax.swing.JLabel txtSubject;
    private javax.swing.JLabel txtTime;
    // End of variables declaration//GEN-END:variables
}
