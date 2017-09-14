package cn.xiaowenjie.myrestutil.beans;

import lombok.Data;

/**
 * 包装服务器信息类，目前只有host，其他自己配置即可。
 * 
 * @author 肖文杰
 * @date 2017.4.30
 */
@Data
public class RestInfo {
	private String host;
}
