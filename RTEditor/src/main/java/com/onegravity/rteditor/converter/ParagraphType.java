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

package com.onegravity.rteditor.converter;

import android.text.Layout;
import android.text.style.AlignmentSpan;
import android.text.style.ParagraphStyle;

import com.onegravity.rteditor.spans.BulletSpan;
import com.onegravity.rteditor.spans.IntendationSpan;
import com.onegravity.rteditor.spans.NumberSpan;

/*
 * This is a helper class for converting from Spanned to HTML and back.
 */
public enum ParagraphType {
    NONE("", "", "", "", false, false),
    ALIGNMENT_LEFT("<div align=\"left\">", "</div>", "", "", true, true),
    ALIGNMENT_CENTER("<div align=\"center\">", "</div>", "", "", true, true),
    ALIGNMENT_RIGHT("<div align=\"right\">", "</div>", "", "", true, true),
    BULLET("<ul>", "</ul>", "<li>", "</li>", false, true),
    NUMBERING("<ol>", "</ol>", "<li>", "</li>", false, true),
    INDENTATION_UL("<ul style='list-style-type:none;'>", "</ul>", "<li style='list-style-type:none;'>", "</li>", false, true),
    INDENTATION_OL("<ol style='list-style-type:none;'>", "</ol>", "<li style='list-style-type:none;'>", "</li>", false, true);

    public static ParagraphType getInstance(ParagraphStyle style) {
        ParagraphType type;
        if (style instanceof AlignmentSpan.Standard) {
            Layout.Alignment align = ((AlignmentSpan.Standard) style).getAlignment();
            type = align == Layout.Alignment.ALIGN_NORMAL ? ParagraphType.ALIGNMENT_LEFT :
                    align == Layout.Alignment.ALIGN_CENTER ? ParagraphType.ALIGNMENT_CENTER :
                            ParagraphType.ALIGNMENT_RIGHT;
        } else {
            type = style instanceof BulletSpan ? ParagraphType.BULLET :
                    style instanceof NumberSpan ? ParagraphType.NUMBERING :
                            style instanceof IntendationSpan ? ParagraphType.INDENTATION_UL : null;
        }
        return type;
    }

    final private String mStartTag;
    final private String mEndTag;
    final private boolean mIsAlignment;
    final private String mListStartTag;
    final private String mListEndTag;
    final private boolean mEndTagAddsLineBreak;

    private ParagraphType(String startTag, String endTag, String listStartTag,
                          String listEndTag, boolean isAlignment, boolean endTagAddsLineBreak) {
        mStartTag = startTag;
        mEndTag = endTag;
        mListStartTag = listStartTag;
        mListEndTag = listEndTag;
        mIsAlignment = isAlignment;
        mEndTagAddsLineBreak = endTagAddsLineBreak;
    }

    public boolean isUndefined() {
        return this == ParagraphType.NONE;
    }

    public boolean isAlignment() {
        return mIsAlignment;
    }

    public boolean isBullet() {
        return this == ParagraphType.BULLET;
    }

    public boolean isNumbering() {
        return this == ParagraphType.NUMBERING;
    }

    public boolean isIndentation() {
        return this == ParagraphType.INDENTATION_UL || this == ParagraphType.INDENTATION_OL;
    }

    public String getStartTag() {
        return mStartTag;
    }

    public String getEndTag() {
        return mEndTag;
    }

    public String getListStartTag() {
        return mListStartTag;
    }

    public String getListEndTag() {
        return mListEndTag;
    }

    public boolean endTagAddsLineBreak() {
        return mEndTagAddsLineBreak;
    }
}