    <?php  
    require_once ("../jpgraph/jpgraph/jpgraph.php");  
    require_once ("../jpgraph/jpgraph/jpgraph_line.php");  
       
    $data1 = array(523,634,371,278,685,587,490,256,398,545,367,577); //第一条曲线的数组  
       
    $graph = new Graph(500,300);   
    $graph->SetScale("textlin");  
    $graph->SetShadow();     
    $graph->img->SetMargin(60,30,30,70); //设置图像边距  
       
    $graph->graph_theme = null; //设置主题为null，否则value->Show(); 无效  
       
    $lineplot1=new LinePlot($data1); //创建设置两条曲线对象  
    $lineplot1->value->SetColor("red");  
    $lineplot1->value->Show();  
    $graph->Add($lineplot1);  //将曲线放置到图像上  
       
    $graph->title->Set("CDN流量图");   //设置图像标题  
    $graph->xaxis->title->Set("月份"); //设置坐标轴名称  
    $graph->yaxis->title->Set("流 量(Gbits)");  
    $graph->title->SetMargin(10);  
    $graph->xaxis->title->SetMargin(10);  
    $graph->yaxis->title->SetMargin(10);  
       
    $graph->title->SetFont(FF_SIMSUN,FS_BOLD); //设置字体  
    $graph->yaxis->title->SetFont(FF_SIMSUN,FS_BOLD);  
    $graph->xaxis->title->SetFont(FF_SIMSUN,FS_BOLD);   
    $graph->xaxis->SetTickLabels($gDateLocale->GetShortMonth());  
      
    $graph->Stroke();  //输出图像  
    ?>