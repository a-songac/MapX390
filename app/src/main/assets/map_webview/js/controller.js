var map;

function Controller(){
	this.floorsJSON = [];
	this.mapHeight = 0;
	this.mapWidth = 0;
	this.currentFloor = 0;
	this.poisJSON = [];
	this.currentPOIs = [];
	this.languageJSON = {};
	this.startingPOIID = -1;
	this.endingPOIID = -1;
	this.inNavigation = false;
	this.offsetY = 0;
	this.offsetX = 0;
	this.mapHeight = 0;
	this.mapWidth = 0;
	this.floorsOverlay = [];
	this.userMarker;

	this.demoPOI = null; //TO REMOVE AFTER DEMO 2

	/* Initiliazes the map upon opening the webview */
	this.initialize = function(options){
		var self = this;

		this.poisJSON = JSON.parse(Android.getPOIsJSON());
		this.floorsJSON = JSON.parse(Android.getFloorsJSON());
		this.languageJSON = JSON.parse(Android.getLanguageJSON());

		/* TEST DATA */
		// this.floorsJSON = [
		// 	{
		//       "floor_num" : "1",
		//       "floor_path" : "tiles/floor_1.jpg",
		//       "floor_width" : 1796,
		//       "floor_height" : 857
		//     },
		//     {
		//       "floor_num" : "2",
		//       "floor_path" : "tiles/floor_2.png",
		//       "floor_width" : 2066,
		//       "floor_height" : 1032
		//     },
		//     {
		//       "floor_num" : "3",
		//       "floor_path" : "tiles/floor_3.png",
		//       "floor_width" : 2060,
		//       "floor_height" : 1038
		//     },
		//     {
		//       "floor_num" : "4",
		//       "floor_path" : "tiles/floor_4.png",
		//       "floor_width" : 2076,
		//       "floor_height" : 1046
		//     },
		//     {
		//       "floor_num" : "5",
		//       "floor_path" : "tiles/floor_5.png",
		//       "floor_width" : 2050,
		//       "floor_height" : 1030
		//     }
		// ];

		// this.poisJSON = [
		// 	{
		//       "_id": "1",
		//       "title": "POI_1",
		//       "type": "exposition",
		//       "sub_type": "null",
		//       "floor": "1",
		//       "x_coord": "71",
		//       "y_coord": "91"
		//     },
		//     {
		//       "_id": "2",
		//       "title": "POI_2",
		//       "type": "exposition",
		//       "sub_type": "null",
		//       "floor": "2",
		//       "x_coord": "500",
		//       "y_coord": "100"
		//     }
		// ];

		// this.languageJSON = {
		// 	"mapx-poi-button":"Go To Destination"
		// };
		/* END TEST DATA */

		/* Set the map frame: Map Size, Map Controls*/
		function setMap(){
			var MIN_ZOOM = -2, MAX_ZOOM = 0, INIT_ZOOM = -1;
			var INIT_POSITION_X = 0, INIT_POSITION_Y = 0;

			var south = -1100, east = 500, north = 1100, west = -500;
			self.mapWidth = 500;
			self.mapHeight = 1050;
			//var north = self.floorsJSON[0]["floor_width"];  
			//var west = self.floorsJSON[0]["floor_height"]; 

			//Map settings
			map = L.map('map', {
		        minZoom: MIN_ZOOM,
		        maxZoom: MAX_ZOOM,
		        zoomControl: false , //Don't change this; we are setting it to false because we will be adding a new one
		        crs: L.CRS.Simple //Don't Change this; don't know what it does, but API says to not touch this if we don't understand it
		    });

		    map.setView([INIT_POSITION_X, INIT_POSITION_Y], INIT_ZOOM);

			new L.Control.Zoom({ position: 'bottomright' }).addTo(map);
	   		map.setMaxBounds(new L.LatLngBounds([south, west], [north, east])); 
		}

		/* Upon initialization, "manually" overlay the first image */
		function setFloorImagesOverlay(){
		    try{
		    	for(var i = 0; i < self.floorsJSON.length; i++){
		    		var imageUrl;
			    	var west = -parseInt(self.floorsJSON[i]["floor_height"])/2;
			    	var north = parseInt(self.floorsJSON[i]["floor_width"])/2;
					var east = parseInt(self.floorsJSON[i]["floor_height"])/2;
					var south = -parseInt(self.floorsJSON[i]["floor_width"])/2;

					imageUrl = self.floorsJSON[i]["floor_path"];

					var imageBounds = [[south, west], [north, east]];
			    	var imageOverlay = L.imageOverlay(imageUrl, imageBounds); 
			    	imageOverlay.addTo(map);
			    	imageOverlay.setOpacity(0);
			    	self.floorsOverlay.push({
			    		leafletObj:imageOverlay,
			    		north: north,
			    		east: east,
			    		imageUrl: imageUrl
			    	}); 
		    	}

		    	self.currentFloor = 1;
		    	var currentImageOverlay = self.floorsOverlay[self.currentFloor-1];

				self.offsetX = self.mapWidth - currentImageOverlay["east"];
				self.offsetY = self.mapHeight - currentImageOverlay["north"];

				currentImageOverlay.leafletObj.setOpacity(1);
			}

			catch(err){
				alert(err);
			}
		}

		/* Function will create the floor controller UI element*/
		function createFloorControlUI(){
			var levels = self.floorsJSON.length;

			var levelControlContainer = document.createElement("div");
			$(levelControlContainer).addClass("leaflet-control leaflet-bar");

			//Find the current zoom control container and create a level control element in it
			$(".leaflet-bottom.leaflet-right").prepend(levelControlContainer); 

			//Loop for creating every floor button
			for(var i = 0; i < levels; i++){
				var levelControl = document.createElement("a");
				$(levelControl).prop("href", "#");
				$(levelControl).text(i+1);

				//First floor has to have the selected css
				if(i === 0){
					$(levelControl).css("background-color", "#ccc");
				}

				//Add a click function to every element
				$(levelControl).click(function(){
					//Unselect current floor
					$("div.leaflet-control.leaflet-bar").find("a").css("background-color", "");

					//Select current floor
					$(this).css("background-color", "#ccc");

					//Replace current floor img source with new floor img source
					var currentImageOverlay = self.floorsOverlay[self.currentFloor-1];
					currentImageOverlay.leafletObj.setOpacity(0);

					// var imgOverlayElement = $("img.leaflet-image-layer.leaflet-zoom-animated")[0];
					var level = parseInt($(this).text());
					var newImageOverlay = self.floorsOverlay[level-1];

					self.offsetX = self.mapWidth - newImageOverlay["east"];
					self.offsetY = self.mapHeight - newImageOverlay["north"];

					newImageOverlay.leafletObj.setOpacity(1);

					// $(imgOverlayElement).prop("src", self.floorsJSON[level-1]["floor_path"]);

					self.currentFloor = level;
					self.removePOIs();
					self.setPOIs();
					if(self.inNavigation){
						self.changeStartAndEndPOIIcons('js/images/pin1.png'); 
						self.changePopupContent();
					}
				});

				//Prepend the floor button to the floor control element
				$(levelControlContainer).prepend(levelControl);
			}

		}

		setMap();
		setFloorImagesOverlay();
		createFloorControlUI();
		self.setPOIs();

//		if(Android.hasUserPosition()){
//			self.updateUserMarker();
//		}
	};

	/* newJSONs takes in a JSON that has "poi" and "language" attributes; Android must set two JSONs within this JSON.*/
	this.changeLanguage = function(newJSONs){
		try{
			if(!newJSONs){
				throw "Error in function: changeLanguage \nVariable: newJSONs \nMessage: newJSONs is null";
			}

			this.languageJSON = newJSONs["language"];
			this.poisJSON = newJSONs["poi"];

			this.removePOIs();
			this.setPOIs();
		}

		catch(error){
			alert(error); //Dev only
			//console.log(error); //Prod only
			//Send a message to Android perhasp?
		}
	};

	/* Display the POIs related to the current floor on the map */
	this.setPOIs = function(){
		var normalIcon = L.icon({
		    iconUrl: 'js/images/marker-icon-2x.png',
		    iconSize:    [41, 41],
			iconAnchor:  [20, 41],
			popupAnchor: [1, -34]
		});

		for(var i = 0; i < this.poisJSON.length; i++){
			var buttonLabel;

			if(this.inNavigation){
				buttonLabel = this.languageJSON["web_change_destination"];
			}else{
				buttonLabel = this.languageJSON["web_go_to_destination"];
			}

			var poi = this.poisJSON[i];
			if(parseInt(this.currentFloor) === parseInt(poi["floor"])){
				var popupContent = "<p id='mapx-poi-title'>"+ poi["title"] +"</p><button id='mapx-poi-button' data-poi-title='"+ poi["title"] +"' data-poi-id='"+ poi["_id"]+"' onclick='controller.navigateToPOI(this)'>" + buttonLabel + "</button>";

				var x = -this.mapWidth + (this.offsetX + parseInt(poi["x_coord"]));
				var y = -this.mapHeight + (this.offsetY + parseInt(poi["y_coord"]));
				var marker = L.marker([y,x]).addTo(map);
				marker.setIcon(normalIcon);
				marker.bindPopup(popupContent);
				marker.poiID = poi["_id"];
				marker.poiTitle = poi["title"];
				this.currentPOIs.push(marker);
			}
		}
	};

	this.changePopupContent = function(){
		for(var i = 0; i < this.poisJSON.length; i++){
			var buttonLabel, javascriptMethod;

			if(this.inNavigation){
				buttonLabel = this.languageJSON["web_change_destination"];
				javascriptMethod = "onclick='controller.navigateToPOI(this)'";
			}else{
				buttonLabel = this.languageJSON["web_go_to_destination"];
				javascriptMethod =  "onclick='controller.navigateToPOI(this)'";
			}

			var marker = this.currentPOIs[i];
			var popupContent;

			if(parseInt(marker.poiID) == parseInt(this.startingPOIID) || parseInt(marker.poiID) == parseInt(this.endingPOIID)){
				popupContent = "<p id='mapx-poi-title'>"+ marker.poiTitle +"</p>";
			}else{
				popupContent = "<p id='mapx-poi-title'>"+ marker.poiTitle +"</p><button id='mapx-poi-button' data-poi-title='"+  marker.poiTitle +"' data-poi-id='"+  marker.poiID +"' " + javascriptMethod + ">" + buttonLabel + "</button>";
			}

			marker.unbindPopup();
			marker.bindPopup(popupContent);
		}
	};

	/* Remove the current POIs displayed on the map */
	this.removePOIs = function(){
		for(var i = 0; i < this.currentPOIs.length; i++){
			map.removeLayer(this.currentPOIs[i]);
		}

		this.currentPOIs = [];
	};

	/* Send call to Android to initiate a navigation to the selected POI */
	this.navigateToPOI = function(elementClicked){
		this.demoPOI = $(elementClicked).attr("data-poi-id");
		Android.navigateToPOI(this.demoPOI);
	};

	/* Called by Android when it has create the path to be done. Options variable is current dummy variable to remind that Android also has to send the path*/
	this.startNavigation = function(){
		try{
			var path = [this.demoPOI,this.demoPOI]; //we'll need a call Android.getPath(); OR find a way to receive data
			
			if(!path){
				throw "Error in function: startNavigation \nVariable: path \nMessage: Path is either null or has a length of 0";
			}

			this.inNavigation = true;
			this.startingPOIID = path[0];
			this.endingPOIID = path[path.length-1];
			this.changeStartAndEndPOIIcons('js/images/pin1.png');
			//Add path creation here in Sprint 3

			for(var i = 0; i < this.currentPOIs.length; i++){
				var marker = this.currentPOIs[i];
				marker.closePopup();
			}

			this.changePopupContent();
		}

		catch(error){
			alert(error); //Dev only
			//console.log(error); //Prod only
			//Send a message to Android perhasp?
		}
	};

	/* Called by Android when the navigation to a POI is cancelled */
	this.cancelNavigation = function(){
		for(var i = 0; i < this.currentPOIs.length; i++){
			var marker = this.currentPOIs[i];
			marker.closePopup();
		}
		
		this.changeStartAndEndPOIIcons('js/images/marker-icon-2x.png');
		this.inNavigation = false;
		this.startingPOIID = -1;
		this.endingPOIID = -1;
		this.changePopupContent();

		//Add path deletion here in Sprint 3
	};

	/* Change POI icon of Starting and Ending POIs */
	this.changeStartAndEndPOIIcons = function(imagePath){
		for(var i = 0; i < this.currentPOIs.length; i++){
			var marker = this.currentPOIs[i];

			if(parseInt(marker.poiID) == parseInt(this.startingPOIID) || parseInt(marker.poiID) == parseInt(this.endingPOIID)){
				//The values before for positioning were taken from the src code of LeafletJS for the default icon positioning
				var normalIcon = L.icon({
				    iconUrl: imagePath,
				    iconSize:    [41, 41],
					iconAnchor:  [20, 41],
					popupAnchor: [1, -34]
				});

				marker.setIcon(normalIcon);
			}
		}
	};

	this.updateUserMarker = function(){
		var latLng = Android.getUserPosition();
		
		/*TEST DATA*/
		// var latLng = {
		// 	lat:25,
		// 	lng:25
		// };

		var x = -this.mapWidth + (this.offsetX + parseInt(latLng["lng"]));
		var y = -this.mapHeight + (this.offsetY + parseInt(latLng["lat"]));

		this.setUserMarker(x, y);
	};

	this.setUserMarker = function(x, y){
		if(!this.userMarker){
			this.userMarker = L.circleMarker(
				[y, x], 
				{
					clickable: false,
				}
			);

			this.userMarker.addTo(map);

			/* 
			 * Leaflet has a weird behavior for drawn components, where they 
			 * will change size depending on your zoom level. This will keep
			 * the components the same size at all levels
			*/
			
			var myZoom = {
			  start:  map.getZoom(),
			  end: map.getZoom()
			};

			map.on('zoomstart', function(e) {
			   myZoom.start = map.getZoom();
			});

			map.on('zoomend', function(e) {
			    myZoom.end = map.getZoom();
			    var diff = myZoom.start - myZoom.end;
			    if (diff > 0) {
			        controller.userMarker.setRadius(controller.userMarker.getRadius() / 2);
			    } else if (diff < 0) {
			        controller.userMarker.setRadius(controller.userMarker.getRadius() * 2);
			    }
			});
		}else{
			this.userMarker.setLatLng([y, x]);
		}
	};
}

var controller = new Controller();
controller.initialize();
