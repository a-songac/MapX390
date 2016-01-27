var map;

function Controller(){
	this.floorPlans = [];
	this.mapHeight = 0;
	this.mapWidth = 0;

	this.initialize = function(options){
		var self = this;

		function setFloorPlans(){
			


		}

		function setMap(options){
			var MIN_ZOOM = 1;
			var MAX_ZOOM = 3;
			var INIT_ZOOM = 1;

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

		function setLevelsControl(){
			var levels = self.floorPlans.length;

			var zoomControlContainer = document.createElement("div");
			$(zoomControlContainer).addClass("leaflet-control leaflet-bar");
			$(".leaflet-bottom.leaflet-right").prepend(zoomControlContainer);

			for(var i = 0; i < levels; i++){
				var levelControl = document.createElement("a");
				$(levelControl).prop("href", "#");
				$(levelControl).text(i+1);
				$(levelControl).click(function(){
					var imgOverlayElement = $("img.leaflet-image-layer.leaflet-zoom-animated")[0];
					var level = parseInt($(this).text())-1;
					$(imgOverlayElement).prop("src", self.floorPlans[level]);
				});

				$(zoomControlContainer).prepend(levelControl);
			}

		}

		this.floorPlans = [
			'tiles/floor_1.jpg',
			'tiles/floor_2.png',
			'tiles/floor_3.png',
			'tiles/floor_4.png',
			'tiles/floor_5.png'
		];

		setMap({
			"south":0,
			"east":0,
			"north":500,//771.3,
			"west":1000,//1592.1,
			"init_position_x":0,
			"init_position_y":0
		});

		setLevelsControl();
		var NORTH = 500;//771.3;//this.mapHeight; //Width
		var EAST = 0;
		var SOUTH = 0;
		var WEST = 1000;//1592.1;//this.mapWidth; //Height


		

	    try{
	    	var imageUrl;

		    if(this.floorPlans.length !== 0){
		    	imageUrl = this.floorPlans[0];
		    }else{
		    	throw "No floor plans available!";
		    }

		    var imageBounds = [[SOUTH, WEST], [NORTH, EAST]];
		    L.imageOverlay(imageUrl, imageBounds).addTo(map);
		}

		catch(err){
			console.log(err);
		}
	};
}

var controller = new Controller();
controller.initialize();

