package com.aoenu.feign.service.fallback;

import com.aoenu.feign.service.FileFeignService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * This is Description
 *
 * @author baoben.wu@hand-china.com
 * @date 2019/04/18
 */
@Component
public class FileFeignServiceImpl implements FileFeignService {

    @Override
    public String uploadFiles(Long organizationId, String bucketName, MultipartFile multipartFile) {
        return "fail";
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String fileType) {
        return null;
    }
}
