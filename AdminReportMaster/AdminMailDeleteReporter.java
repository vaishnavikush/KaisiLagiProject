package com.example.kaisi_lagi.AdminReportMaster;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class AdminMailDeleteReporter {
    @Autowired
    JavaMailSender javaMailSender;

    public void DeleteMailBoxReporter(String email) {
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
    <title>Report Update – Comment Removed</title>
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
        .success {
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
        ✅ Report Update: Action Taken
    </div>

    <div class="content">
        Dear User,
        <br><br>

        Thank you for taking the time to report a comment that you felt violated our community guidelines.
        After careful review by our moderation team, the reported comment has been
        <strong>successfully removed</strong>.

        <div class="success">
            Your report helped us maintain a respectful and safe environment for everyone.
            We truly appreciate your contribution.
        </div>

        If you notice any other content that does not align with our community standards,
        please do not hesitate to report it.

        <br><br>
        Together, we can keep our community positive and welcoming.
    </div>

    <div class="footer">
        This is an automated message. No further action is required.<br><br>
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
