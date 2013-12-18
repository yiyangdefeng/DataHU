package cn.edu.tsinghua.graphdealer;

public class Tester {
	public static void main(String args[]){
		//double[] x={2,3,4,5};
		//double[] y={22,28,40,64,112};
		//double[] y={7,22,43,70};
		double[] x={1,2,3,4,5};
		double[] y={1,1.945910149,2.944438979,3.761200116,4.521788577};
		int numofpara=3;
		PolynomialFitting lin1=new PolynomialFitting(x,y,numofpara);
		LinearFitting lin2=new LinearFitting(x,y);
		LnLinearFitting lin3=new LnLinearFitting(x,y);
		ExpandlogFitting lin4=new ExpandlogFitting(x,y);
		lin1.Out();
		lin2.Out();
		lin3.type="ln(y)=a*x+b";
		lin3.Out();
		lin3.type="y=a*ln(x)+b";
		lin3.Out();
		lin3.type="ln(y)=a*ln(x)+b";
		lin3.Out();
		lin4.type="y=a*x^b+c";
		lin4.Out();
		lin4.type="y=a*b^x+c";
		lin4.Out();
		}
	}

/*
ln(y) = 0.40816 x + 2.57342  R2 = 0.97639362

y = 48.05848 ln(x) + 7.18409  R2 = 0.69652794

ln(y) = 0.95296 ln(x) + 2.88544  R2 = 0.85984794
*
*
*/