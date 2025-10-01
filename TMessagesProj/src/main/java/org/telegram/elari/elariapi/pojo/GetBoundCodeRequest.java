package org.telegram.elari.elariapi.pojo;

import com.google.gson.annotations.SerializedName;

public class GetBoundCodeRequest{
	@SerializedName("account_id")
	private long accountId;
	@SerializedName("ApiKey")
	private String ApiKey = "debug";
	@SerializedName("man_id")
	private String manId;
	@SerializedName("dev_type")
	private String devType;

	public long getAccountId(){
		return accountId;
	}
	public String getApiKey(){
		return ApiKey;
	}
	public String getManId(){
		return manId;
	}
	public String getDevType(){return devType;}

	public void setAccountId(long accountId_){
		 accountId = accountId_;
	}
	public void setApiKey(String apiKey_){
		ApiKey = apiKey_;
	}
	public void setManId(String manId_){
		manId = manId_;
	}
	public void setDevType(String devType_){
		devType = devType_;
	}
}
