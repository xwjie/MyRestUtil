package cn.xiaowenjie.myrestutil.beans;

import java.util.LinkedHashMap;

import lombok.Data;

/**
 * 请求信息包装类
 * 
 * @author 肖文杰
 * @date 2017.4.30
 */
@Data
public class RequestInfo {
	private String url;

	private Class<?> returnType;

	private LinkedHashMap<String, String> params;
}
