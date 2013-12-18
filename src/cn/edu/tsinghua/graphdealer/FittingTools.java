package cn.edu.tsinghua.graphdealer;

import java.text.NumberFormat;


public abstract class FittingTools {
	String outstr,outR;
	double[] x,y,yfit;
	int numofpara;
	int numofnum;
	double[] parameters;
	int frequencies=0;
	boolean resulte=false;
	boolean toofew=false;
	double xaverage,yaverage;
	double R=0;
	double s=0;
	double u=0;
	double q=0;
	public String type;
	
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
			if(toofew || frequencies>2000 || resulte){
				break;
			}
			suqcalculator();
			if(Math.abs(s-olds)<Math.pow(10,-12)){
				resulte=true;
				}
		}
		}
	public void fitting(){
		resulte=false;
		Mins();
		if(resulte){
			outstr="Successful!.";
		}else{
			fittingfailed();
		}
	}
	public String doutoString(double num){
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(numofnum);
		return nf.format(num);
	}
	public void fittingfailed(){
		outstr="Not successed!";
		if(toofew){
			outR="The Data is too few to Fitting.";
		}else if(frequencies>200){
			outR="No convergence.";
		}else{
			outR="Others.";
		}
	}
	}


