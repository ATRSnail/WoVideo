package com.lt.hm.wovideo.model;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/4/16
 */
public class UpdateModel {
	private LatestVersionBean latestVersion;
	/**
	 * latestVersion : {"isDelete":0,"platform":"1","createTime":1468895603000,"updateTime":1461859200000,"versionId":900,"versionCode":"10.0","modiTime":1468895603000,"versionName":"10.0全民乐园版","updateType":"2","url":"http://download/ios/10.0.com","introduction":"本次更新加入了。。。。功能"}
	 * isUpdate : 1
	 */

	private String isUpdate;

	public LatestVersionBean getLatestVersion() {
		return latestVersion;
	}

	public void setLatestVersion(LatestVersionBean latestVersion) {
		this.latestVersion = latestVersion;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public static class LatestVersionBean {
		private int isDelete;
		private String platform;//平台//1-IOS，2-Android
		private long createTime;
		private long updateTime;
		private int versionId;
		private String versionCode;//最新版本号
		private long modiTime;
		private String versionName;//版本名称
		private String updateType;//是否强制更新1非强制 2强制
		private String url;//更新地址
		private String introduction;//详细更新内容介绍

		public int getIsDelete() {
			return isDelete;
		}

		public void setIsDelete(int isDelete) {
			this.isDelete = isDelete;
		}

		public String getPlatform() {
			return platform;
		}

		public void setPlatform(String platform) {
			this.platform = platform;
		}

		public long getCreateTime() {
			return createTime;
		}

		public void setCreateTime(long createTime) {
			this.createTime = createTime;
		}

		public long getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(long updateTime) {
			this.updateTime = updateTime;
		}

		public int getVersionId() {
			return versionId;
		}

		public void setVersionId(int versionId) {
			this.versionId = versionId;
		}

		public String getVersionCode() {
			return versionCode;
		}

		public void setVersionCode(String versionCode) {
			this.versionCode = versionCode;
		}

		public long getModiTime() {
			return modiTime;
		}

		public void setModiTime(long modiTime) {
			this.modiTime = modiTime;
		}

		public String getVersionName() {
			return versionName;
		}

		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}

		public String getUpdateType() {
			return updateType;
		}

		public void setUpdateType(String updateType) {
			this.updateType = updateType;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getIntroduction() {
			return introduction;
		}

		public void setIntroduction(String introduction) {
			this.introduction = introduction;
		}
	}
}
