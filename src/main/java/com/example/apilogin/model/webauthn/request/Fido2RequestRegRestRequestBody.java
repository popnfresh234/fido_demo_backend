package com.example.apilogin.model.webauthn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Fido2RequestRegRestRequestBody {

    private String attestation;
    private AuthenticatorSelectionCriteria authenticatorSelection;
    private String displayName;
    private Object extensions;
    private String friendlyName;
    private String origin;
    private String rpId;
    private String rpName;
    private String username;

    public String getAttestation() {
        return attestation;
    }

    public void setAttestation(String attestation) {
        this.attestation = attestation;
    }

    public AuthenticatorSelectionCriteria getAuthenticatorSelection() {
        return authenticatorSelection;
    }

    public void setAuthenticatorSelection(AuthenticatorSelectionCriteria authenticatorSelection) {
        this.authenticatorSelection = authenticatorSelection;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Object getExtensions() {
        return extensions;
    }

    public void setExtensions(Object extensions) {
        this.extensions = extensions;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRpId() {
        return rpId;
    }

    public void setRpId(String rpId) {
        this.rpId = rpId;
    }

    public String getRpName() {
        return rpName;
    }

    public void setRpName(String rpName) {
        this.rpName = rpName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return attestation + "  " + displayName + " " + extensions + " " + friendlyName +" " + origin + " " + rpId + " " + rpName +" " + username;
    }
}
