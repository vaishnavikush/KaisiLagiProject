package com.example.kaisi_lagi.AdminReportMaster;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class AdminMailDelete {
    @Autowired
    JavaMailSender javaMailSender;

    public void DeleteMailBox(String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setFrom("kaisilagi1005@gmail.com");
            helper.setSubject("Delete Mail");
            String htmlBody = """
                    <!DOCTYPE html>
                      <html lang="en">
                      <head>
                          <meta charset="UTF-8">
                          <title>Comment Report Warning</title>
                          <style>
                              body {
                                  font-family: Arial, sans-serif;
                                  background-color: #f4f6f8;
                                  padding: 20px;
                                  margin: 0;
                              }
                              .container {
                                  background-color: #ffffff;
                                  padding: 25px;
                                  border-radius: 8px;
                                  max-width: 600px;
                                  margin: auto;
                                  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                              }
                              .header {
                                  color: #d32f2f;
                                  font-size: 20px;
                                  font-weight: bold;
                                  margin-bottom: 15px;
                              }
                              .content {
                                  color: #333333;
                                  font-size: 15px;
                                  line-height: 1.6;
                              }
                              .warning {
                                  background-color: #fff3cd;
                                  border-left: 5px solid #ff9800;
                                  padding: 12px;
                                  margin: 15px 0;
                                  border-radius: 4px;
                              }
                              .footer {
                                  margin-top: 25px;
                                  font-size: 13px;
                                  color: #777777;
                              }
                          </style>
                      </head>
                      <body>
                    
                      <div class="container">
                          <div class="header">
                              ⚠️ Warning: Comment Reported
                          </div>
                    
                          <div class="content">
                              Dear User,
                              <br><br>
                    
                              This is to inform you that one of your comments has been
                              <strong>reported by other users</strong> and reviewed by our moderation team.
                              As a result, the comment has been removed.
                    
                              <div class="warning">
                                  Please ensure that your future comments follow our community guidelines
                                  and remain respectful and appropriate.
                              </div>
                    
                              Continued violations may result in:
                              <ul>
                                  <li>Temporary suspension of your account</li>
                                  <li>Permanent account restriction in severe cases</li>
                              </ul>
                    
                              We appreciate your cooperation in helping maintain a safe and positive environment.
                          </div>
                    
                          <div class="footer">
                              If you believe this action was taken in error, you may contact the support team.<br><br>
                              — Team Review System
                          </div>
                      </div>
                    
                      </body>
                      </html>
                    
                    """;
            helper.setText(htmlBody, true);
            javaMailSender.send(mimeMessage);
        }
        catch(Exception e){
            System.out.println("====emial===");
        }


    }

}