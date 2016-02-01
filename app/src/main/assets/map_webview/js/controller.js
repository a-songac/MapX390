var map;

function Controller(){
	this.floorPlans = [];
	this.mapHeight = 0;
	this.mapWidth = 0;
	this.currentFloor = 0;
	this.poiMarkers = [];

	/* Initiliazes the map upon opening the webview */
	this.initialize = function(options){
		var self = this;

		/* TEST DATA */
		this.floorPlans = [
			'tiles/floor_1.jpg',
			'tiles/floor_2.png',
			'tiles/floor_3.png',
			'tiles/floor_4.png',
			'tiles/floor_5.png'
		];
		/* END TEST DATA */

		/* Upon initialization, "manually" overlay the first image */
		function setFirstFloorImageOverlay(NORTH, EAST, SOUTH, WEST){
		    try{
		    	var imageUrl;

			    if(self.floorPlans.length !== 0){
			    	imageUrl = self.floorPlans[0];
			    }else{
			    	throw "No floor plans available!";
			    }

			    var imageBounds = [[SOUTH, WEST], [NORTH, EAST]];
			    L.imageOverlay(imageUrl, imageBounds).addTo(map);

			    self.currentFloor = 1;
			}

			catch(err){
				alert(err);
			}
		}

		/* Set the map frame: Map Size, Map Controls*/
		function setMap(options){
			var MIN_ZOOM = -1;
			var MAX_ZOOM = 2;
			var INIT_ZOOM = -1;

			//Map settings
			map = L.map('map', {
		        maxZoom: MAX_ZOOM,
		        minZoom: MIN_ZOOM,
		        zoomControl: false , //Don't change this; we are setting it to false because we will be adding a new one
		        crs: L.CRS.Simple //Don't Change this; don't know what it does, but API says to not touch this if we don't understand it
		    });

		    map.setView([options.init_position_x, options.init_position_y], INIT_ZOOM);

			new L.Control.Zoom({ position: 'bottomright' }).addTo(map);
	   		map.setMaxBounds(new L.LatLngBounds([options.south, options.west], [options.north, options.east])); 
		}

		/* Function will set as many floor level controls as needed */
		function setLevelsControl(){
			var levels = self.floorPlans.length;

			var levelControlContainer = document.createElement("div");
			$(levelControlContainer).addClass("leaflet-control leaflet-bar");

			//Find the current zoom control container and add a level control container in it
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
					//Note: not the best way to do this... or maybe?
					$("div.leaflet-control.leaflet-bar").find("a").css("background-color", "");

					$(this).css("background-color", "#ccc");
					var imgOverlayElement = $("img.leaflet-image-layer.leaflet-zoom-animated")[0];
					var level = parseInt($(this).text());
					$(imgOverlayElement).prop("src", self.floorPlans[level-1]);

					self.currentFloor = level;
					removePOIs();
					setPOIs("TEMPORARY");
				});

				$(levelControlContainer).prepend(levelControl);
			}

		}

		/* Remove the current POIs displayed on the map */
		function removePOIs(){
			for(var i = 0; i < self.poiMarkers.length; i++){
				map.removeLayer(self.poiMarkers[i]);
			}

			self.poiMarkers = [];
			//Note to self: ^MAYBE better way is to keep a 2D array so that rather than DELETING markers, just keep them and reference and call them back on the map whenever we want?
		}

		/* Display the POIs related to the current floor on the map */
		function setPOIs(POIs){
			//Temp
			var TEMP_POIS = [{
								"floor":1,
								"x_coordinate":100,
								"y_coordinate":90,
								"title":"POI #1"
							},
							{
								"floor":1,
								"x_coordinate":410,
								"y_coordinate":210,
								"title":"POI #2"
							},
							{
								"floor":3,
								"x_coordinate":320,
								"y_coordinate":75,
								"title":"POI #3"
							}
							];

			for(var i = 0; i < TEMP_POIS.length; i++){
				var poi = TEMP_POIS[i];
				if(parseInt(self.currentFloor) === poi["floor"]){
					var marker = L.marker([poi["y_coordinate"], poi["x_coordinate"]]).addTo(map);
					self.poiMarkers.push(marker);
				}
			}
		}

		//TEST DATA
		setMap({
			"south":0,
			"east":0,
			"north":500,//771.3,
			"west":1000,//1592.1,
			"init_position_x":0,
			"init_position_y":0
		});

		setFirstFloorImageOverlay(500, 0, 0, 1000); //TEST DATA
		setLevelsControl();
		setPOIs("TEMPORARY");
	};

	this.changeFloor = function(){};
}

var controller = new Controller();
controller.initialize();

