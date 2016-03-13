var map;

function Controller(){

	/* Managers */
	this.userManager = null;
	this.poiManager = null;
	this.floorManager = null;
	this.pathManager = null;

	this.mapHeight = 0;
	this.mapWidth = 0;
	this.offsetY = 0;
	this.offsetX = 0;
	this.userMarker;

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

		function setViewToFirstFloor(){
			var floors = self.floorManager.getFloorsArr();
			
			self.floorManager.setCurrentFloor(1);
	    	var currentImageOverlay = floors[self.floorManager.getCurrentFloor()-1];

			self.offsetX = self.mapWidth - currentImageOverlay["east"];
			self.offsetY = self.mapHeight - currentImageOverlay["north"];

			currentImageOverlay.leafletObj.setOpacity(1);
		}

		setMap();

		this.pathManager = new PathManager();

		this.floorManager = new FloorManager();
		this.floorManager.initialize();

		setViewToFirstFloor();

		this.poiManager = new POIManager();
		this.poiManager.initialize();

		this.userManager = new UserManager();

		if(Android.isInMode()){
			this.startNavigation();
		}
		
	};

	/* Send call to Android to initiate a navigation to the selected POI */
	this.navigateToPOI = function(elementClicked){
		var poiID = $(elementClicked).attr("data-poi-id");
		Android.navigateToPOI(poiID);
	};

	/* Called by Android when it has create the path to be done. Options variable is current dummy variable to remind that Android also has to send the path*/
	this.startNavigation = function(){

			this.pathManager.drawPath({
				currentFloor:this.floorManager.getCurrentFloor(),
				offsetX: this.offsetX,
				offsetY: this.offsetY,
				poiManager: this.poiManager
			});

			var poiElements = this.poiManager.getPOIElements();

			for(var i = 0; i < poiElements.length; i++){
				var marker = poiElements[i];
				marker.closePopup();
			}

			this.poiManager.changePopupContent({
				pathManager: this.pathManager
			});
		
	};

	/* Called by Android when the navigation to a POI is cancelled */
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

		this.poiManager.changePopupContent({
			pathManager: this.pathManager
		});

		//Add path deletion here in Sprint 3
	};

	this.updateUserMarker = function(){
		this.userManager.updateUserMarker();
	};

	
	this.changeToUserLocationFloor = function(){
		this.floorManager.showUserLocatedFloor();
	};
}

var controller = new Controller();
controller.initialize();
