package rs.ac.uns.ftn.frontend.views.tasks;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.ServiceTaskDto;

public class ServiceTaskDetailView extends VerticalLayout {

  public ServiceTaskDetailView(ServiceTaskDto task) {
    add(new Label("Service ID: " + task.getServiceId()));
    add(new Label("Algorithm ID: " + task.getAlgorithmId()));
    add(new Label("Candidate id: " + task.getCandidateDocumentId()));
    add(new Label("Candidate MD5: " + task.getCandidateDocumentMd5()));
    add(new Label("Status: " + task.getStatus()));
    if (task.getStatusDescription() != null && !task.getStatusDescription().trim().isEmpty()) {
      add(new Label("Status description: " + task.getStatusDescription()));
    }
    add(new TaskResultsDetailView(task.getResults()));
  }
}
