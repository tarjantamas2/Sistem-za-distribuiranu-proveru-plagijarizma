package rs.ac.uns.ftn.frontend.views.register;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedRestClient;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.UserDto;
import rs.ac.uns.ftn.frontend.config.OnlyLoggedOutAllowed;
import rs.ac.uns.ftn.frontend.views.MainLayout;

@PageTitle("Register")
@Route(value = "register", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@OnlyLoggedOutAllowed
@AnonymousAllowed
@Uses(Icon.class)
public class RegisterView extends Div {

  private final EmailField email = new EmailField("Email address");
  private final PasswordField password = new PasswordField("Password");
  private final TextField firstName = new TextField("First name");
  private final TextField lastName = new TextField("Last name");

  private final Button cancel = new Button("Cancel");
  private final Button save = new Button("Save");

  private final Binder<UserDto> binder = new Binder(UserDto.class);

  public RegisterView(SharedRestClient sharedRestClient) {
    addClassName("register-view");

    add(createFormLayout());

    binder.bindInstanceFields(this);
    clearForm();

    cancel.addClickListener(e -> clearForm());
    save.addClickListener(e -> {
      sharedRestClient.register(binder.getBean());
      Notification.show("Registration successful.");
      clearForm();
    });
  }

  private void clearForm() {
    binder.setBean(new UserDto());
  }

  private Component createTitle() {
    return new H3("Personal information");
  }

  private Component createFormLayout() {
    email.setErrorMessage("Please enter a valid email address");

    firstName.setWidthFull();
    lastName.setWidthFull();
    email.setWidthFull();
    password.setWidthFull();

    VerticalLayout layout = new VerticalLayout(createTitle(), vSpacer(10), firstName, vSpacer(10), lastName,
        vSpacer(10), email, vSpacer(10), password, vSpacer(10), createButtonLayout());
    layout.setMargin(true);
    layout.setSpacing(false);
    layout.setWidth(600, Unit.PIXELS);
    return layout;
  }

  private Component vSpacer(int size) {
    Div spacer = new Div();
    spacer.setHeight(size, Unit.PIXELS);
    return spacer;
  }

  private Component createButtonLayout() {
    HorizontalLayout buttonLayout = new HorizontalLayout();
    buttonLayout.addClassName("button-layout");
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    buttonLayout.add(save);
    buttonLayout.add(cancel);
    return buttonLayout;
  }
}
