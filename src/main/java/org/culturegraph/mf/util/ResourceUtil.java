/*
 *  Copyright 2013, 2014 Deutsche Nationalbibliothek
 *
 *  Licensed under the Apache License, Version 2.0 the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.culturegraph.mf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.culturegraph.mf.exceptions.MetafactureException;


/**
 * @author Christoph Böhme <c.boehme@dnb.de>, Markus Michael Geipel
 * 
 */
public final class ResourceUtil {

	private ResourceUtil() {
		// No instances allowed
	}

	/**
	 * first attempts to open resource with name 'name'. On fail attempts to
	 * open file.
	 * 
	 * @param name
	 * @return
	 * @throws FileNotFoundException
	 *             if all attempts fail
	 */
	public static InputStream getStream(final String name) throws FileNotFoundException {
		if (name == null) {
			throw new IllegalArgumentException("'name' must not be null");
		}
		final File file = new File(name);
		if (file.exists()) {
			return getStream(file);
		}

		final InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
		if (stream == null) {
			throw new FileNotFoundException("No file or resource found: " + name);
		}
		return stream;

	}

	public static InputStream getStream(final File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}

	public static Reader getReader(final String name) throws FileNotFoundException {
		return new InputStreamReader(getStream(name));
	}

	public static Reader getReader(final File file) throws FileNotFoundException {
		return new InputStreamReader(getStream(file));
	}

	public static Reader getReader(final String name, final String encoding) throws FileNotFoundException,
			UnsupportedEncodingException {
		return new InputStreamReader(getStream(name), encoding);
	}

	public static Reader getReader(final File file, final String encoding) throws FileNotFoundException,
			UnsupportedEncodingException {
		return new InputStreamReader(getStream(file), encoding);
	}

	public static Properties loadProperties(final String location) {
		try {
			return loadProperties(getStream(location));
		} catch (IOException e) {
			throw new MetafactureException("'" + location + "' could not be loaded", e);
		}
	}

	public static Properties loadProperties(final InputStream stream) throws IOException {
		final Properties properties;
		properties = new Properties();
		properties.load(stream);
		return properties;
	}

	public static Properties loadProperties(final URL url) {
		try {
			return loadProperties(url.openStream());
		} catch (IOException e) {
			throw new MetafactureException("'" + url.getPath() + "' could not be loaded", e);
		}
	}

	public static String loadTextFile(final String location) throws IOException {
		final StringBuilder builder = new StringBuilder();
		final BufferedReader reader = new BufferedReader(getReader(location));

		String line = reader.readLine();
		while (line != null) {
			builder.append(line);
			line = reader.readLine();
		}

		return builder.toString();
	}

	public static List<String> loadTextFile(final String location, final List<String> list) throws IOException {
		final BufferedReader reader = new BufferedReader(getReader(location));

		String line = reader.readLine();
		while (line != null) {
			list.add(line);
			line = reader.readLine();
		}

		return list;
	}

}
