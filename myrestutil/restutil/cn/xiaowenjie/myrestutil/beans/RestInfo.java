package cn.xiaowenjie.myrestutil.beans;

/**
 * 包装服务器信息类，目前只有host，其他自己配置即可。
 * 
 * @author 肖文杰
 * @date 2017.4.30
 */
public class RestInfo {

	private String host;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String toString() {
		return "RestInfo [host=" + host + "]";
	}

}
