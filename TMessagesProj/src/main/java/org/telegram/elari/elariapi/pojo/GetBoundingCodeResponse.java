package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class GetBoundingCodeResponse{

	@SerializedName("code")
	private String code;

	@SerializedName("error")
	private int error;

	@SerializedName("account")
	private int account;

	public String getCode(){
		return code;
	}

	public int getError(){
		return error;
	}

	public int getAccount(){
		return account;
	}
}