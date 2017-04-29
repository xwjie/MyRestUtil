package cn.xiaowenjie.myrestutil.interfaces;

import cn.xiaowenjie.myrestutil.beans.RequestInfo;
import cn.xiaowenjie.myrestutil.beans.RestInfo;

public interface IRequestHandle {

	Object handle(RestInfo restInfo, RequestInfo request);
}
