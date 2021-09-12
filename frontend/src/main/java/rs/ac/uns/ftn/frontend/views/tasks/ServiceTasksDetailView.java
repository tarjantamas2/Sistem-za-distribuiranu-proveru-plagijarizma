package rs.ac.uns.ftn.frontend.views.tasks;

import java.util.List;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.ServiceTaskDto;

public class ServiceTasksDetailView extends HidableVerticalLayout {

  public ServiceTasksDetailView(List<ServiceTaskDto> serviceTasks) {
    super("Show service tasks", "Hide service tasks");

    if (serviceTasks == null || serviceTasks.isEmpty()) {
      setShowHideButtonVisible(false);
    } else {
      setShowHideButtonVisible(true);
      serviceTasks.stream().map(ServiceTaskDetailView::new).forEach(this::add);
    }
  }
}
