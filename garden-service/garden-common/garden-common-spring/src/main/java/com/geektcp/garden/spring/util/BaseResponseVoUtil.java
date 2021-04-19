package com.geektcp.garden.spring.util;

import com.geektcp.garden.spring.model.vo.BaseResponseVO;

public class BaseResponseVoUtil {
    public static BaseResponseVO success() {
        BaseResponseVO resultVO = new BaseResponseVO();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        return resultVO;
    }

    public static BaseResponseVO success(Object object) {
        BaseResponseVO resultVO = new BaseResponseVO();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        resultVO.setData(object);
        return resultVO;
    }

    public static BaseResponseVO error(Exception e) {
        BaseResponseVO resultVO = new BaseResponseVO();
        resultVO.setCode(-1);
        StringBuffer sb = new StringBuffer();
        sb.append(e.getMessage());
        sb.append("\n");
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append("\t");
            sb.append(element.toString());
            sb.append("\n");
        }
        resultVO.setMsg(sb.toString());
        return resultVO;
    }

    public static BaseResponseVO error(Integer code, String msg) {
        BaseResponseVO resultVO = new BaseResponseVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
