package cn.edu.tsinghua.graphdealer;

public class ExpandlogFitting extends LinearFitting{
	
	private double[] inputx=new double[x.length];
	private double[] inputy=new double[y.length];
	
	public ExpandlogFitting(double[] inx, double[] iny) {
		super(inx, iny);
		this.numofpara=3;
		this.parameters=new double[3];
		this.parameters[0]=0;
		this.parameters[1]=0;
		this.parameters[2]=0;
	}
	private void xy(){
		if(type.equals("y=a*x^b+c")){
			for(int i=0;i<x.length;i++){
				inputx[i]=Math.log(x[i]);
				inputy[i]=Math.log(y[i]-parameters[2]);
				}
			str="y = %.5f x^%.5f + %.5f  R2 = %.8f";
		}else if(type.equals("y=a*b^x+c")){
			for(int i=0;i<x.length;i++){
				inputx[i]=x[i];
				inputy[i]=Math.log(y[i]-parameters[2]);
				}
			str="y = %.5f * %.5f^x + %.5f  R2 = %.8f";
		}
	}
	public void parameter(){
		xy();
		frequencies=1;		
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
		}
		//System.out.println(frequencies+" "+(average(y)-average(yfit))+" \n");
		parameters[2]+=(average(y)-average(yfit))*Math.pow(frequencies,0.5);
		if(Math.abs(average(y)-average(yfit))<1e-8){
			resulte=true;
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
	public void Out(){
		parameters[0]=0;
		parameters[1]=0;
		parameters[2]=0;
		resulte=false;
		Mins();
		if(resulte){
			System.out.println();
			System.out.printf(str,parameters[0],parameters[1],parameters[2],R);
			System.out.println();
		}else{
			System.out.println("Not successful!\n1.The Data is too few to Fitting.\n2.No convergence.\n3.Others.");
		}
	}
}
