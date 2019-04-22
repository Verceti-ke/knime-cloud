package org.knime.cloud.aws.comprehend.entities.node;

import org.knime.cloud.aws.comprehend.BaseComprehendNodeModel;
import org.knime.cloud.aws.comprehend.ComprehendOperation;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.ext.textprocessing.data.DocumentCell;


/**
 * Node model for node that extracts entities from text using the Amazon Comprehend service.
 *
 * @author KNIME AG, Zurich, Switzerland
 */
public class ComprehendEntitiesNodeModel extends BaseComprehendNodeModel {

    @Override
    protected ComprehendOperation getOperationInstance() {
        return new EntityOperation(
            this.cxnInfo,
            this.textColumnName.getStringValue(),
            this.sourceLanguage.getStringValue(),
            this.outputTableSpec);
    }

    @Override
    public DataTableSpec generateOutputTableSpec(final DataTableSpec inputSpec) {

        // The columns added from the AWS call.
        DataColumnSpec[] allColSpecs = new DataColumnSpec[5];
        allColSpecs[0] = new DataColumnSpecCreator(textColumnName.getStringValue() + " (Processed)", DocumentCell.TYPE).createSpec();
        allColSpecs[1] = new DataColumnSpecCreator("Entity", StringCell.TYPE).createSpec();
        allColSpecs[2] = new DataColumnSpecCreator("Confidence", DoubleCell.TYPE).createSpec();
        allColSpecs[3] = new DataColumnSpecCreator("Begin Offset", IntCell.TYPE).createSpec();
        allColSpecs[4] = new DataColumnSpecCreator("End Offset", IntCell.TYPE).createSpec();

        // Along with the input data columns.
        return new DataTableSpec(inputSpec, new DataTableSpec(allColSpecs));
    }
}

