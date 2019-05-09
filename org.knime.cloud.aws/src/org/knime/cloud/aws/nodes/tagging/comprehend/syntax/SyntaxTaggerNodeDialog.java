package org.knime.cloud.aws.nodes.tagging.comprehend.syntax;

import org.knime.cloud.aws.comprehend.ComprehendUtils;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.ext.textprocessing.nodes.tagging.TaggerNodeSettingsPane2;

/**
 * Dialog for the Comprehend Syntax tagger.
 *
 * @author KNIME AG, Zurich, Switzerland
 */
public class SyntaxTaggerNodeDialog extends TaggerNodeSettingsPane2 {

    /**
     * Use the tagger node settings pane as the base and add on the
     * language of the input data as another required setting.
     */
    protected SyntaxTaggerNodeDialog() {
        super();

        addDialogComponent(
            new DialogComponentStringSelection(
                new SettingsModelString(ComprehendUtils.CFGKEY_SOURCE_LANG, "English"),
                "Source language:",
                ComprehendUtils.LANG_MAP.keySet()
                )
            );

    }
}

