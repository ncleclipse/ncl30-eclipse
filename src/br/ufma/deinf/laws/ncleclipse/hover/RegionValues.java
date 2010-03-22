package br.ufma.deinf.laws.ncleclipse.hover;

public class RegionValues {
	private String top;
	private String left;
	private String width;
	private String height;
	private String rigth;
	private String bottom;

	public RegionValues() {
		this.top = "-1";
		this.left = "-1";
		this.width = "-1";
		this.height = "-1";
		this.rigth = "-1";
		this.bottom = "-1";
	}

	public String getRigth() {
		return this.rigth;
	}

	public String getBottom() {
		return this.bottom;
	}

	public String getTop() {
		return this.top;
	}

	public String getLeft() {
		return this.left;
	}

	public String getWidth() {
		return this.width;
	}

	public String getHeight() {
		return this.height;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setBottom(String bottom) {
		this.bottom = bottom;
	}

	public void setRigth(String rigth) {
		this.rigth = rigth;
	}

	public void clone(RegionValues v) {
		bottom = v.getBottom();
		height = v.getHeight();
		left = v.getLeft();
		rigth = v.getRigth();
		top = v.getTop();
		width = v.getWidth();
	}

	public String toString() {
		return "Left: " + left + " Top: " + top + " Width: " + width
				+ " Height " + height + " Right: " + rigth + " Bottom: "
				+ bottom;
	}
}
