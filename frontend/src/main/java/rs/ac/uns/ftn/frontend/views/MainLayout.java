package rs.ac.uns.ftn.frontend.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedRestClient;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.UserDto;
import rs.ac.uns.ftn.frontend.views.login.LoginView;
import rs.ac.uns.ftn.frontend.views.register.RegisterView;
import rs.ac.uns.ftn.frontend.views.services.ServicesView;
import rs.ac.uns.ftn.frontend.views.tasks.NewTaskView;
import rs.ac.uns.ftn.frontend.views.tasks.TasksView;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
public class MainLayout extends AppLayout {

  public static final String USER_IMAGE_PLACEHOLDER =
      "https://www.pngfind.com/pngs/m/610-6104451_image-placeholder-png-user-profile-placeholder-image-png.png";

  private final transient SharedRestClient sharedRestClient;

  private final AccessAnnotationChecker accessChecker;

  public static class MenuItemInfo {

    private final String text;
    private final String iconClass;
    private final Class<? extends Component> view;

    public MenuItemInfo(String text, String iconClass, Class<? extends Component> view) {
      this.text = text;
      this.iconClass = iconClass;
      this.view = view;
    }

    public String getText() {
      return text;
    }

    public String getIconClass() {
      return iconClass;
    }

    public Class<? extends Component> getView() {
      return view;
    }

  }

  private H1 viewTitle;

  public MainLayout(SharedRestClient sharedRestClient, AccessAnnotationChecker accessChecker) {
    this.sharedRestClient = sharedRestClient;
    this.accessChecker = accessChecker;
    setPrimarySection(Section.DRAWER);
    addToNavbar(true, createHeaderContent());
    addToDrawer(createDrawerContent());
  }

  private Component createHeaderContent() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setClassName("sidemenu-header");
    layout.getThemeList().set("dark", true);
    layout.setWidthFull();
    layout.setSpacing(false);
    layout.setAlignItems(FlexComponent.Alignment.CENTER);
    layout.add(new DrawerToggle());
    viewTitle = new H1();
    layout.add(viewTitle);

    Optional<UserDto> maybeUser = sharedRestClient.getLoggedInUser();
    if (maybeUser.isPresent()) {
      UserDto user = maybeUser.get();
      Avatar avatar = new Avatar(user.getEmail(), USER_IMAGE_PLACEHOLDER);
      avatar.addClassNames("ms-auto", "me-m");
      ContextMenu userMenu = new ContextMenu(avatar);
      userMenu.setOpenOnClick(true);
      userMenu.addItem("Logout", e -> {
        sharedRestClient.logout();
        UI.getCurrent().getPage().setLocation("/login");
      });
      layout.add(avatar);
    } else {
      Anchor loginLink = new Anchor("login", "Sign in");
      loginLink.addClassNames("ms-auto", "me-m");
      layout.add(loginLink);
    }

    return layout;
  }

  private Component createDrawerContent() {
    H2 appName = new H2("Frontend");
    appName.addClassNames("flex", "items-center", "h-xl", "m-0", "px-m", "text-m");

    com.vaadin.flow.component.html.Section section =
        new com.vaadin.flow.component.html.Section(appName, createNavigation(), createFooter());
    section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full");
    return section;
  }

  private Nav createNavigation() {
    Nav nav = new Nav();
    nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto");
    nav.getElement().setAttribute("aria-labelledby", "views");

    H3 views = new H3("Views");
    views.addClassNames("flex", "h-m", "items-center", "mx-m", "my-0", "text-s", "text-tertiary");
    views.setId("views");

    for (RouterLink link : createLinks()) {
      nav.add(link);
    }
    return nav;
  }

  private List<RouterLink> createLinks() {
    MenuItemInfo[] menuItems = new MenuItemInfo[] { //
        new MenuItemInfo("Register", "la la-user", RegisterView.class), //

        new MenuItemInfo("Login", "la la-user", LoginView.class), //

        new MenuItemInfo("New task", "la la-columns", NewTaskView.class), //

        new MenuItemInfo("Tasks", "la la-columns", TasksView.class), //

        new MenuItemInfo("Detection nodes", "la la-columns", ServicesView.class), //
    };
    List<RouterLink> links = new ArrayList<>();
    for (MenuItemInfo menuItemInfo : menuItems) {
      if (accessChecker.hasAccess(menuItemInfo.getView())) {
        links.add(createLink(menuItemInfo));
      }
    }
    return links;
  }

  private static RouterLink createLink(MenuItemInfo menuItemInfo) {
    RouterLink link = new RouterLink();
    link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
    link.setRoute(menuItemInfo.getView());

    Span icon = new Span();
    icon.addClassNames("me-s", "text-l");
    if (!menuItemInfo.getIconClass().isEmpty()) {
      icon.addClassNames(menuItemInfo.getIconClass());
    }

    Span text = new Span(menuItemInfo.getText());
    text.addClassNames("font-medium", "text-s");

    link.add(icon, text);
    return link;
  }

  private Footer createFooter() {
    Footer layout = new Footer();
    layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs");

    return layout;
  }

  @Override
  protected void afterNavigation() {
    super.afterNavigation();
    viewTitle.setText(getCurrentPageTitle());
  }

  private String getCurrentPageTitle() {
    PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
    return title == null ? "" : title.value();
  }
}
