package cn.edu.tsinghua.graphdealer;


public class PolynomialFitting extends FittingTools{

	public PolynomialFitting(double[] inx,double[] iny,int numofpara){
		x=inx;
		y=iny;
		yfit=new double[x.length];
		this.numofpara=numofpara;
		parameters=new double[numofpara];
	}
	public PolynomialFitting(double[] inx,double[] iny){
		this(inx,iny,2);
	}
	
	@Override
	public void parameter(){
		//System.out.println("parameter");
		if(x.length>=numofpara){
			frequencies=frequencies+1;
			double[][] A=new double[numofpara][2*numofpara];
			double[] B=new double[numofpara];
			for(int i=0;i<numofpara;i++){
				for(int j=0;j<numofpara;j++){
					double sum1=0;
					for(int k=0;k<x.length;k++){
						sum1+=Math.pow(x[k],numofpara-1+i-j);
						}
					A[i][j]=sum1;
					if(i==j){
						A[i][j+numofpara]=1;
					}else{
						A[i][j+numofpara]=0;
						}
					}
				double sum2=0;
				for(int k=0;k<x.length;k++){
					sum2+=y[k]*Math.pow(x[k],i);
					}
				B[i]=sum2;
				}
				
		for(int o=0;o<numofpara;o++){
			if(A[o][o]!=1){
				double bs = A[o][o];
				for(int p=o;p<numofpara*2;p++){
					A[o][p]/=bs;
					}
				}
			for(int q=0;q<numofpara;q++){
				if(q!=o){
					double bs = A[q][o];
					for(int p=0;p<numofpara*2;p++){
						A[q][p]-=bs*A[o][p];
						}
					}
				else {
					continue;
					}
				}
			}
		for(int m=0;m<numofpara;m++){
			double sum3=0;
			for(int n=0;n<numofpara;n++){
				sum3+=A[m][n+numofpara]*B[n];
				}
			parameters[m]=sum3;
			}
		}else{
			toofew=true;
			resulte=false;
		}
		
		//System.out.println("parameter Successful");
		}
	
	@Override
	public double function(double x) {
	//	System.out.println("function");
		int n=numofpara-1;
		double y=0;
		while(n>=0){
			y+=parameters[numofpara-1-n]*Math.pow(x,n);
			n-=1;
		}
	//	System.out.println("function successful");
		return y;
		}
	public void fitting(){
		resulte=false;
		Mins();
		if(resulte){
			outstr="y =";
			for(int i=0;i<(numofpara-2);i++){
				if(Math.abs(parameters[i])<1e-6){
					
				}else{
					if(parameters[i+1]>1e-6){
						outstr=outstr+" "+doutoString(parameters[i])+"*x^"+(numofpara-1-i)+" +";
						}else{
							outstr=outstr+" "+doutoString(parameters[i])+"*x^"+(numofpara-1-i);
							}
					}
				}
			if(Math.abs(parameters[numofpara-2])<1e-6){
				if(Math.abs(parameters[numofpara-1])<1e-6){
					if(outstr.equals("y =")){
						outstr="y = 0";
					}
				}else if(parameters[numofpara-1]<-1e-6){
					outstr=outstr+" - "+doutoString(Math.abs(parameters[numofpara-1]));
				}else{
					outstr=outstr+" + "+doutoString(parameters[numofpara-1]);
				}
			}else{
				if(Math.abs(parameters[numofpara-1])<1e-6){
					outstr=outstr+" "+doutoString(parameters[numofpara-2])+"*x";
				}else if(parameters[numofpara-1]<-1e-6){
					outstr=outstr+" "+doutoString(parameters[numofpara-2])+"*x - "+doutoString(Math.abs(parameters[numofpara-1]));
				}else{
					outstr=outstr+" "+doutoString(parameters[numofpara-2])+"*x + "+doutoString(parameters[numofpara-1]);
					}
			}
			outR="R^2 = "+doutoString(R);
		}else{
			fittingfailed();
		}
	}
}
