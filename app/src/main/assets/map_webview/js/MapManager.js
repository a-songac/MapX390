function MapManager(){
	this.initialize = function(){
		var self = this;

		map.on('dragend', function(event){
			self.setCurrentView();
		});

		map.on('zoomend', function(e) {
			self.setZoomLevel();
		});
	};

	this.setZoomLevel = function(){
		var zoomLevel = map.getZoom();
		Android.setZoomLevel(zoomLevel);
	};

	this.setCurrentView = function(){
		var lngLat = [map.getCenter().lat.toString(), map.getCenter().lng.toString()];
		Android.setCurrentView(lngLat);
	};
}