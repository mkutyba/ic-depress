/*
 ImpressiveCode Depress Framework
 Copyright (C) 2013  ImpressiveCode contributors

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impressivecode.depress.its.oschangemanagement;

import java.awt.Component;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.impressivecode.depress.its.ITSOnlineNodeDialog;
import org.impressivecode.depress.its.oschangemanagement.builder.OsChangeManagementJiraRationalAdapterUriBuilder;
import org.impressivecode.depress.its.oschangemanagement.builder.OsChangeManagementUriBuilder.Mode;
import org.impressivecode.depress.its.oschangemanagement.model.OsChangeManagementProject;
import org.impressivecode.depress.its.oschangemanagement.parser.OsChangeManagementRationalAdapterParser;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

/**
 * 
 * @author Marcin Cho�uj, Wroclaw University of Technology
 * @author Piotr Malek, Wroclaw University of Technology
 * @author Przemys�aw Trepka, Wroclaw University of Technology
 * @author �ukasz Trojak, Wroclaw University of Technology
 * 
 */

public class OsChangeManagementAdapterNodeDialog extends ITSOnlineNodeDialog {

	protected final static String IMB_RATIONAL_ADAPTER = "IBM Rational Adapter";
	public final static String PRIORITY = "PRIORITY";
	public final static String TYPE = "TYPE";
	public final static String RESOLUTION = "RESOLUTION";
	public final static String STATE = "STATE";
	protected DialogComponentStringSelection pluginComponent;
	private OsChangeManagementRestClient client;

	public OsChangeManagementAdapterNodeDialog() {
		super();
		RemoveAdvancedTab();
		AddPluginComponent();
		client = new OsChangeManagementRestClient();
	}

	protected void RemoveAdvancedTab() {
		removeTab(ADVANCED_TAB_NAME);
	}

	protected void AddPluginComponent() {
		JPanel connectionTab = (JPanel) getTab(ITSOnlineNodeDialog.CONNECTION_TAB_NAME);
		connectionTab.add(createPluginComponent());
	}

	protected Component createPluginComponent() {
		pluginComponent = new DialogComponentStringSelection(createPluginComponentSettings(), "Plugin: ", getPluginsName());
		return pluginComponent.getComponentPanel();
	}

	protected SettingsModelString createPluginComponentSettings() {
		return OsChangeManagementAdapterNodeModel.createPluginSettings();
	}

	protected String[] getPluginsName() {
		ArrayList<String> plugins = new ArrayList<String>();
		plugins.add(IMB_RATIONAL_ADAPTER);
		return plugins.toArray(new String[plugins.size()]);
	}

	@Override
	protected void updateProjectsList() {
		List<OsChangeManagementProject> projects;
		List<String> projectNames = new ArrayList<String>();
		try {
			projects = getList(Mode.PROJECT_LIST);
			projectSelection.getModel().setEnabled(true);
			for (OsChangeManagementProject item : projects) {
				projectNames.add(item.getName());
			}
			projectSelection.replaceListItems(projectNames, null);
		} catch (Exception e) {
			Logger.getLogger("Error").severe("Error during connection, list could not be downloaded");
		}
	}

	@Override
	protected void createMappingManager() {

		mappingManager = OsChangeManagementAdapterNodeModel.createMapping();
		mappingManager.createFilterPriority(new RefreshCaller(PRIORITY));
		mappingManager.createFilterType(new RefreshCaller(TYPE));
		mappingManager.createFilterResolution(new RefreshCaller(RESOLUTION));
		mappingManager.createFilterStatus(new RefreshCaller(STATE));
	}

	@Override
	protected Component createAdvancedTab() {
		return new JPanel();
	}

	@Override
	protected void loadSpecificSettingsFrom(NodeSettingsRO settings, PortObjectSpec[] specs) throws NotConfigurableException {
		pluginComponent.loadSettingsFrom(settings, specs);

	}

	@Override
	protected void saveSpecificSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		pluginComponent.saveSettingsTo(settings);

	}

	private <T> List<T> getList(Mode mode) throws Exception {
		OsChangeManagementJiraRationalAdapterUriBuilder builder = new OsChangeManagementJiraRationalAdapterUriBuilder();
		String urlString = ((SettingsModelString) (url.getModel())).getStringValue();
		String login = ((SettingsModelString) (loginComponent.getModel())).getStringValue();
		String password = ((SettingsModelString) (passwordComponent.getModel())).getStringValue();
		String pluginName = ((SettingsModelString) (pluginComponent.getModel())).getStringValue();
		builder.setHostname(urlString);
		builder.setMode(mode);
		String rawData = client.getJSON(builder.build(), login, password);
		switch (pluginName) {
		case IMB_RATIONAL_ADAPTER:
			return (List<T>) new OsChangeManagementRationalAdapterParser().getProjectList(rawData);
		default:
			return new ArrayList<T>();
		}

	}
}
