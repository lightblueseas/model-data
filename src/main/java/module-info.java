/**
 * Copyright (C) 2015 Asterios Raptis
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
module model.data
{
	requires lombok;
	requires java.base;
	requires java.desktop;
	requires java.logging;
	requires org.apache.commons.lang3;
	requires org.danekja.jdk.serializable.functional;

	exports io.github.astrapi69.model;
	exports io.github.astrapi69.model.api;
	exports io.github.astrapi69.model.property;
	exports io.github.astrapi69.model.reflect;
	exports io.github.astrapi69.model.util;
}
