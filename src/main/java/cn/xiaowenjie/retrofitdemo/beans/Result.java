package cn.xiaowenjie.retrofitdemo.beans;

public class Result {

	private String msg;

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Result [msg=" + msg + ", code=" + code + "]";
	}

}
