package cn.org.dialogue.jwt;


import com.alibaba.fastjson.JSONObject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //从 http 请求头中取出 token
        String token = request.getHeader("token");
        if (token == null) {
            //没有token设置返回状态码为401
            response.setStatus(401);
            return false;
        }
        //验证 token
        if(TokenUtil.checkSign(token)){
            return true;
        }else{
            //token解析错误设置返回状态码为401
            response.setContentType("application/json; charset=utf-8");
            try{
                JSONObject json = new JSONObject();
                json.put("code","401");
                json.put("message","token失效");
                response.getWriter().append(json.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            response.setStatus(401);
            return false;
        }
    }
}
