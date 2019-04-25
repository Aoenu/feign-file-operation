package com.aoenu.feign.utils;

import com.aoenu.feign.constant.FileConstants;
import com.aspose.cells.*;
import com.aspose.pdf.Document;
import com.aspose.pdf.devices.PngDevice;
import com.aspose.pdf.devices.Resolution;
import com.aspose.slides.Presentation;
import com.aspose.words.SaveFormat;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

/**
 * 为word、ppt、excel、pdf文件生成缩略图
 *
 * @author chen.qiu@hand-china.com 2019-04-08
 */
public class DocumentToImageUtils {

    /**
     * 为PDF文件生成首页的缩略图
     * 不对流进行关闭
     *
     * @param input  pdf文件流
     * @param output 产生的图片输出流
     */
    public static void pdfToImage(InputStream input, ByteArrayOutputStream output) throws Exception {

        Document pdfDocument = new Document(input);
        Resolution resolution = new Resolution(100);
        PngDevice pngDevice = new PngDevice(resolution);
        pngDevice.process(pdfDocument.getPages().get_Item(1), output);

    }

    /**
     * 为word文件生成首页的缩略图
     * 不对流进行关闭
     *
     * @param input
     * @param output
     */
    public static void wordToImage(InputStream input, ByteArrayOutputStream output) throws Exception {
        com.aspose.words.Document doc = new com.aspose.words.Document(input);
        doc.save(output, SaveFormat.PNG);
    }

    /**
     * 为excel文件生成首页的缩略图
     * 不对流进行关闭
     *
     * @param input
     * @param output
     */
    public static void excelToImage(InputStream input, ByteArrayOutputStream output) throws Exception {

        Workbook wb = new Workbook(input);
        Worksheet sheet = wb.getWorksheets().get(0);

        sheet.getPageSetup().setLeftMargin(0);
        sheet.getPageSetup().setRightMargin(0);
        sheet.getPageSetup().setBottomMargin(0);
        sheet.getPageSetup().setTopMargin(0);

        ImageOrPrintOptions imgOptions = new ImageOrPrintOptions();
        imgOptions.setChartImageType(ImageFormat.getPng());
        imgOptions.setOnePagePerSheet(true);
        imgOptions.setPrintingPage(PrintingPageType.IGNORE_BLANK);

        SheetRender sr = new SheetRender(sheet, imgOptions);
        sr.toImage(0, output);
    }

    /**
     * 为ppt文件生成首页的缩略图：讲ppt的第一页转成pdf然后再转成图片
     * 不对流进行关闭
     *
     * @param input
     * @param output
     */
    public static void pptToImage(InputStream input, ByteArrayOutputStream output) throws Exception {

        Presentation ppt = new Presentation(input);
        int[] slides = new int[]{1};

        //将第一页转成pdf
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        InputStream pdfInputStream = null;

        try {
            ppt.save(pdfOutputStream, slides, com.aspose.slides.SaveFormat.Pdf);

            //将ppt文件的输出流转换成输入流
            pdfInputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());
            Document pdfDocument = new Document(pdfInputStream);

            //将pdf转成图片输出流
            Resolution resolution = new Resolution(100);
            PngDevice pngDevice = new PngDevice(resolution);
            pngDevice.process(pdfDocument.getPages().get_Item(1), output);
        } finally {

            //关闭本方法内定义的流
            IOUtils.closeQuietly(pdfOutputStream);
            IOUtils.closeQuietly(pdfInputStream);
        }
    }

    /**
     * 对图片进行剪切，并产生新的输出流
     * @param fileType
     * @param sourceOutput
     * @param output
     */
    public static void imageCut(String fileType, ByteArrayOutputStream sourceOutput, ByteArrayOutputStream output) {

        //先创建临时文件
        String uuid = UUID.randomUUID().toString();
        File imageFile = new File("/tmp/" + uuid + ".png");
        OutputStream imageOut = null;

        BufferedImage src = null;
        try {
            //先生成图片文件
            imageOut = new FileOutputStream(imageFile);
            sourceOutput.writeTo(imageOut);
            imageOut.flush();

            //再对图片进行处理
            Thumbnails.Builder<File> fileBuilder = Thumbnails.of(imageFile).scale(1.0).outputQuality(1.0);
            src = fileBuilder.asBufferedImage();

            //获取图片原始的高度和宽度
            int height = src.getHeight();
            int width = src.getWidth();

            //根据不同的文件类型重新计算高度
            int cutHeight = 0;
            if (FileConstants.FileFormatCode.DOC_TYPE.equalsIgnoreCase(fileType) ||
                    FileConstants.FileFormatCode.DOCX_TYPE.equalsIgnoreCase(fileType)) {
                cutHeight = height - height / 5;
            } else if (FileConstants.FileFormatCode.XSL_TYPE.equalsIgnoreCase(fileType) ||
                    FileConstants.FileFormatCode.XSLX_TYPE.equalsIgnoreCase(fileType)) {
                cutHeight = height - height / 10;
            } else if (FileConstants.FileFormatCode.PPT_TYPE.equalsIgnoreCase(fileType) ||
                    FileConstants.FileFormatCode.PPTX_TYPE.equalsIgnoreCase(fileType)) {
                cutHeight = height - height / 9;
            } else if (FileConstants.FileFormatCode.PDF_TYPE.equalsIgnoreCase(fileType)) {
                cutHeight = height - height / 10;
            }

            //进行图片剪切
            fileBuilder.sourceRegion(Positions.CENTER_LEFT, width, cutHeight).toOutputStream(output);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流以及删除临时文件
            IOUtils.closeQuietly(imageOut);
            if(imageFile.exists()){
                imageFile.delete();
            }
        }

    }

}
