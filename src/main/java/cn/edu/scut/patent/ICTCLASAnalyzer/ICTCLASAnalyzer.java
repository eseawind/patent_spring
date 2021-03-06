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
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;

/**
 * An Analyzer that uses {@link WhitespaceTokenizer}.
 * <p>
 * <a name="version">You must specify the required {@link Version} compatibility
 * when creating {@link CharTokenizer}:
 * <ul>
 * <li>As of 3.1, {@link WhitespaceTokenizer} uses an int based API to normalize and
 * detect token codepoints. See {@link CharTokenizer#isTokenChar(int)} and
 * {@link CharTokenizer#normalize(int)} for details.</li>
 * </ul>
 * <p>
 **/

/**
 * 完全由WhiteSpaceAnalyzer.java拷贝过来
 */
public final class ICTCLASAnalyzer extends Analyzer {

	private final Version matchVersion;

	/**
	 * Creates a new {@link WhitespaceAnalyzer}
	 * 
	 * @param matchVersion
	 *            Lucene version to match See
	 *            {@link <a href="#version">above</a>}
	 */
	public ICTCLASAnalyzer(Version matchVersion) {
		this.matchVersion = matchVersion;
	}

	@Override
	protected TokenStreamComponents createComponents(final String fieldName,
			final Reader reader) {
		return new TokenStreamComponents(new ICTCLASTokenizer(matchVersion,
				reader));
	}
}