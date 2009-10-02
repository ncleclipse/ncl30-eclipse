package br.ufma.deinf.laws.ncleclipse.hover;

public class RegionValues {
	private String top;
	private String left;
	private String width;
	private String height;
		public RegionValues(){
			this.top="-1";
			this.left="-1";
			this.width="-1";
			this.height="-1";
		}
		
		public String getTop(){
			return this.top;
		}
		public String getLeft(){
			return this.left;
		}
		public String getWidth(){
			return this.width;
		}
		public String getHeight(){
			return this.height;
		}
		
		public void setTop(String top){
			 this.top=top;;
		}
		public void setLeft(String left){
			 this.left=left;;
		}
		public void setWidth(String width){
			 this.width=width;;
		}
		public void setHeight(String height){
			 this.height=height;;
		}
}
