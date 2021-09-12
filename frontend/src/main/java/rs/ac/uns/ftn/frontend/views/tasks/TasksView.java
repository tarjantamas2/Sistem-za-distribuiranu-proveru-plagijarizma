package rs.ac.uns.ftn.frontend.views.tasks;

import java.util.stream.Collectors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedRestClient;
import rs.ac.uns.ftn.frontend.config.LoggedInAllowed;
import rs.ac.uns.ftn.frontend.views.MainLayout;
import rs.ac.uns.ftn.frontend.views.common.AllItemsGrid;

@PageTitle("Tasks")
@Route(value = "tasks", layout = MainLayout.class)
@LoggedInAllowed
@Uses(Icon.class)
public class TasksView extends Div {

  public TasksView(SharedRestClient sharedRestClient) {
    addClassNames("detectiontasks-view", "flex", "flex-col", "h-full");

    VerticalLayout wrapperLayout = new VerticalLayout();
    wrapperLayout.setSizeFull();
    AllItemsGrid<SharedTaskViewModel> grid = new AllItemsGrid<>(SharedTaskViewModel.class);
    wrapperLayout.add(grid);
    add(wrapperLayout);

    grid.setViewModelItems(sharedRestClient.findAllSharedTasks()
      .stream()
      .map(SharedTaskViewModel::fromSharedTask)
      .collect(Collectors.toList()));

    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() == null) {
        return;
      }
      UI.getCurrent().navigate(String.format(TaskDetailView.ROUTE_TEMPLATE, event.getValue().getId()));
    });
  }
}
