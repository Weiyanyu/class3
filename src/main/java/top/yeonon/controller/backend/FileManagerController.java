package top.yeonon.controller.backend;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.ManagerPermission;

import top.yeonon.service.IFileService;
import top.yeonon.util.PropertiesUtil;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
@RequestMapping("manage/files")
public class FileManagerController {

    @Autowired
    private IFileService fileService;

    @ManagerPermission
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse uploadWorkFiles(@RequestParam("uploadFiles")MultipartFile uploadFiles,
                                      HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.uploadMulitFile(uploadFiles, path, "multiFile");
        String url = PropertiesUtil.getProperty("ftp.server.http.multifile.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);
    }
}
