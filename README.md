# gapidu
Gapidu allows marketers automatically store website visitors' identifiers in a DB and use them to asynchronously send data from marketo/salesforce to Google Analytics.

Implementation steps:

	Upload JAR to server and launch it
	Retrive the tracking servlet URL and insert it to the configuration in tracking.min.js
	Insert the tracking.min.js to all your website pages
	
	
	
About the parts:
	tracking.min.js
		It's an asynchronously javascript..
		