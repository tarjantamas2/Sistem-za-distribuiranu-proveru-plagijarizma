package rs.ac.uns.ftn.frontend.views.tasks;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskResult;

public class TaskResultDetailView extends VerticalLayout {

  public TaskResultDetailView(TaskResult taskResult) {
    add(new Label("Reference id: " + taskResult.getReferenceDocumentId()));
    add(new Label("Reference MD5: " + taskResult.getReferenceDocumentMd5()));
    add(new Label("Similarity score: " + taskResult.getScore()));
    add(new TaskResultPartsDetailView(taskResult.getParts()));
  }
}
