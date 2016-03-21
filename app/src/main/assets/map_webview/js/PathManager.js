function PathManager(){
	var polylines = [];
	var sourcePoiId, destinationPoiId;
	var path = [];

	this.drawPath = function(){
		var currentFloor = controller.floorManager.getCurrentFloor();
		var offsetX = controller.offsetX;
		var offsetY = controller.offsetY;
		var poisJSON = controller.poiManager.getPOISJSON();
		var mapWidth = controller.mapWidth;
		var mapHeight = controller.mapHeight;

		if(path.length === 0){
			var pathJSON = Android.getPath();

			if(!pathJSON){
				console.log("Error in function: startNavigation \nVariable: path \nMessage: Path is either null or has a length of 0");
				return;
			}

			path = JSON.parse(pathJSON);
		}

		if(polylines.length != 0){
			deletePolylines();
		}

		var pastNode = null;
		for(var i in path){
			if(pastNode != null){
				var latlngs  = getLatLng(pastNode, path[i]);
				
				if(latlngs.length != 2){
					pastNode = path[i];
					continue;
				}

				var polyline = L.polyline(latlngs, {color: 'red'}).addTo(map);
				polylines.push(polyline);
			}

			pastNode = path[i];
		}

		sourcePoiId = path[0];

		if(Android.isInNavigationMode()){
			destinationPoiId = path[path.length-1];
		}else{
			destinationPoiId = controller.poiManager.getNextPOI();
		}

		controller.poiManager.changeDestinationPOIIcon({
			imagePath : 'js/images/pin1.png'
		}); //TO CHANGE

		function getLatLng(pastNode, currentNode){
			var latLng = [];
			for(var i = 0; i < poisJSON.length; i++){

				var poi = poisJSON[i];
				if(parseInt(currentFloor) == parseInt(poi["floor"]) && ( parseInt(poi["_id"]) == parseInt(currentNode) || parseInt(poi["_id"]) == parseInt(pastNode) ) ){
					var x = -mapWidth + (offsetX + parseInt(poi["x_coord"]));
					var y = -mapHeight + (offsetY + parseInt(poi["y_coord"]));
					latLng.push([y,x]);
					continue;
				}

				if(latLng.length == 2){
					break;
				}
			}
			return latLng;
		}
	};

	this.deletePath = function(){
		deletePolylines();

		path = [];
		polylines = [];
		sourcePoiId = null;
		destinationPoiId = null;
	};

	function deletePolylines(){
		for(var i = 0; i < polylines.length; i++){
			map.removeLayer(polylines[i]);
		}
	}

	this.updatePath = function(){
		console.log('updating path');
		var userMarkerLatlng = controller.userManager.getUserMarker().getLatLng();

		var userPOI = Android.getUserPosition();
		while(path.length !== 0){
			if(parseInt(userPOI) === parseInt(path[0])){
				break;
			}

			path.shift();
		}

		sourcePoiId = path[0];
		
		if(Android.isInStorylineMode()){
			controller.poiManager.changeDestinationPOIIcon({
				imagePath: 'js/images/marker-icon-2x.png',
			});

			destinationPoiId = controller.poiManager.getNextPOI();

			controller.poiManager.changeDestinationPOIIcon({
				imagePath : 'js/images/pin1.png'
			}); 
		}

		var stringPath = [];
		for(var i = 0; i < path.length; i++){
			stringPath.push(path[i].toString());
		}

		Android.setPath(stringPath);

		while(polylines.length !== 0){
			var pathElement = polylines[0];
			var latLngs = pathElement.getLatLngs();

			if(parseInt(userMarkerLatlng.lng) === parseInt(latLngs[0].lng) && parseInt(userMarkerLatlng.lat) === parseInt(latLngs[0].lat)){
				break;
			}

			map.removeLayer(polylines[0]);
			polylines.shift();
		}
	};

	this.getPath = function(){
		return path;
	};

	this.getSourcePOI = function(){
		return sourcePoiId;
	};

	this.getDestinationPOI = function(){
		return destinationPoiId;
	};
}