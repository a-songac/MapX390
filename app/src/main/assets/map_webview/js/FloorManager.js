function FloorManager(){
	var floorJSON = [];
	var floors = [];
	var currentFloor;

	this.initialize = function(){
		this.setJSON();
		this.setFloorImages();
	};

	this.setJSON = function(){
		floorJSON = JSON.parse(Android.getFloorsJSON());
	};

	this.setCurrentFloor = function(floor){
		currentFloor = floor;
	};

	this.getCurrentFloor = function(){
		return currentFloor;
	};

	this.getFloorsArr = function(){
		return floors;
	};

	this.setFloorImages = function(opts){
		for(var i = 0; i < floorJSON.length; i++){
    		var imageUrl;
	    	var west = -parseInt(floorJSON[i]["floor_width"])/2;
	    	var north = parseInt(floorJSON[i]["floor_height"])/2;
			var east = parseInt(floorJSON[i]["floor_width"])/2;
			var south = -parseInt(floorJSON[i]["floor_height"])/2;

			imageUrl = floorJSON[i]["floor_path"];

			var imageBounds = [[south, west], [north, east]];
	    	var imageOverlay = L.imageOverlay(imageUrl, imageBounds);
	    	imageOverlay.addTo(map);
	    	imageOverlay.setOpacity(0);

	    	floors.push({
	    		leafletObj:imageOverlay,
	    		north: north,
	    		east: east,
	    		imageUrl: imageUrl
	    	});
    	}
	};

}