package cn.sharesdk.tpl;

public class UserInfo {
	private String userIcon;
	private String userName;
	private Gender userGender;
	private String userNote;
	private String platName;

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getPlatName() {
		return platName;
	}

	public void setPlatName(String platName) {
		this.platName = platName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Gender getUserGender() {
		return userGender;
	}

	public void setUserGender(Gender userGender) {
		this.userGender = userGender;
	}

	public String getUserNote() {
		return userNote;
	}

	public void setUserNote(String userNote) {
		this.userNote = userNote;
	}

	public static enum Gender {
		BOY, GIRL
	}

	@Override
	public String toString() {
		return "UserInfo [userIcon=" + userIcon + ", userName=" + userName
				+ ", userGender=" + userGender + ", userNote=" + userNote
				+ ", platName=" + platName + "]";
	}

}
