function MapManager(){
	this.initialize = function(){
		var self = this;

		map.on('dragend', function(event){
			self.setCurrentView();
		});

		map.on('zoomend', function(e) {
			self.setZoomLevel();
		});

		this.createShowUserUIControl();
	};

	this.setZoomLevel = function(){
		var zoomLevel = map.getZoom();
		Android.setZoomLevel(zoomLevel);
	};

	this.setCurrentView = function(){
		var lngLat = [map.getCenter().lat.toString(), map.getCenter().lng.toString()];
		Android.setCurrentView(lngLat);
	};

	this.createShowUserUIControl = function(){
		var showUserContainer = document.createElement("a");
		$(showUserContainer).addClass("leaflet-control leaflet-bar");
		$(showUserContainer).attr('id', 'show-user-position-container');

		//Find the current zoom control container and create a level control element in it
		$(".leaflet-bottom.leaflet-right").prepend(showUserContainer);

		var showUserIcon = document.createElement("img");
		$(showUserIcon).attr('src', 'js/images/show_user_position.png')
		$(showUserIcon).attr('id', "show-user-position");
			
		$(showUserContainer).prepend(showUserIcon);

		$(showUserContainer).click(function(){
			controller.floorManager.showUserLocatedFloor();
		});

		// $(showUserContainer).click(function(){
		// 	console.log('clicked');
		// 	$(showUserContainer).attr('background-color', '#ccc');

		// 	setTimeout(function(){
		// 		console.log('ciasda');
		// 		$(showUserContainer).attr('background-color', '#FFFFFF');
		// 	}, 5000);
		// });
	};
}