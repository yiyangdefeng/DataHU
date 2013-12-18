package cn.edu.tsinghua.graphdealer;


public class LinearFitting extends PolynomialFitting{
	
	public LinearFitting(double[] inx, double[] iny) {
			super(inx,iny,2);
	}
	@Override
	public void parameter(){
		//System.out.println("parameter");
		if(x.length>=numofpara){
		double xave=average(x);
		double yave=average(y);
		double sxx=0,sxy=0;
		for(int i=0;i<x.length;i++){
			sxx+=Math.pow(x[i]-xave,2);
			sxy+=(x[i]-xave)*(y[i]-yave);
		}
		parameters[0]=sxy/sxx;
		parameters[1]=yave-parameters[0]*xave;
		}else{
			toofew=true;
			resulte=false;
		}	
		//System.out.println("parameter Successful");
		}
	}