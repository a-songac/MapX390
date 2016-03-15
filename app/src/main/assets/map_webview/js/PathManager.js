function PathManager(){
	var polylines = [];
	var sourcePoiId, destinationPoiId;

	this.drawPath = function(){
		var currentFloor = controller.floorManager.getCurrentFloor();
		var offsetX = controller.offsetX;
		var offsetY = controller.offsetY;
		var poisJSON = controller.poiManager.getPOISJSON();
		var mapWidth = controller.mapWidth;
		var mapHeight = controller.mapHeight;

		var pathJSON = Android.getPath();

		if(!pathJSON){
			console.log("Error in function: startNavigation \nVariable: path \nMessage: Path is either null or has a length of 0");
			return;
		}

		var path = JSON.parse(pathJSON);

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

		polylines = [];
		sourcePoiId = null;
		destinationPoiId = null;
	};

	this.getSourcePOI = function(){
		return sourcePoiId;
	};

	this.getDestinationPOI = function(){
		return destinationPoiId;
	};
}