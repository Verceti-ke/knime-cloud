/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
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
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
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
 *   02.07.2020 (Alexander Bondaletov): created
 */
package org.knime.cloud.aws.filehandling.s3.node;

import java.time.Duration;

import org.knime.cloud.aws.filehandling.s3.fs.S3FileSystem;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import software.amazon.awssdk.http.SdkHttpConfigurationOption;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;

/**
 * Settings for {@link S3ConnectorNodeModel}.
 *
 * @author Alexander Bondaletov
 */
public class S3ConnectorNodeSettings {

    private static final boolean DEFAULT_NORMALIZE = true;

    private static final String DEFAULT_WORKING_DIR = S3FileSystem.PATH_SEPARATOR;

    private static final boolean DEFAULT_SSE_ENABLED = false;
    private static final String DEFAULT_SSE_MODE = SSEMode.getDefault().getKey();
    private static final boolean DEFAULT_SSE_KMS_USE_AWS_MANAGED = true;
    private static final String DEFAULT_SSE_KMS_KEY_ID = "";

    private static final String KEY_SOCKET_TIMEOUTS = "readWriteTimeoutInSeconds";

    private static final String KEY_NORMALIZE_PATHS = "normalizePaths";

    private static final String KEY_WORKING_DIRECTORY = "workingDirectory";

    private static final String KEY_SSE_ENABLED = "sseEnabled";
    private static final String KEY_SSE_MODE = "sseMode";
    private static final String KEY_SSE_KMS_USE_AWS_MANAGED = "sseKmsUseAwsManaged";
    private static final String KEY_SSE_KMS_KEY_ID = "sseKmsKeyId";


    private final SettingsModelIntegerBounded m_socketTimeout;

    private final SettingsModelBoolean m_normalizePath;

    private final SettingsModelString m_workingDirectory;

    private final SettingsModelBoolean m_sseEnabled;
    private final SettingsModelString m_sseMode;

    private final SettingsModelBoolean m_sseKmsUseAwsManaged;
    private final SettingsModelString m_sseKmsKeyId;

    /**
     * Creates new instance
     */
    public S3ConnectorNodeSettings() {
        m_socketTimeout = new SettingsModelIntegerBounded(KEY_SOCKET_TIMEOUTS, Math.max(1, getDefaultSocketTimeout()),
            0, Integer.MAX_VALUE);
        m_normalizePath = new SettingsModelBoolean(KEY_NORMALIZE_PATHS, DEFAULT_NORMALIZE);
        m_workingDirectory = new SettingsModelString(KEY_WORKING_DIRECTORY, DEFAULT_WORKING_DIR);

        m_sseEnabled = new SettingsModelBoolean(KEY_SSE_ENABLED, DEFAULT_SSE_ENABLED);
        m_sseMode = new SettingsModelString(KEY_SSE_MODE, DEFAULT_SSE_MODE);
        m_sseKmsUseAwsManaged = new SettingsModelBoolean(KEY_SSE_KMS_USE_AWS_MANAGED, true);
        m_sseKmsKeyId = new SettingsModelString(KEY_SSE_KMS_KEY_ID, DEFAULT_SSE_KMS_KEY_ID);

        m_sseEnabled.addChangeListener(e -> updateEnabledness());
        m_sseKmsUseAwsManaged.addChangeListener(e -> updateEnabledness());
        updateEnabledness();
    }

    private void updateEnabledness() {
        m_sseMode.setEnabled(isSseEnabled());
        m_sseKmsUseAwsManaged.setEnabled(isSseEnabled());
        m_sseKmsKeyId.setEnabled(isSseEnabled() && sseKmsUseAwsManaged());
    }

    private static int getDefaultSocketTimeout() {
        Duration duration =
            SdkHttpConfigurationOption.GLOBAL_HTTP_DEFAULTS.get(SdkHttpConfigurationOption.READ_TIMEOUT);
        if (duration != null) {
            return (int)duration.getSeconds();
        }
        return 0;
    }

    /**
     * @return the socketTimeout model
     */
    public SettingsModelIntegerBounded getSocketTimeoutModel() {
        return m_socketTimeout;
    }

    /**
     * @return the socketTimeout
     */
    public int getSocketTimeout() {
        return m_socketTimeout.getIntValue();
    }

    /**
     * @return the normalizePath model
     */
    public SettingsModelBoolean getNormalizePathModel() {
        return m_normalizePath;
    }

    /**
     * @return the normalizePath
     */
    public boolean getNormalizePath() {
        return m_normalizePath.getBooleanValue();
    }

    /**
     * @return the workingDirectory model
     */
    public SettingsModelString getWorkingDirectoryModel() {
        return m_workingDirectory;
    }

    /**
     * @return selected working directory or the root directory if it is not set
     */
    public String getWorkingDirectory() {
        return m_workingDirectory.getStringValue();
    }

    /**
     * @return the sseEnabled model
     */
    public SettingsModelBoolean getSseEnabledModel() {
        return m_sseEnabled;
    }

    /**
     * @return whether Sever Side Encryption is enabled.
     */
    public boolean isSseEnabled() {
        return m_sseEnabled.getBooleanValue();
    }

    /**
     * @return the sseMode model
     */
    public SettingsModelString getSseModeModel() {
        return m_sseMode;
    }

    /**
     * @return the sseMode
     */
    public SSEMode getSseMode() {
        return SSEMode.fromKey(m_sseMode.getStringValue());
    }

    /**
     * @return settings model for whether to use the default AWS managed CMK (in SSE-KMS mode).
     */
    public SettingsModelBoolean getSseKmsUseAwsManagedModel() {
        return m_sseKmsUseAwsManaged;
    }

    /**
     * @return whether to use the default AWS managed CMK (in SSE-KMS mode).
     */
    public boolean sseKmsUseAwsManaged() {
        return m_sseKmsUseAwsManaged.getBooleanValue();
    }

    /**
     * @return the kmsKeyId model
     */
    public SettingsModelString getKmsKeyIdModel() {
        return m_sseKmsKeyId;
    }

    /**
     * @return the kmsKeyId
     */
    public String getKmsKeyId() {
        return m_sseKmsKeyId.getStringValue();
    }

    /**
     * Saves the settings in this instance to the given {@link NodeSettingsWO}
     *
     * @param settings Node settings.
     */
    public void saveSettingsTo(final NodeSettingsWO settings) {
        m_socketTimeout.saveSettingsTo(settings);
        m_normalizePath.saveSettingsTo(settings);
        m_workingDirectory.saveSettingsTo(settings);
        m_sseEnabled.saveSettingsTo(settings);
        m_sseMode.saveSettingsTo(settings);
        m_sseKmsUseAwsManaged.saveSettingsTo(settings);
        m_sseKmsKeyId.saveSettingsTo(settings);
    }

    /**
     * Validates the settings in a given {@link NodeSettingsRO}
     *
     * @param settings Node settings.
     * @throws InvalidSettingsException
     */
    public void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_socketTimeout.validateSettings(settings);
        if (settings.containsKey(KEY_NORMALIZE_PATHS)) {
            m_normalizePath.validateSettings(settings);
        }
        if (settings.containsKey(KEY_WORKING_DIRECTORY)) {
            m_workingDirectory.validateSettings(settings);
        }
        if (settings.containsKey(KEY_SSE_ENABLED)) {
            m_sseEnabled.validateSettings(settings);
        }
        if (settings.containsKey(KEY_SSE_MODE)) {
            m_sseMode.validateSettings(settings);
        }
        if (settings.containsKey(KEY_SSE_KMS_USE_AWS_MANAGED)) {
            m_sseKmsUseAwsManaged.validateSettings(settings);
        }
        if (settings.containsKey(KEY_SSE_KMS_KEY_ID)) {
            m_sseKmsKeyId.validateSettings(settings);
        }
    }

    /**
     * Loads settings from the given {@link NodeSettingsRO}
     *
     * @param settings Node settings.
     * @throws InvalidSettingsException
     */
    public void loadSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_socketTimeout.loadSettingsFrom(settings);
        if (settings.containsKey(KEY_NORMALIZE_PATHS)) {
            m_normalizePath.loadSettingsFrom(settings);
        } else {
            m_normalizePath.setBooleanValue(DEFAULT_NORMALIZE);
        }
        if (settings.containsKey(KEY_WORKING_DIRECTORY)) {
            m_workingDirectory.loadSettingsFrom(settings);
        } else {
            m_workingDirectory.setStringValue(DEFAULT_WORKING_DIR);
        }
        if (settings.containsKey(KEY_SSE_ENABLED)) {
            m_sseEnabled.loadSettingsFrom(settings);
            m_sseMode.loadSettingsFrom(settings);
            m_sseKmsUseAwsManaged.loadSettingsFrom(settings);
            m_sseKmsKeyId.loadSettingsFrom(settings);
        } else {
            m_sseEnabled.setBooleanValue(DEFAULT_SSE_ENABLED);
            m_sseMode.setStringValue(DEFAULT_SSE_MODE);
            m_sseKmsUseAwsManaged.setBooleanValue(DEFAULT_SSE_KMS_USE_AWS_MANAGED);;
            m_sseKmsKeyId.setStringValue(DEFAULT_SSE_KMS_KEY_ID);
        }
        updateEnabledness();
    }

    @Override
    protected S3ConnectorNodeSettings clone() {
        NodeSettings transferSettings = new NodeSettings("ignored");
        saveSettingsTo(transferSettings);

        S3ConnectorNodeSettings clone = new S3ConnectorNodeSettings();
        try {
            clone.loadSettingsFrom(transferSettings);
        } catch (InvalidSettingsException ex) {
            throw new IllegalStateException(ex);
        }
        return clone;
    }

    /**
     * Enum representing different available S3 server-side encryption modes.
     *
     */
    public enum SSEMode {
            /**
             * SSE-S3 mode
             */
            S3("S3-Managed Keys (SSE-S3)", "SSE-S3",ServerSideEncryption.AES256),
            /**
             * SSE-KMS mode
             */
            KMS("Keys in KMS (SSE-KMS)", "SSE-KMS",ServerSideEncryption.AWS_KMS);

        private String m_title;
        private String m_key;

        private ServerSideEncryption m_encryption;

        private SSEMode(final String title, final String key, final ServerSideEncryption encryption) {
            m_title = title;
            m_key = key;
            m_encryption = encryption;
        }

        /**
         * @return the key
         */
        public String getKey() {
            return m_key;
        }

        /**
         * @return the encryption
         */
        public ServerSideEncryption getEncryption() {
            return m_encryption;
        }

        @Override
        public String toString() {
            return m_title;
        }

        /**
         * @return The default mode.
         */
        public static SSEMode getDefault() {
            return S3;
        }

        /**
         * @param key The mode key.
         * @return The mode with the given key or the default mode in case no mode with a given key is found.
         */
        public static SSEMode fromKey(final String key) {
            for (SSEMode mode : values()) {
                if(mode.getKey().equals(key)) {
                    return mode;
                }
            }
            return getDefault();
        }
    }
}
