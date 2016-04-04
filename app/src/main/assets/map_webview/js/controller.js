var map;

function Controller(){

	/* Managers */
	this.userManager = null;
	this.poiManager = null;
	this.floorManager = null;
	this.pathManager = null;

	/* Global settings */
	this.mapHeight = 0;
	this.mapWidth = 0;
	this.offsetY = 0;
	this.offsetX = 0;

	/* Initiliazes the map upon opening the webview */
	this.initialize = function(){
		var self = this;

		/* Setting the Leaflet map frame*/
		function setMap(){
			var MIN_ZOOM = -2, MAX_ZOOM = 0, INIT_ZOOM = -1;
			var INIT_POSITION_X = 0, INIT_POSITION_Y = 0;

			// var south = -1500, east = 1500, north = 1500, west = -1500;
			// self.mapWidth = 1500;
			// self.mapHeight = 1500;

			var floorJSON = self.floorManager.getJSON();
			var maxWidth = 0; maxHeight = 0;
			for(var i = 0; i < floorJSON.length; i++){
				var floor = floorJSON[i];

				if(maxWidth < parseInt(floor["floor_width"])){
					maxWidth = floor["floor_width"];
				}

				if(maxHeight < parseInt(floor["floor_height"])){
					maxHeight = floor["floor_height"];
				}
			}

			self.mapWidth = maxWidth/2 + 275;
			self.mapHeight = maxHeight/2 + 275;

			south = -self.mapHeight; east = self.mapWidth; north = self.mapHeight; west = -self.mapWidth;

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

		/* After initializing Floor Manager, set Floor View to first floor */
		function setViewToFirstFloor(){
			var floors = self.floorManager.getFloorsArr();
			
			if(Android.getCurrentFloor() != null){
				var lngLat = JSON.parse(Android.getCurrentView());
				var zoomLevel = parseInt(Android.getZoomLevel());

				self.floorManager.clickFloor(Android.getCurrentFloor());
				map.setView(lngLat, zoomLevel);
			}else{
				var first_floor = floors[0];

				self.floorManager.clickFloor(first_floor.num);
				self.mapManager.setCurrentView();
				self.mapManager.setZoomLevel();
			}
		}

		this.pathManager = new PathManager();
		this.poiManager = new POIManager();
		this.floorManager = new FloorManager();
		this.mapManager = new MapManager();
		this.userManager = new UserManager();
		
		this.floorManager.initialize();
		setMap();

		this.mapManager.initialize();
		this.floorManager.initializeFollowUp();
		this.poiManager.initialize();
		setViewToFirstFloor();
		this.poiManager.setPOIs();

		//Center to user position, if the correct floor is shown
		var userPOI = Android.getUserPosition();
		this.userManager.centerToUserPOI(userPOI);

		if(Android.isInMode()){
			this.startNavigation();
		}

		console.log('Webview initialized');
		Android.initialized();
	};

	/* Called by Android to start navigation mode */
	this.startNavigation = function(){
		this.pathManager.drawPath();

		var poiElements = this.poiManager.getPOIElements();

		for(var i = 0; i < poiElements.length; i++){
			var marker = poiElements[i];
			marker.closePopup();
		}

		this.poiManager.changePopupContent();

		this.floorManager.showUserLocatedFloor();
		this.poiManager.clickPOI(Android.getUserPosition());
	};

	/* Called by Android to cancel navigation mode */
	this.cancelNavigation = function(){
		var poiElements = this.poiManager.getPOIElements();

		for(var i = 0; i < poiElements.length; i++){
			var marker = poiElements[i];
			marker.closePopup();
		}

		this.poiManager.changeDestinationPOIIcon({
			imagePath: 'js/images/marker-icon-2x.png'
		});

		this.pathManager.deletePath();
		this.poiManager.changePopupContent();
	};

	/* Called by Android to update user position */
	this.updateUserMarker = function(){
		console.log('controller.updateUserMarker()');
		this.userManager.updateUserMarker();
		
		if(Android.isInMode()){
			this.pathManager.updatePath();
		}
		
		this.poiManager.changePopupContent();
		this.poiManager.clickPOI(Android.getUserPosition());
	};

	/* Called by Android to set floor to user position */
	this.changeToUserLocationFloor = function(){
		this.floorManager.showUserLocatedFloor();

		//if(Android.isInStorylineMode()){
			this.poiManager.clickPOI(Android.getUserPosition());
		//}
	};

	/* Called by Android to display floor and view of specific POI */
	this.changeToPOIFloor = function(poiID){
		console.log("controller.changeToPOIFloor - poiID: " + poiID);
		var poisJSON = this.poiManager.getPOISJSON();
		var floor;

		for(var i = 0; i < poisJSON.length; i++){
			var poi = poisJSON[i];

			if(parseInt(poi["_id"]) == parseInt(poiID)){
				floor = parseInt(poi["floor"]);
				break;
			}
		}

		this.floorManager.clickFloor(floor);
		this.poiManager.clickPOI(poiID);
	};
}

var controller = new Controller();
controller.initialize();
