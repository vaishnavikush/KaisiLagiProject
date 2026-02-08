package com.example.kaisi_lagi.AdminReportMaster;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class AdminEmailServiceReporter {

    @Autowired
    JavaMailSender javaMailSender;


    public void sendWarningMailReporter(String email){

        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message , true);

            helper.setTo(email);
            helper.setFrom("kaisilagi1005@gmail.com");
            helper.setSubject("Warning mail.");


            String htmlBody = """
<!DOCTYPE html>
<html>
<head>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
            padding: 20px;
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
        ✅ Report Reviewed – Action Taken
    </div>

    <div class="content">
        Dear User,
        <br><br>

        Thank you for reporting a comment that you believed violated our community guidelines.
        After reviewing the report, our moderation team has taken appropriate action and
        <strong>issued a warning to the user involved</strong>.

        <div class="success">
            Your report helped us maintain a respectful and positive environment.
            We appreciate your effort in keeping the community safe.
        </div>

        If you encounter similar content in the future, please continue to report it.
        Your participation plays an important role in improving our platform.
    </div>

    <div class="footer">
        This is an automated message. No further action is required.<br><br>
        — Team Review System
    </div>
</div>

</body>
</html>
""";

            helper.setText(htmlBody , true);
            javaMailSender.send(message);

        }catch (Exception e){
            System.out.println("===================email=============");
        }

    }
}
