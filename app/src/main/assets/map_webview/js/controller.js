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

			var south = -1100, east = 500, north = 1100, west = -500;
			self.mapWidth = 500;
			self.mapHeight = 1050;

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
				self.floorManager.clickFloor(1);
				self.mapManager.setCurrentView();
				self.mapManager.setZoomLevel();
			}
		}

		setMap();

		this.pathManager = new PathManager();
		this.poiManager = new POIManager();
		this.floorManager = new FloorManager();
		this.mapManager = new MapManager();
		this.userManager = new UserManager();

		this.mapManager.initialize();
		this.floorManager.initialize();
		this.poiManager.initialize();
		setViewToFirstFloor();
		this.poiManager.setPOIs();


		if(Android.isInMode()){
			this.startNavigation();
		}

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
		this.userManager.updateUserMarker();
		if(Android.isInMode()){
			controller.pathManager.updatePath();
		}
	};

	/* Called by Android to set floor to user position */
	this.changeToUserLocationFloor = function(){
		this.floorManager.showUserLocatedFloor();
	};
}

var controller = new Controller();
controller.initialize();
