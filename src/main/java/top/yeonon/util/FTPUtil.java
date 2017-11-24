package top.yeonon.util;


import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {
    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPwd = PropertiesUtil.getProperty("ftp.pass");


    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    private FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }


    public static boolean uploadFile(List<File> fileList, String remotePath) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPwd);
        logger.info("开始连接服务器");
        boolean result = ftpUtil.uploadFile(remotePath, fileList);
        logger.info("开始上传,上传结果是： " + result);
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.enterLocalPassiveMode();
                for (File file : fileList) {
                    fis = new FileInputStream(file);
                    String name = new String(file.getName().getBytes("UTF-8"), "iso-8859-1");
                    ftpClient.storeFile(name, fis);
                }
            } catch (IOException e) {
                logger.error("上传文件失败，服务器异常");
                uploaded =false;
                e.printStackTrace();
            } finally {
                fis.close();
                ftpClient.disconnect();
            }

        }
        return uploaded;
    }

    private boolean connectServer(String ip, int port, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接服务器异常");
        }
        return isSuccess;
    }
}
