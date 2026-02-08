package com.example.kaisi_lagi.AdminReportMaster;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class RejectEmailReporter {
    @Autowired
    JavaMailSender javaMailSender;
    public void RejectReporter (String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setFrom("kaisilagi1005@gmail.com");
            helper.setSubject("Reject");

            String htmlBody = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <title>Report Status Update</title>
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
                                color: #1565c0;
                                font-size: 20px;
                                font-weight: bold;
                                margin-bottom: 15px;
                            }
                            .content {
                                color: #333333;
                                font-size: 15px;
                                line-height: 1.6;
                            }
                            .info {
                                background-color: #e3f2fd;
                                border-left: 5px solid #2196f3;
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
                            ℹ️ Report Status Update
                        </div>
                    
                        <div class="content">
                            Dear User,
                            <br><br>
                    
                            Thank you for taking the time to report a comment on our platform.
                            Our moderation team has carefully reviewed the reported content.
                    
                            <div class="info">
                                After evaluation, the report has been <strong>rejected</strong> as the comment
                                does not violate our community guidelines.
                            </div>
                    
                            No further action has been taken at this time. We encourage you to continue
                            reporting content that you believe may breach our guidelines, as it helps
                            us maintain a safe and respectful environment.
                        </div>
                    
                        <div class="footer">
                            If you have additional concerns or believe there was an oversight, you may contact
                            our support team.<br><br>
                            — Team Review System
                        </div>
                    </div>
                    
                    </body>
                    </html>
                    """;
            helper.setText(htmlBody, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
