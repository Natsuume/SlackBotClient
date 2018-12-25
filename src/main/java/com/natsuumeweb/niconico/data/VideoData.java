package com.natsuumeweb.niconico.data;

import com.google.gson.annotations.SerializedName;

public class VideoData {
	private String id;
	private String status;
	private String title;
	@SerializedName("videoWatchPageId")
	private String watchId;
	public String getId() {
		return id;
	}
	public String getStatus() {
		return status;
	}
	public String getTitle() {
		return title;
	}
	public String getWatchId() {
		return watchId;
	}
	
	public String toString() {
		return watchId + ":" + title;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((watchId == null) ? 0 : watchId.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VideoData other = (VideoData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (watchId == null) {
			if (other.watchId != null)
				return false;
		} else if (!watchId.equals(other.watchId))
			return false;
		return true;
	}
}
