function tab(container) {
	return new Tabulator(container);
}

function Tabulator(container) {
	
	this.METHOD_AFTER = "AFTER";
	
	this.METHOD_BEFORE = "BEFORE";
	
	
	var contWidth;
	
	var contHeight;
	
	var absolute;
	
	var contpos;
	
	var compParam;
	
	var method;
	
	
	this.centerHorizontal = centerHorizontal;
	
	this.centerVertical = centerVertical;
	
	this.on = on;
	
	this.centerBoth = centerBoth;
	
	this.after = after;
	
	this.before = before;
	
	this.setVertical = setVertical;
	
	this.setHorizontal = setHorizontal;
	
	this.north = north;
	
	this.south = south;
	
	this.east = east;
	
	this.west = west;
	
	
	return this.on(container);
	
	
	function on(container2) {
		if(container2 == "document") {
			contpos = null;
			absolute = true;
			contWidth = $(document).width();
			contHeight = $(document).height();
		} else {
			contpos = $("#"+container2).offset();
			absolute = false;
			contWidth = $("#"+container2).width();
			contHeight = $("#"+container2).height();
		}
		return this;
	}
	
	
	function centerHorizontal(compID) {
		var width = $("#"+compID).width();
		var x = (contWidth - width) / 2;
    if(x < 0) x = 0;
    
		$("#"+compID).css("position", "absolute");
		$("#"+compID).css("left", x);
		
		return this;
	}
	
	
	function centerVertical(compID) {
		var height = $("#"+compID).height();
		var y = parseInt((contHeight - height) / 2);
    if(y < 0) y = 0;

		$("#"+compID).css("position", "absolute");
		$("#"+compID).css("top", y);
		
		return this;
	}
	
	
	function centerBoth(compID) {
		this.centerHorizontal(compID).centerVertical(compID);
		return this;
	}
	
	
	function after(comp) {
		compParam = comp;
    //alert($("#"+comp).attr("id") + " - " +  $("#"+comp).offset());
		method = this.METHOD_AFTER;
		return this;
	}
	
	
	function before(comp) {
		compParam = comp;
		method = this.METHOD_BEFORE;
		return this;
	}
	
	
	function setVertical(compID, dist) {
		if(method == this.METHOD_AFTER)
			setVerticalAfter(compID, dist);
		else
			setVerticalBefore(compID, dist);
		
		return this;
	}
	
	
	function setHorizontal(compID, dist) {
		if(method == this.METHOD_AFTER)
			setHorizontalAfter(compID, dist);
		else
			setHorizontalBefore(compID, dist);
		
		return this;
	}
	
	
	function setVerticalAfter(compID, dist) {
    //alert(compParam + " " + $("#"+compParam));
		var y = $("#"+compParam).offset().top + 
				$("#"+compParam).height() + parseInt(dist) -
				(contpos ? contpos.top : 0);

		$("#"+compID).css("position", "absolute");
		$("#"+compID).css("top", y);
		
		return this;
	}
	

	function setVerticalBefore(compID, dist) {
		var y = $("#"+compParam).offset().top - 
				parseInt(dist) - $("#"+compID).height() -
				(contpos ? contpos.top : 0);

		$("#"+compID).css("position", "absolute");
		$("#"+compID).css("top", y);
		
		return this;
	}
	
	
	function setHorizontalAfter(compID, dist) {
		var x = $("#"+compParam).offset().left +
				$("#"+compParam).width() + parseInt(dist) -
				(contpos ? contpos.left : 0); 

		$("#"+compID).css("position", "absolute");
		$("#"+compID).css("left", x);
		
		return this;
	}


	function setHorizontalBefore(compID, dist) {
		var x = $("#"+compParam).offset().left - 
				parseInt(dist) - $("#"+compID).width() -
				(contpos ? contpos.left : 0);

		$("#"+compID).css("position", "absolute");
		$("#"+compID).css("left", x);
		
		return this;
	}
	
	
	function north(compID, dist) {
		var y = parseInt(dist);
			
		$("#"+compID).css("position", "absolute");
		$("#"+compID).css("top", y);
		
		return this;
	}
	
	
	function south(compID, dist) {
		var y = contHeight - parseInt(dist) -
				$("#"+compID).height() - 6;
			
		$("#"+compID).css("position", "absolute");
		$("#"+compID).css("top", y);
		
		return this;
	}
	
	
	function east(compID, dist) {
		var x = contWidth - parseInt(dist) -
				$("#"+compID).width() - 18;
			
		$("#"+compID).css("position", "absolute");
		$("#"+compID).css("left", x);
		
		return this;
	}
	
	
	function west(compID, dist) {
		var x = parseInt(dist);
			
		$("#"+compID).css("position", "absolute");
		$("#"+compID).css("left", x);
		
		return this;
	}
	
} 
