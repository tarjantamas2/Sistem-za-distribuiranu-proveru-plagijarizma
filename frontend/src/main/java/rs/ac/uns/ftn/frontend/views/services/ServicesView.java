package rs.ac.uns.ftn.frontend.views.services;

import java.util.stream.Collectors;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.ServiceMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedRestClient;
import rs.ac.uns.ftn.frontend.config.LoggedInAllowed;
import rs.ac.uns.ftn.frontend.views.MainLayout;
import rs.ac.uns.ftn.frontend.views.common.AllItemsGrid;

@PageTitle("Detection nodes")
@Route(value = "services", layout = MainLayout.class)
@LoggedInAllowed
@Uses(Icon.class)
public class ServicesView extends Div {

  public ServicesView(SharedRestClient sharedRestClient) {
    addClassNames("detectiontasks-view", "flex", "flex-col", "h-full");

    VerticalLayout wrapperLayout = new VerticalLayout();
    wrapperLayout.setSizeFull();
    AllItemsGrid<ServiceMetadata> grid = new AllItemsGrid<>(ServiceMetadata.class);
    wrapperLayout.add(grid);
    add(wrapperLayout);

    grid.setViewModelItems(
        sharedRestClient.findAllServices().stream().map(ServiceMetadata.class::cast).collect(Collectors.toList()));
  }
}
