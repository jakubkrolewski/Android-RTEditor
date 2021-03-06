/*
 * Copyright (C) 2015 Emanuel Moecklin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onegravity.rteditor.effects;

import android.text.Spannable;

import com.onegravity.rteditor.RTEditText;
import com.onegravity.rteditor.spans.IntendationSpan;
import com.onegravity.rteditor.spans.ParagraphSpan;
import com.onegravity.rteditor.utils.Paragraph;
import com.onegravity.rteditor.utils.Selection;

import java.util.ArrayList;
import java.util.List;

/**
 * Text indentation.
 * <p>
 * LeadingMarginSpans are always applied to whole paragraphs and each paragraphs gets its "own" LeadingMarginSpan (1:1).
 * Editing might violate this rule (deleting a line feed merges two paragraphs).
 * Each call to applyToSelection will again make sure that each paragraph has again its own LeadingMarginSpan
 * (call applyToSelection(RTEditText, null, null) and all will be good again).
 * <p>
 * The Boolean parameter is used to increment, decrement the indentation
 */
public class IndentationEffect extends LeadingMarginEffect {

    @Override
    public void applyToSelection(RTEditText editor, Selection selectedParagraphs, Boolean increment) {
        final Spannable str = editor.getText();

        List<ParagraphSpan> spans2Process = new ArrayList<ParagraphSpan>();

        for (Paragraph paragraph : editor.getParagraphs()) {
            int indentation = 0;

            // find existing indentations/spans for this paragraph
            Object[] existingSpans = getCleanSpans(str, paragraph);
            boolean hasExistingSpans = existingSpans != null && existingSpans.length > 0;
            if (hasExistingSpans) {
                for (Object span : existingSpans) {
                    spans2Process.add(new ParagraphSpan(span, paragraph, true));
                    indentation += ((IntendationSpan) span).getLeadingMargin();
                }
            }

            // if the paragraph is selected inc/dec the existing indentation
            int incIndentation = increment == null ? 0 : (increment.booleanValue() ? 1 : -1) * getLeadingMargingIncrement();
            indentation += paragraph.isSelected(selectedParagraphs) ? incIndentation : 0;

            // if indentation>0 then apply a new span
            if (indentation > 0) {
                IntendationSpan leadingMarginSpan = new IntendationSpan(indentation, paragraph.isEmpty(), paragraph.isFirst(), paragraph.isLast());
                spans2Process.add(new ParagraphSpan(leadingMarginSpan, paragraph, false));
            }
        }

        // add or remove spans
        for (final ParagraphSpan spanDef : spans2Process) {
            spanDef.process(str);
        }
    }

    @Override
    protected IntendationSpan[] getSpans(Spannable str, Selection selection) {
        return str.getSpans(selection.start(), selection.end(), IntendationSpan.class);
    }

}