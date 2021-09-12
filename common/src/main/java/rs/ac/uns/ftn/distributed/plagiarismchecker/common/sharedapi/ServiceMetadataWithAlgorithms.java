package rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.Algorithm;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.ServiceMetadata;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceMetadataWithAlgorithms extends ServiceMetadata {

  private List<Algorithm> algorithms;
}
