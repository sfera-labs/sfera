function ScriptNode(id) {
	this.id = id;
	Packages.cc.sferalabs.sfera.scripts.ScriptNodes.put(id, this);
};

ScriptNode.prototype.getId = function() {
	return this.id;
};

ScriptNode.prototype.postEvent = function(id, value) {
	Packages.cc.sferalabs.sfera.scripts.ScriptNodes.postEvent(this, this.id, id, value);
};