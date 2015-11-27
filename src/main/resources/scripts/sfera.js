function ScriptNode(id) {
	this.id = id;
	Packages.cc.sferalabs.sfera.script.ScriptNodes.put(id, this);
};

ScriptNode.prototype.getId = function() {
	return this.id;
};