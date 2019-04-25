package com.aoenu.feign.constant;

import java.math.BigDecimal;

/**
 * 投资规划服务常量
 *
 * @author baoben.wu@hand-china.com
 */
public interface FileConstants {

    interface FeignService {
        /**
         * 文件服务
         */
        String FILE_SERVICE = "feign-server";
    }

    /**
     * 常量
     */
    interface Constant {
        String ENC = "UTF-8";

        Long SUM = 0L;
    }

    /**
     * 文件存储大小限制单位
     */
    interface FileStorageUnitCode {

        String KB = "KB";

        String MB = "MB";
    }


    /**
     * 文件分类
     */
    interface FileTypeCode {

        String APPLICATION = "application";

        String AUDIO = "audio";

        String VIDEO = "video";

        String IMAGE = "image";

        String TEXT = "text";

    }

    /**
     * 文件格式
     */
    interface FileFormatCode {

        String DOC_TYPE = "doc";

        String DOCX_TYPE = "docx";

        String PPT_TYPE = "ppt";

        String PPTX_TYPE = "pptx";

        String XSL_TYPE = "xls";

        String XSLX_TYPE = "xlsx";

        String PDF_TYPE = "pdf";
    }

    /**
     * 文档管理存储分类
     */
    interface TypeCode {

        String PROJECT = "PROJECT";

        String SITE = "SITE";

    }

}
