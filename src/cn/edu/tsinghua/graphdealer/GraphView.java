package cn.edu.tsinghua.graphdealer;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.view.View;

@SuppressLint({ "ViewConstructor", "DrawAllocation" })
public class GraphView extends View{
	
	public static final int RECT_SIZE = 10;  

    public static enum Mstyle{
    	Line,scroll
    }
    private Mstyle mstyle=Mstyle.Line;
    private double[] ykey;
    private double[] xkey;
    private double[] fity;
    private double[] fitx;
    FittingTools fit = null;
    double size;
      
    Context context;
    Activity act;
    double ymax,ymin,xmax,xmin,ykeysize,xkeysize,ykeymin,xkeymin;
    float ysize,xsize,yup,ydo,ydown,xleft,xri,xright;
    int height,width;
    String xstr,ystr,reslutstr,Rstr;
    Boolean isshowx=false,isshowy=false,isshowdetail=false,islinear=true;
    Boolean isfitting=false;
    int fittingtype=1,numofnum=6,numofpara;
    
    public GraphView(Context context,double[] x, double[] y,String xstr,String ystr) 
	    {
	        super(context);
	        this.context=context;
	        this.act = (Activity)context;
	        this.setXkey(x);
	        this.setYkey(y);
	        this.xstr=xstr;
	        this.ystr=ystr;
	        act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE); 
	    }

	public GraphView() {
		this(null,null,null,"","");
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.setBackgroundColor(Color.WHITE);
		ymax=getmax(ykey);
		ymin=getmin(ykey);
		xmax=getmax(xkey);
		xmin=getmin(xkey);
		if(Math.abs(ymax-ymin)<1e-6){
			ykeysize=1.0;
		}else{
			int[] ydtoe=doutoE((ymax-ymin)/4,2);
			ykeysize=ydtoe[0]*Math.pow(10,ydtoe[1]-1);
		}
		if(Math.abs(xmax-xmin)<1e-6){
			xkeysize=1;
		}else{
			int[] xdtoe=doutoE((xmax-xmin)/6,2);
			xkeysize=xdtoe[0]*Math.pow(10,xdtoe[1]-1);
			}
		int xfen=(int)Math.round(xmax/xkeysize)-(int)Math.round(xmin/xkeysize)+2;
		int xfenmin=(int)Math.round(xmin/xkeysize)-1;
		int yfen=(int)Math.round(ymax/ykeysize)-(int)Math.round(ymin/ykeysize)+2;
		int yfenmin=(int)Math.round(ymin/ykeysize)-1;
		ykeymin=yfenmin*ykeysize;
		xkeymin=xfenmin*xkeysize;		
		height=getHeight();
		width=getWidth();
		yup=0.05f*height;
		ydown=0.77f*height;
		ysize=(ydown-yup)/yfen;
		xleft=0.18f*width;
		xright=0.90f*width;
		xsize=(xright-xleft)/xfen;
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(2);
		paint.setStyle(Style.STROKE);
		canvas.drawLine(xleft,yup,xright,yup,paint);
		canvas.drawLine(xleft,ydown,xright,ydown,paint);
		canvas.drawLine(xleft,yup,xleft,ydown,paint);
		canvas.drawLine(xright,yup,xright,ydown,paint);
		for(int i=0;i<yfen+1;i++){
			if(i==0 || i==yfen){
			}else{
				canvas.drawLine(xleft,yup+ysize*i,xleft+10,yup+ysize*i,paint);
				if(isshowy){
					for(int j=1;j<((xright-xleft)/10);j++){
						canvas.drawLine((xleft+10*j),yup+ysize*i,(xleft+10*j+3),yup+ysize*i,paint);
						}
					}
			}
			if(i==0){
				
			}else{
				if(isshowdetail){
					paint.setColor(Color.LTGRAY);
					paint.setStrokeWidth(1);
					for(int k=1;k<10;k++){
						canvas.drawLine(xleft,yup+(int)(ysize*0.1*k)+ysize*(i-1),xright,yup+(int)(ysize*0.1*k)+ysize*(i-1),paint);
						}
					paint.setStrokeWidth(2);
					paint.setColor(Color.GRAY);
				}
			}
			drawstring(doutoS(ykeysize,(i+yfenmin)), (int)(xleft*0.7), (int)(ydown-ysize*i+10), canvas);
		}
		
		drawstring(xstr, (int)(width*0.54), (int)(0.85*height), canvas);
		
		for(int i=0;i<xfen+1;i++){
			if(i==0 || i==xfen){
			}else{
				canvas.drawLine(xleft+xsize*i,ydown,xleft+xsize*i,ydown-10,paint);
				if(isshowx){
					for(int j=1;j<((ydown-yup)/10);j++){
						canvas.drawLine(xleft+xsize*i,(ydown-10*j),xleft+xsize*i,(ydown-10*j-2),paint);
						}
					}
			}
			if(i == 0){
				
			}else{
				if(isshowdetail){
					paint.setColor(Color.LTGRAY);
					paint.setStrokeWidth(1);
					for(int k=1;k<10;k++){
						canvas.drawLine(xleft+(int)(xsize*0.1*k)+xsize*(i-1),ydown,xleft+(int)(xsize*0.1*k)+xsize*(i-1),yup,paint);
						}
					paint.setStrokeWidth(2);
					paint.setColor(Color.GRAY);
					}
			}				
			drawstring(doutoS(xkeysize,(i+xfenmin)), (int)(xleft+xsize*i),(int)(height*0.81), canvas);
		}
		canvas.rotate(-90.0f);
		canvas.translate(-height,0.0f);
		drawstring(ystr,(int)(0.6*height),(int)(xleft*0.4), canvas);
		canvas.rotate(90.0f);
		canvas.translate(0.0f,-height);
     
	        

		Point[] ps=getpoints(xkey, ykey);		
		paint.setColor(Color.RED);
		paint.setStyle(Style.FILL);
		for (int i=0; i<ps.length; i++){
			canvas.drawRect(pointToRect(ps[i]),paint);
			}
		
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(1);
		
		if(isfitting){
			setfitting();
			if(fit.resulte){
				Point[] fitps=getpoints(fitx, fity);
				drawline(fitps,canvas,paint);
			}
				drawstring(reslutstr, (int)(width*0.5), (int)(0.9*height), canvas);
				drawstring(Rstr, (int)(width*0.5), (int)(0.95*height), canvas);
			
		}else if(islinear){
			if(mstyle==Mstyle.scroll){
				drawline(canvas, paint);
			}else{
				drawline(ps, canvas, paint);
			}
		}else{
			
		}
		}
	private double getmax(double[] a) {
		double max;
		max=a[0];
		for(int i=0;i<a.length;i++){
			if(a[i]>max){
				max=a[i];
			}			
		}
		return max;
	}

	private double getmin(double[] a) {
		double min;
		min=a[0];
		for(int i=0;i<a.length;i++){
			if(a[i]<min){
				min=a[i];
			}			
		}
		return min;
	}

	 private void drawstring(String text,int x,int y,Canvas canvas)   //锟斤拷锟斤拷锟斤拷锟�
	    {
	    	Paint p = new Paint();
	    	p.setAlpha(0x0000ff);   
	    	p.setTextSize(20);//  
	    	String familyName = "TimesNewRoman.ttf";   
	    	Typeface font = Typeface.create(familyName,Typeface.ITALIC);   
	    	p.setTypeface(font);   
	    	p.setTextAlign(Paint.Align.CENTER);     
	    	canvas.drawText(text, x, y, p);
	    }
	 private Point[] getpoints(double[] px,double[] py){
	    	Point[] points=new Point[px.length];
	    	for(int i=0;i<px.length;i++){
	    		int xwidth=(int)(xsize*(px[i]-xkeymin)/xkeysize+xleft);
	    		int yheight=(int)(ydown-ysize*(py[i]-ykeymin)/ykeysize);
	    		points[i]=new Point(xwidth,yheight);
	    	}
	    	return points;
	    }
	 private void drawline(Canvas canvas,Paint paint){
	    	double[] morex=null,morey=null;
	    	int n=xkey.length;
	    	double[] a=new double[n-1];
	    	double[] b=new double[n-1];
	    	double[] c=new double[n];
	    	double[] d=new double[n-1];
	    	double[] dx=new double[n-1];
	    	double[] dy=new double[n-1];
	    	double[][] A=new double[n][2*n];
	    	double[] B=new double[n];
	    	for(int i=0;i<a.length;i++){
	    		a[i]=ykey[i];
	    		dx[i]=xkey[i+1]-xkey[i];
	    		dy[i]=ykey[i+1]-ykey[i];
	    		
	    	}
	    	for(int i=1;i<B.length-1;i++){
	    		A[i][i-1]=dx[i-1];
	    		A[i][i]=2*(dx[i-1]+dx[i]);
	    		A[i][i+1]=dx[i];
	    		B[i]=3*(dy[i]/dx[i]-dy[i-1]/dx[i-1]);
	    	}
	    	B[0]=0;B[n-1]=0;
	    	A[0][0]=1;A[n-1][n-1]=1;
	    	
	    	for(int i=0;i<n;i++){
	    		A[i][i+n]=1;
	    	}
	    	for(int o=0;o<n;o++){
				if(A[o][o]!=1){
					double bs = A[o][o];
					for(int p=o;p<n*2;p++){
						A[o][p]/=bs;
						}
					}
				for(int q=0;q<n;q++){
					if(q!=o){
						double bs = A[q][o];
						for(int p=0;p<n*2;p++){
							A[q][p]-=bs*A[o][p];
							}
						}
					else {
						continue;
						}
					}
				}
			for(int r=0;r<n;r++){
				double sum3=0;
				for(int s=0;s<n;s++){
					sum3+=A[r][s+n]*B[s];
					}
				c[r]=sum3;
				}
			
			
	    	for(int i=0;i<a.length;i++){
	    		d[i]=(c[i+1]-c[i])/(3*dx[i]);
	    		b[i]=dy[i]/dx[i]-dx[i]*(2*c[i]+c[i+1])/3;
	    	}
	    	morex=new double[1001];
	    	morey=new double[1001]; 
	    	double xx=xmax-xmin;
	    	double xxsize=xx/1000;
	    	for(int i=0;i<morex.length;i++){
	    		morex[i]=xmin+i*xxsize;
	    	}
	    	int j=0;
	    	for(int i=0;i<morex.length;i++){
	    		if(j==n-1){
	    			break;
	    		}

	    		if((xkey[j+1]-morex[i])>xxsize){
	    			morey[i]=a[j]+b[j]*(morex[i]-xkey[j])+c[j]*Math.pow(morex[i]-xkey[j],2)+d[j]*Math.pow(morex[i]-xkey[j],3);
	    		}else{
	    			morey[i]=a[j]+b[j]*(morex[i]-xkey[j])+c[j]*Math.pow(morex[i]-xkey[j],2)+d[j]*Math.pow(morex[i]-xkey[j],3);
	    			j=j+1;
	    			}
	    	}
	    	Point[] morexy=getpoints(morex,morey);
	    	drawline(morexy,canvas, paint);	    	   
	    }
	 private void drawline(Point[] ps,Canvas canvas,Paint paint) 
	    {
	    	Point startp=new Point();
	    	Point endp=new Point();
	    	for(int i=0;i<ps.length-1;i++)
	    	{	
	    	startp=ps[i];
	    	endp=ps[i+1];
	    	canvas.drawLine(startp.x,startp.y,endp.x,endp.y, paint);
	    	}
	    }
	 private RectF pointToRect(Point p) 
	    {  
	        return new RectF(p.x -RECT_SIZE/2, p.y - RECT_SIZE/2,p.x + RECT_SIZE/2, p.y + RECT_SIZE/2);
	    }
	 
	 public void setfitting(){
		 switch(fittingtype){
		 case 1:
			 fit=new LinearFitting(xkey,ykey);
			 break;
		 case 2:
			 fit=new PolynomialFitting(xkey,ykey,numofpara);
			 break;
		 case 3:
			 fit=new LnLinearFitting(xkey,ykey);
			 fit.type="y=a*ln(x)+b";
			 break;
		 case 4:
			 fit=new LnLinearFitting(xkey,ykey);
			 fit.type="ln(y)=a*x+b";
			 break;
		 case 5:
			 fit=new LnLinearFitting(xkey,ykey);
			 fit.type="ln(y)=a*ln(x)+b";
			 break;
		 case 6:
			 fit=new ExpandlogFitting(xkey,ykey);
			 fit.type="y=a*b^x+c";
			 break;
		 case 7:
			 fit=new ExpandlogFitting(xkey,ykey);
			 fit.type="y=a*x^b+c";
			 break;
			 }
			fit.numofnum=numofnum;
			fit.fitting();
			setReslutstr(fit.outstr);
			setRstr(fit.outR);
	        fitx=new double[2000];
	        fity=new double[2000];
	        size=(xmax-xmin)/2000;
			for(int i=0;i<fitx.length;i++){
				double fitxx=size*i+xmin;
				double fityy=fit.function(fitxx);
				if(fityy>ykeymin && fityy<(ymax+ykeysize)){
					fitx[i]=fitxx;
					fity[i]=fityy;
				}else if(fityy<=ykeymin){
					fitx[i]=fitxx;
					fity[i]=ykeymin;
					
				}else{
					fitx[i]=fitxx;
					fity[i]=(ymax+ykeysize);
				}
			}
			}
	 

	 public int[]  doutoE(double dou, int numofnum){
			double num=dou;
			double dounum;
			int[] result=new int[2];
			int E=0;
			int a=(int)(num);
			if(Math.abs(num)<1e-12){
				result[0]=0;
				result[1]=0;
				return result;
			}else{
				a=a/10;
				while(a>0){
					E=E+1;
					a=a/10;
					}
				if(E==0){
					a=(int)(num);
					while(a==0){
						num=num*10.0;
						a=(int)(num);
						E=E-1;
						}
					}
			}
			dounum=(dou/Math.pow(10,E));
			result[0]=(int)Math.round(dounum*Math.pow(10,(numofnum-1)));
			result[1]=E;
			return result;
	 }
	 public String doutoS(double dou,int i){
		 int[] dtoe=doutoE(dou,2);
		 String str;
		 if(i==0){
			 str="0.0";
		}else{
			switch(dtoe[1]){
			case 3:
				str=Integer.toString(dtoe[0]*i)+"00";
				break;
			case 2:
				str=Integer.toString(dtoe[0]*i)+"0";
				break;
			case 1:
				str=Integer.toString(dtoe[0]*i);
				break;
			case 0:
				str=dtoe[0]*i/10+"."+Math.abs(dtoe[0]*i%10);
				break;
			case -1:
				if(dtoe[0]*i>0){
					str=dtoe[0]*i/100+"."+(dtoe[0]*i%100)/10+(dtoe[0]*i%100)%10;
				}else{
					str="-"+(-dtoe[0]*i)/100+"."+((-dtoe[0]*i)%100)/10+((-dtoe[0]*i)%100)%10;
				}
				break;
			case -2:
				if(dtoe[0]*i>0){
					str=dtoe[0]*i/1000+"."+(dtoe[0]*i%1000)/100+((dtoe[0]*i%1000)%100)/10+((dtoe[0]*i%1000)%100)%10;
				}else{
					str="-"+(-dtoe[0]*i)/1000+"."+((-dtoe[0]*i)%1000)/100+(((-dtoe[0]*i)%1000)%100)/10+(((-dtoe[0]*i)%1000)%100)%10;
				}
				break;
			default:
				str=dtoe[0]*i/10+"."+Math.abs(dtoe[0]*i%10)+"E"+dtoe[1];
				break;
				}
			}
		 return str;
	 }
     //getset鏂规硶



	public void setYkey(double[] key) {
		this.ykey=new double[key.length];
		for(int i=0;i<key.length;i++){
			this.ykey[i] = key[i];
			}
	}


	public void setXkey(double[] key) {
		this.xkey=new double[key.length];
		for(int i=0;i<key.length;i++){
			this.xkey[i] = key[i];
			}
	}	

	public Mstyle getMstyle() {
		return mstyle;
	}

	public void setMstyle(Mstyle mstyle) {
		this.mstyle = mstyle;
	}

	public String getXstr() {
		return xstr;
	}

	public void setXstr(String xstr) {
		this.xstr = xstr;
	}

	public String getYstr() {
		return ystr;
	}

	public void setYstr(String ystr) {
		this.ystr = ystr;
	}

	public String getReslutstr() {
		return reslutstr;
	}

	public void setReslutstr(String reslutstr) {
		this.reslutstr = reslutstr;
	}

	public String getRstr() {
		return Rstr;
	}

	public void setRstr(String rstr) {
		Rstr = rstr;
	}

	public Boolean getIsshowx() {
		return isshowx;
	}

	public void setIsshowx(Boolean isshowx) {
		this.isshowx = isshowx;
	}

	public Boolean getIsshowy() {
		return isshowy;
	}

	public void setIsshowy(Boolean isshowy) {
		this.isshowy = isshowy;
	}

	public Boolean getIsshowdetail() {
		return isshowdetail;
	}

	public void setIsshowdetail(Boolean isshowdetail) {
		this.isshowdetail = isshowdetail;
	}

	public Boolean getIslinear() {
		return islinear;
	}

	public void setIslinear(Boolean islinear) {
		this.islinear = islinear;
	}

	public Boolean getIsfitting() {
		return isfitting;
	}

	public void setIsfitting(Boolean isfitting) {
		this.isfitting = isfitting;
	}

	public int getFittingtype() {
		return fittingtype;
	}

	public void setFittingtype(int fittingtype) {
		this.fittingtype = fittingtype;
	}

	public int getNumofnum() {
		return numofnum;
	}

	public void setNumofnum(int numofnum) {
		this.numofnum = numofnum;
	}

	public int getNumofpara() {
		return numofpara;
	}

	public void setNumofpara(int numofpara) {
		this.numofpara = numofpara;
	}


	
}
