<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@taglib uri="/WEB-INF/tld/fn.tld" prefix="fn"%>
<fmt:setLocale value="pt_BR" />

<html>
  
  <head>
    <title>File Upload</title>
    <link type="text/css" rel="stylesheet" href="css/font-awesome.min.css"/>
    <style type="text/css">
      
      #upload-div {
        margin-left: 50px;
        margin-top: 50px;
        width: 90%;
        box-shadow: 2px 2px 3px rgba(60,60,60,0.6);
      }
      
      #upload-header {
        background: linear-gradient(180deg, rgb(250,250,250), rgb(198,198,198));
        border: solid thin lightgray;
        height: 35px;
        /*text-align: center;*/
        font-weight: bold;
        font-size: 18px;
        color: rgb(60,60,60);
        vertical-align: middle;
        padding-top: 15px;
        padding-left: 30px;
      }
      
      #upload-body {
        text-align: center;
        padding: 20px;
        font-size: 14px;
      }
      
      #upload-response {
        margin-left: 50px;
        margin-top: 10px;
        width: 86.5%;
        box-shadow: 2px 2px 3px rgba(60,60,60,0.6);
        text-align: center;
        padding: 20px;
        font-size: 14px;
        display: none;
      }
      
      #file {
        color: rgb(100,100,100);
      }
      
      #upload-loader {
        display: none;
        margin-left: 15px;
        top: 6px;
        position: relative;
      }
      
      .litebutton {
        height: 30px;
        border-radius: 5px;
        background-color: white;
        border: solid thin slateblue;
        color: darkslateblue;
      }
      
      .litebutton:hover {
        background-color: whitesmoke;
      }
      
      .a4spacer:after {
        content:"\00A0\00A0\00A0\00A0";
      }
      
      .a2spacer:after {
        content:"\00A0\00A0";
      }
      
      .b4spacer:before {
        content:"\00A0\00A0\00A0\00A0";
      }
      
      .b2spacer:before {
        content:"\00A0\00A0";
      }
      
      li:before {
        content:"\2022\00A0";
      }
      
    </style>
  </head>
  
  <body>
    
    <div id="upload-div">
      <div id="upload-header">
        <i class="fa fa-upload a2spacer"></i>
        File Upload
      </div>
      <div id="upload-body">
        <span>Selecione um arquivo: </span>
        <input id="file" type="file" multiple="true"/><br/><br/>
        <button id="upload-button" class="litebutton" onclick="doUpload();">Carregar Arquivo</button>
        <img id="upload-loader" src="images/loading.gif" width="25" height="25"/>
      </div>
    </div>
    <div id="upload-response"></div>
    
    
    <script type="text/javascript">
      function doUpload() {
        var files = $("#file")[0].files;
        if(!files || files.length < 1) {
          alert("Selectione um arquivo para upload!");
          return;
        }
        var fd = new FormData();
        for(var i = 0; i < files.length; i++) {
          fd.append("file"+i, files[i]);
          console.log("* file"+ i+ ": "+ JSON.s)
        }
        
        $.ajax({
            url: "/disecAplic/upload",
            type: "POST",
            processData: false,
            data: fd,
            contentType: false,
            beforeSend: function (xhr) {
              $("#upload-loader").show();
            },
            success: function(resp, status) {
              console.log("* Upload Status: "+ status);
              $("#upload-loader").hide();
              $("#upload-response").html(resp).show();
            }
        });
      }
    </script>
    
  </body>
  
</html>