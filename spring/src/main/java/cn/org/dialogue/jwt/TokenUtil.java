package cn.org.dialogue.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;
import java.util.Map;

public class TokenUtil {
    private static final String ISSUER = "dialogue";
    /**
     * 1小时后过期
     */
    private static final long EXPIRE_TIME = 60 * 60 * 1000;
    private static final String SECRET = "2004"; //加密盐

    /**
     * 生成token
     * @param info 存放需要保存在payload中的数据，一般为userId与sessionId
     * */
    public static String sign(Map<String, Object> info) {
        try {
            Date expiresTime = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    //以下均为payload中携带信息
                    .withIssuer(ISSUER)
                    .withClaim("info", info)
                    .withExpiresAt(expiresTime)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据token获取自定义数据info
     * */
    public static Map<String, Object> getInfo(String token) {
        try {
            return JWT.decode(token).getClaim("info").asMap();
        }catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 校验token
     * */
    public static boolean checkSign(String token) {
        try {
            Algorithm algorithm  = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                                      .withIssuer(ISSUER)
                                      .build();
            verifier.verify(token);
            return true;
        }catch (JWTVerificationException e) {
            return false;
        }
    }
}
