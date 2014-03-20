package org.jenkinsci.plugins.knappsack;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.knappsack.models.Application;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public class KnappsackBuildStepDescriptor extends BuildStepDescriptor<Publisher> {
    public KnappsackBuildStepDescriptor() {
        super(KnappsackRecorder.class);
        load();
    }

    public boolean isApplicable(Class<? extends AbstractProject> aClass) {
        return true;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this, json);
        save();
        return true;
    }

    public String getDisplayName() {
        return "Push artifact to Knappsack";
    }

    // public ListBoxModel doFillApplicationItems(@QueryParameter String userName, @QueryParameter Secret userPassword, @QueryParameter String knappsackURL) {
    //     ListBoxModel m = new ListBoxModel();

    //     if (!userName.isEmpty() && !userPassword.getEncryptedValue().isEmpty() && !knappsackURL.isEmpty()) {
    //         KnappsackAPI knappsackAPI = new KnappsackAPI(knappsackURL, userName, userPassword);
    //         Application[] applications = knappsackAPI.getApplications();
    //         for (Application application : applications) {
    //             m.add(application.getName(), application.getId().toString());
    //         }
    //     }
    //     return m;
    // }

    public ListBoxModel doFillApplicationStateItems() {
        ListBoxModel m = new ListBoxModel();
        m.add("Publish to Group", "GROUP_PUBLISH");
        m.add("Publish to Organization", "ORGANIZATION_PUBLISH");
        m.add("Request Publish to Group", "ORG_PUBLISH_REQUEST");
        m.add("Disabled", "DISABLED");

        return m;
    }

    public FormValidation doCheckKnappsackURL(@QueryParameter String userName, @QueryParameter Secret userPassword, @QueryParameter String knappsackURL) {
        if (!userName.isEmpty() && !userPassword.getEncryptedValue().isEmpty() && !knappsackURL.isEmpty()) {
            KnappsackAPI knappsackAPI = new KnappsackAPI(knappsackURL, userName, userPassword);
            try {
                knappsackAPI.getTokenResponse();
            } catch (RuntimeException e) {
                return FormValidation.error(e.getMessage());
            }

        }
        return FormValidation.ok();
    }
}

