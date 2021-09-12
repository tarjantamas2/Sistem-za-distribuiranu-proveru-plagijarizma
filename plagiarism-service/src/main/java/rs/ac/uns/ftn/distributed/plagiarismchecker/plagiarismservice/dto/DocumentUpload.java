package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUpload {

  private MultipartFile[] files;

  private String name;
}
