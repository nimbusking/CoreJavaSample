package cc.nimbusk.pdfbox;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfBoxMain {

    public static void main(String[] args) throws IOException {
        String pdfPath = "G:\\Temp\\pdfbox\\产品经理手册.pdf";
        String outputPath = "G:\\Temp\\pdfbox\\output4-fix\\";
        PDDocument document = PDDocument.load(new File(pdfPath));
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage image = pdfRenderer.renderImageWithDPI(page, 150); // 设置图片分辨率为300dpi
            FileOutputStream fileOutputStream = FileUtils.openOutputStream(new File(outputPath + "-" + (page + 1) + ".png"));
            ImageIOUtil.writeImage(image, "PNG", fileOutputStream, 360);
        }
    }

}
