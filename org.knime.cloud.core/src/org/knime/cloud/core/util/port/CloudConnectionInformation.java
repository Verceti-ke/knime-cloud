/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   Aug 30, 2016 (oole): created
 */
package org.knime.cloud.core.util.port;

import org.knime.base.filehandling.remote.connectioninformation.port.ConnectionInformation;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.ModelContentRO;
import org.knime.core.node.ModelContentWO;

/**
 * Extended {@link ConnectionInformation}. This provides functionality to have information about whether or not to use a
 * credentials key chain. This is just a flag. To use of a key chain must be implemented in the cloud storage connection
 *
 * @author Ole Ostergaard, KNIME.com GmbH
 */
public class CloudConnectionInformation extends ConnectionInformation {

	private static final long serialVersionUID = 1L;

	private boolean m_useKeyChain;

	/**
	 * Parameterless constructor
	 */
	public CloudConnectionInformation() { }

	/**
	 * @param model
	 * @throws InvalidSettingsException
	 */
	protected CloudConnectionInformation(ModelContentRO model) throws InvalidSettingsException {
        super(model);
        this.setUseKeyChain(model.getBoolean("keyChain", false));
	}


	/**
	 * Set whether some key chain should be used when connecting
	 * @param use <code>true</code> if key chain should be used, <code>false</code> if not
	 */
	public void setUseKeyChain(final boolean use) {
		m_useKeyChain = use;
	}

	/**
	 * Returns whether the key chain should be used or not
	 * @return whether key chain should be used, <code>true</code> if it should be used, <code>false</code> if not
	 */
	public boolean useKeyChain() {
		return m_useKeyChain;
	}

	@Override
	public void save(final ModelContentWO model) {
		super.save(model);
		model.addBoolean("keyChain", m_useKeyChain);
	}

	public static CloudConnectionInformation load(ModelContentRO model) throws InvalidSettingsException {
		return new CloudConnectionInformation(model);
	}
}
