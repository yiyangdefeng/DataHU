package cn.edu.tsinghua.graphdealer;


public class LnLinearFitting extends LinearFitting{
	private double[] inputx;
	private double[] inputy;
	
	public LnLinearFitting(double[] inx, double[] iny) {
		super(inx,iny);
	}
	private void xy(){
		double[] inx=new double[x.length];
		double[] iny=new double[x.length];
		int j=0;
		if(type.equals("y=a*ln(x)+b")){
			for(int i=0;i<x.length;i++){
				if(x[i]>0){
					inx[j]=Math.log(x[i]);
					iny[j]=y[i];
					j=j+1;
					}
				}
		}else if(type.equals("ln(y)=a*x+b")){
			for(int i=0;i<x.length;i++){
				if(y[i]>0){
					inx[j]=x[i];
					iny[j]=Math.log(y[i]);
					j=j+1;
					}
				}
			
		}else if(type.equals("ln(y)=a*ln(x)+b")){
			for(int i=0;i<x.length;i++){
				if(x[i]>0 && y[i]>0){
					inx[j]=Math.log(x[i]);
					iny[j]=Math.log(y[i]);
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
		LinearFitting lin=new LinearFitting(inputx,inputy);
		lin.parameter();
		lin.suqcalculator();
		parameters[0]=lin.parameters[0];
		parameters[1]=lin.parameters[1];
		R=lin.R;
		resulte=true;
		}else{
			toofew=true;
			resulte=false;
		}
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
	public void fitting(){
		parameters[0]=0;
		parameters[1]=0;
		resulte=false;	
		Mins();
		if(resulte){
			if(type.equals("y=a*ln(x)+b")){
				if(Math.abs(parameters[0])<1e-6){
					if(Math.abs(parameters[1])<1e-6){
						outstr="y = 0";
					}else{
						outstr="y = "+doutoString(parameters[1]);
					}
				}else{
					if(Math.abs(parameters[1])<1e-6){
						outstr="y = "+doutoString(parameters[0])+"*ln(x)";
					}else if(parameters[1]>1e-6){
						outstr="y = "+doutoString(parameters[0])+"*ln(x) + "+doutoString(parameters[1]);
					}else{
					outstr="y = "+doutoString(parameters[0])+"*ln(x) - "+doutoString(Math.abs(parameters[1]));
					}
				}
			}else if(type.equals("ln(y)=a*x+b")){
				if(Math.abs(parameters[0])<1e-6){
					if(Math.abs(parameters[1])<1e-6){
						outstr="y = 1";
					}else{
						outstr="ln(y) = "+doutoString(parameters[1]);
					}
				}else{
					if(Math.abs(parameters[1])<1e-6){
						outstr="ln(y) = "+doutoString(parameters[0])+"*x";
					}else if(parameters[1]>1e-6){
						outstr="ln(y) = "+doutoString(parameters[0])+"*x + "+doutoString(parameters[1]);
					}else{
					outstr="ln(y) = "+doutoString(parameters[0])+"*x - "+doutoString(Math.abs(parameters[1]));
					}
				}
			}else if(type.equals("ln(y)=a*ln(x)+b")){
				if(Math.abs(parameters[0])<1e-6){
					if(Math.abs(parameters[1])<1e-6){
						outstr="y = 1";
					}else{
						outstr="ln(y) = "+doutoString(parameters[1]);
					}
				}else{
					if(Math.abs(parameters[1])<1e-6){
						outstr="ln(y) = "+doutoString(parameters[0])+"*ln(x)";
					}else if(parameters[1]>1e-6){
						outstr="ln(y) = "+doutoString(parameters[0])+"*ln(x) + "+doutoString(parameters[1]);
					}else{
					outstr="ln(y) = "+doutoString(parameters[0])+"*ln(x) - "+doutoString(Math.abs(parameters[1]));
					}
				}
			}
			outR="R^2 = "+doutoString(R);	
		}else{
			fittingfailed();
		}
	}

}
