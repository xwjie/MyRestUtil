package cn.xiaowenjie.retrofitdemo.beans;

import lombok.Data;

@Data
public class ResultBean {
	private int code;

	private String msg = "success";

	private String data;
}
