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
 *   May 8, 2019 (jfalgout): created
 */
package org.knime.cloud.aws.comprehend;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.knime.ext.textprocessing.data.Tag;
import org.knime.ext.textprocessing.data.TagBuilder;

/**
 * Syntax tags used by the Amazon Comprehend service.
 *
 * @author
 */
public enum AmazonSyntaxTag implements TagBuilder {
    /** Adjective - Words that typically modify nouns.*/
    ADJ,
    /** Adposition - The head of a prepositional or postpositional phrase. */
    ADP,
    /** Adverb - Words that typically modify verbs. They may also modify adjectives and other adverbs. */
    ADV,
    /** Auxillary - Function words that accompanies the verb of a verb phrase. */
    AUX,
    /** Coordinating conjunciton - Words that links words or phrases without subordinating one to the other. */
    CCONJ,
    /** Determiner - Articles and other words that specify a particular noun phrase. */
    DET,
    /** Interjection - Words used as an exclamation or part of an exclamation */
    INTJ,
    /** Noun - Words that specify a person, place, thing, animal, or idea. */
    NOUN,
    /** Numeral - Words, typically determiners, adjectives, or pronouns, that express a number. */
    NUM,
    /** Other - Words that can't be assigned a part of speech category. */
    O,
    /** Particle - Function words associated with another word or phrase to impart meaning. */
    PART,
    /** Pronoun - Words that substitute for nouns or noun phrases. */
    PRON,
    /** Proper noun - A noun that is the name of a specific individual, place or object. */
    PROPN,
    /** Punctuation - Non-alphabetical characters that delimit text. */
    PUNCT,
    /** Subordinating conjunction - A conjunction that links parts of sentences by make one of them part of the other. */
    SCONJ,
    /** Symbol - Word-like entities such as the dollar sign ($) or mathematical symbols. */
    SYM,
    /** Verb - Words that signal events and actions. */
    VERB,
    /** Unknown part of syntax (not supported by AWS, but needed) */
    UNKNOWN;

    private final Tag m_tag;

    /**
     * The constant for Syntax tag types.
     */
    public static final String TAG_TYPE = "SYNTAX";

    /**
     * Creates new instance of {@code SyntaxTag}.
     */
    private AmazonSyntaxTag() {
        m_tag = new Tag(name(), TAG_TYPE);
    }

    /**
     * @return The {@link org.knime.ext.textprocessing.data.Tag} corresponding to the specified {@code AmazonSyntaxTag}.
     */
    public Tag getTag() {
        return m_tag;
    }

    /**
     * Returns the enum fields as a String list of their names.
     *
     * @return - the enum fields as a String list of their names.
     */
    @Override
    public List<String> asStringList() {
        Enum<AmazonSyntaxTag>[] values = values();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            list.add(values[i].name());
        }
        return list;
    }

    /**
     * Returns the {@link org.knime.ext.textprocessing.data.Tag} related to the given string. If no corresponding
     * {@link org.knime.ext.textprocessing.data.Tag} is available the <code>UNKNOWN</code> tag is returned.
     *
     * @param str The string representing a {@link org.knime.ext.textprocessing.data.Tag}.
     * @return The related {@link org.knime.ext.textprocessing.data.Tag} to the given string.
     */
    public static Tag stringToTag(final String str) {
        for (AmazonSyntaxTag pos : values()) {
            if (pos.getTag().getTagValue().equals(str)) {
                return pos.getTag();
            }
        }
        return AmazonSyntaxTag.UNKNOWN.getTag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag buildTag(final String value) {
        return AmazonSyntaxTag.stringToTag(value);
    }

    /**
     * @return The default "UNKNOWN" {@code AmazonSyntaxTag} as {@code TagBuilder}.
     */
    public static TagBuilder getDefault() {
        return AmazonSyntaxTag.UNKNOWN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return TAG_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Tag> getTags() {
        Set<Tag> tagSet = new LinkedHashSet<>(values().length);
        for (AmazonSyntaxTag tag : values()) {
            tagSet.add(tag.getTag());
        }
        return tagSet;
    }

}
