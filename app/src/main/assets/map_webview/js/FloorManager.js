function FloorManager(){
	var floorJSON = [];
	var floors = [];
	var currentFloor;
	var floorButtons = [];

	this.initialize = function(){
		this.setJSON();
		this.setFloorImages();
		this.setFloorLevelUIControl();
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

	this.setFloorImages = function(){
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

	this.setFloorLevelUIControl = function(){
		var levels = floors.length;

		var levelControlContainer = document.createElement("div");
		$(levelControlContainer).addClass("leaflet-control leaflet-bar");

		//Find the current zoom control container and create a level control element in it
		$(".leaflet-bottom.leaflet-right").prepend(levelControlContainer);

		//Loop for creating every floor button
		for(var i = 0; i < levels; i++){
			var levelControl = document.createElement("a");
			$(levelControl).prop("href", "#");
			$(levelControl).text(i+1);
			$(levelControl).attr("data-floorId", i+1);  //TOCHANGE for id attribute in JSON in future sprint

			//First floor has to have the selected css
			if(i === 0){
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
				currentImageOverlay.leafletObj.setOpacity(0);

				var level = parseInt($(this).text());
				var updatedFloorOverlay = floors[level-1];

				currentFloor = level;
				updatedFloorOverlay.leafletObj.setOpacity(1);

				controller.floorClicked({
					updatedFloorOverlay: updatedFloorOverlay
				});
			});

			//Prepend the floor button to the floor control element
			$(levelControlContainer).prepend(levelControl);
			floorButtons.push(levelControl);
		}
	};

	this.showUserLocatedFloor = function(){
		var floor = Android.getCurrentPOIFloor();
		for(var i = 0; i < floorButtons.length; i++){
			var floorBtn = floorButtons[i];

			//TODO: Maybe change, if floor can be something other than int
			if(parseInt($(floorBtn).attr("data-floorId")) == parseInt(floor)){
				$(floorBtn).click();
			}
		}
	};

}