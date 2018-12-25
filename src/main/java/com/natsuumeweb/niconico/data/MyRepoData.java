package com.natsuumeweb.niconico.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MyRepoData {
	@SerializedName("meta")
	private MetaData metaData;
	@SerializedName("data")
	private List<NiconicoReport> reports;
	private String status;
	private List<String> errors;
	public MetaData getMetaData() {
		return metaData;
	}
	public List<NiconicoReport> getReports() {
		return new ArrayList<>(reports);
	}
	public String getStatus() {
		return status;
	}
	public List<String> getErrors() {
		return new ArrayList<>(errors);
	}
}
