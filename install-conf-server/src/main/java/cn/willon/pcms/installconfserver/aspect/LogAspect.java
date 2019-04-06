package cn.willon.pcms.installconfserver.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * LogAspect
 *
 * @author Willon
 * @since 2019-04-06
 */
@Component
@Aspect
@Slf4j
public class LogAspect {


    /**
     * 切点
     */
    @Pointcut(value = "execution(* cn.willon.pcms.installconfserver.controller..*.*(..))")
    public void pointCut() {
    }


    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();
        String remoteAddr = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String args = Arrays.toString(joinPoint.getArgs());
        Map<String, String[]> parameterMap = request.getParameterMap();
        RequestLog requestLog = new RequestLog();
        requestLog.setIp(remoteAddr);
        requestLog.setUrl(url);
        requestLog.setRequestMethod(method);
        requestLog.setParam(parameterMap);
        requestLog.setExecMethod(classMethod);
        requestLog.setArgs(args);
        log.info("Request: " + JSON.toJSONString(requestLog, SerializerFeature.DisableCircularReferenceDetect));
    }


    @AfterReturning(returning = "res", pointcut = "pointCut()")
    public void afterReturn(Object res) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = Objects.requireNonNull(requestAttributes).getResponse();
        int status = Objects.requireNonNull(response).getStatus();
        ResponseLog responseLog = new ResponseLog();
        responseLog.setStatus(status);
        responseLog.setData(res);
        log.info("Response: " + JSON.toJSONString(responseLog));
    }


    @Data
    private class RequestLog {
        private String ip;
        private String url;
        private String requestMethod;
        private Map param;
        private String execMethod;
        private String args;
    }

    @Data
    private class ResponseLog {
        private int status;
        private Object data;

    }
}
