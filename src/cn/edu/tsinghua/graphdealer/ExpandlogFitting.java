package cn.edu.tsinghua.graphdealer;


public class ExpandlogFitting extends LinearFitting{
	private double[] inputx;
	private double[] inputy;
	
	public ExpandlogFitting(double[] inx, double[] iny) {
		super(inx, iny);
		this.numofpara=3;
		this.parameters=new double[3];
		this.parameters[0]=0;
		this.parameters[1]=0;
		this.parameters[2]=0;
	}
	
	private void xy(){
		double[] inx=new double[x.length];
		double[] iny=new double[y.length];
		int j=0;
		if(type.equals("y=a*x^b+c")){
			for(int i=0;i<x.length;i++){
				if(x[i]>0 && (y[i]-parameters[2])>0){
					inx[j]=Math.log(x[i]);
					iny[j]=Math.log(y[i]-parameters[2]);
					j=j+1;
					}
				}
		}else if(type.equals("y=a*b^x+c")){
			for(int i=0;i<x.length;i++){
				if((y[i]-parameters[2])>0){
					inx[j]=x[i];
					iny[j]=Math.log(y[i]-parameters[2]);
					j=j+1;
					}
				}
			}
		inputx=new double[j];
		inputy=new double[j];
		for(int i=0;i<inputx.length;i++){
			inputx[i]=inx[i];
			inputy[i]=iny[i];
		}
		}
	public void parameter(){
		xy();
		if(inputx.length>=numofpara){
		frequencies=frequencies+1;		
		LinearFitting lin=new LinearFitting(inputx,inputy);
		lin.parameter();
		lin.suqcalculator();
		R=lin.R;
		if(type.equals("y=a*x^b+c")){
			parameters[0]=Math.exp(lin.parameters[1]);
			parameters[1]=lin.parameters[0];
		}else if(type.equals("y=a*b^x+c")){
			parameters[0]=Math.exp(lin.parameters[1]);
			parameters[1]=Math.exp(lin.parameters[0]);
		}		
		for(int i=0;i<x.length;i++){
			yfit[i]=function(x[i]);
			//System.out.println(y[i]+"   "+inputy[i]+"   "+yfit[i]);
		}
		parameters[2]+=(average(y)-average(yfit));
		System.out.println(frequencies+" "+(average(y)-average(yfit))+"  "+parameters[2]+" \n");
		if(Math.abs(average(y)-average(yfit))<1e-8){
			resulte=true;
		}
		}else{
			toofew=true;
			resulte=false;
		}
	}
	public double function(double a) {
		double b=0;
		if(type.equals("y=a*x^b+c")){
			b=parameters[0]*Math.pow(a,parameters[1])+parameters[2];
		}else if(type.equals("y=a*b^x+c")){
			b=parameters[0]*Math.pow(parameters[1],a)+parameters[2];
		}
		return b;
		}
	public void fitting(){
		parameters[0]=0;
		parameters[1]=0;
		parameters[2]=0;
		resulte=false;
		Mins();
		if(resulte){
			if(type.equals("y=a*x^b+c")){
				if(Math.abs(parameters[0])<1e-6){
					if(Math.abs(parameters[2])<1e-6){
						outstr="y = 0";
					}else{
						outstr="y = "+doutoString(parameters[2]);
					}
				}else {
					if(Math.abs(parameters[1])<1e-6){
						outstr="y = "+doutoString(parameters[0]);
					}else{
						outstr="y = "+doutoString(parameters[0])+"*x^"+doutoString(parameters[1]);
					}
					if(Math.abs(parameters[2])<1e-6){
					}else if(parameters[2]>1e-6){
						outstr=outstr+" + "+doutoString(parameters[2]);
					}else{
						outstr=outstr+" - "+doutoString(Math.abs(parameters[2]));
					}
				}
			}else if(type.equals("y=a*b^x+c")){
				if(Math.abs(parameters[0])<1e-6){
					if(Math.abs(parameters[2])<1e-6){
						outstr="y = 0";
					}else{
						outstr="y = "+doutoString(parameters[2]);
					}
				}else {
					if(Math.abs(parameters[1])<1e-6){
						outstr="y = "+doutoString(parameters[0]);
					}else if(parameters[1]>1e-6){
						outstr="y = "+doutoString(parameters[0])+"*"+doutoString(parameters[1])+"^x";
					}else{
						outstr="y = "+doutoString(parameters[0])+"*("+doutoString(parameters[1])+")^x";
					}
					if(Math.abs(parameters[2])<1e-6){
					}else if(parameters[2]>1e-6){
						outstr=outstr+" + "+doutoString(parameters[2]);
					}else{
						outstr=outstr+" - "+doutoString(Math.abs(parameters[2]));
					}
				}
			}
			outR="R^2 = "+doutoString(R);
		}else{
			fittingfailed();
		}
	}
}
