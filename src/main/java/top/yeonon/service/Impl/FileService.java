package top.yeonon.service.Impl;

import com.google.common.collect.Lists;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import top.yeonon.service.IFileService;
import top.yeonon.util.FTPUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("fileService")
public class FileService implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileService.class);

    @Override
    public String upload(MultipartFile file, String path, String remotePath, String avatarName) {

        String fileName = file.getOriginalFilename();
        String extendName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + extendName;
        if (avatarName != null) {
            uploadFileName = avatarName + '.' + extendName;
        }

        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);

        try {
            file.transferTo(targetFile);
            FTPUtil.uploadFile(Lists.newArrayList(targetFile), remotePath);
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常", e);
            e.printStackTrace();
            return null;
        }
        return targetFile.getName();
    }


}
