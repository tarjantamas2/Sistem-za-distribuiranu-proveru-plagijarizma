package rs.ac.uns.ftn.frontend.views.tasks;

import static java.util.UUID.randomUUID;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileData;
import com.vaadin.flow.component.upload.receivers.UploadOutputStream;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.ServiceTaskDto;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedRestClient;
import rs.ac.uns.ftn.frontend.config.LoggedInAllowed;
import rs.ac.uns.ftn.frontend.config.TempFileConfig;
import rs.ac.uns.ftn.frontend.views.MainLayout;

@Slf4j
@PageTitle("New Task")
@Route(value = "new-task", layout = MainLayout.class)
@LoggedInAllowed
@Uses(Icon.class)
public class NewTaskView extends Div {

  private final TempFileConfig tempFileConfig;
  private final SharedRestClient sharedRestClient;
  private final ServiceChooserComponent serviceChooserComponent;

  public NewTaskView(SharedRestClient sharedRestClient, TempFileConfig tempFileConfig) {
    this.tempFileConfig = tempFileConfig;
    this.sharedRestClient = sharedRestClient;

    serviceChooserComponent = new ServiceChooserComponent(sharedRestClient);

    VerticalLayout layout = new VerticalLayout();
    layout.add(serviceChooserComponent);
    layout.add(getFileUpload());

    add(layout);
  }

  private VerticalLayout getFileUpload() {
    String tempFile = tempFileConfig.resolve(randomUUID().toString()).toAbsolutePath().toString();
    TextField fileNameField = new TextField("Document name", "Optional");
    UploadBuffer uploadBuffer = new UploadBuffer(tempFile);
    Upload upload = new Upload(uploadBuffer);
    upload.setAutoUpload(true);
    upload.setUploadButton(new Button("Select a document and submit task"));

    upload.addFinishedListener(event -> createTask(fileNameField, uploadBuffer));

    try {
      FileUtils.forceDelete(new File(tempFile));
    } catch (Exception e) {
      log.error("Could not delete temp file: ", e);
    }

    upload.addFileRejectedListener(event -> Notification.show("Failed to upload file"));

    return new VerticalLayout(fileNameField, upload);
  }

  private void createTask(TextField fileNameField, UploadBuffer uploadBuffer) {
    List<ServiceTaskDto> selectedServices = serviceChooserComponent.getSelectedServices();

    if (selectedServices == null || selectedServices.isEmpty()) {
      Notification.show("At least one service algorithm must be selected.");
      return;
    }

    String fileName = StringUtils.isNotBlank(fileNameField.getValue()) ? fileNameField.getValue()
        : uploadBuffer.getOriginalFileName();
    sharedRestClient.createTask(uploadBuffer.getFile(), fileName, selectedServices)
      .ifPresent(dto -> Notification.show("Task was successfully submitted."));
  }

  @Slf4j
  public static class UploadBuffer implements Receiver {

    private FileData file;

    private final String tempFileName;

    private String originalFileName;

    public UploadBuffer(String tempFileName) {
      this.tempFileName = tempFileName;
    }

    @Override
    public OutputStream receiveUpload(String fileName, String mimeType) {
      originalFileName = fileName;
      try {
        UploadOutputStream outputBuffer = new UploadOutputStream(new File(tempFileName));
        file = new FileData(fileName, mimeType, outputBuffer);
        return outputBuffer;
      } catch (Exception e) {
        log.error("Could not save file to temp file: ", e);
        return null;
      }
    }

    public File getFile() {
      return file.getFile();
    }

    public String getOriginalFileName() {
      return originalFileName;
    }
  }
}
