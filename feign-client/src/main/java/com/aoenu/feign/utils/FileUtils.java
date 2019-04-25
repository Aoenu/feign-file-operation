package com.aoenu.feign.utils;

import com.aoenu.feign.constant.FileConstants;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;

/**
 * This is Description
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/24
 */
public class FileUtils {

    public static MultipartFile getImageMultipartFile(MultipartFile multipartFile, String fileType, String fileName) {
        InputStream is = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        MultipartFile imageMultipartFile = null;
        try {
            is = multipartFile.getInputStream();
            if (FileConstants.FileFormatCode.DOC_TYPE.equals(fileType) || FileConstants.FileFormatCode.DOCX_TYPE.equals(fileType)) {
                DocumentToImageUtils.wordToImage(is, os);
            } else if (FileConstants.FileFormatCode.PPT_TYPE.equals(fileType) || FileConstants.FileFormatCode.PPTX_TYPE.equals(fileType)) {
                DocumentToImageUtils.pptToImage(is, os);
            } else if (FileConstants.FileFormatCode.XSL_TYPE.equals(fileType) || FileConstants.FileFormatCode.XSLX_TYPE.equals(fileType)) {
                DocumentToImageUtils.excelToImage(is, os);
            } else if (FileConstants.FileFormatCode.PDF_TYPE.equals(fileType)) {
                DocumentToImageUtils.pdfToImage(is, os);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
        return imageMultipartFile;
    }

    public static String getFileType(String fileName) {
        if (fileName == null) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }


    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Java获取文件ContentType
     * 该方式支持本地文件，也支持http/https远程文件
     *
     * @param file
     * @return
     */
    public static String getContentType(File file) {
        String contentType = "";
        try {
            contentType = new MimetypesFileTypeMap().getContentType(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentType;
    }

}
