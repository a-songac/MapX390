var map;

function Controller(){
	this.floorsJSON = [];
	this.mapHeight = 0;
	this.mapWidth = 0;
	this.currentFloor = 0;
	this.poisJSON = [];
	this.currentPOIs = [];
	this.languageJSON = {};

	/* Initiliazes the map upon opening the webview */
	this.initialize = function(options){
		var self = this;

		//this.poisJSON = Android.getPOIsJSON();
		//this.floorsJSON = Android.getFloorsJSON();
		//this.languageJSON = Android.getLanguageJSON();

		/* TEST DATA */
		this.floorsJSON = [
			{
		      "floor_num" : "1",
		      "floor_path" : "tiles/floor_1.jpg",
		      "floor_width" : 857,
		      "floor_height" : 1796
		    },
		    {
		      "floor_num" : "2",
		      "floor_path" : "tiles/floor_2.png",
		      "floor_width" : 857,
		      "floor_height" : 1796
		    },
		    {
		      "floor_num" : "3",
		      "floor_path" : "tiles/floor_3.png",
		      "floor_width" : 857,
		      "floor_height" : 1796
		    },
		    {
		      "floor_num" : "4",
		      "floor_path" : "tiles/floor_4.png",
		      "floor_width" : 857,
		      "floor_height" : 1796
		    },
		    {
		      "floor_num" : "5",
		      "floor_path" : "tiles/floor_5.png",
		      "floor_width" : 857,
		      "floor_height" : 1796
		    }
		];

		this.poisJSON = [
			{
		      "_id": "1",
		      "title": "POI_1",
		      "type": "exposition",
		      "sub_type": "null",
		      "floor": "1",
		      "x_coord": "75",
		      "y_coord": "100"
		    },
		    {
		      "_id": "2",
		      "title": "POI_2",
		      "type": "exposition",
		      "sub_type": "null",
		      "floor": "2",
		      "x_coord": "500",
		      "y_coord": "100"
		    }
		];

		this.languageJSON = {
			"mapx-poi-button":"Go To Destination"
		};
		/* END TEST DATA */

		/* Set the map frame: Map Size, Map Controls*/
		function setMap(){
			var MIN_ZOOM = -1, MAX_ZOOM = 2, INIT_ZOOM = 0;
			var INIT_POSITION_X = 0, INIT_POSITION_Y = 0;

			var south = 0, east = 0;
			var north = self.floorsJSON[0]["floor_width"];  
			var west = self.floorsJSON[0]["floor_height"]; 

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
		function setFirstFloorImageOverlay(){
		    try{
		    	var imageUrl;
		    	var south = 0, east = 0;
				var north = self.floorsJSON[0]["floor_width"];  
				var west = self.floorsJSON[0]["floor_height"]; 


			    if(self.floorsJSON.length !== 0){
			    	imageUrl = self.floorsJSON[0]["floor_path"];
			    }else{
			    	throw "No floor plans available!";
			    }

			    var imageBounds = [[south, west], [north, east]];
			    L.imageOverlay(imageUrl, imageBounds).addTo(map);

			    self.currentFloor = 1;
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
					var imgOverlayElement = $("img.leaflet-image-layer.leaflet-zoom-animated")[0];
					var level = parseInt($(this).text());
					$(imgOverlayElement).prop("src", self.floorsJSON[level-1]["floor_path"]);

					self.currentFloor = level;
					self.removePOIs();
					self.setPOIs();
				});

				//Prepend the floor button to the floor control element
				$(levelControlContainer).prepend(levelControl);
			}

		}

		setMap();
		setFirstFloorImageOverlay();
		createFloorControlUI();
		self.setPOIs();
	};

	/* newJSONs takes in a JSON that has "poi" and "language" attributes; Android must set two JSONs within this JSON. See test data below as example*/
	this.changeLanguage = function(newJSONs){
		// /* TEST DATA */
		// newJSONs = {
		// 	"poi":[
		// 		{
		// 	      "_id": "1",
		// 	      "title": "POI_1_FR",
		// 	      "type": "exposition",
		// 	      "sub_type": "null",
		// 	      "floor": "1",
		// 	      "x_coord": "75",
		// 	      "y_coord": "100"
		// 	    },
		// 	    {
		// 	      "_id": "2",
		// 	      "title": "POI_2_FR",
		// 	      "type": "exposition",
		// 	      "sub_type": "null",
		// 	      "floor": "2",
		// 	      "x_coord": "500",
		// 	      "y_coord": "100"
		// 	    }
		// 	],
		// 	"language":
		// 	{
		// 		"mapx-poi-button":"Aller vers destination"
		// 	}
		// };
		// /*TEST DATA */

		this.languageJSON = newJSONs["language"];
		this.poisJSON = newJSONs["poi"];

		this.removePOIs();
		this.setPOIs();
	};

	/* Display the POIs related to the current floor on the map */
	this.setPOIs = function(){
		for(var i = 0; i < this.poisJSON.length; i++){
			var poi = this.poisJSON[i];
			if(parseInt(this.currentFloor) === parseInt(poi["floor"])){
				var popupContent = "<p id='mapx-poi-title'>"+ poi["title"] +"</p><button id='mapx-poi-button' onclick='navigateToPOI(\"" + poi["title"] + "\")'>" + this.languageJSON["mapx-poi-button"] + "</button>";

				var marker = L.marker([poi["y_coord"], poi["x_coord"]]).addTo(map);
				marker.bindPopup(popupContent);
				this.currentPOIs.push(marker);
			}
		}
	};

	/* Remove the current POIs displayed on the map */
	this.removePOIs = function(){
		for(var i = 0; i < this.currentPOIs.length; i++){
			map.removeLayer(this.currentPOIs[i]);
		}

		this.currentPOIs = [];
	};
}

var controller = new Controller();
controller.initialize();

// /*TEST*/
// controller.changeLanguage(null);
// /*TEST*/

function navigateToPOI(poiTitle) {

	Android.navigateToPOI(poiTitle);

}

