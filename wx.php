<?php
		function reponseMsg(){
		$postArr = file_get_contents("php://input");
		//var_dump ($postArr);
		$postObj = simplexml_load_string($postArr);
		//$postObj->ToUserName = '';
		//$postObj->FromUserName = '';
		//$postObj->CreateTime = '';
		//$postObj->MsgType = '';
		//$postObj->Event = '';
		$toUser = $postObj->FromUserName;
		$fromUser = $postObj->ToUserName;
		

		if(strtolower($postObj->MsgType) == 'event'){
			//如果是subscribe 事件
			if(strtolower($postObj->Event == 'subscribe') ){
				//$toUser = $postObj->FromUserName;
				//$fromUser = $postObj->ToUserName;
				$time = time();
				$msgType = 'text';
				$content = '对说的就是你，戳戳我爱你！';
				$template = "<xml>
						<ToUserName><![CDATA[%s]]></ToUserName>
						<FromUserName><![CDATA[%s]]></FromUserName>
						<CreateTime>%s</CreateTime>
						<MsgType><![CDATA[%s]]></MsgType>
						<Content><![CDATA[%s]]></Content>
						</xml>";
				$info = sprintf($template, $toUser, $fromUser, $time, $msgType, $content);
				echo $info;			
			}
		}
		

		if($postObj->MsgType == 'text'){
				$time = time();
				$msgType = 'text';
				$content = '我听着呢';
				$template = "<xml>
						<ToUserName><![CDATA[%s]]></ToUserName>
						<FromUserName><![CDATA[%s]]></FromUserName>
						<CreateTime>%s</CreateTime>
						<MsgType><![CDATA[%s]]></MsgType>
						<Content><![CDATA[%s]]></Content>
						</xml>";
				$reponseTxt = sprintf($template, $toUser, $fromUser, $time, $msgType, $content);
				echo $reponseTxt;			
		}
	}


		//获得参数 signature nonce token timestamp echostr
		$nonce     = $_GET['nonce'];
		$token     = 'lwl620';
		$timestamp = $_GET['timestamp'];
		$echostr   = $_GET['echostr'];
		$signature = $_GET['signature'];
		//形成数组，然后按字典序排序
		$array = array();
		$array = array($nonce, $timestamp, $token);
		sort($array);
		//拼接成字符串,sha1加密 ，然后与signature进行校验
		$str = sha1( implode( $array ) );
		if( $str == $signature && $echostr ){
			//第一次接入weixin api接口的时候
			echo  $echostr;
			exit;
		} else {
			reponseMsg();
		}


	?>
	
