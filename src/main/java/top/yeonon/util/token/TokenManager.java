package top.yeonon.util.token;

public interface TokenManager {

    //创建一个token，userId为key
    TokenModel createToken(Integer userId);

    //检查一个token是否合法
    boolean checkToken(TokenModel model);

    //从字符串中解析token，参数是一个加密后的字符串
    TokenModel getToken(String authentication);

    //删除一个token，根据userID
    void deleteToken(Integer userId);


}

