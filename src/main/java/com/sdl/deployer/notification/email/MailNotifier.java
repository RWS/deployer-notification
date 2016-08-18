package com.sdl.deployer.notification.email;

import com.sdl.deployer.notification.Notifier;
import com.sdl.deployer.notification.NotifierConfig;
import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.transport.transportpackage.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static com.sdl.deployer.notification.ConfigurationLoader.getProperty;
import static com.sdl.deployer.notification.ConfigurationLoader.toProperties;
import static com.sdl.deployer.notification.VariableReplacer.replaceVars;

public class MailNotifier implements Notifier {
    private static final Logger LOG = LoggerFactory.getLogger(MailNotifier.class);

    private Map<String, String> defaultProperties;
    private Properties mailProperties;

    @Override
    public void configure(Configuration configuration) throws ConfigurationException {
        defaultProperties = toProperties(configuration);
        mailProperties = new Properties();
        mailProperties.putAll(defaultProperties);
    }

    @Override
    public void sendNotification(Optional<NotifierConfig> notifierConditionConfig, Item item) {
        Session session = Session.getDefaultInstance(mailProperties, null);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            String mailTo = getProperty(notifierConditionConfig, defaultProperties, "mail.to");
            String mailFrom = getProperty(notifierConditionConfig, defaultProperties, "mail.from");
            String body = replaceVars(getProperty(notifierConditionConfig, defaultProperties, "template"), item);
            String subject = replaceVars(getProperty(notifierConditionConfig, defaultProperties, "subjectTemplate"), item);
            String user = getProperty(notifierConditionConfig, defaultProperties, "mail.smtp.user");
            String password = getProperty(notifierConditionConfig, defaultProperties, "mail.smtp.password");
            String smtpHost = getProperty(notifierConditionConfig, defaultProperties, "mail.smtp.host");

            mimeMessage.setFrom(new InternetAddress(mailFrom));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(body, "text/html");

            Transport transport = session.getTransport("smtp");
            transport.connect(smtpHost, user, password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            LOG.error("Unable to send mail message for item: {} reason: {}", item, e.getMessage());
        }
    }

}
