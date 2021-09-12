package rs.ac.uns.ftn.frontend.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedRestClient;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.UserDto;
import rs.ac.uns.ftn.frontend.config.OnlyLoggedOutAllowed;

@PageTitle("Login")
@AnonymousAllowed
@OnlyLoggedOutAllowed
@Route(value = "login")
public class LoginView extends LoginOverlay {

  public LoginView(SharedRestClient sharedRestClient) {
    setAction("login");

    LoginI18n i18n = LoginI18n.createDefault();
    i18n.setHeader(new LoginI18n.Header());
    i18n.getHeader().setTitle("Distributed Plagiarism Detector");
    i18n.setAdditionalInformation(null);
    setI18n(i18n);

    setForgotPasswordButtonVisible(false);
    setOpened(true);

    addLoginListener(loginEvent -> {
      boolean success = sharedRestClient.login(new UserDto(loginEvent.getUsername(), loginEvent.getPassword()));
      if (success) {
        Notification.show("Login successful.");
        UI.getCurrent().getPage().setLocation("/tasks");
      }
    });
  }
}
