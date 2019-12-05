<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>码云</title>
<link href="statics/css/login.css" rel="stylesheet" type="text/css" />
<script src="statics/js/jquery-3.3.1.min.js" type=text/javascript></script>
<script src="statics/js/config.js" type=text/javascript></script>
<script src="statics/js/util.js" type=text/javascript></script>
 
</head>
<body>
<!--head-->
<div id="head">
	<div class="top">
        <div class="fl logomg"><img src="statics/images/logo.png" /></div>
        <div class="fl"><img src="statics/images/li.png" /></div>
        <div class="fl yahei18">开启移动办公新时代！</div>       
    </div>
</div>

<!--login box-->
<div class="wrapper">
	<div id="box">
        <div class="loginbar">码云用户登录</div>
        <div id="tabcon">
	        <form action="user" method="post" id="loginForm">
		        <div class="box show">
		        	<input type="hidden" name="actionName" value="login" />
		        	<input type="text" class="user yahei16" id="userName" name="uname" value="${resultInfo.result.uname }" /><br /><br />
		            <input type="password" class="pwd yahei16" id="userPwd" name="upwd" value="${resultInfo.result.upwd }" /><br /><br />
		            <input name="rem" type="checkbox" value="1"  class="inputcheckbox"/> <label>记住我</label>&nbsp; &nbsp; 
		            <span id="msg">${resultInfo.msg }</span><br /><br />
		            <input type="button" class="log jc yahei16" value="登 录" onclick="checkLogin()" />&nbsp; &nbsp; &nbsp; <input type="reset" value="取 消" class="reg jc yahei18" />
				</div>
			</form>
        </div>        
    </div>
</div>

<div id="flash">
	<div class="pos">
		<a bgUrl="statics/images/banner-bg1.jpg" id="flash1" style="display:block;"><img src="statics/images/banner_pic1.png"></a>
	    <a bgUrl="statics/images/banner-bg2.jpg" id="flash2"                       ><img src="statics/images/banner-pic2.jpg"></a>	   
	</div>    
	<div class="flash_bar">
		<div class="dq" id="f1" onclick="changeflash(1)"></div>
		<div class="no" id="f2" onclick="changeflash(2)"></div>		
	</div>
</div>

<!--bottom-->
<div id="bottom">
	<div id="copyright">
        <div class="quick">
        	<ul>
                    <li><input type="button" class="quickbd iphone" onclick="location.href='https://www.pxcoder.com'" /></li>
                    <li><input type="button" class="quickbd android" onclick="location.href='https://www.pxcoder.com'" /></li>
                    <li><input type="button" class="quickbd pc" onclick="location.href='https://www.pxcoder.com'" /></li>
                <div class="clr"></div>
            </ul>
            <div class="clr"></div>
        </div>
        <div class="text">
        <a href="https://www.pxcoder.com">码歌</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://www.pxcoder.com">杭州码歌</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://www.pxcoder.com">南昌码歌</a>&nbsp;&nbsp;&nbsp;&nbsp; <br />
     Copyright © 2006-2026  南昌码歌  All Rights Reserved  电话：0791-88269138 QQ：1811112688
        </div>
    </div>
</div>
</body>

</html>