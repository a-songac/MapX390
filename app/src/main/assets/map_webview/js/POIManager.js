function POIManager(){
	var poiElements = [];
	var poisJSON = [];
	var languageJSON = [];

	this.initialize = function(opts){
		this.setJSON();
		this.setPOIs(opts);
	};

	this.setJSON = function(){
		poisJSON = JSON.parse(Android.getPOIsJSON());
		languageJSON = JSON.parse(Android.getLanguageJSON());
	};

	this.getPOISJSON = function(){
		return poisJSON;
	};

	this.setPOIs = function(opts){
		//console.log(JSON.stringify(opts));

		var normalIcon = L.icon({
		    iconUrl: 'js/images/marker-icon-2x.png',
		    iconSize:    [41, 41],
			iconAnchor:  [20, 41],
			popupAnchor: [1, -34]
		});

		var mapHeight = opts.mapHeight;
		var mapWidth = opts.mapWidth;
		var offsetX = opts.offsetX;
		var offsetY = opts.offsetY;
		var currentFloor = opts.currentFloor;

		for(var i = 0; i < poisJSON.length; i++){
			var buttonLabel;

			if(Android.isInMode()){
				buttonLabel = languageJSON["web_change_destination"];
			}else{
				buttonLabel = languageJSON["web_go_to_destination"];
			}

			var poi = poisJSON[i];
			if(parseInt(currentFloor) === parseInt(poi["floor"]) && poi["type"] != "t"){
				var popupContent = "<p id='mapx-poi-title'>"+ poi["title"] +"</p><button id='mapx-poi-button' data-poi-title='"+ poi["title"] +"' data-poi-id='"+ poi["_id"]+"' onclick='controller.navigateToPOI(this)'>" + buttonLabel + "</button>";

				var x = -mapWidth + (offsetX + parseInt(poi["x_coord"]));
				var y = -mapHeight + (offsetY + parseInt(poi["y_coord"]));
				
				var marker = L.marker([y, x]).addTo(map);
				marker.setIcon(normalIcon);
				marker.bindPopup(popupContent);
				marker.poiID = poi["_id"];
				marker.poiTitle = poi["title"];

				poiElements.push(marker);
			}
		}
	};

	/* Remove the current POIs displayed on the map */
	this.removePOIs = function(){
		for(var i = 0; i < poiElements.length; i++){
			map.removeLayer(poiElements[i]);
		}

		poiElements = [];
	};
}