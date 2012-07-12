<?php 
class BarGraph 
{ 
    var $barWidth; 
    var $imgHeight=400; 
    var $imgWidth=600; 
    var $bgColor,$barColor; 
    var $barPadding; 
    var $data,$rangeMax=10; 
    var $im; 

    function init() /* initializes the image */ 
    { 
        $this->im=imagecreate($this->imgWidth,$this->imgHeight); 
    } 

    function setHeightWidth($h,$w) /** sets the hieght and with of the image **/ 
    { 
        $this->imgHeight=$h; 
        $this->imgWidth=$w; 
    } 

    function setBarWidth($width) /* sets the bar width */ 
    { 
        $this->barWidth=$width; 
    } 

    function setBarPadding($padding) /* sets the bar padding */ 
    { 
        $this->barPadding=$padding; 
    } 

    function setMax($max) /* sets the maximum posible value in the data set */ 
    { 
        $this->rangeMax=$max; 
    } 

    function loadData($data) /* load data, the input shud be an array */ 
    { 
        $this->data=$data; 
    } 

    function setBgColor($r,$g,$b) /* sets the background color of the image */ 
    { 
        $this->bgColor=imagecolorallocate($this->im,$r,$g,$b); 
    } 

    function setBarColor($r,$g,$b) /* sets the bar color of the image */ 
    { 
        $this->barColor=imagecolorallocate($this->im,$r,$g,$b); 
    } 

    function drawGraph($flag=0) /* to draw graphs on the image */ 
    { 
        if($flag) /* flag set to 1 to draw the second bar **/ 
        { 
            $t=$this->barWidth+$this->barPadding; 
        } 
        else /* else draws the first bar set */ 
        { 
            imagefilledrectangle($this->im,0,0,$this->imgWidth,$this->imgHeight,$this->bgColor); 
            $t=0; 
        } 

        for ( $mon = 0 ; $mon < count($this->data) ; $mon ++ )  
        { 
     
            $X = (($this->imgWidth/count($this->data))*$mon) + $this->barPadding + $t; 
            $Y = (50 - $this->data[$mon])*($this->imgHeight/$this->rangeMax); 
            $X1 = ($X + $this->barWidth); 
            $Y1 = $this->imgHeight; 

            imagefilledrectangle($this->im,$X,$Y,$X1,$Y1,$this->barColor); 
        } 
    } 

    function renderImage() /* creates the image & sends in to the browser */ 
    { 
        imagepng($this->im, "image.png");
    } 
}  
