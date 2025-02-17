package access.manage;

public enum EntityType {

    SAML20_SP, OIDC10_RP, SAML20_IDP;

    public String collectionName() {
        return name().toLowerCase();
    }
}
