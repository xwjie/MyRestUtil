package cn.xiaowenjie.retrofitdemo.beans;

public class ResultBean {

	private int code;

	private String msg = "success";

	private String data;

	public ResultBean() {
		super();
	}

	public ResultBean(String data) {
		super();
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "ResultBean [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}

}
