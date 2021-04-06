package de.intension.keycloak.sendinblue.internal;

import java.util.Arrays;
import java.util.Map;

import org.jboss.logging.Logger;
import org.keycloak.email.EmailException;
import org.keycloak.models.UserModel;

import de.intension.keycloak.sendinblue.LogId;
import lombok.NoArgsConstructor;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailTo;

@NoArgsConstructor
public class SendinblueSender
{

    private static final Logger LOGGER = Logger.getLogger(SendinblueSender.class);

    private ApiClient           apiClient;

    public static SendinblueSender create()
    {
        return new SendinblueSender();
    }

    /**
     * Sends an email using sendinblue API.
     *
     * @param user recipient of the email
     * @param templateId ID of the template to use for the mail
     * @param params paramter to be replaced in the mail template
     * @throws EmailException with message from Sendinblue's {@link ApiException} (for example:
     *             'Unauthorized' when API key is invalid or
     *             'Not Found' when template couldn't be found.
     */
    public void postToSendinblue(UserModel user, long templateId, Map<String, String> params)
        throws EmailException
    {
        TransactionalEmailsApi apiInstance = new TransactionalEmailsApi(apiClient);
        SendSmtpEmailTo recipient = new SendSmtpEmailTo()
            .name(user.getFirstName() + " " + user.getLastName())
            .email(user.getEmail());
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail().templateId(templateId).params(params)
            .to(Arrays.asList(recipient));

        try {
            apiInstance.getSmtpTemplate(templateId);
            apiInstance.sendTransacEmail(sendSmtpEmail);
        } catch (ApiException e) {
            LOGGER.errorf("[%s] Unable to send transactional mail for user '%s'", LogId.KCSIB0002, user.getEmail());
            throw new EmailException(e.getMessage(), e);
        }
    }

    /**
     * Sets the API key and update the default client.
     *
     * @param apiKey api-key to use sendinblue API
     * @return ths instance
     */
    public SendinblueSender setApiKey(String apiKey)
    {
        LOGGER.infof("setApiKey(apiKey='*******') has been invoked");
        apiClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKeyAuth = (ApiKeyAuth)apiClient.getAuthentication("api-key");
        apiKeyAuth.setApiKey(apiKey);
        return this;
    }
}
