package de.intension.keycloak.sendinblue;

import org.jboss.logging.Logger;
import org.keycloak.Config.Scope;
import org.keycloak.email.EmailSenderProvider;
import org.keycloak.email.EmailSenderProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class SendinblueEmailProviderFactory
    implements EmailSenderProviderFactory
{

    private static final Logger     LOGGER                  = Logger.getLogger(SendinblueEmailProviderFactory.class);

    private static final String     PROVIDER_ID             = "sendinblue-email-provider";

    private SendinblueEmailProvider sendinblueEmailProvider = new SendinblueEmailProvider();

    @Override
    public EmailSenderProvider create(KeycloakSession session)
    {
        return sendinblueEmailProvider;
    }

    @Override
    public void init(Scope config)
    {
        String apiKey = config.get(ConfigParameter.API_KEY.asString());

        if (apiKey == null) {
            LOGGER.errorf("[%s] Unable to set API key", LogId.KCSIB0003);
        }
        else {
            sendinblueEmailProvider.setApiKey(apiKey);
        }
    }

    @Override
    public void postInit(KeycloakSessionFactory factory)
    {
        // nothing to do
    }

    @Override
    public void close()
    {
        // nothing to do
    }

    @Override
    public String getId()
    {
        return PROVIDER_ID;
    }
}