package com.example.kaisi_lagi.AdminReportMaster;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class RejectEmailOwner {
    @Autowired
    JavaMailSender javaMailSender;
    public void RejectOwner(String email){
        try {
            MimeMessage message=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setTo(email);
            helper.setFrom("kaisilagi1005@gmail.com");
            helper.setSubject("Reject Email");

            String htmlBody = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Report Review Update</title>
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
            color: #2e7d32;
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
            background-color: #e8f5e9;
            border-left: 5px solid #4caf50;
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
        ✅ Report Review Update
    </div>

    <div class="content">
        Dear User,
        <br><br>

        We would like to inform you that a report was submitted regarding one of your comments.
        After careful review by our moderation team, the report has been
        <strong>rejected</strong> as it does not violate our community guidelines.

        <div class="info">
            No action has been taken against your comment, and it remains visible on the platform.
        </div>

        We appreciate your continued contribution and encourage you to keep sharing
        respectful and constructive feedback within our community.
    </div>

    <div class="footer">
        If you have any questions or concerns, feel free to contact our support team.<br><br>
        — Team Review System
    </div>
</div>

</body>
</html>
""";

            helper.setText(htmlBody,true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
