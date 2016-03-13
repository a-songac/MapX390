var map;

function Controller(){

	this.mapHeight = 0;
	this.mapWidth = 0;
	this.offsetY = 0;
	this.offsetX = 0;
	this.mapHeight = 0;
	this.mapWidth = 0;
	this.userMarker;

	/* Initiliazes the map upon opening the webview */
	this.initialize = function(options){
		var self = this;

		/* Set the map frame: Map Size, Map Controls*/
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

		function setFloorImagesOverlay(){
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

		setFloorImagesOverlay();

		this.poiManager = new POIManager();
		this.poiManager.initialize({
			mapHeight: this.mapHeight,
			mapWidth: this.mapWidth,
			currentFloor: this.floorManager.getCurrentFloor(),
			offsetY: this.offsetY,
			offsetX: this.offsetX
		});

		if(Android.isInMode()){
			this.startNavigation();
		}
		
	};

	/* Send call to Android to initiate a navigation to the selected POI */
	this.navigateToPOI = function(elementClicked){
		this.demoPOI = $(elementClicked).attr("data-poi-id");
		Android.navigateToPOI(this.demoPOI);
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
			imagePath: 'js/images/marker-icon-2x.png',
			pathManager: this.pathManager
		});

		this.pathManager.deletePath();

		this.poiManager.changePopupContent({
			pathManager: this.pathManager
		});

		//Add path deletion here in Sprint 3
	};

	this.updateUserMarker = function(){
		var userPOI = Android.getUserPosition();
		var poisJSON = this.poiManager.getPOISJSON();
		var currentFloor = this.floorManager.getCurrentFloor();
		var latLng;

		if(userPOI == null){
			return;
		}

		for(var i = 0; i < poisJSON.length; i++){

			var poi = poisJSON[i];
			if(parseInt(currentFloor) == parseInt(poi["floor"]) && parseInt(poi["_id"]) == parseInt(userPOI) ){
				var x = -this.mapWidth + (this.offsetX + parseInt(poi["x_coord"]));
				var y = -this.mapHeight + (this.offsetY + parseInt(poi["y_coord"]));
				latLng = [y,x];
				break;
			}
		}

		console.log(latLng);

		this.setUserMarker(latLng);
	};

	this.setUserMarker = function(latLng){
		if(!latLng){
			latLng = [-10000, -10000]
		}

		if(!this.userMarker){
			this.userMarker = L.circleMarker(
				latLng,
				{
					clickable: false,
					radius: 10,
					color: 'red'
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
				this.userMarker.setLatLng(latLng);
		}
	};

	this.changeToUserLocationFloor = function(){
		this.floorManager.showUserLocatedFloor();
	};

	this.floorClicked = function(opts){
		var updatedFloorOverylay = opts.updatedFloorOverlay;

		this.offsetX = this.mapWidth - updatedFloorOverylay["east"];
		this.offsetY = this.mapHeight - updatedFloorOverylay["north"];

		this.poiManager.removePOIs();
		this.poiManager.setPOIs({
			mapHeight: this.mapHeight,
			mapWidth: this.mapWidth,
			currentFloor: this.floorManager.getCurrentFloor(),
			offsetY: this.offsetY,
			offsetX: this.offsetX
		});

		this.updateUserMarker();

		if(Android.isInMode()){
			this.poiManager.changeDestinationPOIIcon({
				imagePath: 'js/images/pin1.png',
				pathManager: this.pathManager
			});

			this.poiManager.changePopupContent({
				pathManager: this.pathManager
			});

			this.pathManager.deletePath();
			this.pathManager.drawPath({
				currentFloor:this.floorManager.getCurrentFloor(),
				offsetX: this.offsetX,
				offsetY: this.offsetY,
				poiManager: this.poiManager
			});
		}
	};
}

var controller = new Controller();
controller.initialize();
