/**
 * @author betty352008@yeah.net(Zhao Bingqing)
 * javascript for page 2 of the analysis
 */

var demands=[];
var chosenTopics=[];
/* Vue.use(Lazyload, {
    //ms
    time: 200,
    fade: true,
    nohori: false,
    container:$("#container")
}); */
var app1=new Vue({
	el:"#app1",
	data:
	{
		loading:false,
		a1:true,
		show:true,
		topics:null,
		chosenT:chosenTopics,
		demands:demands,
		dDefault:null,
		allD:[],
		addDemand:"",
		map:null,
		map1:null,
		imgBase1:"https://g-search",
		imgBase2:".alicdn.com/img/",
	},
	methods:{
		getAllKeys:function(arr)
		{
			var re="";
			for(var key in arr)re=re+key+",";
			return re.substring(0,re.length-1);
		},
		getAllTerms:function(arr)
		{
			var re="";
			for(var i=0;i<arr.length;i++)
			{
				re=re+arr[i].t+",";
			}
			return re.substring(0,re.length-1);
		}		
	}
});

var app2=new Vue({
	el:"#app2",
	data:
	{
		loading:false,
		a2:false,
		show:false,
		showPro:false,
		products:null,
		userProduct:null,
		sortMode:true,
		pearsonR:null,
		sortType:"geo",
		imgBase1:"https://g-search",
		imgBase2:".alicdn.com/img/",
	},
	methods:{
		getAllKeys:function(arr)
		{
			var re="";
			for(var key in arr)re=re+key+",";
			return re.substring(0,re.length-1);
		}		
	}
});

function getMultiScore()
{
	var sw=getParam("q");
	var IDs=getParam("select");	
	var updated=getParam("up");
	$.ajax({
	    type:"POST",
	    url:"../multi",
	    data:{topicIDs:IDs,sw:sw},
	    datatype: "text",
	    beforeSend:function(){app2.loading=true;},           
	    success:function(data,status){
	    console.log(data.p);
	    app2.products=data.p;
	    mapping();
	    showRadar();
	    chart.write("chartdiv");
	    prepareUserProduct();
	    app2.show=true;
	    app2.showPro=true;
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown){
	    }         
	 });
	
	setTimeout(function(){$("img.lazy").lazyload({effect : "fadeIn", container: $("#container")});},2000);
}

function prepareUserProduct()
{
	var model=app2.products[0].multiScore;
	app2.userProduct=[];
	for(var k=0;k<model.length;k++)
	{
		var dimension=model[k].topicID;
		var prepare={topicID:dimension,good:0,bad:0};
		app2.userProduct.push(prepare);
	}
}

function getParam(key) 
{ 
	var reg = new RegExp("(^|&)"+key+"=([^&]*)(&|$)");
    var result = window.location.search.substr(1).match(reg);
    return result?decodeURIComponent(result[2]):null;
	//var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	//var r = window.location.search.substr(1).match(reg); 
	//if (r != null) return unescape(r[2]); return null; 
} 

$(function(){
var IDs=getParam("select");	
var updated=getParam("up");
//alert("topicIDs:"+IDs+";updated:"+updated);

$.ajax({
    type:"POST",
    url:"../topics",
    data:{topicIDs:IDs},
    datatype: "json",
    beforeSend:function(){app1.loading=true;},           
    success:function(data,status){
    	app1.loading=false;
    	app1.show=true;
    	for(var i=0;i<data.data.topics.length;i++)
    	{data.data.topics[i].map=false;data.data.topics[i].terms=sortTerms(data.data.topics[i].terms);}
		app1.topics=data.data.topics;
    },
    error: function(XMLHttpRequest, textStatus, errorThrown){
	alert(XMLHttpRequest.status);
	alert(XMLHttpRequest.readyState);
	alert(textStatus);
    	//console.log("error");
    }         
 });
 
$.ajax({
    type:"POST",
    url:"../demands",
    data:{method:"byTopics",id:IDs},
    datatype: "json",
    beforeSend:function(){},           
    success:function(data,status){
    	app1.dDefault=data;
    	console.log(data);
    	for(var i=0;i<data.length;i++)
    	{
    		var d1=data[i];
    		for(var key in d1.terms)
    		{
    			if(app1.allD.indexOf(key)==-1)
    				app1.allD.push(key);
    		}
    	}
    },
    error: function(XMLHttpRequest, textStatus, errorThrown){
	alert(XMLHttpRequest.status);
	alert(XMLHttpRequest.readyState);
	alert(textStatus);
    	//console.log("error");
    }         
 });
 
});


function nextStep()
{
	var tID="";
	var dName="";
	var map="";
	for(var i=0;i<app1.chosenT.length;i++)
		tID=tID+app1.chosenT[i]+",";
	for(var i=0;i<app1.demands.length;i++)
		dName=dName+app1.demands[i].name+",";
	for(var i=0;i<app1.map1.length;i++)
	{
		for(var j=0;j<app1.map1[i].length;j++)
		{
			map=map+app1.map1[i][j];
			if(j<app1.map1[i].length-1)
				map=map+",";
			else map=map+";";
		}
	}
	updateTD(tID,dName,map);
	app1.a1=false;
	app2.a2=true;
	getMultiScore();
}

function newDemand()
{
	var d={"name":"","del":false};
	demands.push(d);
}

function delDemand(name1)
{
	for(var i=0;i<demands.length;i++)
	{
		if(demands[i].name==name1)
			demands.splice(i,1);
	}
}

function updateChosenT()
{
	//("选中项已更新");
	app1.chosenT.splice(0,app1.chosenT.length);
	for(var i=0;i<app1.topics.length;i++)
		if(app1.topics[i].map)
			app1.chosenT.push(app1.topics[i].topicID)
	//$("#chosenT").val("修改");
}

function refreshMatrix()
{
	var tID="";
	var dName="";
	for(var i=0;i<app1.chosenT.length;i++)
		tID=tID+app1.chosenT[i]+",";
	for(var i=0;i<app1.demands.length;i++)
		dName=dName+app1.demands[i].name+",";
	getAvgWeight(tID,dName);
}


function getAvgWeight(topicID,demandName)
{
	$.ajax({
	    type:"POST",
	    url:"../demands",
	    data:{method:"avgW",topicIDs:topicID,demandNames:demandName},
	    datatype: "text",
	    beforeSend:function(){},           
	    success:function(data,status){
	    	console.log(data.avgWeight);
	    	app1.map1=data.avgWeight;
	    	app1.map=data.avgWeight;
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown){
		alert(XMLHttpRequest.status);
		alert(XMLHttpRequest.readyState);
		alert(textStatus);
	    	//console.log("error");
	    }         
	 });
}

function updateTD(topicID,demandName,map)
{
	$.ajax({
	    type:"POST",
	    url:"../demands",
	    data:{method:"update",topicIDs:topicID,demandNames:demandName,map:map},
	    datatype: "text",
	    beforeSend:function(){},           
	    success:function(data,status){
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown){
	    }         
	 });
}

function mapping()
{
	var mapT=app1.chosenT.length;
	var mapD=app1.demands.length;
	for(var i=0;i<app2.products.length;i++)
	{
		var tempD=[];
		for(var j=0;j<app1.demands.length;j++)
		{
			var dtemp={topicID:app1.demands[j].name,good:0,bad:0};
			tempD.push(dtemp);
		}
		
		var ms=app2.products[i].multiScore;
		for(var j=0;j<ms.length;j++)
		{
			if(inChosenT(ms[j].topicID))
			{
				console.log("inChosenT:"+ms[j].topicID);
				var rowIndex=getChosenIndex(ms[j].topicID);
				var mapRow=app1.map1[rowIndex*1];
				for(var k=0;k<app1.demands.length;k++)
				{
					tempD[k].good=tempD[k].good*1+ms[j].good*1*mapRow[k];
					tempD[k].bad=tempD[k].bad*1+ms[j].bad*1*mapRow[k];
				}
				app2.products[i].multiScore.splice(j,1);
				j=j-1;
			}
			else
			{
				for(var n=0;n<app1.topics.length;n++)
					if(ms[j].topicID==app1.topics[n].topicID)
						ms[j].topicID=app1.topics[n].name;
			}
		}
		console.log("tempD.length:"+tempD.length);
		for(var k=0;k<tempD.length;k++)
		{
			console.log("push tempD:"+k);
			console.log(tempD[k]);
			app2.products[i].multiScore.push(tempD[k]);
		}
	}
}

function inChosenT(id)
{
	var inIt=false;
	for(var n=0;n<app1.chosenT.length;n++)
		if(id==app1.chosenT[n]) {inIt=true;}
	return inIt;
}

function getChosenIndex(id)
{
	for(var n=0;n<app1.chosenT.length;n++)
		if(id==app1.chosenT[n]) return n;
}

var chart = new AmCharts.AmRadarChart();

function showRadar()
{
	var i=0;
	var dataSource=app2.products[i*1].multiScore;
    chart.dataProvider = dataSource;
    chart.categoryField = "topicID"
   	chart.startDuration = 1;
    
    var valueAxis = new AmCharts.ValueAxis();
    valueAxis.axisAlpha = 0.55; //轴透明度
    valueAxis.minimum = 0;
    valueAxis.maximum=1;
    valueAxis.dashLength = 0; //0:实线,1:相隔1像素虚线,2:相差2像素虚线 , ....以此类推
    valueAxis.axisTitleOffset = 10;  //每列列名偏移量
    valueAxis.gridCount = 5;  //网状圈数
    valueAxis.gridAlpha = 0.55;  //圈圈透明度0:透明,1:不透明,默认0.2
    valueAxis.radarCategoriesEnabled = true; //是否显示轴的列名,默认true
    chart.addValueAxis(valueAxis);
    
    var graph1 = new AmCharts.AmGraph();
    graph1.valueField = "good";
    graph1.title = "积极情绪"; //该Graph标题
    graph1.lineThickness = 2; //线条粗细度
    graph1.bullet = "round"; //点类型,气泡
    graph1.balloonText = "[[value]]";
    
    var graph2 = new AmCharts.AmGraph();
    graph2.valueField = "bad";
    graph2.title = "消极情感"; //该Graph标题
    graph2.dashLength = 2; //虚线间隔
    graph2.bullet = "diamond"; //点类型,气泡
    graph2.balloonText = "[[value]]";
    chart.addGraph(graph1);
    chart.addGraph(graph2);
    var legend = new AmCharts.AmLegend();
    legend.position = "right";
    chart.addLegend(legend); 
}

function updateRadar(i)
{
	if($("#chartdiv").html()==""||$("#chartdiv").html()==null) chart.write("chartdiv");
	var dataSource=app2.products[i*1].multiScore;
    chart.dataProvider = dataSource;
    chart.validateData();
}

function addDemand()
{
	var key=app1.addDemand;
	if(app1.allD.indexOf(key)==-1)
		app1.allD.push(key);
	alert("添加成功");
	app1.addDemand="";
}

function changeMode()
{
	if(app2.sortMode) 
	{
		app2.pearsonR=[];
		var ps=app2.products;
		var len=ps[0].multiScore.length;
		var sales=new Array();
		var price=new Array();
		for(var i=0;i<ps.length;i++)
		{
			sales[i]=ps[i].sales*1.0;
			price[i]=ps[i].price*1.0;
		}
		
		for(var i=0;i<len;i++)
		{
			var nameT=ps[0].multiScore[i].topicID;
			var g=new Array();
			var b=new Array();
			for(var j=0;j<ps.length;j++)
			{
				g[j]=ps[j].multiScore[i].good*1.0;
				b[j]=ps[j].multiScore[i].bad*1.0;
			}
			var gR=pearsonR(g,sales);
			var bR=pearsonR(b,sales);
			app2.pearsonR.push({name:nameT,b:bR,g:gR});
		}
		
		var pR=pearsonR(price,sales);
		app2.pearsonR.push({name:"价格",b:pR,g:pR});
			
		app2.sortMode=false;
	}
	else app2.sortMode=true;
}

function sortByDistance()
{
	var data=app2.products;
	var userP=app2.userProduct;
	if(app2.sortType=="geo")
	{
		for(var i=0;i<data.length;i++)
		{
			var p=data[i];
			var pM=data[i].multiScore;
			var distance=0;
			for(var j=0;j<userP.length;j++)
			{
				var gD=userP[j].good*1.0-pM[j].good*1.0;
				var bD=userP[j].bad*1.0-pM[j].bad*1.0;
				var temp=Math.sqrt(gD*gD+bD*bD);
				distance=distance+temp;
			}
			p.dis=distance;
		}
	}
	else //cosine
	{
		for(var i=0;i<data.length;i++)
		{
			var p=data[i];
			var pM=data[i].multiScore;
			var distance=0;
			var x2=0;
			var y2=0;
			for(var j=0;j<userP.length;j++)
			{
				var gD=userP[j].good*1.0*pM[j].good;
				var bD=userP[j].bad*1.0*pM[j].bad;
				var uP_x2=userP[j].good*userP[j].good+userP[j].bad*userP[j].bad;
				var p_y2=pM[j].good*pM[j].good+pM[j].bad*pM[j].bad;
				distance=distance+temp;
				x2=x2+uP_x2;
				y2=y2+p_y2;
			}
			x2=Math.sqrt(x2);
			y2=Math.sqrt(y2);
			distance=distance/(x2+y2);
			p.dis=distance;
		}
	}
	app2.showPro=false;
	sortArray(app2.products,"dis");
	app2.showPro=true;
	$("img.lazy").lazyload({effect : "fadeIn", container: $("#container")});
	//sortArray
}


function stepWise()
{	
	var d1=getUsableData();
	$.ajax({
	    type:"POST",
	    url:"../stepwise",
	    data:{d:d1},
	    datatype: "json",
	    beforeSend:function(){},           
	    success:function(data,status){
	    	console.log("stepwise regression result:");
	    	console.log(data);
	    },
	    error: function(XMLHttpRequest, textStatus, errorThrown){
		alert(XMLHttpRequest.status);
		alert(XMLHttpRequest.readyState);
		alert(textStatus);
	    	//console.log("error");
	    }         
	 });
}

function getUsableData()
{
	var d=app2.products;
	var re=[];
	for(var i=0;i<d.length;i++)
	{
		var p=d[i];
		var m=p.multiScore;
		var m1=[];
		for(var j=0;j<m.length;j++)
		{
			var temp={topicID:m[j].topicID,good:m[j].good*1.0,bad:m[j].bad*1.0};
			m1.push(temp);
		}
		var k={sales:p.sales,multiScore:m1};
		re.push(k);
	}
	return JSON.stringify(re);
}