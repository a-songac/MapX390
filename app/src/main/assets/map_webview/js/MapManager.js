function MapManager(){
	this.initialize = function(){
		var self = this;
		map.on('dragend', function(event){
			self.setCurrentView();
		});
	};

	this.setZoomLevel = function(){
		Android.setZoomLevel(map.getZoom());
	};

	this.setCurrentView = function(){
		var lngLat = [map.getCenter().lat.toString(), map.getCenter().lng.toString()];
			Android.setCurrentView(lngLat);
	};
}