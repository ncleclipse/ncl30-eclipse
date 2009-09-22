package br.ufma.deinf.laws.ncleclipse.hover;

public class RegionValues {
	private String top;
	private String left;
	private String width;
	private String height;
		public RegionValues(){
			this.top="0%";
			this.left="0%";
			this.width="0%";
			this.height="0%";
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
