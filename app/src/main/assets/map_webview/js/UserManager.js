function UserManager(){
	var userMarker;

	this.updateUserMarker = function(){
		var userPOI = Android.getUserPosition();
		var poisJSON = controller.poiManager.getPOISJSON();
		var currentFloor = controller.floorManager.getCurrentFloor();
		var latLng;

		if(userPOI == null){
			return;
		}

		for(var i = 0; i < poisJSON.length; i++){

			var poi = poisJSON[i];
			if(parseInt(currentFloor) == parseInt(poi["floor"]) && parseInt(poi["_id"]) == parseInt(userPOI) ){
				var x = -controller.mapWidth + (controller.offsetX + parseInt(poi["x_coord"]));
				var y = controller.mapHeight - (controller.offsetY + parseInt(poi["y_coord"]));
				latLng = [y,x];
				break;
			}
		}

		this.setUserMarker(latLng);
	};

	this.setUserMarker = function(latLng){
		if(!latLng){
			latLng = [-10000, -10000]
		}

		if(!userMarker){
			userMarker = L.circleMarker(
				latLng,
				{
					clickable: false,
					radius: 10,
					color: 'red'
				}
			);

			userMarker.addTo(map);

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
			        userMarker.setRadius(userMarker.getRadius() / 2);
			    } else if (diff < 0) {
			        userMarker.setRadius(userMarker.getRadius() * 2);
			    }

			});
		}else{
				userMarker.setLatLng(latLng);
		}
	};

	this.getUserMarker = function(){
		return userMarker;
	};
}