var map;

function Controller(){
	this.floorsJSON = [];
	this.mapHeight = 0;
	this.mapWidth = 0;
	this.currentFloor = 0;
	this.poisJSON = [];
	this.currentPOIs = [];

	/* Initiliazes the map upon opening the webview */
	this.initialize = function(options){
		var self = this;

		//this.poisJSON = Android.getPOIsJSON();
		//this.floorsJSON = Android.getFloorsJSON();

		/* TEST DATA */
		this.floorsJSON = [
			{
		      "floor_num" : "1",
		      "floor_path" : "tiles/floor_1.jpg",
		      "floor_width" : 700
		    },
		    {
		      "floor_num" : "2",
		      "floor_path" : "tiles/floor_2.png"
		    },
		    {
		      "floor_num" : "3",
		      "floor_path" : "tiles/floor_3.png"
		    },
		    {
		      "floor_num" : "4",
		      "floor_path" : "tiles/floor_4.png"
		    },
		    {
		      "floor_num" : "5",
		      "floor_path" : "tiles/floor_5.png"
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
		/* END TEST DATA */

		/* Set the map frame: Map Size, Map Controls*/
		function setMap(){
			var MIN_ZOOM = 0, MAX_ZOOM = 3, INIT_ZOOM = 1;
			var INIT_POSITION_X = 0, INIT_POSITION_Y = 0;

			var south = 0, east = 0;
			var north = 857;  //TEST DATA
			var west = 1796;  //TEST DATA

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
				var north = 857;  //TEST DATA
				var west = 1796;  //TEST DATA


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
					removePOIs();
					setPOIs();
				});

				//Prepend the floor button to the floor control element
				$(levelControlContainer).prepend(levelControl);
			}

		}

		/* Display the POIs related to the current floor on the map */
		function setPOIs(){
			for(var i = 0; i < self.poisJSON.length; i++){
				var poi = self.poisJSON[i];
				if(parseInt(self.currentFloor) === parseInt(poi["floor"])){
					var popupContent = "<p class='mapx-poi-title'>"+ poi["title"] +"</p><p>Hello World</p>";

					var marker = L.marker([poi["y_coord"], poi["x_coord"]]).addTo(map);
					marker.bindPopup(popupContent);
					self.currentPOIs.push(marker);
				}
			}
		}

		/* Remove the current POIs displayed on the map */
		function removePOIs(){
			for(var i = 0; i < self.currentPOIs.length; i++){
				map.removeLayer(self.currentPOIs[i]);
			}

			self.currentPOIs = [];
		}

		setMap();
		setFirstFloorImageOverlay();
		createFloorControlUI();
		setPOIs();
	};

	this.changeFloor = function(){};
}

var controller = new Controller();
controller.initialize();

