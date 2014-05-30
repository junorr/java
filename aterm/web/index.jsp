<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title>Ajaxterm4j</title>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>

  <script type="text/javascript" src="rsc/ajaxterm.js"></script>
  <link type="text/css" href="rsc/ajaxterm.css"/>
</head>
<body style="background-color:#888">
    <div id="term" class="ajaxterm" style="width: 660px; height: 420px; background-color: black; color: gold; font-family: monospace; font-size: 12px;"></div>
    <script type="text/javascript">
        window.onload=function() {
            t=new ajaxterm.Terminal("term",{width:80,height:25,endpoint:"./u"});
        };
   	</script>
</body>
</html>
