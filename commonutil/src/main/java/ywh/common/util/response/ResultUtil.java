package ywh.common.util.response;

import ywh.common.util.exception.ExceptionEnum;

public class ResultUtil {

    /**
     * 请求成功返回
     * @param object
     * @return
     */
    public static Msg success(Object object){
        Msg msg=new Msg();
        msg.setCode(200);
        msg.setMsg("success");
        msg.setData(object);
        return msg;
    }
    public static Msg success(){
        return success(null);
    }

    /**
     * 自定义错误信息
     * @param code
     * @param msg
     * @return
     */
    public static Msg error(Integer code,String msg){
        Msg result = new Msg();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    /**
     * 返回异常信息，在已知的范围内
     * @param exceptionEnum
     * @return
     */
    public static Msg error(ExceptionEnum exceptionEnum){
        Msg result = new Msg();
        result.setCode(exceptionEnum.getCode());
        result.setMsg(exceptionEnum.getMsg());
        result.setData(null);
        return result;
    }

}
