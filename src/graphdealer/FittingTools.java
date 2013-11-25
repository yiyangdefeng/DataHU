package graphdealer;

public abstract class FittingTools {
	double[] x,y,yfit;
	int numofpara;
	double[] parameters;
	int frequencies=0;
	boolean resulte=false;
	double xaverage,yaverage;
	double R=0;
	double s=0;
	double u=0;
	double q=0;
	
	public double average(double[] a){
		double sum=0;
		for(int i=0;i<a.length;i++){
			sum+=a[i];
		}
		return (sum/a.length);
	}
	public abstract void parameter();
	public abstract double function(double x);
	public void suqcalculator(){
		s=0;
		u=0;
		q=0;
		R=0;
		yaverage=average(y);
		for(int i=0;i<x.length;i++){
			yfit[i]=function(x[i]);
			s+=Math.pow((yfit[i]-y[i]),2);
			u+=Math.pow((yfit[i]-yaverage),2);
			q+=Math.pow((y[i]-yaverage),2);
		}
		R=u/q;
	}
	public void Mins(){
		frequencies=0;
		s=0;
		while(true){
			double olds=s;
			parameter();
			if(x.length<numofpara || frequencies>200 || resulte){
				break;
			}
			suqcalculator();
			if(Math.abs(s-olds)<Math.pow(10,-12)){
				resulte=true;
				}
		}
		}
	public void Out(){
		resulte=false;
		Mins();
		if(resulte){
			System.out.println("Successful!.");
		}else{
			System.out.println("Not successful!\n1.The Data is too few to Fitting.\n2.No convergence.\n3.Others.");
		}
	}
	}


