//package com.example.zalupa2.mail;//package com.example.sukagram.mail;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class MailConfig {
//
//    @Value("${spring.mail.username}")
//    private String username;
//
//    @Value("${spring.mail.password}")
//    private String password;
//
//    @Value("${spring.mail.properties.mail.smtp.auth}")
//    private String mailServerAuth;
//
//    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
//    private String mailServerStartTls;
//    @Value("${spring.mail.port}")
//    private Integer mailServerPort;
//    @Value("${spring.mail.host}")
//    private String mailServerHost;
//
//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//
//        mailSender.setHost(mailServerHost);
//        mailSender.setPort(mailServerPort);
//
//        mailSender.setUsername(username);
//        mailSender.setPassword(password);
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", mailServerAuth);
//        props.put("mail.smtp.starttls.enable", mailServerStartTls);
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
//
//}
//
