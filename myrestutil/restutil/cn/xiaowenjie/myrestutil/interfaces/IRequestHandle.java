package cn.xiaowenjie.myrestutil.interfaces;

import cn.xiaowenjie.myrestutil.beans.RequestInfo;
import cn.xiaowenjie.myrestutil.beans.RestInfo;

/**
 * 处理网络请求接口
 * 
 * @author 肖文杰
 * @date 2017.4.30
 */
public interface IRequestHandle {
	Object handle(RestInfo restInfo, RequestInfo request);
}
