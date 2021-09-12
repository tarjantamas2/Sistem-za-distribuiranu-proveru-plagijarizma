package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.processing;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public final class FullTextExtractor {

  public static String extract(File file, String mimeType, String extension) throws IOException, TikaException {
    if (isNotPdf(mimeType) && isNotPdf(extension)) {
      return StringUtils.EMPTY;
    }
    String extracted = extractFullText(file);
    if (StringUtils.isBlank(extracted)) {
      return StringUtils.EMPTY;
    }
    return extracted;
  }

  private static boolean isNotPdf(String mimeTypeOrExtension) {
    return !"application/pdf".equalsIgnoreCase(mimeTypeOrExtension) && !"pdf".equalsIgnoreCase(mimeTypeOrExtension);
  }

  private static String extractFullText(File file) throws IOException, TikaException {
    try {
      return extractFullTextPDFParser(file);
    } catch (Exception e) {
      return extractFullTextTika(file);
    }
  }

  private static String extractFullTextTika(File file) throws IOException, TikaException {
    Tika tika = new Tika();
    tika.setMaxStringLength(-1);
    return tika.parseToString(file);
  }

  private static String extractFullTextPDFParser(File file) throws IOException {
    PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
    parser.parse();
    PDFTextStripper textStripper = new PDFTextStripper();
    String text = textStripper.getText(parser.getPDDocument());
    parser.getPDDocument().close();

    return text;
  }

  private FullTextExtractor() {
  }
}
