package org.knime.cloud.aws.nodes.comprehend.keyphrases;

import org.knime.cloud.aws.comprehend.ComprehendUtils;
import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 *
 *
 * @author KNIME AG, Zurich, Switzerland
 */
public class ComprehendKeyPhrasesNodeDialog extends DefaultNodeSettingsPane {

    /**
     * Create a basic dialog with column selection and selection for the source language.
     */
    @SuppressWarnings("unchecked")
    protected ComprehendKeyPhrasesNodeDialog() {
        super();

        addDialogComponent(
            new DialogComponentColumnNameSelection(
                new SettingsModelString(ComprehendUtils.CFG_KEY_DOCUMENT_COL, "text"),
                "Text column to analyze:",
                1,
                StringValue.class)
        );

        addDialogComponent(
            new DialogComponentStringSelection(
                new SettingsModelString(ComprehendUtils.CFGKEY_SOURCE_LANG, "English"),
                "Source language:",
                ComprehendUtils.LANG_MAP.keySet()
                )
            );

    }
}

