package cn.edu.scut.patent.ICTCLASAnalyzer;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Reader;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

/**
 * A WhitespaceTokenizer is a tokenizer that divides text at whitespace.
 * Adjacent sequences of non-Whitespace characters form tokens. <a
 * name="version"/>
 * <p>
 * You must specify the required {@link Version} compatibility when creating
 * {@link WhitespaceTokenizer}:
 * <ul>
 * <li>As of 3.1, {@link CharTokenizer} uses an int based API to normalize and
 * detect token characters. See {@link CharTokenizer#isTokenChar(int)} and
 * {@link CharTokenizer#normalize(int)} for details.</li>
 * </ul>
 */

/**
 * 由WhiteSpaceTokenizer.java仿照过来
 */
public final class ICTCLASTokenizer extends CharTokenizer {

	/**
	 * Construct a new WhitespaceTokenizer. * @param matchVersion Lucene version
	 * to match See {@link <a href="#version">above</a>}
	 * 
	 * @param in
	 *            the input to split up into tokens
	 */
	public ICTCLASTokenizer(Version matchVersion, Reader in) {
		super(matchVersion, in);
	}

	/**
	 * Construct a new WhitespaceTokenizer using a given
	 * {@link org.apache.lucene.util.AttributeSource.AttributeFactory}.
	 * 
	 * @param matchVersion
	 *            Lucene version to match See
	 *            {@link <a href="#version">above</a>}
	 * @param factory
	 *            the attribute factory to use for this {@link Tokenizer}
	 * @param in
	 *            the input to split up into tokens
	 */
	public ICTCLASTokenizer(Version matchVersion, AttributeFactory factory,
			Reader in) {
		super(matchVersion, factory, in);
	}

	/**
	 * Collects only characters which do not satisfy
	 * {@link Character#isWhitespace(int)}.
	 */
	@Override
	protected boolean isTokenChar(int c) {
		return !(Character.isWhitespace(c) || isChinesePunctuation(c));
		// return Character.isLetterOrDigit(c);
	}

	/**
	 * 根据UnicodeBlock方法判断中文标点符号
	 * 
	 * @param c
	 * @return
	 */
	private boolean isChinesePunctuation(int c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
				|| ub == Character.UnicodeBlock.VERTICAL_FORMS) {
			return true;
		} else {
			return false;
		}
	}
	
//	/**
//	 * 根据UnicodeBlock方法判断中文标点符号
//	 * 
//	 * 为了适应jdk1.6所作出的调整，jdk1.7版本为上述注释部分
//	 * 
//	 * @param c
//	 * @return
//	 */
//	private boolean isChinesePunctuation(int c) {
//		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
//		if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
//				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
//				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
//				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS) {
//			return true;
//		} else {
//			return false;
//		}
//	}
}