package com.example.kaisi_lagi.AdminReportMaster;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class AdminEmailService {

    @Autowired
    JavaMailSender javaMailSender;


    public void sendWarningMail(String email){

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

        We would like to inform you that one of your comments has been
        <strong>reported by other users</strong> for violating our community guidelines.
        
        <div class="warning">
            Please review your comment and ensure that your future interactions remain
            respectful and appropriate.
        </div>

        Repeated violations may result in:
        <ul>
            <li>Temporary suspension of your account</li>
            <li>Permanent ban in severe cases</li>
        </ul>

        We encourage you to contribute positively and help maintain a healthy community.
    </div>

    <div class="footer">
        If you believe this report was a mistake, you may contact our support team.<br><br>
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
