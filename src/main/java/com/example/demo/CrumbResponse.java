package com.example.demo;

public class CrumbResponse {

	private String crumbRequestField;

	private String crumb;
	private String _class;

	public String getCrumbRequestField() {
		return crumbRequestField;
	}

	public void setCrumbRequestField(String crumbRequestField) {
		this.crumbRequestField = crumbRequestField;
	}

	public String getCrumb() {
		return crumb;
	}

	public void setCrumb(String crumb) {
		this.crumb = crumb;
	}

	public String get_class() {
		return _class;
	}

	public void set_class(String _class) {
		this._class = _class;
	}

	@Override
	public String toString() {
		return "CrumbResponse [crumbRequestField=" + crumbRequestField + ", crumb=" + crumb + ", _class=" + _class
				+ "]";
	}

}
