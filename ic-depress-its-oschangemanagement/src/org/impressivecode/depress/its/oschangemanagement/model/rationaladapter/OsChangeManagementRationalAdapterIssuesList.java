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
package org.impressivecode.depress.its.oschangemanagement.model.rationaladapter;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Marcin Cho�uj, Wroclaw University of Technology
 * @author Piotr Malek, Wroclaw University of Technology
 * @author Przemys�aw Trepka, Wroclaw University of Technology
 * @author �ukasz Trojak, Wroclaw University of Technology
 * 
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class OsChangeManagementRationalAdapterIssuesList {

	@JsonProperty("oslc:responseInfo")
	private OsChangeManagementRationalAdapterPagingInfo responseInfo;

	public OsChangeManagementRationalAdapterPagingInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(OsChangeManagementRationalAdapterPagingInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public ArrayList<OsChangeManagementRationalAdapterIssueListItem> getResults() {
		return results;
	}

	public void setResults(ArrayList<OsChangeManagementRationalAdapterIssueListItem> results) {
		this.results = results;
	}

	@JsonProperty("oslc:results")
	private ArrayList<OsChangeManagementRationalAdapterIssueListItem> results;

}
