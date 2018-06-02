package sg.activewealth.roboadvisor.infra.helper.notification;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import sg.activewealth.roboadvisor.infra.helper.PropertiesHelper;
import sg.activewealth.roboadvisor.infra.utils.ValidationUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Component
public class NotificationMacroResolver {

  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private VelocityEngine velocityEngine;

  @Autowired
  private PropertiesHelper propertiesHelper;

  public NotificationMetaData from(final NotificationType notificationType, final Map<String, Object> dataMap, final String userEmailAddress) throws IOException {

    final String subjectTemplate = IOUtils.toString(resourceLoader.getResource("classpath:email_tpl/" + notificationType.getSubjectTemplate()).getInputStream());
    String bodyTemplate = "";
    if(!ValidationUtils.getInstance().isEmptyString(notificationType.getBodyTemplate())) {
    	bodyTemplate = IOUtils.toString(resourceLoader.getResource("classpath:email_tpl/" + notificationType.getBodyTemplate()).getInputStream());
    }
    final StringWriter subjectText = new StringWriter();
    final StringWriter bodyText = new StringWriter();

    velocityEngine.evaluate(new VelocityContext(dataMap), subjectText, "", subjectTemplate);
    velocityEngine.evaluate(new VelocityContext(dataMap), bodyText, "", bodyTemplate);

    return new NotificationMetaData(subjectText.toString(), bodyText.toString(), notificationType.isSendToAdmin() ? getAdminUserEmails(): new String[] {userEmailAddress});
  }

  private String[] getAdminUserEmails() {
    return propertiesHelper.adminUserEmailId.split(",");
  }


  class NotificationMetaData {
    private final String subject;
    private final String body;
    private final String[] recipients;

    private NotificationMetaData(final String subject, final String body, final String[] recipients) {
      this.subject = subject;
      this.body = body;
      this.recipients = recipients;
    }

    public String getSubject() {
      return subject;
    }

    public String getBody() {
      return body;
    }

    public String[] getRecipients() {
      return recipients;
    }
  }
}
