package de.intension.keycloak.sendinblue;

/**
* Enum for all the parameters used to configure the provider.
*/
public enum ConfigParameter
{

    API_KEY("api-key");

    private String parameterName;

    private ConfigParameter(String parameterName)
    {
        this.parameterName = parameterName;
    }

    public String asString() {
        return this.parameterName;
    }
}