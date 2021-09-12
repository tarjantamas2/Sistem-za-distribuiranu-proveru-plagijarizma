package rs.ac.uns.ftn.frontend.views.tasks;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskResultPart;

public class TaskResultPartDetailView extends VerticalLayout {

  public TaskResultPartDetailView(TaskResultPart part) {
    add(new Label("Candidate MD5: " + part.getCandidatePartMd5()));
    add(new Label("Reference MD5: " + part.getReferencePartMd5()));
  }
}
