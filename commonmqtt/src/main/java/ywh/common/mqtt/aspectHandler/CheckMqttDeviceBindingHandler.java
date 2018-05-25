package ywh.common.mqtt.aspectHandler;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ywh.common.entity.Device;
import ywh.common.entity.User;
import ywh.common.mqtt.annotation.CheckMqttDeviceBinding;
import ywh.common.repository.UserRepository;
import ywh.common.util.exception.ExceptionEnum;
import ywh.common.util.response.ResultUtil;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Aspect
public class CheckMqttDeviceBindingHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CheckMqttDeviceBindingHandler.class);

    @Autowired
    UserRepository userRepository;

    @Before("@annotation(checkMqttDeviceBinding)")
    public void doBefore(JoinPoint joinPoint,CheckMqttDeviceBinding checkMqttDeviceBinding){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //url
        LOGGER.info("url={}",request.getRequestURL());
        //method
        LOGGER.info("method={}",request.getMethod());
        //ip
        LOGGER.info("id={}",request.getRemoteAddr());
        //class_method
        LOGGER.info("class_method={}",joinPoint.getSignature().getDeclaringTypeName() + "," + joinPoint.getSignature().getName());
        //args[]
        LOGGER.info("args={}",joinPoint.getArgs());
    }


    @Around(value = "@annotation(checkMqttDeviceBinding)")
    public Object around(ProceedingJoinPoint joinPoint, CheckMqttDeviceBinding checkMqttDeviceBinding) throws Throwable {
        LOGGER.info("simulate device binding judging here 2");
        Object[] args = joinPoint.getArgs();

        Principal principal = (Principal)args[1];
        String deviceSn = (String)args[0];
        String userName = principal.getName();

        LOGGER.info("deviceSn: "+ deviceSn + " userName: " + userName );
        User user = userRepository.findByUsername(userName);
        Device device = new Device(deviceSn);
        if(user.isDeviceExist(device)){
            return joinPoint.proceed();
        }
        return ResultUtil.error(ExceptionEnum.DEVICE_NO_BIND);
    }
}
