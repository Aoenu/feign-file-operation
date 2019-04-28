package com.aoenu.feign.service;


import com.aoenu.feign.config.MultipartSupportConfig;
import com.aoenu.feign.constant.FileConstants;
import com.aoenu.feign.service.fallback.FileFeignServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * This is Description
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/17
 */
@FeignClient(value = FileConstants.FeignService.FILE_SERVICE,
        fallback = FileFeignServiceImpl.class,
        configuration = MultipartSupportConfig.class)
public interface FileFeignService {
    /**
     * 上传文件
     *
     * @param organizationId 租户ID
     * @param bucketName     存储筒
     * @param multipartFile  上传文件
     * @return
     */
    @RequestMapping(value = "/{organizationId}/files/multipart",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadFiles(@PathVariable(value = "organizationId") Long organizationId,
                       @RequestParam(value = "bucketName") String bucketName,
                       @RequestPart(value = "file") MultipartFile multipartFile);


    /**
     * 下载
     *
     * @param fileType
     * @return
     */
    @RequestMapping("/downloadFile")
    ResponseEntity<byte[]> downloadFile(@RequestParam(value = "fileType")  String fileType);
}
