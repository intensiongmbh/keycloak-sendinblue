package de.intension.keycloak.sendinblue;

import java.util.Map;

import org.jboss.logging.Logger;
import org.keycloak.email.EmailException;
import org.keycloak.email.EmailSenderProvider;
import org.keycloak.models.UserModel;

import de.intension.keycloak.sendinblue.internal.SendinblueSender;

public class SendinblueEmailProvider
    implements EmailSenderProvider
{

    private static final Logger LOGGER = Logger.getLogger(SendinblueEmailProvider.class);
    private SendinblueSender    sendinblueSender;

    public SendinblueEmailProvider()
    {
        sendinblueSender = SendinblueSender.create();
        LOGGER.infof("Instantiated %s", getClass().getSimpleName());
    }

    @Override
    public void close()
    {
        // nothing to close        
    }

    @Override
    public void send(Map<String, String> config, UserModel user, String subject, String textBody, String htmlBody)
        throws EmailException
    {
        String templateIdConfigValue = config.get("templateId");
        if (templateIdConfigValue == null) {
            LOGGER.errorf("[%s] templateID not provided. Unable to send email.", LogId.KCSIB0001);
            throw new EmailException("Config is missing template ID.");
        }
        long templateId = Long.parseLong(templateIdConfigValue);

        sendinblueSender.postToSendinblue(user, templateId, config);
    }

    public SendinblueEmailProvider setApiKey(String apiKey) {
        sendinblueSender.setApiKey(apiKey);
        return this;
    }
}
