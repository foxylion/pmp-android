package de.unistuttgart.ipvs.pmp.editor.util;

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

public interface IServerProvider {

	public List<RGIS> getAvailableRessourceGroups();
}