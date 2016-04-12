function FloorManager(){
	var floorJSON = [];
	var floors = {};
	var currentFloor;
	var floorButtons = [];

	this.initialize = function(){
		this.setJSON();
	};

	this.initializeFollowUp = function(){
		this.setFloorImages();
		this.setFloorLevelUIControl();
	};

	this.setJSON = function(){
		floorJSON = JSON.parse(Android.getFloorsJSON());
	};

	this.getJSON = function(){
		return floorJSON;
	};

	this.setCurrentFloor = function(floor){
		currentFloor = floor;
		Android.setCurrentFloor(currentFloor);
	};

	this.getCurrentFloor = function(){
		return currentFloor;
	};

	this.getFloorsArr = function(){
		return floors;
	};

	this.setFloorImages = function(){
		for(var i = 0; i < floorJSON.length; i++){
    		var imageUrl;
	    	var west = -parseInt(floorJSON[i]["floor_width"])/2;
	    	var north = parseInt(floorJSON[i]["floor_height"])/2;
			var east = parseInt(floorJSON[i]["floor_width"])/2;
			var south = -parseInt(floorJSON[i]["floor_height"])/2;

			imageUrl = floorJSON[i]["floor_path"];

			//If path starts with /, it'll be start from outside the webview. TODO: Temporary fix, as image path should start from storage folder, not webview folder
			if(imageUrl.charAt(0) == "/"){
				imageUrl = imageUrl.substring(1);
			}

			var imageBounds = [[south, west], [north, east]];
	    	var imageOverlay = L.imageOverlay(imageUrl, imageBounds);
	    	imageOverlay.addTo(map);
	    	imageOverlay.setOpacity(0);

	    	var floor_num = floorJSON[i]["floor_num"];

	    	floors[parseInt(floor_num)-1] = {
						    		leafletObj:imageOverlay,
						    		north: north,
						    		east: east,
						    		imageUrl: imageUrl,
						    		num: floor_num
						    	};
    	}
	};

	this.setFloorLevelUIControl = function(){
		var self = this;
		var levels = floors.length;

		var levelControlContainer = document.createElement("div");
		$(levelControlContainer).addClass("leaflet-control leaflet-bar");

		//Find the current zoom control container and create a level control element in it
		$(".leaflet-bottom.leaflet-right").prepend(levelControlContainer);

		//Loop for creating every floor button
		var counter = 0;
		for(var key in floors){
			var levelControl = document.createElement("a");
			$(levelControl).prop("href", "#");
			$(levelControl).text(floors[key].num);
			$(levelControl).attr("data-floorId", floors[key].num); 

			//First floor has to have the selected css
			if(counter === 0){
				$(levelControl).css("background-color", "#ccc");
			}

			//REFACTORING POSSIBLITY: Don't have click function creation within loop
			//Add a click function to every element
			$(levelControl).click(function(){
				//Unselect current floor
				$("div.leaflet-control.leaflet-bar").find("a").css("background-color", "");

				//Select current floor
				$(this).css("background-color", "#ccc");

				//Replace current floor img source with new floor img source
				var currentImageOverlay = floors[currentFloor-1];

				if(currentImageOverlay){
					currentImageOverlay.leafletObj.setOpacity(0);
				}

				var level = parseInt($(this).text());
				var updatedFloorOverlay; //= floors[level-1];

				for(var key in floors){
					if(parseInt(floors[key].num) == level){
						updatedFloorOverlay = floors[key];
						break;
					}
				}

				self.setCurrentFloor(level);
				updatedFloorOverlay.leafletObj.setOpacity(1);

				self.floorClicked({
					updatedFloorOverlay: updatedFloorOverlay
				});
			});

			//Prepend the floor button to the floor control element
			$(levelControlContainer).prepend(levelControl);
			floorButtons.push(levelControl);

			counter++;
		}
	};

	this.showUserLocatedFloor = function(){
		var floor = Android.getCurrentPOIFloor();
		var userPOI = Android.getUserPosition();

		this.clickFloor(floor);

		controller.userManager.centerToUserPOI(userPOI);
	};

	this.clickFloor = function(floor){
		console.log("floorManager.clickFloor() - floor: " + floor);
		for(var i = 0; i < floorButtons.length; i++){
			var floorBtn = floorButtons[i];

			if(parseInt($(floorBtn).attr("data-floorId")) == parseInt(floor)){
				$(floorBtn).click();
			}
		}
	};

	this.floorClicked = function(opts){
		var updatedFloorOverylay = opts.updatedFloorOverlay;

		controller.offsetX = controller.mapWidth - updatedFloorOverylay["east"];
		controller.offsetY = controller.mapHeight - updatedFloorOverylay["north"];

		controller.poiManager.removePOIs();
		controller.poiManager.setPOIs();

		if(Android.isInMode()){
			controller.poiManager.changeDestinationPOIIcon({
				imagePath: 'js/images/pin1.png'
			});

			controller.poiManager.changePopupContent();
			controller.pathManager.deletePath();
			controller.pathManager.drawPath();
		}

		controller.userManager.updateUserMarker();
	};

}