function sortArray(array,attr)
{
	var len=array.length;
	for(var i=0;i<len;i++)
	{
		for(var j=0;j<len-i-1;j++)
		{
			if(array[j][attr]*1.0>array[j+1][attr]*1.0)
			{
				var temp=array[j];
				array[j]=array[j+1];
				array[j+1]=temp;
			}
		}
	}
	return array;
}

function sortDesc(array,attr)
{
	var len=array.length;
	for(var i=0;i<len;i++)
	{
		for(var j=0;j<len-i-1;j++)
		{
			if(array[j][attr]*1.0<array[j+1][attr]*1.0)
			{
				var temp=array[j];
				array[j]=array[j+1];
				array[j+1]=temp;
			}
		}
	}
	return array;
}

function sortTerms(terms)
{
	var temp=[];
	for(var key in terms)
	{
		var value=terms[key];
		var ob={t:key,val:value};
		temp.push(ob);
	}
	return sortDesc(temp,"val");
	
}

function pearsonR(x,y)
{
	var xBar=0;
	var yBar=0;
	for(var i=0;i<x.length;i++)
	{
		xBar+=x[i]*1.0;
		yBar+=y[i]*1.0;
	}
	xBar=xBar*1.0/x.length;
	yBar=yBar*1.0/y.length;
	
	var top=0;
	var bX=0; var bY=0;
	for(var i=0;i<x.length;i++)
	{
		var temp=(x[i]-xBar)*(y[i]-yBar);
		top+=temp;
		var tempX=(x[i]-xBar)*(x[i]-xBar);
		bX+=tempX;
		var tempY=(y[i]-yBar)*(y[i]-yBar);
		bY+=tempY;
	}
	
	var r=top/Math.sqrt(bX*bY);
	return r;
}