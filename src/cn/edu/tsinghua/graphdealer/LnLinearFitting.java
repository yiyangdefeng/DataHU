package cn.edu.tsinghua.graphdealer;

public class LnLinearFitting extends LinearFitting{
	private double[] inputx=new double[x.length];
	private double[] inputy=new double[y.length];
	public LnLinearFitting(double[] inx, double[] iny) {
		super(inx,iny);
	}
	private void xy(){
		if(type.equals("y=a*ln(x)+b")){
			for(int i=0;i<x.length;i++){
				inputx[i]=Math.log(x[i]);
				inputy[i]=y[i];
				}
			str="y = %.5f ln(x) + %.5f  R2 = %.8f";
		}else if(type.equals("ln(y)=a*x+b")){
			for(int i=0;i<x.length;i++){
				inputx[i]=x[i];
				inputy[i]=Math.log(y[i]);
				}
			str="ln(y) = %.5f x + %.5f  R2 = %.8f";
		}else if(type.equals("ln(y)=a*ln(x)+b")){
			for(int i=0;i<x.length;i++){
				inputx[i]=Math.log(x[i]);
				inputy[i]=Math.log(y[i]);
				}
			str="ln(y) = %.5f ln(x) + %.5f  R2 = %.8f";
		}
	}
	public void parameter(){
		xy();
		frequencies=frequencies+1;		
		LinearFitting lin=new LinearFitting(inputx,inputy);
		lin.parameter();
		lin.suqcalculator();
		parameters[0]=lin.parameters[0];
		parameters[1]=lin.parameters[1];
		R=lin.R;
		resulte=true;
		}
	public double function(double a) {
		double b=0;
		if(type.equals("y=a*ln(x)+b")){
			b = parameters[0]*Math.log(a) + parameters[1];
		}else if(type.equals("ln(y)=a*x+b")){
			b = Math.exp(parameters[0]*a + parameters[1]);
		}else if(type.equals("ln(y)=a*ln(x)+b")){
			b = Math.exp(parameters[0]*Math.log(a) + parameters[1]);
		}
		return b;
		}	
	public void Out(){
		parameters[0]=0;
		parameters[1]=0;
		resulte=false;	
		Mins();
		if(resulte){
			System.out.println();
			System.out.printf(str,parameters[0],parameters[1],R);
			System.out.println();
		}else{
			System.out.println("Not successful!\n1.The Data is too few to Fitting.\n2.No convergence.\n3.Others.");
		}
	}

}
