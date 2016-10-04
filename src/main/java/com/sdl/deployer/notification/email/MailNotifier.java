/**
 * Copyright (c) 2016 All Rights Reserved by the SDL Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
