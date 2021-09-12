package rs.ac.uns.ftn.frontend.views.tasks;

import java.util.Optional;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedRestClient;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedTaskDto;
import rs.ac.uns.ftn.frontend.config.LoggedInAllowed;
import rs.ac.uns.ftn.frontend.views.MainLayout;

@PageTitle("Tasks")
@Route(value = "tasks/:taskId", layout = MainLayout.class)
@LoggedInAllowed
@Uses(Icon.class)
public class TaskDetailView extends VerticalLayout implements BeforeEnterObserver {

  public static final String TASK_ID = "taskId";
  public static final String ROUTE_TEMPLATE = "tasks/%s";

  private final SharedRestClient sharedRestClient;

  public TaskDetailView(SharedRestClient sharedRestClient) {
    this.sharedRestClient = sharedRestClient;
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Optional<String> optionalTaskId = event.getRouteParameters().get(TASK_ID);
    if (!optionalTaskId.isPresent()) {
      event.forwardTo(TasksView.class);
      return;
    }

    String taskId = optionalTaskId.get();
    Optional<SharedTaskDto> optionalTask = sharedRestClient.findTaskById(taskId);
    if (!optionalTask.isPresent()) {
      Notification.show(String.format("The requested task was not found, ID = %s", taskId));
      event.forwardTo(TasksView.class);
      return;
    }

    showTask(optionalTask.get());
  }

  private void showTask(SharedTaskDto task) {
    removeAll();

    add(new Label("Task ID: " + task.getId()));
    add(new Label("Owner ID: " + task.getOwnerId()));
    add(new ServiceTasksDetailView(task.getServiceTasks()));
  }
}
