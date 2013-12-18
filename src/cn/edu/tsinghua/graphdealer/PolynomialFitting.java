package cn.edu.tsinghua.graphdealer;

public class PolynomialFitting extends FittingTools{

	public PolynomialFitting(double[] inx,double[] iny,int num){
		x=inx;
		y=iny;
		yfit=new double[x.length];
		numofpara=num;
		parameters=new double[num];
	}
	public PolynomialFitting(double[] inx,double[] iny){
		this(inx,iny,2);
	}
	
	@Override
	public void parameter(){
		//System.out.println("parameter");
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
	public void Out(){
		resulte=false;
		Mins();
		if(resulte){
			System.out.println();
			System.out.printf("y = ");
			for(int i=0;i<(numofpara-2);i++){
				System.out.printf("%.5f x"+(numofpara-1-i)+" + ",parameters[i]);
			}
			System.out.printf("%.5f x + %.5f ",parameters[numofpara-2],parameters[numofpara-1]);
			System.out.printf("  R2 = %.8f",R);
			System.out.println();
		}else{
			System.out.println("Not successful!\n1.The Data is too few to Fitting.\n2.No convergence.\n3.Others.");
		}
	}
}
