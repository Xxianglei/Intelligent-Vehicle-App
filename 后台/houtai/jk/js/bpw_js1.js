var level1 = "偏低";
var level2 = "正常";
var level3 = "正常高值";
var level4 = "1级高血压";
var level5 = "2级高血压";
var level6 = "3级高血压";
var level1Color = "#5AB2EF";
var level2Color = "#97B653";
var level3Color = "#B0D26A";
var level4Color = "#DD6AAB";
var level5Color = "#BC47AB";
var level6Color = "#8533C0";
var isZoom=false;
var testDay=0;
$(function() {
if (typeof(WeixinJSBridge) == "undefined") { 
    document.addEventListener("WeixinJSBridgeReady", function (e) { 
        setTimeout(function(){ 
            WeixinJSBridge.invoke('setFontSizeCallback', { 'fontSize':0}, function(res){ 
//                alert(JSON.stringify(res+"1111111111")); 
            }) 
        }, 0) 
    }); 
}else{   
    setTimeout(function(){ 
        WeixinJSBridge.invoke('setFontSizeCallback', { 'fontSize':0}, function(res){ 
//            alert(JSON.stringify(res+"22222222222")); 
        }) 
    }, 0)    
} 
    $(document).ready(function() {
    	
		var allClass=document.getElementsByClassName("c_cp_padding_left");
		for(var i=0;i<allClass.length;i++){
			var thisSrc=allClass[i].children[0].src;
			thisSrc=thisSrc.substring(0,thisSrc.length-4);
			allClass[i].children[0].src=thisSrc+"_n.png";
		}
		
    	if(typeof(startTime) != "undefined" && typeof(endTime) != "undefined"){
    		var startDate = new Date(startTime);
        	var endDate = new Date(endTime);
        	$("#i_bpw_date").text((101 + startDate.getMonth() + "").substr(1,3) + "/" + (startDate.getDate() + 100 + "").substr(1,3)
        			+ " ~ " + (101 + endDate.getMonth() + "").substr(1,3) + "/" + (endDate.getDate() + 100 + "").substr(1,3));
        	$(".c_bpw_tip_img").click(function(e) {
        		e.stopPropagation();
        		$("#i_bpw_tip_content").removeClass("c_bpw_hide");
        	});
        	$(document).on('touchend',function() {
        		if (!$("#i_bpw_tip_content").hasClass("c_bpw_hide")) {
        			$("#i_bpw_tip_content").addClass("c_bpw_hide");
        		}
        	});
    	}
    	
    	testDay=caculTestDay(scatterHighData);
	  	testTimes=barXaxisData.length;
	  	
	  	if(testTimes>0)
	  	{
	  		$("#i_bpw_load").click(function(){
		  		$("#i_bpw_load_detail").slideToggle("slow");
		  		var load_button_src = $("#i_bpw_load_button").attr("src");
		  		if(load_button_src.indexOf("button3")!=-1)
	    		{
			    	$("#i_bpw_load_button").attr("src", load_button_src.replace("button3", "button4"));
			    } 
		  		else 
		  		{
			    	$("#i_bpw_load_button").attr("src", load_button_src.replace("button4", "button3"));
			    }
		  	});
	  	}
	  	else
	  	{
	  		$("#i_bpw_load").hide();
	  		$("#i_bpw_load_detail").hide();
	  	}
	  		
	  		
    	$("#i_bpw_load_ppd").click(function(){
	  		$("#i_bpw_load_detail_ppd").slideToggle("slow");
	  		var load_button_src_ppd = $("#i_bpw_load_button_ppd").attr("src");
	  		if(load_button_src_ppd.indexOf("button3")!=-1)
	  		{
	  			$("#i_bpw_load_button_ppd").attr("src", load_button_src_ppd.replace("button3", "button4"));
	  		}
	  		else
	  		{
	  			$("#i_bpw_load_button_ppd").attr("src", load_button_src_ppd.replace("button4", "button3"));
	  		}
	  	});
	  	
	  	
    	$("#i_bpw_references").click(function(){
    		$("#i_bpw_references_detail").slideToggle("slow");
    		var references_button_src = $("#i_bpw_references_button").attr("src");
    		if(references_button_src.indexOf("button1")!=-1)
    		{
		    	$("#i_bpw_references_button").attr("src", references_button_src.replace("button1", "button2"));
		    } 
    		else 
		    {
		    	$("#i_bpw_references_button").attr("src", references_button_src.replace("button2", "button1"));
		    }
	  	});
	  	
	  	
	  
	  	
	  	if(testTimes>20)
	  	{
	  		isZoom=true;
	  	}
	  	else
	  	{
	  		isZoom=false;
	  	}
	  	if(testDay > 0){
	  		drawPieChart();
	  		drawTrendTimesChart();
			if (testDay >= 7) {
				drawScatterChart();  
			} else if (testDay > 0) {
				drawScatterChart2();
			}
			drawBarPPDChart();
			
			
			//血压极值颜色处理
		  	if(document.getElementById("i_bpw_high_highst").innerHTML.indexOf("偏高")!=-1)
	    	{
	    			document.getElementById("i_bpw_high_highst").className="c_bpw_middle c_bpw_color_piangao";
	    	}
		  	else if(document.getElementById("i_bpw_high_highst").innerHTML.indexOf("偏低")!=-1)
	    	{
	    			document.getElementById("i_bpw_high_highst").className="c_bpw_middle c_bpw_color_piandi";
	    	}
		  	else if(document.getElementById("i_bpw_high_highst").innerHTML.indexOf("正常")!=-1)
	    	{
	    			document.getElementById("i_bpw_high_highst").className="c_bpw_middle c_bpw_color_zhengchang";
	    	}
		  	
		  	if(document.getElementById("i_bpw_low_highst").innerHTML.indexOf("偏高")!=-1)
	    	{
	    			document.getElementById("i_bpw_low_highst").className="c_bpw_middle c_bpw_color_piangao";
	    	}
		  	else if(document.getElementById("i_bpw_low_highst").innerHTML.indexOf("偏低")!=-1)
	    	{
	    			document.getElementById("i_bpw_low_highst").className="c_bpw_middle c_bpw_color_piandi";
	    	}
		  	else if(document.getElementById("i_bpw_low_highst").innerHTML.indexOf("正常")!=-1)
	    	{
	    			document.getElementById("i_bpw_low_highst").className="c_bpw_middle c_bpw_color_zhengchang";
	    	}
	  	}
	  	
	  	
	  	
	  	
    });
});


function drawPieChart() {
	var pieChart = echarts.init(document.getElementById("i_bpw_chart_pie"));
		pieOption = {
		    tooltip: {
		        trigger: 'item',
		        formatter: function (obj) {
							    return obj.seriesName+"<br/>"+obj.name+":"+obj.value+"次 ("+Math.round(obj.percent)+"%)";
				}
		    },
		    grid:{
		    	show:false,
		    	top:0,
		    	bottom :1
		    },
		    legend: {
		    	top:0,
				orient: 'horizontal',
				data: piecolorsdata,
				textStyle: {
					color: "#686868"
				}
			},
		    series: [
		        {
		            name:'血压等级分析：',
		            type:'pie',
		            radius: ['40%', '65%'],
		            center: ["50%", "58%"],
		            label: {
		                normal: {
		                    show: true,
							formatter: function (obj) {
							    return Math.round(obj.percent)+'%';
							},
							position: "outer",
							textStyle: {
								color: "#686868",
								fontSize: 16
							}	
		                }
		            },
		            labelLine: {
		                normal: {
		                    show: true,
							lineStyle: {
								color: "#686868"
							}
		                }
		            },
		            data:piedata
		        }
		    ]
		};
		pieChart.setOption(pieOption);
}

function drawTrendTimesChart()
{
	//3、血压线性散点
	var i_bpw_chart_trend_times = echarts.init(document.getElementById('i_bpw_chart_trend_times'));
	var optionScatter = {
							tooltip : {
							        trigger: 'axis',
							        formatter: function (value) {
										var data0=value[0].data;
										var date0=new Date(data0[0]);
										var month=(1+date0.getMonth());
										if(month<10)
										{
											month='0'+month;
										}
										var day=(date0.getDate());
										if(day<10)
										{
											day='0'+day;
										}
										
										var date0Str=month+'-'+day+' '+(date0.getHours())+':'+(date0.getMinutes());	
										
										var data1=value[1].data;
										var date1=new Date(data1[0]);
									   
									    return data0[1]+'/'+data1[1]+'mmHg'+'<br/>'+date0Str;
									}
							},
							grid:{
								bottom:80
							},
							 dataZoom:[
							    	{
							    		show:true,
								    	type:'slider',
								    	orient:'horizontal',
								    	zoomLock:false,
								    	filterMode:'empty',
								        handleColor:'rgba(155,155,155,0.9)',
								        dataBackgroundColor:'#E9E9E9',
								        backgroundColor:'rgba(215,215,215,0)',
								        fillerColor:'rgba(215,215,215,0.4)'
								        	
								       
							    	},
							    	{
								    	type:'inside',
								    	orient:'horizontal'
							    	}
							],
						    legend: {
						    	top: 'top',
	        					orient:'horizontal',
						        data:['收缩压','舒张压'],
						        textStyle :{
						            		fontSize:10,
						            		color:'#7A7A7A'
						        }
						    },
						    xAxis : {
						            type : 'time',
						            splitNumber :7,
						            
						            axisLine:{
						            	lineStyle:{
						            		color :'#9C9C9C',
						            		width :3
						            	}
						            },
						            axisTick :{
						            	lineStyle :{
						            		color :'#9C9C9C'
						            	}
						            },
						            splitLine :{
						    			show:false
						            },
						            axisLabel :{
						            	interval :0,
						            	rotate:45,
						            	margin :10,
						            	textStyle :{
						            		fontSize:10,
						            		color:'#7A7A7A'
						            	}
						            	
						            }
						    },
						    yAxis :{
						    		splitLine :{
						    			show:true,
						    			lineStyle:{
						    				color:'#E9E8E9'
						    			}
						            },
						    		axisLine:{
							    			show:false
						    		},
						    		axisTick:{
						    			show:false
						    		},
						            type : 'value',
				                    axisLabel :{
					                    textStyle:{
											color:'#7A7A7A'
					                    }
						    		},
						    		min:30
						    },
						    series : [
						        {
						            name:'收缩压',
						            type:'scatter',
						            symbol : 'diamond',
						            symbolSize:8,
						            data:timesHighData,
						            lineStyle:{
						            	normal:{
						            		color:'#5AB1EF'
						            	}
						            },
						            itemStyle :{
						            	normal:{
						            		color:'#5AB1EF'
						            	}
						            },
						            markPoint : {
							                data : [
							                    {type : 'max', name: '最大值'},
							                    {type : 'min', name: '最小值'}
							                ],
							                symbolSize :40
							        },
						            markLine : {
							        	label:{
							        		normal:{
							        			show:true,
							        			formatter: function (obj) {
												    return '均值\n'+obj.value;
												}
							        		}
							        	},
							        	lineStyle:{
						            		normal:{
						            			type:'solid'
						            		}
						            	},
						            	symbol:['image://','image://'],
						                data : [
						                    {
						                    	type : 'average', 
						                    	name: '收缩压平均值'
						                    }
						                ],
						                precision :0
						                
						            }
						        },
						        {
						            name:'舒张压',
						            type:'scatter',
						            symbol : 'diamond',
						            symbolSize:8,
						            data:timesLowData,
						            
						            lineStyle:{
						            	normal:{
						            		color:'#97B552'
						            	}
						            },
						            itemStyle :{
						            	normal:{
						            		color:'#97B552'
						            	}
						            },
						            markPoint : {
							                data : [
							                    {type : 'max', name: '最大值'},
							                    {type : 'min', name: '最小值'}
							                ],
							                symbolSize :40
						            },
						            markLine : {
						            	label:{
							        		normal:{
							        			show:true,
							        			formatter: function (obj) {
												    return '均值\n'+obj.value;
												}
							        		}
							        	},
						            	lineStyle:{
						            		normal:{
						            			type:'solid'
						            		}
						            	},
						            	symbol:['image://','image://'],
						                data : [
						                    {type : 'average', name: '舒张压平均值'}
						                ],
						                precision :0
						            }
						        }
						    ]
						};
		i_bpw_chart_trend_times.setOption(optionScatter);
}




function drawScatterChart()
{
	var xAxisData=calcuXAxisData();
	
	//3、血压线性散点		
		var scatterChart = echarts.init(document.getElementById('i_bpw_chart_trend_day'));
		var optionScatter = {
							    tooltip : {
							        trigger: 'axis',
							        formatter: function (obj){
										return obj[0].value+"/"+obj[1].value+"mmHg<br/>"+obj[0].name+"，日均值";
							        }
							    },
							    legend: {
							    	top: 'top',
		        					orient:'horizontal',
							        data:['收缩压','舒张压','收缩压趋势线','舒张压趋势线'],
							        textStyle :{
							            		fontSize:10,
							            		color:'#7A7A7A'
							        }
							    },
							    grid: {
							        left: 30,
							        right: 20,
							        bottom: 40,
							        top :60
							    },
							    xAxis : {
							            type : 'category',
							            data: xAxisData, // calcuWeeks(timesHighData)
							            boundaryGap : false,
							            splitLine :{
							    			show:false
							            },
							            axisLine:{
							            	lineStyle:{
							            		color :'#9C9C9C',
							            		width :3
							            	}
							            },
							            axisTick :{
							            	interval :0,
							            	lineStyle :{
							            		color :'#9C9C9C'
							            	}
							            },
							            axisLabel :{
							            	interval :0,
							            	rotate:45,
							            	margin :10,
							            	textStyle :{
							            		fontSize:10,
							            		color:'#7A7A7A'
							            	}
							            	
							            }
							    },
							    yAxis :{
								    	splitLine :{
							    			show:true,
							    			lineStyle:{
							    				color:'#E9E8E9'
							    			}
							            },
							    		axisLine:{
							    			show:false
							    		},
							    		axisTick:{
							    			show:false
							    		},
							            type : 'value',
					                    axisLabel :{
						                    textStyle:{
												color:'#7A7A7A'
						                    }
							    		},
							    		min:30
							    },
							    series : [
							        {
							            name:'收缩压',
							            type:'scatter',
							            symbol : 'diamond',
							            symbolSize:8,
							            data:scatterHighData,
							            lineStyle:{
							            	normal:{
							            		color:'#5AB1EF'
							            	}
							            },
							            itemStyle :{
							            	normal:{
							            		color:'#5AB1EF'
							            	}
							            }
							        },
							        {
							            name:'舒张压',
							            type:'scatter',
							            symbol : 'diamond',
							            symbolSize:8,
							            data:scatterLowData,
							           
							            lineStyle:{
							            	normal:{
							            		color:'#97B552'
							            	}
							            },
							            itemStyle :{
							            	normal:{
							            		color:'#97B552'
							            	}
							            }
							        },
							        {
							            name:'收缩压趋势线',
							            type:'line',
							            symbolSize:0,
							            data:highKBData,
							            lineStyle:{
							            	normal:{
							            		color:'#5AB1EF',
							            		type :'dashed',
							            		width :1
							            	}
							            },
							            itemStyle :{
							            	normal:{
							            		color:'#5AB1EF'
							            		
							            	}
							            }
							        },
							        {
							            name:'舒张压趋势线',
							            type:'line',
							            symbolSize:0,
							            data:lowKBData,
							            lineStyle:{
							            	normal:{
							            		color:'#97B552',
							            		type :'dashed',
							            		width :1
							            	}
							            },
							            itemStyle :{
							            	normal:{
							            		color:'#97B552'
							            	}
							            }
							        }
							    ]
							};
		scatterChart.setOption(optionScatter);
}
function drawScatterChart2() {
	var highData=calcuWeeks(scatterHighData);
	var lowData=calcuWeeks(scatterLowData);
	var xAxisData=calcuXAxisData();
	//3、血压线性散点		
	var scatterChart = echarts.init(document.getElementById('i_bpw_chart_trend_day'));
	var optionScatter = {
	    tooltip : {
	        trigger: 'axis',
	        formatter: function (obj){
	        	var hValue=obj[0].value.join("");
	        	hValue=hValue.substring(2,hValue.length);
	        	var lValue=obj[1].value.join("");
	        	lValue=lValue.substring(2,lValue.length);
	        	var name=obj[0].value.join("");
	        	name=name.substring(0,2);
				return hValue+"/"+lValue+"mmHg<br/>"+name+"，日均值";
	        }
	    },
	    legend: {
	    	top: 'top',
			orient:'horizontal',
	        data:['收缩压','舒张压','收缩压趋势线','舒张压趋势线'],
	        textStyle :{
	            		fontSize:10,
	            		color:'#7A7A7A'
	        }
	    },
	    grid: {
	        left: 30,
	        right: 20,
	        bottom: 40,
	        top :60
	    },
	    xAxis : {
	            type : 'category',
	            data: xAxisData,
	            boundaryGap : false,
	            splitLine :{
	    			show:false
	            },
	            axisLine:{
	            	lineStyle:{
	            		color :'#9C9C9C',
	            		width :3
	            	}
	            },
	            axisTick :{
	            	interval :0,
	            	lineStyle :{
	            		color :'#9C9C9C'
	            	}
	            },
	            axisLabel :{
	            	interval :0,
	            	margin :10,
	            	textStyle :{
	            		fontSize:10,
	            		color:'#7A7A7A'
	            	}
	            	
	            }
	    },
	    yAxis :{
		    	splitLine :{
	    			show:true,
	    			lineStyle:{
	    				color:'#E9E8E9'
	    			}
	            },
	    		axisLine:{
	    			show:false
	    		},
	    		axisTick:{
	    			show:false
	    		},
	            type : 'value',
	            axisLabel :{
	                textStyle:{
						color:'#7A7A7A'
	                }
	    		},
	    		min:30
	    },
	    series : [
	        {
	            name:'收缩压',
	            type:'line',
	            symbol : 'diamond',
	            symbolSize:8,
	            data:highData,
	            lineStyle:{
	            	normal:{
	            		color:'#5AB1EF'
	            	}
	            },
	            itemStyle :{
	            	normal:{
	            		color:'#5AB1EF'
	            	}
	            }
	        },
	        {
	            name:'舒张压',
	            type:'line',
	            symbol : 'diamond',
	            symbolSize:8,
	            data:lowData,
	           
	            lineStyle:{
	            	normal:{
	            		color:'#97B552'
	            	}
	            },
	            itemStyle :{
	            	normal:{
	            		color:'#97B552'
	            	}
	            }
	        }
	    ]
	};
	scatterChart.setOption(optionScatter);
}
function drawBarPPDChart()
{
	var piandiData=new Array();
	var zhengchangData=new Array();
	var piangaoData=new Array();
	for(var num in barData)
	{
		if(barData[num]<20)
		{
			piandiData[num]=barData[num];
		}
		else
		{
			piandiData[num]='-';
		}
		if(barData[num]>60)
		{
			piangaoData[num]=barData[num];
		}
		else
		{
			piangaoData[num]='-';
		}
		if(barData[num]>=20&&barData[num]<=60)
		{
			zhengchangData[num]=barData[num];
		}
		else
		{
			zhengchangData[num]='-';
		}
	}
	
	
	//4、脉压范围图
	var barPPDChart = echarts.init(document.getElementById('i_bpw_chart_bar_ppd'));

	var optionBarPPD= {
					    	tooltip : {
						        trigger: 'axis',
						         formatter: function (params) {
						            var tar;
						            if (params[1].value != '-') {
						                tar = params[1];
						            }
						            else if(params[2].value != '-') {
						                tar = params[2];
						            }
						            else{
						            	 tar = params[0];
						            }
						            return '第'+tar.name + ' 次<br/>' + tar.seriesName + ' : ' + tar.value+'mmHg';
						        }
						    },
						    dataZoom:[
							    	{
							    		show:isZoom,
								    	type:'slider',
								    	orient:'horizontal',
								        handleColor:'rgba(155,155,155,0.9)',
								        dataBackgroundColor:'#E9E9E9',
								        backgroundColor:'rgba(215,215,215,0)',
								        fillerColor:'rgba(215,215,215,0.4)',
								        bottom:0
							    	},
							    	{
								    	type:'inside',
								    	orient:'horizontal'
							    	}
							],
						    legend: {
						    	top: 'top',
	        					orient:'horizontal',
						        data:['偏小','正常','偏大'],
						        textStyle :{
						            		fontSize:10,
						            		color:'#7A7A7A'
						        }
						     
						    },
						    grid: {
						        left: 25,
						        right: 20,
						        top:40,
						        bottom:55
						    },
						    xAxis : {
						            type : 'category',
						            boundaryGap : true,
						            splitLine :{
						    			show:false
						            },
						            axisLine:{
						            	lineStyle:{
						            		color :'#9C9C9C',
						            		width :3
						            	}
						            },
						            axisTick :{
						            	interval :0,
						            	lineStyle :{
						            		color :'#9C9C9C'
						            	}
						            },
						            axisLabel :{
						            	interval :0,
						            	rotate:45,
						            	margin :10,
						            	textStyle :{
						            		fontSize:10,
						            		color:'#7A7A7A'
						            	}
						            },
						            data : barXaxisData
						            
						    },
						    yAxis :{
						    		splitLine :{
						    			show:true,
						    			lineStyle:{
						    				color:'#E9E8E9'
						    			}
						            },
						    		axisLine:{
						    			show:false
						    		},
						    		axisTick:{
						    			show:false
						    		},
						            type : 'value',
				                    axisLabel :{
					                    textStyle:{
											color:'#7A7A7A'
					                    }
						    		}
						    },
						    series : [
						        {
						            name:'偏小',
						            type:'bar',
						            stack: '总量',
						            barMaxWidth:30,
						            itemStyle : { normal: {color :'#5ab1ef',label : {show: true, position: 'top'}}},
						            data:piandiData
						        },
						        {
						            name:'正常',
						            type:'bar',
						            stack: '总量',
						            barMaxWidth:30,
						            itemStyle : { normal: {color :'#97b552',label : {show: true, position: 'top'}}},
						            data:zhengchangData
						        },
						        {
						            name:'偏大',
						            type:'bar',
						            stack: '总量',
						            barMaxWidth:30,
						            itemStyle : { normal: {color :'#dd68ab',label : {show: true, position: 'top'}}},
						            data:piangaoData
						        }
						        
						          
						    ]
					};
	barPPDChart.setOption(optionBarPPD);
}

(function (doc, win) {
	var docEl = doc.documentElement,
    resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
    recalc = function () {
      var clientWidth = docEl.clientWidth;
      if (!clientWidth||clientWidth>480) return;
      docEl.style.fontSize = 20 * (clientWidth / 480) + 'px';
    };
	if (!doc.addEventListener) return;
	win.addEventListener(resizeEvt, recalc, false);
	doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);


function calcuXAxisData(){
	var da=['周六', '周日','周一', '周二', '周三', '周四', '周五'];
	var startDate = new Date(startTime);
	var weekNum = startDate.getDay(); 
	if (weekNum==0){
		da=[ '周日','周一', '周二', '周三', '周四', '周五','周六'];
	}
	else if (weekNum==1){
		 da=[ '周一', '周二', '周三', '周四', '周五','周六','周日'];
	}
	else if (weekNum==2){
		 da=[ '周二', '周三', '周四', '周五','周六','周日','周一'];
	}
	else if (weekNum==3){
		 da=[  '周三', '周四', '周五','周六','周日','周一','周二'];
	}
	else if (weekNum==4){
		 da=[  '周四', '周五','周六','周日','周一','周二','周三'];
	}
	else if (weekNum==5){
		 da=[   '周五','周六','周日','周一','周二','周三','周四'];
	}
	else if (weekNum==6){
		 da=[   '周六','周日','周一','周二','周三','周四','周五'];
	}
	
	return da;
}


function calcuWeeks(timesHighData){
	var week = new Array("周六","周日", "周一", "周二", "周三", "周四", "周五");
	
	var startDate = new Date(startTime);
	var weekNum = startDate.getDay(); 
	if (weekNum==0){
		 week = new Array("周日","周一", "周二", "周三", "周四", "周五","周六");
	}
	else if (weekNum==1){
		 week = new Array("周一", "周二", "周三", "周四", "周五","周六","周日");
	}
	else if (weekNum==2){
		 week = new Array( "周二", "周三", "周四", "周五","周六","周日","周一");
	}
	else if (weekNum==3){
		 week = new Array( "周三", "周四", "周五","周六","周日","周一","周二");
	}
	else if (weekNum==4){
		 week = new Array( "周四", "周五","周六","周日","周一","周二","周三");
	}
	else if (weekNum==5){
		 week = new Array( "周五","周六","周日","周一","周二","周三","周四");
	}
	else if (weekNum==6){
		 week = new Array( "周六","周日","周一","周二","周三","周四","周五");
	}
	
	var arr=timesHighData;
	var lastArr=new Array();
	
	if(arr[0]){
		var tmp0=new Array(week[0],arr[0]);
		lastArr.push(tmp0);
	}else{
		var tmp0=new Array(week[0],"-");
		lastArr.push(tmp0);
	}
	
	if(arr[1]){
		var tmp1=new Array(week[1],arr[1]);
		lastArr.push(tmp1);
	}
	if(arr[2]){
		var tmp2=new Array(week[2],arr[2]);
		lastArr.push(tmp2);
	}
	if(arr[3]){
		var tmp3=new Array(week[3],arr[3]);
		lastArr.push(tmp3);
	}
	if(arr[4]){
		var tmp4=new Array(week[4],arr[4]);
		lastArr.push(tmp4);
	}
	if(arr[5]){
		var tmp5=new Array(week[5],arr[5]);
		lastArr.push(tmp5);
	}
	if(arr[6]){
		var tmp6=new Array(week[6],arr[6]);
		lastArr.push(tmp6);
	}else{
		var tmp6=new Array(week[6],"-");
		lastArr.push(tmp6);
	}
	
	return lastArr;
}
function caculTestDay(scatterHighData){
	var arr=scatterHighData;
	var day=0;
	for(var i in arr){
		if(arr[i]){
			day++;
		}
	}
	return day;
}
