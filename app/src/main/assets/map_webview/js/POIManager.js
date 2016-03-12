function POIManager(){
	var poiElements = [];
	var poisJSON = [];
	var languageJSON = [];

	this.initialize = function(opts){
		poisJSON = JSON.parse(Android.getPOIsJSON());
		languageJSON = JSON.parse(Android.getLanguageJSON());
		this.setPOIs(opts);
	};

	this.setJSONs = function(opts){
		if(!opts.poisJSON){
			poisJSON = opts.poisJSON;
		}

		if(!opts.languageJSON){
			languageJSON = opts.languageJSON;
		}

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

	this.changePopupContent = function(opts){
		var sourcePOI = opts.pathManager.getSourcePOI();
		var destinationPOI = opts.pathManager.getDestinationPOI();

		for(var i = 0; i < poiElements.length; i++){
			var buttonLabel, javascriptMethod;

			if(Android.isInMode()){
				buttonLabel = languageJSON["web_change_destination"];
				javascriptMethod = "onclick='controller.navigateToPOI(this)'";
			}else{
				buttonLabel = languageJSON["web_go_to_destination"];
				javascriptMethod =  "onclick='controller.navigateToPOI(this)'";
			}

			var marker = poiElements[i];
			var popupContent;

			if(parseInt(marker.poiID) == parseInt(sourcePOI) || parseInt(marker.poiID) == parseInt(destinationPOI)){
				popupContent = "<p id='mapx-poi-title'>"+ marker.poiTitle +"</p>";
			}else{
				popupContent = "<p id='mapx-poi-title'>"+ marker.poiTitle +"</p><button id='mapx-poi-button' data-poi-title='"+  marker.poiTitle +"' data-poi-id='"+  marker.poiID +"' " + javascriptMethod + ">" + buttonLabel + "</button>";
			}

			marker.unbindPopup();
			marker.bindPopup(popupContent);
		}
	};

	this.changeDestinationPOIIcon(opts){
		var destinationPOI = opts.pathManager.getDestinationPOI();

		for(var i = 0; i < poiElements.length; i++){
			var marker = poiElements[i];

			if(parseInt(marker.poiID) == parseInt(destinationPOI)){
				//The values before for positioning were taken from the src code of LeafletJS for the default icon positioning
				var normalIcon = L.icon({
				    iconUrl: opts.imagePath,
				    iconSize:    [41, 41],
					iconAnchor:  [20, 41],
					popupAnchor: [1, -34]
				});

				marker.setIcon(normalIcon);
			}
		}
	};
}