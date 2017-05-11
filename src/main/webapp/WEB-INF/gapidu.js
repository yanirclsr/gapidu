(function(){
	
	var servletUrl = "http://localhost:8080/trk";
	var delayTime = 3000;
	
	var identifiers =  {
		
		gpdu: {
			
			cookieHandler: function (newSession){
				
				var newCookie;
				var tagId = location.hostname.replace(/\./g,'');
				var timeNow = Math.round(+new Date() / 1000);
				var uuid = utils.general.generateUUID();
				var oldCookie = utils.cookies.getCookie("gpdu");
				var oldCookieArr = oldCookie ? oldCookie.split(":") : "";
				var newSession = true;
				
				var email = null;
				
	            if (!oldCookie) {
	            	
	            	//cookie doesn't exist - generate a new cookie
	            	newCookie = tagId + "-" + uuid + ":" + timeNow + ":1";
	                
	            } else if(identifiers.gpdu.checkIfNewSession(oldCookieArr[1])){
	            	
	            	//cookie exists and it's a new session - update the sessions count and timestamp in the cookie
	            	var sessionCount = Number(oldCookieArr[2]) + 1;
	            	newCookie = oldCookieArr[0]  + ":" + timeNow + ":" +  sessionCount;
	            	
	            }else{
	            	
	            	//cookie exists and it's another pageview - update the timestamp in the cookie
	            	newSession = false;
	            	newCookie = oldCookieArr[0] + ":" + timeNow + ":" + oldCookieArr[2] ;
	            	utils.cookies.setCookie("gpdu", newCookie);
	            }
	            
	            try {
	            	//catch email from pre-filled forms
	            	email = utils.email.find();
//	            	console.log("catch email from pre-filled forms: " + email);
				} catch (e) {
					// TODO: handle exception
				}
	            
				if(email == null){
	            	//catch email from cookie	
					try {
						email = oldCookieArr[3];
					} catch (e) {
						email = null;
					}
//					console.log("catch email from cookies: " + email);
					
				}
	            var res = {
            		newSession: newSession,
            		email: email,
            		cookie: newCookie
	            }
//	            console.log(res);
	            return res;
	            
			},
			checkIfNewSession: function(lastTimestamp){
				var maxSessionTime = 30;
	            var now = Math.round(+new Date() / 1000);
	            var minutesPassed = Number(((now - lastTimestamp) / 60).toFixed(0));
	            return minutesPassed > maxSessionTime;
			},
			sessionsCount: function(cookie){
				
				var cookieArr = cookie.split(":");
				var sessions;
//				console.log(cookieArr);
				try{
					return Number(cookieArr[2]);
				}catch (e) {
					return 0;
				}
				
			}
			
		}
	
	}
	
	var utils = {
			
			cookies: {
				
		        getCookie: function (cname) {
		            var name = cname + "=";
		            var ca = document.cookie.split(';');
		            for (var i = 0; i < ca.length; i++) {
		                var c = ca[i];
		                while (c.charAt(0) == ' ') {
		                    c = c.substring(1);
		                }
		                if (c.indexOf(name) == 0) {
		                    return c.substring(name.length, c.length);
		                }
		            }
		            return false;
		        },
			    setCookie: function (cname, cvalue) {
			    	if(cvalue == "undefined" || typeof(cvalue) == "undefined" ){
			    		return;
			    	}
			        var d = new Date();
			        d.setTime(d.getTime() + (2 * 365 * 24 * 60 * 60 * 1000));
			        var expires = "expires=" + d.toUTCString();
			        var domain = function(){
			        	var hostArr = location.hostname.split('.').reverse();
			        	if(hostArr.length == 1){
			        		return "domain=" + location.hostname.split('.').reverse()[0];
			        	}else{
			        		return "domain=." + location.hostname.split('.').reverse()[1] + "." + location.hostname.split('.').reverse()[0];
			        	}
			        	
			        } 
			        var setCookie = cname + "=" + cvalue + ";" + domain() + "; " + expires + "; path=/";
//			        console.log(setCookie);
			        document.cookie = setCookie;
			        
			    },
	
			},
			general: {
				
		        generateUUID: function () {
		            var d = new Date().getTime();
		            var uuid = 'xxxxxxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
		                var r = (d + Math.random() * 16) % 16 | 0;
		                d = Math.floor(d / 16);
		                return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
		            });
		            return uuid;
		        },
		        checkIfBot: function(){
		        	return /bot|google|aolbuild|baidu|bing|msn|duckduckgo|teoma|slurp|yandex/i.test(navigator.userAgent);
		        }
				
			},
			server: {
				post: function(gpduCookie){
					
					if(utils.general.checkIfBot()){
						return;
					}
					
					var data = {}
					if(gpduCookie.newSession){
						data = utils.server.collectIds();
					}
					
					if(gpduCookie.email != null){
						data.email = gpduCookie.email;
					}
					
					data["vid"] = gpduCookie.cookie.split(":")[0];
					data["tid"] = data.vid.split("-")[0];
					 
					//executing a POST request to the DB with form data
					xmlhttp = new XMLHttpRequest();
					var parsed = Object.keys(data).map(function(k) {
						return encodeURIComponent(k) + '=' + encodeURIComponent(data[k])
					}).join('&');
					var url = servletUrl + "/gapidu?" + parsed;
					xmlhttp.timeout = 2000; 
					xmlhttp.open("GET", url, true);
					xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					xmlhttp.onreadystatechange = function () { //Call a function when the state changes.
					    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) { 
					        utils.cookies.setCookie("gpdu", gpduCookie.cookie);
					    }
					}

					xmlhttp.send(parsed);
					
				},
				collectIds: function(){
					
					var data = {};
					try{			
						//try to process the gpdu cookie id's
						data.referrer = document.referrer;
						data.url = location.href;
						data["cookies"] = document.cookie;
//						console.log(data);
					}catch (e) {
						return;
					}
					
					try {
						//check if user agent is a known bot
						data.userAgent = navigator.userAgent;						
					} catch (e) {}
					
					return data;
				}
			},
			email: {
		        isEmail: function(email){
		            return email.match(/@.*\./);
		        },
		        find: function(){
		        	var allInputs = document.querySelectorAll("input[type=text] ,input[type=email]");
		        	for(var i = 0; i <allInputs.length; i++){
		        		var val = allInputs[i].value;
		        		if(utils.email.isEmail(val)){
//		        			console.log(val)
		        			return val;
		        		}
		        	}
		        	return null;
		        },
		        storeInCookies: function(email){
		        	var gpduArr = utils.cookies.getCookie("gpdu").split(":");

	        		var cookie =  gpduArr[0] + ":" + gpduArr[1] + ":" + gpduArr[2] + ":" + email;
        			utils.cookies.setCookie("gpdu", cookie);
		        	
		        }
			}

				
	}
	
	var listeners = {
			init: function(){
				listeners.inputFields();
			},
			inputFields: function(){
				var eventList = ["change", "keyup", "paste", "input", "click"];
				for(event in eventList) {
					var ename = eventList[event];
				    document.addEventListener(ename, function() {
						try {
							var email = utils.email.find();
							if(email != null){
								utils.email.storeInCookies(email);
							}
						} catch (e) {};
				        
				    });
				};
			},
			forms: function(){
//				$("form").submit(function(){
//				    
//				});
			}
			
	
	
	}

	var uuid = {
		loadIframe: function(vid){
			//this part is inactive
//			var newDiv = document.createElement('div');
//			newDiv.id = "gapidu-div";
//			newDiv.style = "width: 0px; height: 0px; display: none; visibility: hidden;";
//			
//			var ifrm = document.createElement('iframe');
//			ifrm.setAttribute("style","width: 0px; height: 0px; display: none; visibility: hidden;");
//			ifrm.setAttribute("frameborder","0");
//			ifrm.setAttribute("marginwidth","0");
//			ifrm.setAttribute("marginheight","0");
//			ifrm.setAttribute("vspace","0");
//			ifrm.setAttribute("hspace","0");
//			ifrm.setAttribute("allowtransparency","true");
//			ifrm.setAttribute("scrolling","no");
//			
//			ifrm.setAttribute("src", servletUrl +  "/gapidu-pixel?guid=" + vid);// + "&guid=" + guid);
////			ifrm.setAttribute("src", "//trk.gapidu.com/gapidu-fixel");//?vid=" + vid + "&guid=" + guid);
//			
//			newDiv.appendChild(ifrm);
//			document.body.appendChild(newDiv); // to place at end of document

		}
	}
	//Runtime flow
	/*
	 * 1. check visitor / session status
	 * 		if first time / first pageview
	 * 			2. (only if first visit) Create gpdu  cookie
	 * 			3. collect all cookies
	 * 			4. post to db
	 *
	 * 2. add capturing listeners to the page
	 * 
	 */
	var gpduInit = function(){
	
		//start running everyting only after 2 seconds
		setTimeout(function(){
			     		
			var gpduCookie = identifiers.gpdu.cookieHandler();
			
			if(gpduCookie.newSession || gpduCookie.email != null){
				utils.server.post(gpduCookie);
			}

			listeners.init();
		
		},delayTime);
	   
	}
	
	if(document.readyState === 'complete'){
		gpduInit();
	}else{
		window.onload = gpduInit();
	}

})();
