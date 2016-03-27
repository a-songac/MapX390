function POIManager(){
	var poiElements = [];
	var poisJSON = [];
	var languageJSON = [];

	this.initialize = function(){
		poisJSON = JSON.parse(Android.getPOIsJSON());
		languageJSON = JSON.parse(Android.getLanguageJSON());
	};

	this.getPOISJSON = function(){
		return poisJSON;
	};

	this.getPOIElements = function(){
		return poiElements;
	};

	this.setPOIs = function(){
		var normalIcon = L.icon({
		    iconUrl: 'js/images/marker-icon-2x.png',
		    iconSize:    [41, 41],
			iconAnchor:  [20, 41],
			popupAnchor: [1, -34]
		});

		var mapHeight = controller.mapHeight;
		var mapWidth = controller.mapWidth;
		var offsetX = controller.offsetX;
		var offsetY = controller.offsetY;
		var currentFloor = controller.floorManager.getCurrentFloor();

		for(var i = 0; i < poisJSON.length; i++){
			var buttonLabel;

			if(Android.isInMode()){
				buttonLabel = languageJSON["web_change_destination"];
			}else{
				buttonLabel = languageJSON["web_go_to_destination"];
			}

			var poi = poisJSON[i];
			if(parseInt(currentFloor) === parseInt(poi["floor"]) && poi["type"] != "t"){
				var popupContent 

				if(parseInt(poi["_id"]) == parseInt(Android.getUserPosition())){
					popupContent = "<p id='mapx-poi-title'>"+ poi["title"] +"</p>";
				}else{
					popupContent = "<p id='mapx-poi-title'>"+ poi["title"] +"</p><button id='mapx-poi-button' data-poi-title='"+ poi["title"] +"' data-poi-id='"+ poi["_id"]+"' onclick='controller.poiManager.navigateToPOI(this)'>" + buttonLabel + "</button>";
				}

				var x = -mapWidth + (offsetX + parseInt(poi["x_coord"]));
				var y = -mapHeight + (offsetY + parseInt(poi["y_coord"]));
				
				var getInfoLabel = languageJSON["web_view_info"];
				popupContent += "<button onclick='controller.poiManager.showInfo(this)' data-poi-id='"+  poi["_id"] +"' >" + getInfoLabel + "</button>";

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

	this.changePopupContent = function(){
		var sourcePOI = controller.pathManager.getSourcePOI();
		var destinationPOI = controller.pathManager.getDestinationPOI();

		for(var i = 0; i < poiElements.length; i++){
			var buttonLabel, javascriptMethod;

			if(Android.isInNavigationMode()){
				buttonLabel = languageJSON["web_change_destination"];
				javascriptMethod = "onclick='controller.poiManager.navigateToPOI(this)'";
			}else if(!Android.isInMode()){
				buttonLabel = languageJSON["web_go_to_destination"];
				javascriptMethod =  "onclick='controller.poiManager.navigateToPOI(this)'";
			}

			var marker = poiElements[i];
			var popupContent;
			if(Android.isInNavigationMode() || !Android.isInMode()){
				if(parseInt(marker.poiID) == parseInt(sourcePOI) || parseInt(marker.poiID) == parseInt(destinationPOI)){
					popupContent = "<p id='mapx-poi-title'>"+ marker.poiTitle +"</p>";
				}else if(parseInt(marker.poiID) == parseInt(Android.getUserPosition())){
					popupContent = "<p id='mapx-poi-title'>"+ marker.poiTitle +"</p>";
				}else{
					popupContent = "<p id='mapx-poi-title'>"+ marker.poiTitle +"</p><button id='mapx-poi-button' data-poi-title='"+  marker.poiTitle +"' data-poi-id='"+  marker.poiID +"' " + javascriptMethod + ">" + buttonLabel + "</button>";
				}
			}else{
				popupContent = "<p id='mapx-poi-title'>"+ marker.poiTitle +"</p>" ;
			}

			var getInfoLabel = languageJSON["web_view_info"];
			popupContent += "<button onclick='controller.poiManager.showInfo(this)' data-poi-id='"+  marker.poiID +"' >" + getInfoLabel + "</button>";


			marker.unbindPopup();
			marker.bindPopup(popupContent);
		}
	};

	this.changeDestinationPOIIcon = function(opts){
		var destinationPOI = controller.pathManager.getDestinationPOI();

		for(var i = 0; i < poiElements.length; i++){
			var marker = poiElements[i];

			if(parseInt(marker.poiID) == parseInt(destinationPOI)){
				//The values before for positioning were taken from the src code of LeafletJS for the default icon positioning
				var icon = L.icon({
				    iconUrl: opts.imagePath,
				    iconSize:    [41, 41],
					iconAnchor:  [20, 41],
					popupAnchor: [1, -34]
				});

				marker.setIcon(icon);
			}
		}
	};

	this.getNextPOI = function(){
		var nodeId;
		var nextPOI = Android.getNextPOIInStoryline();
//		var path = controller.pathManager.getPath();
//
//		for(var i = 1; i < path.length; i++){
//			nodeId = path[i];
//
//			for(var o = 0; o < poisJSON.length; o++){
//				var poi = poisJSON[o];
//
//				if(parseInt(poi["_id"]) == parseInt(nodeId)){
//					if(poi["type"] != "t"){
//						nextPOI = nodeId;
//					}else{
//						break;
//					}
//				}
//			}
//
//			if(!nextPOI){
//				continue;
//			}else{
//				break;
//			}
//		}

		return nextPOI;
	};

	/* Send call to Android to initiate a navigation to the selected POI */
	this.navigateToPOI = function(elementClicked){
		var poiID = $(elementClicked).attr("data-poi-id");
		Android.navigateToPOI(poiID);
	};

	this.showInfo = function(elementClicked){
		var poiID = $(elementClicked).attr("data-poi-id");
		Android.viewInfo(poiID);
	};

	this.clickPOI = function(poiID){
		console.log('poiManager.clickPOI() - poiID: ' + poiID);
		for(var i = 0; i < poiElements.length; i++){
			var marker = poiElements[i];

			if(parseInt(marker.poiID) == parseInt(poiID)){
				marker.openPopup();
				break;
			}
		}
	};
}