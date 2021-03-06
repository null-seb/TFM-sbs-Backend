package es.upm.tfm_sbs.service.cms.feign;

import es.upm.tfm_sbs.common.base.result.Result;
import es.upm.tfm_sbs.service.cms.feign.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(value = "service-oss", fallback = OssFileServiceFallBack.class)
public interface OssFileService {

    @DeleteMapping("/admin/oss/file/remove")
    Result removeFile(@RequestBody String url);
}
