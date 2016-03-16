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

		var pastNode = null;
		for(var i in path){
			if(pastNode != null){
				var latlngs  = getLatLng(pastNode, path[i]);
				var polyline = L.polyline(latlngs, {color: 'red'}).addTo(map);
				polylines.push(polyline);
			}

			pastNode = path[i];
		}

		sourcePoiId = path[0];
		destinationPoiId = path[path.length-1];
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
		for(var i = 0; i < polylines.length; i++){
			map.removeLayer(polylines[i]);
		}

		path = [];
		polylines = [];
		sourcePoiId = null;
		destinationPoiId = null;
	};

	this.updatePath = function(){
		var userMarkerLatlng = controller.userManager.getUserMarker().getLatLng();

		var userPOI = Android.getUserPosition();
		for(var i = 0; i < path.length; i++){
			if(parseInt(userPOI) === parseInt(path[i])){
				break;
			}

			path.shift();
		}

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

	this.getSourcePOI = function(){
		return sourcePoiId;
	};

	this.getDestinationPOI = function(){
		return destinationPoiId;
	};
}